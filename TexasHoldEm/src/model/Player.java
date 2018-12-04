package model;

/**
 * Initializes a new player, and allows a player to fold/bet
 * @author Naresh
 */
public class Player {
	private int chips;
	private Hand hand;

	/**
	 * Constructor
	 * Initializes Player with a new hand of cards and 2000 poker chips
	 */
	public Player(){
		this.chips = 1000;
		this.hand = new Hand();
	}

	/**
	 * getter for chips
	 * @return Return chips passed to the constructor
	 */
	public int getChips() {
		return chips;
	}

	/**
	 * Method that sets chips
	 * @param chips int - the amount of chips to set
	 */
	public void setChips(int chips){
		this.chips = chips;
	}

	/**
	 * When method is called, the value of chips increments by amount specified
	 * @param chips int - number of chips to add
	 */
	public void gainChips(int chips) {
		this.chips += chips;
	}

	/**
	 * When method is called, the value of chips decrements by amount specified 
	 * @param chips int - the number of chips that are lost
	 */
	public void loseChips(int chips) {
		this.chips -= chips;
	}

	/**
	 * getter for hand
	 * @return Return hand passed to the constructor	
	 */
	public Hand getHand() {
		return hand;
	}

	/**
	 * setter for hand
	 * @param hand 
	 */
	public void setHand(Hand hand) {
		this.hand = hand;
	}


}
