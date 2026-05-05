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
    protected int dlyDepLimit = 0;
    protected int dCapD = 0;
    protected int ylyDepLimit = 0;
    protected int yCapD = 0;
    protected LocalDate acsDate = null;

    public BankAccount() {}                 // Constructor
    public BankAccount(String a, String p, int b) {
        this.accNumber = a;                 // Holds the accounts number
        this.accPasswd = p;                 // Holds the accounts password
        this.balance = b;                   // Total amount of money on the account
        this.withdrawalLimit = 500;         // Limit on how much can be withdrawn daily
        this.dCapWD = 0;                    // Counts how much has been withdrawn on current day
        this.dlyDepLimit = 3000;           // Limit on how much can be deposited daily
        this.dCapD = 0;                     // Counts how much has been deposited on current day
        this.ylyDepLimit = 20000;           // Limit on how much can be deposited yearly
        this.yCapD = 0;                     // Counts how much has been deposited this year
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
    public int getDailyCapD() { return dCapD;}
    public int getDepositLimit() { return dlyDepLimit;}
    public int getYearlyCap() { return yCapD;}
    public int getYearlyLimit() { return ylyDepLimit;}
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
        } else if ( dlyDepLimit < amount || (dlyDepLimit - dCapD) < amount){
            return false; // Needs to alert user that they can only deposit "x" amount more today
        } else if ( ylyDepLimit < amount || (ylyDepLimit - yCapD) < amount){
            return false; // Needs to alert user that they can't deposit more than yearly allowance and must wait till 1st January
        }else {
            balance = balance + amount;  // Add amount to balance
            dCapD = dCapD + amount;     // Update daily cap
            yCapD = yCapD + amount;     // Update yearly cap
            return true;
        }
    }

    // Check if the time has passed since the account was last accessed
    // If it's a new day, the daily cap is reset
    public boolean accessDate() {
        LocalDate toCheck = firstOfJanuary();
        if (LocalDate.now().isAfter(this.acsDate)){          // If a day has passed since the user last accessed then
            if ( toCheck.isBefore(LocalDate.now()) && toCheck.isAfter(this.acsDate)){   // If it's passed January 1st
                this.yCapD = 0;                        // Their yearly deposit limit needs to be reset
            }
            this.dCapD = 0;                            // Their daily deposit limit needs to be reset
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

    // Method to retrieve the next yearly reset for deposit limit
    public LocalDate firstOfJanuary() {
        return this.acsDate.withMonth(1).withDayOfMonth(1).withYear(this.acsDate.getYear() + 1);
    }

    // Temporarily made separate getters and setters for subclasses - I feel like the public account number and password should be different?
    protected void setAccountNumber(String s){
        this.accNumber = s;
    }
    protected void setAccountPassword(String s){
        this.accPasswd = s;
    }
    protected void setAccountBalance(int i){
        this.balance = i;
    }

    public boolean checkAccountNumber(String s) {
        return(s.equals(getAccountNumber()));
    }
    public boolean checkPassword(String s) {
        return(s.equals(getAccountPassword()));
    }

    //temporary change password
    public void changePassword(String prevPassword, String newPassword) {
        if (checkPassword(prevPassword)){
            setAccountPassword(newPassword);
        }
    }
}

