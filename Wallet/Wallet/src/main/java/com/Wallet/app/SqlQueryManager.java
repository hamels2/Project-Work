package com.Wallet.app;



import java.sql.SQLException;
import java.util.ArrayList;

import com.Wallet.app.Account;


/**
 * SqlQueryManager is an interface for creating classes that connect to your database and query data.
 */
public interface SqlQueryManager {
    

    /**
     * This method should query the database for all the transaction associated with the specified account.
     * @param accountId The id of the account who's transactions are to be queried.
     * @return Returns an arrayList of strings, where each string is a Transaction.
     * @throws SQLException
     */
    ArrayList<String> queryAccountTransactions(int accountId) throws SQLException;

    
    /**
     * This method should query the database for a specified account from a specified wallet
     * and return an account object made from that data.
     * @param accountId The account id to be queried.
     * @param walletId The wallet id which the account belongs to.
     * @return Returns an account object made from the data in the database.
     * @throws SQLException
     */
    Account querySingleAccount(int accountId, int walletId) throws SQLException;

    
    /**
     * This method should query the database to check if a wallet with the specified id exists.
     * @param walletId The id of the wallet to be queried.
     * @return returns true if the query is successful and false otherwise.
     * @throws SQLException
     */
    boolean queryWallet(int walletId) throws SQLException;
    

}