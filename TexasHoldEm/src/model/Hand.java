
package model;

import java.util.ArrayList;
import java.util.Collections;

public class Hand extends ArrayList<Card>{
    int strength;
    
    /**
     * Method sets the strength of the hand 
     * @param x int - hand strength
     */
    public void setStrength(int x){
        this.strength = x;
    }
    
    /**
     * Method evaluates the hand and returns the strength of the hand
     * @return int
     */
    public int getStrength(){
        boolean flush = false;
        boolean straightFlu = true;
        boolean straight = false;
        boolean quad = false;
        boolean FullH = false;
        boolean trips = false;
        boolean twopair = false;
        boolean pair = false;
        int count =0;
        Hand clone = new Hand();
        Hand played = new Hand();
        
        for(int i=0; i<this.size(); i++){
            clone.add(this.get(i));
        }
        
        Collections.sort(clone);
        Collections.reverse(clone);
        
        //can be optimized if needed
        if(this.size()>2){
            FLUSH:
                for(int i =0; i<this.size(); i++){
                    played = new Hand();
                    count = 0;
                    for(int j =0; j<this.size(); j++){
                        if(clone.get(i).getSuit() == clone.get(j).getSuit()){
                            count++;
                            played.add(clone.get(j));
                        }
                        if(count ==5){
                            
                            flush = true;
                            break FLUSH;
                        }
                    }
                }
                
                
                // can be optimized if needed
                if(flush){
                      for(int i=0; i<4; i++){
                            if(played.get(i+1).getRank() != played.get(i).getRank()-1){
                                straightFlu = false;
                                break;
                            }
                        
                        }
                }
                //STRAIGHT FLUSH
                if(straightFlu && flush){
                    return clone.get(0).getRank()*100000000;
                }
                
                //can be optimized
                
               //QUADS
                for(int i =0; i<this.size(); i++){
                    played = new Hand();
                    count = 0;
                    for(int j=i; j<this.size(); j++){
                        if(clone.get(i).getRank() == clone.get(j).getRank()){
                            played.add(clone.get(j));
                            count ++;   
                        }
                        if(count ==4){
                            quad = true;
                            return played.get(0).getRank()*10000000;
                        }
                    }
                }
            
                
                
                TRIPS:
                for(int i =0; i<this.size(); i++){
                    played = new Hand();
                    count = 0;
                    for(int j=0; j<this.size(); j++){
                        if(clone.get(i).getRank() == clone.get(j).getRank()){
                            played.add(clone.get(j));
                            count ++;   
                        }
                        if(count ==3){
                            trips = true;
                            break TRIPS;
                        }
                    }
                }
                
                //FULLHOUSE
                if(trips){
                    for(int i =0; i<this.size(); i++){
                        count = 0;
                        int pos = 0;
                        for(int j=0; j<this.size(); j++){
                            if(clone.get(i).getRank() == clone.get(j).getRank() && clone.get(i).getRank() != played.get(0).getRank()){
                                pos = j;
                                count ++;   
                            }
                            if(count ==2){
                                FullH = true;
                                return played.get(0).getRank()*1000000 + clone.get(pos).getRank();
                            }
                        }
                    }
                    
                }
        }
        
        //FLUSH
        if(this.size()>2){
            if(flush){
                for(int i =0; i<this.size(); i++){
                    played = new Hand();
                    count = 0;
                    for(int j =0; j<this.size(); j++){
                        if(clone.get(i).getSuit() == clone.get(j).getSuit()){
                            count++;
                            played.add(clone.get(j));
                        }
                        if(count ==5){
                            return played.get(0).getRank()*100000;
                        }
                    }
                }
                
            }
        }

        //STRAIGHT
        if(this.size()>2){
            for(int i =0; i<3; i++){
                played = new Hand();
                played.add(clone.get(i));
                count = 1;
                for(int j=i; j<this.size()-1; j++){
                    if(clone.get(j+1).getRank() == clone.get(j).getRank()-1){
                        played.add(clone.get(j+1));
                        count ++;   
                    }
                    else{
                        break;
                    }
                    if(count ==5){
                        straight = true;
                        return played.get(0).getRank()*10000;
                    }
                }
            }
        }
        
        if(this.size()>2){
            //TRIPS
            if(trips){
                    for(int i =0; i<this.size(); i++){
                        played = new Hand();
                        count = 0;
                        for(int j=0; j<this.size(); j++){
                            if(clone.get(i).getRank() == clone.get(j).getRank()){
                                played.add(clone.get(j));
                                count ++;   
                            }
                            if(count ==3){
                                return played.get(0).getRank()*1000;
                            }
                        }
                    }
                
            }
        }
        
        PAIR:
        for(int i =0; i<this.size(); i++){
            played = new Hand();
            count = 0;
            for(int j=0; j<this.size(); j++){
                if(clone.get(i).getRank() == clone.get(j).getRank()){
                    played.add(clone.get(j));
                    count ++;   
                }
                if(count ==2){
                    pair = true;
                    break PAIR;
                }
            }
        }
        int pair1 = played.get(0).getRank();
        
        if(pair){
                for(int i =0; i<this.size(); i++){
                    played = new Hand();
                    count = 0;
                    for(int j=0; j<this.size(); j++){
                        if(clone.get(i).getRank() == clone.get(j).getRank() && clone.get(j).getRank() != pair1){
                            played.add(clone.get(j));
                            count ++;   
                        }
                        //TWO PAIR
                        if(count ==2){
                            if(played.get(0).getRank() > pair1){
                                return played.get(0).getRank()*100;
                            }
                            return pair1*100;
                        }
                    }
                }
                //PAIR
                return pair1*10;
                
            
        }
        //HIGH CARD
        return clone.get(0).getRank();
    }
    
    /**
     * Method displays the cards in hand and community
     * @return String
     */
    public String toString(){
        String hand = "";
        for(int i =0; i<this.size(); i++){
            hand = hand+ this.get(i).toString() + " ";
        }
        return hand;
    }
    
    /** 
     * Method displays the cards in hand 
     * @return String
     */
    public String myhand(){
        String hand = "";
        for(int i =0; i<3; i++){
            hand = hand+ this.get(i).toString() + " ";
        }
        return hand;
    }    
       
    
}
