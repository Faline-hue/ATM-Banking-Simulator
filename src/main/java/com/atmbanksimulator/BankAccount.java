package com.atmbanksimulator;

// ===== 📚🌐BankAccount (Domain / Service / Business Logic) =====

// BankAccount class:
// - Stores instance variables for account number, password, and balance
// - Provides methods to withdraw, deposit, check balance, etc.
public class BankAccount {
    protected String accNumber = "";
    protected String accPasswd = "";
    protected int balance = 0;
    protected int withdrawalLimit = 0;
    protected int dailyCap = 0;

    public BankAccount() {}
    public BankAccount(String a, String p, int b) {
        accNumber = a;
        accPasswd = p;
        balance = b;
        withdrawalLimit = 500;
        dailyCap = 0;
    }

    // Withdraw money from this account.
    // Returns true if successful, or false if the amount is negative or exceeds the current balance.
    public boolean withdraw( int amount ) {
        if (amount < 0 || balance < amount ) {
            return false; // Needs to alert user that they don't have the funds to withdraw that amount
        } else if ( withdrawalLimit < amount || (withdrawalLimit - dailyCap) < amount){
            return false; // Needs to alert user that they only have "x" amount left that they can withdraw today
        }else {
            balance = balance - amount; // Subtract amount withdrawn
            dailyCap = dailyCap + amount; // Update daily cap
            return true;
        }
    }

    // deposit the amount of money into this account.
    // Return true if successful,or false if the amount is negative
    public boolean deposit( int amount ) {
        if (amount < 0) {
            return false;
        } else {
            balance = balance + amount;  // add amount to balance
            return true;
        }
    }

    // Getter for the account balance
    // Returns the current balance of this account
    public int getBalance() {
        return balance;
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

    protected String getAccountNumber(){
        return accNumber;
    }
    protected String getAccountPassword(){
        return accPasswd;
    }
    protected int getAccountBalance(){
        return balance;
    }

    public boolean checkAccountNumber(String s) {
        return(s.equals(getAccountNumber()));
    }
    public boolean checkPassword(String s) {
        return(s.equals(getAccountPassword()));
    }

    //temporary change password
    public boolean changePassword(String prevPassword, String newPassword) {
        if (checkPassword(prevPassword) && !checkPassword(newPassword)) {
            setAccountPassword(newPassword);
            return true;
        }
        else {
            return false;
        }
    }
}

