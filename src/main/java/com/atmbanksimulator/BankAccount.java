package com.atmbanksimulator;

import java.io.Serializable;
import java.time.*;

// ===== 📚🌐BankAccount (Domain / Service / Business Logic) =====

// BankAccount class:
// - Stores instance variables for account number, password, and balance
// - Provides methods to withdraw, deposit, check balance, etc.
public class BankAccount implements Serializable {
    // Initialize variables
    protected String accNumber = "";
    protected String accPasswd = "";
    protected int balance = 0;
    protected int withdrawalLimit = 0;
    protected int dCapWD = 0;
    protected int depositLimit = 0;
    protected int dCapD = 0;
    protected LocalDate acsDate = null;

    public BankAccount() {}                 // Constructor
    public BankAccount(String a, String p, int b) {
        this.accNumber = a;                 // Holds the accounts number
        this.accPasswd = p;                 // Holds the accounts password
        this.balance = b;                   // Total amount of money on the account
        this.withdrawalLimit = 500;         // Limit on how much can be withdrawn daily
        this.dCapWD = 0;                    // Counts how much has been withdrawn on current day
        this.depositLimit = 3000;           // Limit on how much can be deposited daily
        this.dCapD = 0;                     // Counts how much has been deposited on current day
        this.acsDate = LocalDate.now();     // Default assignment of current date when created
    }

    // Getters - utilised by methods
    public String getAccountNumber(){
        return accNumber;
    }
    public String getAccountPassword(){
        return accPasswd;
    }
    public int getBalance() {
        return balance;
    }
    public int getWithdrawalLimit() { return withdrawalLimit;}
    public int getDailyCap() { return dCapWD;}
    public LocalDate getAcsDate() { return acsDate;}

    // Withdraw money from this account.
    // Returns true if successful, or false if the amount is negative or exceeds the current balance.
    public boolean withdraw( int amount ) {
        if (amount < 0 || balance < amount ) {
            return false; // Needs to alert user that they don't have the funds to withdraw that amount
        } else if ( withdrawalLimit < amount || (withdrawalLimit - dCapWD) < amount){
            return false; // Needs to alert user that they only have "x" amount left that they can withdraw today
        }else {
            balance = balance - amount;     // Subtract amount withdrawn from balance
            dCapWD = dCapWD + amount;   // Update daily cap
            return true;
        }
    }

    // Deposit the amount of money into this account.
    // Return true if successful,or false if the amount is negative to avoid errors
    public boolean deposit( int amount ) {
        if (amount < 0) {                // Check user has entered an amount
            // *Need to check if they have reached the daily/Annual limit for deposits*
            return false; // Needs to alert user that they haven't entered a number
        } else if( depositLimit < amount || (depositLimit - dCapD) < amount){
            return false; // Needs to alert user that they can only deposit "x" amount more today
        } else {
            balance = balance + amount;  // Add amount to balance
            dCapD = dCapD + amount;     // Update daily cap
            return true;
        }
    }

    // Check if the time has passed since the account was last accessed
    // If it's a new day, the daily cap is reset
    public boolean accessDate() {
        if (LocalDate.now().isAfter(this.acsDate)){          // If a day has passed since the user last accessed then
            this.dCapWD = 0;                           // Their daily withdrawal limit needs to be reset
            this.acsDate = LocalDate.now();
            return true;
        } else if (LocalDate.now().isBefore(this.acsDate)){  // Avoiding errors with system
            return false;
        } else if (LocalDate.now().isEqual(this.acsDate)){   // Nothing changes if the same date
            return false;
        }
        return false;
    }


    // Temporarily made separate getters and setters for subclasses - I feel like the public account number and password should be different?
    protected void setAccountNumber(String s){
        accNumber = s;
    }
    protected void setAccountPassword(String s){
        accPasswd = s;
    }
    protected void setAccountBalance(int i){
        balance = i;
    }

    protected boolean checkAccountNumber(String s) {
        return(s.equals(getAccountNumber()));
    }
    protected boolean checkPassword(String s) {
        return(s.equals(getAccountPassword()));
    }

    //temporary change password
    protected boolean changePassword(String s) {
        setAccountPassword(s);
        if (checkPassword(s)) {
            return true;
        }
        else {
            return false;
        }
    }
}

