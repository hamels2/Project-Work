package com.Wallet.app;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.sql.DriverManager;

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
public class SqlWalletInsert implements SqlInsertManager{

    private String url;
    


    /**
     * Creates a new SqlWalletInsert with url indicating the location of the database
     * @param url location of the database in file
     */
    public SqlWalletInsert(String url){
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
     * Adds a new id to the wallet table.
     * @return Returns the new id.
     */
    public int addWallet() throws SQLException{
        String sql = "INSERT INTO Wallet(id) VALUES(NULL)";
        Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        sql = "SELECT id FROM Wallet ORDER BY id DESC LIMIT 1";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int id = rs.getInt("id");
        conn.close();
        return id;
    }
    
    /**
     * Adds a new account with the specified wallet id, to the account table.
     * @param walletId The specified wallet id.
     * @return Return the id of the new account.
     */
    public int addAccount(int walletId) throws SQLException {
        String sql = "INSERT INTO Account(wallet,balance) VALUES(?,?)";
        Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        BigDecimal balance = new BigDecimal("0.00");
        pstmt.setInt(1, walletId);
        pstmt.setBigDecimal(2, balance);
        pstmt.executeUpdate();
        sql = "SELECT id FROM Account ORDER BY id DESC LIMIT 1";
        Statement stmt  = conn.createStatement();
        ResultSet rs    = stmt.executeQuery(sql);
        rs.next();
        int id = rs.getInt("id");
        conn.close();
        return id;
    }

    /**
     * Adds a transaction to the transaction table.
     * @param accountId The account linked to the transaction.
     * @param type The type of transaction.
     * @param amount The amount used in the transaction.
     * @param date The date of the transaction.
     * @return return the new uniquely generated transaction id.
     */
    public int addTransaction(int accountId, String type, BigDecimal amount, String date) throws SQLException {
        String sql = "INSERT INTO Transactions(account,type,amount,date) VALUES(?,?,?,?)";
        Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, accountId);
        pstmt.setString(2,type);
        pstmt.setBigDecimal(3, amount);
        pstmt.setString(4, date);
        pstmt.executeUpdate();
        Statement stmt  = conn.createStatement();
        sql = "SELECT id FROM Transactions ORDER BY id DESC LIMIT 1";
        ResultSet rs    = stmt.executeQuery(sql);
        rs.next();
        int id = rs.getInt("id");
        conn.close();
        return id;

    }
    
    /**
     * Updates the balance of the specified account.
     * @param accountId The id of the account to be updated
     * @param balance The new balance for the account
     */
    public void updateAccountBalance(int accountId, BigDecimal balance) throws SQLException {
        String sql = "UPDATE Account SET balance= ? WHERE id = ?";    
        Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setBigDecimal(1, balance);
        pstmt.setInt(2, accountId);
        pstmt.executeUpdate();
        conn.close();

    }
}