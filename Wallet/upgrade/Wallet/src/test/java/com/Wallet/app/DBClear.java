
package com.Wallet.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.SQLException;



class DBClear{
   
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

    public static void clearWallet() throws SQLException {
        String sql ="DELETE FROM Wallet";
        Connection conn = DBClear.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        conn.close();
    }
    public static void clearAccount() throws SQLException {
        String sql ="DELETE FROM Account";
        Connection conn = DBClear.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        conn.close();
    }
   
    public static void clearTransactions() throws SQLException {
        String sql ="DELETE FROM Transactions";
        Connection conn = DBClear.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        conn.close();
    }

}