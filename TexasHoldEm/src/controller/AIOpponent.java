package controller;

import java.util.Random;

import model.Player;

public class AIOpponent extends Player{
	public boolean first = true;
	private int currentBet;
	private int bet;
	private int countTilCall;
	private Random rand = new Random();
	
	//constructor
	public AIOpponent(){
		super();
		currentBet = 0;
		bet = 0;
		countTilCall = rand.nextInt(5)+ 1;
	}
	
	/**
	 * Method that determines the amount of chips to bet
	 * @return int
	 */
	public int choose(){
		
		if(getHand().getStrength() >=100000000){
			return getChips();
			
		}
		if(getHand().getStrength() >= 10000000){
			double d = getChips()*0.5;
			if((int)d == 0){
				return getChips();
			}
			return (int)d;
		}
		if(getHand().getStrength()>=1000000){
			double d = getChips()*0.45;
			if((int)d == 0){
				return getChips();
			}
			return (int)d;
		}
		if(getHand().getStrength()>=100000){
			double d = getChips()*0.4;
			if((int)d == 0){
				return getChips();
			}
			return (int)d;
		}
		
		if(getHand().getStrength()>=10000){
			double d = getChips()*0.3;
			if((int)d == 0){
				return getChips();
			}
			return (int)d;
		}
		if(getHand().getStrength()>=1000){
			double d = getChips()*0.2;
			if((int)d == 0){
				return getChips();
			}
			return (int)d;
		}
		if(getHand().getStrength()>=100){
			double d = getChips()*0.1;
			if((int)d == 0){
				return getChips();
			}
			return (int)d;
		}
		if(getHand().getStrength()>=10){
			double d = getChips()*0.05;
			if((int)d == 0){
				return getChips();
			}
			return (int)d;
		}
		return getHand().getStrength();
		
	}
	
	/**
	 * Method chooses the action that the AI will take 
	 * @param pot
	 */
	public void getAction(Pot pot){
//		System.out.println(countTilCall);
		if (countTilCall == 0) {
			pot.call();
			return;
		}
		if (first) { //call on first turn
			currentBet = pot.getBet();
			bet = this.choose();
			pot.call();
			first = false;
		}
		else if(currentBet == pot.getBet()){
			currentBet += this.choose();
			pot.raise(this.choose());
			this.countTilCall--;
		}else{ 
		if ((currentBet+pot.getBet()) >= 0.8*bet && (currentBet+pot.getBet()) <= 1.6*bet){
			currentBet += pot.getBet();
			pot.call();
			this.countTilCall = 0;
		}else{
				if (currentBet - pot.getBet() > 2*bet){
					this.currentBet = 0;
					pot.fold();
					this.countTilCall = rand.nextInt(10) + 1;
				}
				else{
				pot.raise(bet);
				currentBet += bet;
				this.countTilCall--;
			}
		}
		}
	}
}
