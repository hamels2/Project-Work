package com.Wallet.app;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

/**The Wallet class creates objects that are synchronized with a specified database. 
 * The class is also a factory for creating new account objects.
 * 
 */

public class Wallet{
    int id;
    SqlQueryManager query;
    SqlInsertManager insert;

    /**
     * This wallet constructor creates a wallet object from a wallet id that already exists inside the database
     * specified by the SqlqueryManager and SqlInsertManager objects
     * 
     * @param query The SqlqueryManager used to query the database
     * @param insert The SqlInsertManager used to insert and update values in the database
     * @param id The id of the wallet you wish to create an object from
     * @throws SQLException
     * @throws WalletException if an id is specified for a wallet that doesn't exist in the database
     * specified by the sqlqueryManager and SqlInsertManager objects
     */
    public Wallet(SqlQueryManager query, SqlInsertManager insert, int id) throws SQLException, WalletException {
        this.insert = insert;
        this.query = query;
        boolean wallet = query.queryWallet(id);
        if(!wallet){
            throw new WalletException("No wallet with this id exists");
         }
    }


    /**
     * This wallet constructor creates a new wallet object with a unique id that doesn't exist in your database. 
     * It stores that id inside the database specified through your sqlInsertManager and SqlQueryManageer
     * 
     * @param query
     * @param insert
     * @throws SQLException
     */
    public Wallet(SqlQueryManager query, SqlInsertManager insert) throws SQLException{
        this.insert = insert;
        this.query = query; 
        this.id = insert.addWallet();
         
    }
    
    
    /**
     * 
     * @return Returns an <code>int</code> representing the wallet id
     */
    public int getId(){
        return id;
    }
    
    
    /**
     * Creates an account for this wallet and stores the account info in your database
     * 
     * @return returns a new account with a unique account id
     * @throws SQLException
     */
    public Account createAccount() throws SQLException {
        int accountId = insert.addAccount(id);
        return new Account(accountId, id, new BigDecimal("0.00"));
    }
   
   
    /**
     * Creates an account object for an existing account in your database. 
     * The account id, balance, and transaction history for this account will be copied
     * over from the database into the new account object
     * 
     * @param accountId The id of the account stored in your database
     * @return The created account
     * @throws SQLException
     * @throws WalletException If the account id specified doesn't currently exist in your database
     */
     public Account getAccount(int accountId) throws SQLException, WalletException {
        Account account = query.querySingleAccount(accountId,id);
        if(account.getId()==-1){
            throw new WalletException("No account exists with this id in this wallet");
        }
        ArrayList<String> history = query.queryAccountTransactions(accountId);
        for(String tran : history){
            account.addToHistory(tran);
        }
        return account;
    }
}