package com.Wallet.app;

import java.math.BigDecimal;


import java.sql.SQLException;

/**
 * SqlInsertManager is an interface for creating classes that connect to your database and insert/update data.
 */
public interface SqlInsertManager {
    
 
    /**
     * Should implement a method that adds a wallet to your database and returns 
     * some value such as a primary key to identify that wallet.
     * 
     * @return An integer that represents the data created in your database 
     * such as a primary key.
     *  @throws SQLException
     */
    int addWallet() throws SQLException;
    
   
   
    /**
     * Should implement a method that adds an account to your database and returns 
     * some value such as a primary key to identify that account.
     * 
     * @param walletId The id of the wallet that the created account belongs to.
     * @return An integer that represents the data created in your database 
     * such as a primary key
     * 
     * @throws SQLException
     */
    int addAccount(int walletId) throws SQLException;


    /**
     * Should implement a method that adds a transaction to your database and returns 
     * some value such as a primary key to identify that transaction.
     *
     * @param accountId The id of the account associated with the transaction
     * @param type The type of transaction
     * @param amount The amount used in the transaction
     * @param date The timestamp for the transaction
     * @return 
     * @throws SQLException
     */
    int addTransaction(int accountId, String type, BigDecimal amount, String date) throws SQLException;
    
    
    /**
     * Should implement a method that updates the specified account's balance
     * @param accountId The id of the account to update.
     * @param balance The new balance of the account.
     * @throws SQLException
     */
    void updateAccountBalance(int accountId, BigDecimal balance) throws SQLException;
}
