package com.atmbanksimulator;

public class StudentAccount extends BankAccount {
    // Subclass of BankAccount used to hold the information for Student Accounts
    private int withdrawLimit = 0;
    private int dailyCap = 0;


    public StudentAccount() {}
    public StudentAccount(String w) {
        withdrawLimit = 250;



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
