package com.atmbanksimulator;

public class StudentAccount extends BankAccount {
    // Subclass of BankAccount used to hold the information for Student Accounts

    public StudentAccount() {}
    public StudentAccount(String a, String p, int b) {
        accNumber = a;
        accPasswd = p;
        balance = b;
        withdrawalLimit = 250;
        dailyCap = 0;
    }
    // Uses method from superclass to withdraw cash from accounts balance
    // Updates the daily cap so user cannot take more than the cap
    public boolean withdraw( int amount ) {
        return super.withdraw(amount);
    }

    /* Use a method to check if the withdrawal limit has been reached for the day,

         Everytime the account is accessed:
          - Check if current date == last accessed date
          - If the date hasn't changed, daily cap stays
          - If the date has changed, daily cap resets.
    */

    /* Take requested withdraw amount, compare to withdraw limit - daily cap to see if they can still withdraw */
}
