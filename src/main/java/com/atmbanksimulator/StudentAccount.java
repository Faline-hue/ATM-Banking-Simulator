package com.atmbanksimulator;

import java.time.LocalDate;

// StudentAccount Class:
// - Subclass of BankAccount used to hold the information for Student Accounts
// - Inherits the methods from parent class
public class StudentAccount extends BankAccount {


    public StudentAccount() {}              // Constructor
    public StudentAccount(String a, String p, int b) {
        this.accNumber = a;                 // Holds the accounts number
        this.accPasswd = p;                 // Holds the accounts password
        this.balance = b;                   // Total amount of money on the account
        this.withdrawalLimit = 250;         // Limit on how much can be withdrawn daily
        this.dCapWD = 0;                    // Counts how much has been withdrawn daily
        this.dlyDepLimit = 3000;           // Limit on how much can be deposited daily
        this.dCapD = 0;                     // Counts how much has been deposited on current day
        this.ylyDepLimit = 20000;           // Limit on how much can be deposited yearly
        this.yCapD = 0;                     // Counts how much has been deposited this year
        this.acsDate = LocalDate.now();     // Default assignment of current date when created
    }
    // Uses method from superclass to withdraw cash from accounts balance
    // Updates the daily cap so user cannot take more than the cap
    public boolean withdraw( int amount ) {
        return super.withdraw(amount);
    }

    // Uses method from superclass to deposit cash into accounts balance
    // Updates the daily deposit cap so user cannot deposit more than their cap allows
    public boolean deposit( int amount ) { return super.deposit(amount);}

    // Uses method from superclass to compare last accessed date to current date
    // Utilised in daily deposit and withdrawal checks
    public boolean accessDate() { return super.accessDate();}

}
