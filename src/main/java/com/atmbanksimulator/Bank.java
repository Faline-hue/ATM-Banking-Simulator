package com.atmbanksimulator;

// ===== Bank (Domain / Service / Business Logic) =====

/* Bank class: a simple implementation of a bank, containing a list of bank accounts
*  and has a currently logged-in account (loggedInAccount).
*
*   . Created by main upon program start
*   . New bank accounts are added to the array contained within this class using addBankAccount
*/
public class Bank {

    /*
     ToDO: Optional extension:
     Improve account management in the Bank class:
     Replace Array with ArrayList for managing BankAccount objects.
     Refactor addBankAccount and login methods to leverage ArrayList.
    */

    // Instance variables storing bank information
    final int maxAccounts = 10;                       // Maximum number of accounts the bank can hold
    private int numAccounts = 0;                        // Current number of accounts in the bank
    final BankAccount[] accounts = new BankAccount[maxAccounts];  // Array to hold BankAccount objects
    private BankAccount loggedInAccount = null;         // Currently logged-in account ('null' if no one is logged in)

    // A method to create new BankAccount - this is known as a 'factory method' and is a more
    // flexible way to do it than just using the 'new' keyword directly.
    public BankAccount makeBankAccount(String accNumber, String accPasswd, int balance) {
        return new BankAccount(accNumber, accPasswd, balance);
    }
    // a method to create a new StudentAccount -
    public BankAccount makeStudentAccount(String accNumber, String accPasswd, int balance){
        return new StudentAccount(accNumber, accPasswd, balance);
    }

    // a method to add a new bank account to the bank - it returns true if it succeeds
    // or false if it fails (because the bank is 'full')
    public boolean addBankAccount(BankAccount a) {
        if (numAccounts < maxAccounts) {
            accounts[numAccounts] = a; // adds new account in next space (X-1 = last account in array)
            numAccounts++ ;
            return true;
        } else {
            return false;
        }
    }

    // Variant of addBankAccount: creates a BankAccount and adds it in one step.
    // This is an example of method overloading: two methods can share the same name
    // if they have different parameter lists.
    //
     // Checks what type of account the user has, used to verify withdrawal limits and daily caps

    public boolean addBankAccount(String accNumber, String accPasswd, int balance, String accType) {
        return switch (accType) {
            case "Basic" -> addBankAccount(makeBankAccount(accNumber, accPasswd, balance));
            case "Student" -> addBankAccount(makeStudentAccount(accNumber, accPasswd, balance));
            default -> false;
        };
    }

    // Check whether the given accountNumber and password match an existing BankAccount.
    // If successful, set 'loggedInAccount' to that account and return true.
    // Otherwise, set 'loggedInAccount' to null and return false.

    public boolean login(String accountNumber, String password) {
        logout(); // logout of any previous loggedInAccount

        // Search the accounts array to find a BankAccount with a matching accountNumber and password.
        // - If found, set 'loggedInAccount' to that account and return true.
        // - If not found, reset 'loggedInAccount' to null and return false.
        for (BankAccount b: accounts) {
            if (b.checkAccountNumber(accountNumber) && b.checkPassword(password)) {
                // found the right account
                loggedInAccount = b;
                return true;
            }
        }
        // not found - return false
        loggedInAccount = null;
        return false;
    }

    // Log out of the currently logged-in account, if any
    public void logout() {
        if (loggedIn()) {
            loggedInAccount = null;
        }
    }

    // Check if an account is logged in
    public boolean loggedIn() {
        return loggedInAccount != null;
    }


    // Attempt to deposit money into the currently logged-in account
    // by calling the deposit method of the BankAccount object
    public boolean deposit(int amount)
    {
        if (loggedIn()) {
            return loggedInAccount.deposit(amount);
        } else {
            return false;
        }
    }


    // Attempt to withdraw money from the currently logged-in account
    // by calling the withdrawal method of the BankAccount object

    // Needs to be made to check the withdrawal limit
    public boolean withdraw(int amount)
    {
        if (loggedIn()) {
            return loggedInAccount.withdraw(amount);
        } else {
            return false;
        }
    }

    // Get the currently logged-in account balance
    // by calling the getBalance method of the BankAccount object
    public int getBalance()
    {
        if (loggedIn()) {
            return loggedInAccount.getBalance();
        } else {
            return -1; // use -1 as an indicator of an error
        }
    }
}
