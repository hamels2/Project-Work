package com.Wallet.app;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import org.junit.AfterClass;
import org.junit.Before;

import org.junit.Test;

public class SqlWalletInsertTest{
    
    SqlQueryManager query;
    SqlInsertManager insert;




    @Before
    public void setup() throws SQLException{
    
        this.query = new SqlWalletQuery("jdbc:sqlite:test.db");
        this.insert = new SqlWalletInsert("jdbc:sqlite:test.db");


    }

    
    @Test
    public void addWalletTest() throws SQLException {
        int id = insert.addWallet();
        String sql = "SELECT id FROM Wallet WHERE id="+id;
        Connection conn = WalletTest.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        assertTrue(id==rs.getInt("id"));
        conn.close();
    }

    @Test
    public void addAccountTest() throws SQLException {
        int acc = insert.addAccount(1);
        String sql = "SELECT id FROM Account WHERE id="+acc+" AND wallet=1";
        Connection conn = WalletTest.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        assertTrue(acc==rs.getInt("id"));
        conn.close();
    }


    @Test
    public void addTransactionTest() throws SQLException {
        int tran = insert.addTransaction(1, "test", new BigDecimal("0.00"), "now");
        String sql = "SELECT id FROM Transactions WHERE id="+tran+" AND account="+1;
        Connection conn = WalletTest.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        assertTrue(tran==rs.getInt("id"));
        conn.close();
    }

    


    @Test
    public void updateAccountBalanceTest() throws SQLException {
        int a = insert.addAccount(1);
        insert.updateAccountBalance(a, new BigDecimal("10.00"));
        String sql = "SELECT * FROM Account WHERE id="+a;
        Connection conn = WalletTest.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        assertTrue(rs.getInt("id")==a);
        assertTrue(rs.getBigDecimal("balance").equals(new BigDecimal("10.00")));
        conn.close();
    }

    @AfterClass
    public static void clean() throws SQLException {
        DBClear.clearWallet();
        DBClear.clearAccount();
    }



}