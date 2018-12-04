package com.Wallet.app;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The Transaction class is a class of static methods that perfroms transactions on a specified account, 
 * records these transactions into the specified account and updates the database.
 * <p>
 * The transaction record is a string in this format:
 * "ID: {id} TYPE: {type} ACCOUNT: {account id} AMOUNT: {amount} TIMESTAMP: {date}"
 * where id is the uniquely generated transaction id, type is the type of transaction (withdrawal, deposit, transfered)
 * account is the account id and date is the current date in the format  E yyyy.MM.dd 'at' hh:mm:ss a zzz
 * 
 */

public class Transaction{
    
    
    /**
     * Takes the account and reduces it's balance by the amount specified, then records the transaction into the
     * specified account and updates the database to reflect the changes made.
     * 
     * @param account The account to withdraw from
     * @param amount The amount to be withdrawn. This value is rounded up to two decimal places
     * and must not be negative.
     *
     *  @param insert The SqlInsertManager used to update the database
     * @throws SQLException
     * @throws TransactionException Thrown when the amount specified is negative
     */
    public synchronized static void withdraw(Account account, BigDecimal amount, SqlInsertManager insert) throws SQLException, TransactionException {
        BigDecimal currentBalance = account.getBalance();
        if(currentBalance.compareTo(amount)==-1){
            throw new TransactionException("insufficient funds in this account to withdraw this amount");
        }
        if(amount.intValue()<0){
            throw new TransactionException("cannot withdraw negative amount");
        }
        amount = amount.setScale(2, RoundingMode.CEILING);
        account.setBalance(currentBalance.subtract(amount));
        insert.updateAccountBalance(account.getId(), currentBalance.subtract(amount));
        Transaction.recordTransaction("withdrawal", account, amount, insert);
    }
   
   
    /**
     * Takes the account and increases it's balance by the amount specified, then records the transaction into the
     * specified account and updates the database to reflect the changes made.
     * 
     * @param account The account to deposit to
     * @param amount The amount to be deposited. This value is rounded up to two decimal places
     * and must not be negative.
     * 
     * @param insert The SqlInsertManager used to update the database
     * @throws SQLException
     * @throws TransactionException Thrown when the amount specified is negative
     */
    public synchronized static void deposit(Account account, BigDecimal amount, SqlInsertManager insert) throws SQLException, TransactionException {
        BigDecimal currentBalance = account.getBalance();
        if(amount.intValue()<0){
            throw new TransactionException("cannot deposit negative amount");
        }
        amount = amount.setScale(2, RoundingMode.CEILING);
        account.setBalance(currentBalance.add(amount));
        
        insert.updateAccountBalance(account.getId(), currentBalance.add(amount));
        Transaction.recordTransaction("deposit", account, amount, insert);
    }

    
    /**
     * Withdraws the specified amount from one account and deposits the same amount into the other account, 
     * then records the transaction into the specified accounts and updates the database to reflect the changes made
     *
     *  @param take The account withdrawn from.
     * @param give The account deposited to.
     * @param amount The amount transfered.
     * @param insert The SqlInsertManager used to update the database.
     * @throws SQLException
     * @throws TransactionException
     */
    public synchronized static void transfer(Account take, Account give, BigDecimal amount, SqlInsertManager insert)
            throws SQLException, TransactionException {
        Transaction.withdraw(take, amount, insert);
        Transaction.recordTransaction("Transfered", take, amount, insert);
        Transaction.deposit(give, amount, insert);
        Transaction.recordTransaction("deposit", give, amount, insert);
    }

    private static void recordTransaction(String type, Account account, BigDecimal amount, SqlInsertManager insert) throws SQLException{
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        
        int id =insert.addTransaction(account.getId(), type, amount, ft.format(now)+"");
        String tran = "ID: " +id;
        tran+=" TYPE: "+type;
        tran+=" AMOUNT: "+amount.toString();
        tran+=" ACCOUNT: "+account.getId();
        tran+=" TIMESTAMP: "+ft.format(now);
        account.addToHistory(tran);
    }

}