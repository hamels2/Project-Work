package com.Wallet.app;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.Wallet.app.Account;

/**
 * An implementation of SqlInsertManager for an sqlite database.
 * <p>
 * The database should have this structure:
 * <p>
 * A table Wallet with field id (autoincremented)
 * <p>
 * A table Account with fields id(autoincremented), balance, walletid
 * <p>
 * A table Transactions with fields id(autoincremented), date, accountid, type, amount
 * 
 */

 public  class SqlWalletQuery implements SqlQueryManager {

    private String url;

    
    /**
     * Creates a new SqlWalletQuery with url indicating the location of the database
     * @param url location of the database in file
     */
    public SqlWalletQuery(String url){
        this.url=url;
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * This method queries the database for all the transaction associated with the specified account.
     * @param accountId The id of the account who's transactions are to be queried.
     * @return Returns an arrayList of strings, where each string is a Transaction.
     * @throws SQLException
     */
    public ArrayList<String> queryAccountTransactions(int accountId) throws SQLException {
        String sql = "SELECT id, date, type, amount, account FROM Transactions WHERE account ="+accountId;    
        Connection conn = this.connect();
        Statement stmt  = conn.createStatement();
        ResultSet rs    = stmt.executeQuery(sql);
        ArrayList<String> transactions = new ArrayList<String>();
        while(rs.next()){
            String tran = "ID: " + rs.getInt("id");
            tran+=" TYPE: "+rs.getString("type");
            tran+=" AMOUNT: "+rs.getBigDecimal("amount");
            tran+=" ACCOUNT: "+rs.getInt("account");
            tran+=" TIMESTAMP: "+rs.getString("date");
            transactions.add(tran);
        }
        conn.close();
        return transactions;
    }


    /**
     * This method queries the database for a specified account from a specified wallet
     * and returns an account object made from that data.
     * @param accountId The account id to be queried.
     * @param walletId The wallet id which the account belongs to.
     * @return Returns an account object made from the data in the database.
     * @throws SQLException
     */
    public Account querySingleAccount(int accountId, int walletId) throws SQLException{
        String sql = "SELECT * FROM Account WHERE id ="+accountId +" AND wallet="+walletId;
        Connection conn = this.connect();
        Statement stmt  = conn.createStatement();
        ResultSet rs    = stmt.executeQuery(sql);
        try{
            rs.next();
            int id = rs.getInt("id");
            BigDecimal balance = rs.getBigDecimal("balance");
            conn.close();
            return new Account(id, walletId, balance);
        }
        catch(SQLException e){
            conn.close();
            return new Account(-1, -1, new BigDecimal("0.00"));
        }
    }

    /**
     * This method queries the database to check if a wallet with the specified id exists.
     * @param walletId The id of the wallet to be queried.
     * @return returns true if the query is successful and false otherwise.
     * @throws SQLException
     */
    public boolean queryWallet(int walletId) throws SQLException{
        String sql = "SELECT id FROM Wallet WHERE id ="+walletId;
        Connection conn = this.connect();
        Statement stmt  = conn.createStatement();
        ResultSet rs    = stmt.executeQuery(sql);
        try{
            rs.next();
            rs.getInt("id");
            conn.close();
            return true;
        }
        catch(SQLException e){
            conn.close();
            return false;
        } 
    }   
}