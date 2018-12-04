package com.Wallet.app;

import java.math.BigDecimal;
import java.sql.SQLException;

class ConcurrentWithdraw implements Runnable {
   private Thread t;
   private String threadName;
   private SqlInsertManager insert;
   private int n;
   private Account acc;
   String name;
   
   ConcurrentWithdraw( String name, SqlInsertManager insert, int n, Account acc) {
        this.name=name;
        threadName = name;
        System.out.println("Creating " +  threadName );
        this.insert = insert;
        this.n = n;
        this.acc =acc;
   }
   
   public void run() {
      System.out.println("Running " +  threadName );
      try {
         for(int i = 0; i < n; i++) {
            System.out.println("Thread: " + threadName + ", " + "withdraw 1");
            
            Transaction.withdraw(acc, new BigDecimal("1.00"), insert);
            // Let the thread sleep for a while.
            Thread.sleep(50);
         }
      } catch (InterruptedException e) {
         System.out.println("Thread " +  threadName + " interrupted.");
      } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("sql");
            e.printStackTrace();
        } catch (TransactionException e) {
            System.out.println("tran");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      System.out.println("Thread " +  threadName + " exiting.");
   }
   
   public void start () {
      System.out.println("Starting " +  threadName );
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   }
}