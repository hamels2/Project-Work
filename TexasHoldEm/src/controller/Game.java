package controller;

import java.io.File;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import model.Deck;
import model.Hand;
import model.Player;
import userInterface.TexasHoldEm;

public class Game{
	private int currentPlayer;
	private Player player1, player2;
	private AIOpponent AIplayer;
	private Deck deck;
	private Hand p1hand;
    private Hand p2hand;
    private int cardCount;
    private boolean GameEnd, RoundEnd, turnEnd;
    private TexasHoldEm view;
    private Pot pot;
	
//    //constructor for 2 player
//	public Game(Player player1, Player player2, TexasHoldEm view){
//		this.player1 = player1;
//		this.player2 = player2;
//		this.currentPlayer = 0;
//		deck = new Deck();
//		deck.Set();
//		deck.Shuffle();
//		this.GameEnd = false;
//		this.RoundEnd = false;
//		this.turnEnd = false;
//		pot = new Pot(player1, player2, this);
//		this.view = view;
//	}
	
	//constructor for AI
	public Game(Player player1, AIOpponent AIplayer, TexasHoldEm view){
		this.player1 = player1;
		this.AIplayer = AIplayer;
		this.player2 = this.AIplayer;
		this.currentPlayer = 0;
		deck = new Deck();
		deck.Set();
		deck.Shuffle();
		this.GameEnd = false;
		this.turnEnd = false;
		this.RoundEnd = false;
		pot = new Pot(this.player1, AIplayer, this);
		this.view = view;
		gameTracker();
	}
	
	/**
	 * Method that switches the current player, used for turn switch
	 */
	public void switchPlayer(){ //0 is player 1, 1 is player 2
		this.currentPlayer = (this.currentPlayer + 1) % 2 ;
	}
	
	/**
	 * Method keeps track of the AI as well as whether there is a winner
	 */
	public void gameTracker(){
		Thread th = new Thread(){
			@Override
			public void run(){
				while(true){
					//choose AI action when the game is not ended
					if (!RoundEnd && !GameEnd){
						if (currentPlayer == 1) AIplayer.getAction(pot);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		th.start();
	}
	
	/**
	 * Method contains methods to perform when a turn ends
	 */
	public void turnEnd(){
		turnEnd = true;
		next_card();
		view.createCommunityCard();
		view.showCommunity();
	}
	
	/**
	 * Method returns the value of parameter turnEnd
	 * @return turnEnd
	 */
	public boolean isTurnEnd(){
		return turnEnd;
	}
	/**
	 * Method returns the value of parameter view
	 * @return view 
	 */
	public TexasHoldEm getView(){
		return view;
	}
	
	/**
	 * Method that returns the value of parameter pot
	 * @return Pot
	 */
	public Pot getPot(){
		return pot;
	}
	
	/**
	 * Method that returns the value of parameter currentPlayer
	 * @return int
	 */
	public int getCurrentPlayer(){
		return currentPlayer;
	}
	
	/**
	 * Method that returns the value of parameter chips for player 1
	 * @return chips int
	 */
	public int getChipsP1(){
		return player1.getChips();
	}
	
	/**
	 * Method that returns the value of parameter chips for player 2
	 * @return chips int
	 */
	public int getChipsP2(){
		return player2.getChips();
	}
	
	/**
	 * Method resets state variables to default values
	 */
	public void startNewGame(){
		deck = new Deck();
		deck.Set();
		deck.Shuffle();
		player1.setChips(1000);
		player2.setChips(1000);
		this.GameEnd = false;
		gameTracker();
		pot.reset();
		view.reset();
	}
	
	/**
	 * Method that deals 3 cards to each player
	 */
	public void deal(){
		deck.Shuffle();
		p1hand = new Hand();
		p2hand = new Hand();
		player1.setHand(p1hand);
		player2.setHand(p2hand);
		cardCount = 0;
		p1hand.add(deck.get(cardCount));
		cardCount++;
		p2hand.add(deck.get(cardCount));
		cardCount++;
		p1hand.add(deck.get(cardCount));
		cardCount++;
		p2hand.add(deck.get(cardCount));
		cardCount++;
		for(int i =0; i<3; i++){
			next_card();
		}
		RoundEnd = false;
		view.log("Cards are dealt.");
		view.updateChipLabels();
	}

	/**
	 * Method that deals the same card to each player, represents the community card
	 */
	public void next_card(){
		p1hand.add(deck.get(cardCount));
		p2hand.add(deck.get(cardCount));
		cardCount++;
	}
	
	/**
	 * Method that starts a new round by reinitializing the state variables
	 */
	public void newRound(){
		//when one player's chip falls below 0, end the game and display the winner
		if (player1.getChips() <= 0 || player2.getChips() <= 0){
			GameEnd = true;
			int winOption = (player1.getChips() <= 0) ? 4 : 5;
			TexasHoldEm.displayMessage(winOption);
			return;
		}
		AIplayer.first = true;
		deck.Shuffle();
		view.reset();
		switchPlayer();
		RoundEnd = true;
		pot.reset();
		view.log("New Round");
		view.updateChipLabels();
	}
	
	/**
	 * Method that evaluates the round if no player has folded  
	 */
	public void evaluateRound(){
		view.log("Round Evaluated");
		if(player1.getHand().getStrength() > player2.getHand().getStrength())
			pot.distributePot(1);
		else if(player1.getHand().getStrength() < player2.getHand().getStrength())
			pot.distributePot(2);
		else
			pot.distributePot(3);
		
		view.showDealerCard();
		newRound();
	}
	
//	public static void main(String args[]){
//		Player p1 = new Player();
//		Player p2 = new Player();
//		TexasHoldEm t= new TexasHoldEm();
//		Game game = new Game(p1, p2, t);
//		game.deal();
//		System.out.println(p1.getHand().toString());
//		game.deal();
//		System.out.println(p1.getHand().toString());
//	}
	

}
