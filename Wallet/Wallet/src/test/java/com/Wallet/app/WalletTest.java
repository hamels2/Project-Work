package com.Wallet.app;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class WalletTest{
    SqlInsertManager insert;
    SqlQueryManager query;
    Wallet usertest;
    Account test;

    public static Connection connect() {
        // SQLite connection string
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    @Before
    public void setup() throws SQLException{
        this.insert = new SqlWalletInsert("jdbc:sqlite:test.db");
        this.query = new SqlWalletQuery("jdbc:sqlite:test.db");

    }
    @Test
    public void constructor() throws SQLException {
        Wallet test = new Wallet(query, insert);
        String sql = "SELECT id FROM Wallet WHERE id=1";
        Connection conn = WalletTest.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int id = rs.getInt("id");
        conn.close();
        assertTrue(id==test.getId());
        
    }
    @Test
    public void constructorWithId() throws SQLException, WalletException {
        DBClear.clearWallet();
        Wallet test = new Wallet(query, insert);
        test = new Wallet(query,insert,test.getId());
        String sql = "SELECT COUNT(*) FROM Wallet";
        Connection conn = WalletTest.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        int count=0;
        while (rs.next()){
            count = rs.getInt(1);
        }
        conn.close();
        assertTrue(count==1); // check if the amount of wallets did not increase
        
    }
    @Test(expected = WalletException.class)
    public void constructor_WrongId() throws SQLException, WalletException {
        new Wallet(query, insert, 2);
        
    }

    @Test
    public void createAccountTest() throws SQLException {
        Wallet test = new Wallet(query, insert);
        Account acc = test.createAccount();
        assertTrue(acc.getId()>0);
        assertTrue(acc.getHistory(10).size()==0);
    }

    @Test
    public void getAccountTest() throws SQLException, WalletException {
        Wallet test = new Wallet(query, insert);
        Account acc = test.createAccount();
        Account acc1 = test.getAccount(acc.getId());
        assertTrue(acc.getId()==acc1.getId());
        assertTrue(acc.getHistory(10).size()==0);
    }

    @Test(expected = WalletException.class)
    public void getAccountTest_WrongId() throws SQLException, WalletException {
        Wallet test = new Wallet(query, insert);
        test.getAccount(1);
    }

    @AfterClass
    public static void clean() throws SQLException {
        DBClear.clearWallet();
        DBClear.clearAccount();
    }



}