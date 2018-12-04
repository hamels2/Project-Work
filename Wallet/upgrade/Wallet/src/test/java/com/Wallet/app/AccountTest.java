package com.Wallet.app;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class AccountTest{
    SqlInsertManager insert;
    SqlQueryManager query;
    Wallet usertest;
    Account test;
    
    @Before
    public void setup() throws SQLException{
        this.insert = new SqlWalletInsert("jdbc:sqlite:test.db");
        this.query = new SqlWalletQuery("jdbc:sqlite:test.db");
        this.usertest = new Wallet(query, insert);
        test = usertest.createAccount();
    }
    @Test
    public void getBalanceTest(){
        assertTrue(test.getBalance().doubleValue()==0.00);
        
    }
    @Test
    public void getHistoryTest(){
        assertTrue(test.getHistory(10).size()==0);
    }

    @Test
    public void addToHistoryTest(){
        test.addToHistory("test");
        String hist = test.getHistory(1).get(0);
        assertTrue(hist.equals("test"));
    }

    @AfterClass
    public static void clean() throws SQLException {
        DBClear.clearWallet();
        DBClear.clearAccount();
    }



}