package com.Wallet.app;


import java.math.BigDecimal;

import java.util.*;

/**
 * The Account class is a data strucure representing an account. An account can only be created through the Wallet class.
 * @see Wallet
 */

public class Account{
    private int id;
    private BigDecimal balance;
    private ArrayList<String> history;
    private int wallet;
    
    
    Account(int id, int wallet, BigDecimal balance){
        this.id = id;
        this.wallet = wallet;
        this.balance = balance;
        history = new ArrayList<String>();
        
    }


    /**
     * gets the current balance of the account 
     * @return A <code>BigDecimal</code> of the current balance
     */
    public BigDecimal getBalance(){
        return this.balance;
    }
    
    void setBalance(BigDecimal balance){
        this.balance=balance;
    }

    
    /**
     * gets the n most recent transactions associated with this account 
     * @param n The n amount of transactions to retrieve
     * @return returns an <code>ArrayList</code> of strings where each string is a transaction, ordered from latest to last
     */
    public ArrayList<String> getHistory(int n){
        if(n>=history.size()){
            return history;
        }
        ArrayList<String> request = new ArrayList<String>();
        int i = history.size()-1;
        for(int j=0; i<n; j++){
            request.add(history.get(i));
            i--;
        }
        return request;

    }
    
    void addToHistory(String transaction){
        history.add(transaction);
    }
    
    
    /**
     * gets the id of the account 
     * @return returns an int representing the id of the account
     */
    public int getId(){
        return id;
    }

    
    /**
     * gets the id of the wallet that this account belong to
     * @return return an int representing the id
     */
    public int getWalletId(){
        return wallet;
    }

}