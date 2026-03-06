package com.atmbanksimulator;

public class PrimeAccount extends BankAccount {
    private int withdrawalLimit;

    public PrimeAccount(){}
    public PrimeAccount(String a, String p, int b){
        setAccountNumber(a);
        setAccountPassword(p);
        setAccountBalance(b);
        withdrawalLimit = 300;
    }

    public PrimeAccount(String a, String p, int b, int w) {
        setAccountNumber(a);
        setAccountPassword(p);
        setAccountBalance(b);
        withdrawalLimit = w;
    }

    // Withdraw money from this account.
    // Returns true if successful, or false if the amount is negative or exceeds the current balance or withdrawal limit
    public boolean withdraw( int amount ) {
        if (amount < 0 || amount > withdrawalLimit || getAccountBalance() < amount) {
            return false;
        } else {
            setAccountBalance(getAccountBalance() - amount);  // subtract amount from balance
            return true;
        }
    }
}
