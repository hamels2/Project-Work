package com.Wallet.app;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;

import org.junit.Test;

public class TransactionTest{
    
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
    public void depositTest() throws SQLException, TransactionException {
        Account acc = user.createAccount();
        
        //check integer value
        Transaction.deposit(acc, new BigDecimal("5.00"), insert);
        assertTrue(acc.getBalance().intValue()==5);
        
        //check decimal
        Transaction.deposit(acc, new BigDecimal("0.50"), insert);
        assertTrue(acc.getBalance().equals(new BigDecimal("5.50")));

        //check rounding 
        Transaction.deposit(acc, new BigDecimal("1.001"), insert);
        assertTrue(acc.getBalance().equals(new BigDecimal("6.51")));

        //check zero
        Transaction.deposit(acc, new BigDecimal("0"), insert);
        assertTrue(acc.getBalance().equals(new BigDecimal("6.51")));
    }

    @Test(expected=TransactionException.class)
    public void depositTest_negativeAmount() throws SQLException, TransactionException {
        Account acc = user.createAccount();
        
        //check integer value
        Transaction.deposit(acc, new BigDecimal("-5.00"), insert);
        
        

    }


    @Test
    public void withdrawTest() throws SQLException, TransactionException {
        Account acc = user.createAccount();
        Transaction.deposit(acc, new BigDecimal("10.00"), insert);
        
        //check integer value
        Transaction.withdraw(acc, new BigDecimal("5.00"), insert);
        assertTrue(acc.getBalance().intValue()==5);
        
        //check decimal
        Transaction.withdraw(acc, new BigDecimal("0.50"), insert);
        assertTrue(acc.getBalance().equals(new BigDecimal("4.50")));

        //check rounding 
        Transaction.withdraw(acc, new BigDecimal("0.999"), insert);
        assertTrue(acc.getBalance().equals(new BigDecimal("3.50")));

        //check zero
        Transaction.withdraw(acc, new BigDecimal("0"), insert);
        assertTrue(acc.getBalance().equals(new BigDecimal("3.50")));

    }

    @Test(expected = TransactionException.class)
    public void withdrawTest_negative() throws SQLException, TransactionException {
        Account acc = user.createAccount();
        
        Transaction.withdraw(acc, new BigDecimal("-5.00"), insert);
        
    }

    @Test(expected = TransactionException.class)
    public void withdrawTest_negativeBalance() throws SQLException, TransactionException {
        Account acc = user.createAccount();
        
        Transaction.withdraw(acc, new BigDecimal("5.00"), insert);
        
    }

    @Test
    public void transferTest() throws SQLException, TransactionException {
        Account take = user.createAccount();
        Account give = user.createAccount();
        
        Transaction.deposit(take, new BigDecimal("10.00"), insert);
        Transaction.transfer(take,give,new BigDecimal("10.00"),insert);
        assertTrue(take.getBalance().intValue()==0);
        assertTrue(give.getBalance().intValue()==10);

        // decimal check
        take = user.createAccount();
        give = user.createAccount();
        
        Transaction.deposit(take, new BigDecimal("10.00"), insert);
        Transaction.transfer(take,give,new BigDecimal(".50"),insert);
        assertTrue(take.getBalance().equals(new BigDecimal("9.50")));
        assertTrue(give.getBalance().equals(new BigDecimal("0.50")));

        // round check
        take = user.createAccount();
        give = user.createAccount();
        
        Transaction.deposit(take, new BigDecimal("10.00"), insert);
        Transaction.transfer(take,give,new BigDecimal("0.991"),insert);
        assertTrue(take.getBalance().equals(new BigDecimal("9.00")));
        assertTrue(give.getBalance().equals(new BigDecimal("1.00")));
        
        // zero check 
        take = user.createAccount();
        give = user.createAccount();
        
        Transaction.deposit(take, new BigDecimal("10.00"), insert);
        Transaction.transfer(take,give,new BigDecimal("0"),insert);
        assertTrue(take.getBalance().equals(new BigDecimal("10.00")));
        assertTrue(give.getBalance().equals(new BigDecimal("0.00")));


    }

    @Test
    public void depositTest_concurrent() throws SQLException {
        Account acc = user.createAccount();
        ConcurrentDeposit one =  new ConcurrentDeposit("one", insert, 20, acc);
        ConcurrentDeposit two = new ConcurrentDeposit("two", insert, 20, acc);
        Thread t1 = new Thread(one);
        Thread t2 = new Thread(two);
        t1.start();
        t2.start();
        while(t1.isAlive() || t2.isAlive()){

        }
        assertTrue(acc.getBalance().intValue()==40);

    }

    @Test
    public void withdrawTest_concurrent() throws SQLException, TransactionException {
        Account acc = user.createAccount();
        Transaction.deposit(acc, new BigDecimal("100"), insert);
        ConcurrentWithdraw one =  new ConcurrentWithdraw("one", insert, 20, acc);
        ConcurrentWithdraw two = new ConcurrentWithdraw("two", insert, 20, acc);
        Thread t1 = new Thread(one);
        Thread t2 = new Thread(two);
        t1.start();
        t2.start();
        while(t1.isAlive() || t2.isAlive()){

        }
        assertTrue(acc.getBalance().intValue()==60);
    }

    @Test
    public void withdrawal_Deposit_concurrent() throws SQLException, TransactionException {
        Account acc = user.createAccount();
        Transaction.deposit(acc, new BigDecimal("100"), insert);
        ConcurrentWithdraw one =  new ConcurrentWithdraw("one", insert, 20, acc);
        ConcurrentDeposit two = new ConcurrentDeposit("two", insert, 20, acc);
        Thread t1 = new Thread(one);
        Thread t2 = new Thread(two);
        t1.start();
        t2.start();
        while(t1.isAlive() || t2.isAlive()){

        }
        assertTrue(acc.getBalance().intValue()==100);

    }

    @Test
    public void transferTest_concurrent() throws SQLException, TransactionException {
        Account acc = user.createAccount();
        Account acc1= user.createAccount();
        Transaction.deposit(acc, new BigDecimal("100"), insert);
        ConcurrentTransfer one =  new ConcurrentTransfer("one", insert, 20, acc,acc1);
        ConcurrentTransfer two =  new ConcurrentTransfer("two", insert, 10, acc,acc1);
        Thread t1 = new Thread(one);
        Thread t2 = new Thread(two);
        t1.start();
        t2.start();
        while(t1.isAlive() || t2.isAlive()){

        }
        assertTrue(acc.getBalance().intValue()==70);
        assertTrue(acc1.getBalance().intValue()==30);

    }

    @Test
    public void allTransactions_concurrent() throws SQLException, TransactionException {
        Account acc = user.createAccount();
        Account acc1= user.createAccount();
        Transaction.deposit(acc, new BigDecimal("100"), insert);
        ConcurrentTransfer one =  new ConcurrentTransfer("one", insert, 20, acc,acc1);
        ConcurrentTransfer two =  new ConcurrentTransfer("two", insert, 10, acc,acc1);
        ConcurrentDeposit three = new ConcurrentDeposit("three", insert, 10, acc);
        ConcurrentWithdraw four = new ConcurrentWithdraw("four", insert, 10, acc1);
        Thread t1 = new Thread(one);
        Thread t2 = new Thread(two);
        Thread t3 = new Thread(three);
        Thread t4 = new Thread(four);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        while(t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive()){

        }
        assertTrue(acc.getBalance().intValue()==80);
        assertTrue(acc1.getBalance().intValue()==20);

    }
 

    @AfterClass
    public static void clean() throws SQLException {
        DBClear.clearWallet();
        DBClear.clearAccount();
        DBClear.clearTransactions();
    }



}