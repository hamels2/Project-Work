package model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card> {
	/**
	 * Method used to create 52 cards in the deck
	 */
    public void Set(){ //numbered cards from 2-10, face cards from 11-13 and 14 being ace
    	//create club cards
        for(int i =2; i<=14; i++){
            Card card = new Card(i,'C');
            this.add(card);
        }
        //create diamond cards
        for(int i =2; i<=14; i++){
            Card card = new Card(i,'D');
            this.add(card);
        }
        //create spades cards
        for(int i =2; i<=14; i++){
            Card card = new Card(i,'S');
            this.add(card);
        }
        //create heart cards
        for(int i =2; i<=14; i++){
            Card card = new Card(i,'H');
            this.add(card);
        }
    }
    
    /**
     * Method shuffles the deck
     */
    public void Shuffle(){
        Collections.shuffle(this);
    }
    
    
    
}