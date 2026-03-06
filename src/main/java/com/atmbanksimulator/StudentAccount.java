package com.atmbanksimulator;

public class StudentAccount extends BankAccount {
    // Subclass of BankAccount used to hold the information for Student Accounts
    private int withdrawLimit;

    public StudentAccount() {}
    public StudentAccount(String a, String p, int b) {
        setAccountNumber(a);
        setAccountPassword(p);
        setAccountBalance(b);
        withdrawalLimit = 250;
    }
    
        public StudentAccount(String a, String p, int b, int w){
        setAccountNumber(a);
        setAccountPassword(p);
        setAccountBalance(b);
        withdrawalLimit = w;
    }

    // Withdraw money from this account.
    // Returns true if successful, or false if the amount is negative or exceeds the current balance.
    public boolean withdraw( int amount ) {
        if (amount < 0 || amount > withdrawalLimit || getAccountBalance() < amount) {
            return false;
        } else {
            setAccountBalance(getAccountBalance() - amount);  // subtract amount from balance
            return true;
        }
    }
    
    /* Use a method to check if the withdrawal limit has been reached for the day,

         Everytime the account is accessed:
          - Check if current date == last accessed date
          - If the date hasn't changed, daily cap stays
          - If the date has changed, daily cap resets.
    */

    /* Take requested withdraw amount, compare to withdrawlimit - dailycap to see if they can still withdraw
    *
    * */
}
