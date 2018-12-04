package com.Wallet.app;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;

import org.junit.Test;

public class SqlWalletQueryTest{
    
    SqlQueryManager query;
    SqlInsertManager insert;
    Account acc1;
    Account acc2;
    Wallet user;


    @Before
    public void setup() throws SQLException{
    
        this.query = new SqlWalletQuery("jdbc:sqlite:test.db");
        this.insert = new SqlWalletInsert("jdbc:sqlite:test.db");
        user = new Wallet(query, insert);

    }

    @Test
    public void queryAccountTransactionsTest() throws SQLException {
        String sql = "INSERT INTO Transactions(account,type,amount,date) VALUES(?,?,?,?)";
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, 0);
        pstmt.setString(2,"test");
        pstmt.setBigDecimal(3, new BigDecimal("5.00"));
        pstmt.setString(4, "now");
        pstmt.executeUpdate();
        conn.close();
        sql = "SELECT id FROM Transactions WHERE account="+0;  
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement stmt  = conn.createStatement();
        ResultSet rs    = stmt.executeQuery(sql);
        rs.next();
        int id = rs.getInt("id");
        ArrayList<String> test = query.queryAccountTransactions(0);

        String tran = "ID: " +id;
        tran+=" TYPE: "+"test";
        tran+=" AMOUNT: "+new BigDecimal("5.0").toString();
        tran+=" ACCOUNT: "+0;
        tran+=" TIMESTAMP: "+"now";
        conn.close();
        assertTrue(tran.equals(test.get(0)));
        

    }


    @Test
    public void queryAccountTransactionsTest_wrongID() throws SQLException {
        ArrayList<String> test = query.queryAccountTransactions(-1);
        assertTrue(test.size()==0);
    }

    @Test
    public void querySingleAccountTest() throws SQLException{

        Account test1 = user.createAccount();
        Account quer = query.querySingleAccount(test1.getId(), user.getId());

        assertTrue(quer.getId()==test1.getId());
       

    }
    @Test
    public void querySingleAccountTest_WrongWalletId() throws SQLException{
       Account test = query.querySingleAccount(1, 2);
       assertTrue(test.getId()==-1);
        
    }

    @Test
    public void querySingleAccountTest_WrongAccountId() throws SQLException{
        Account test = query.querySingleAccount(2, 1);
        assertTrue(test.getId()==-1);
        
    }


    @Test
    public void queryWalletTest() throws SQLException{

        boolean exists = query.queryWallet(user.getId());
        assertTrue(exists);
        boolean noId = query.queryWallet(-1);
        assertTrue(!noId);

    }
 

    @AfterClass
    public static void clean() throws SQLException {
        DBClear.clearWallet();
        DBClear.clearAccount();
        DBClear.clearTransactions();
    }



}