package com.atmbanksimulator;

// ===== Bank (Domain / Service / Business Logic) =====

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Bank class: a simple implementation of a bank, containing a list of bank accounts
*  and has a currently logged-in account (loggedInAccount).
*
*   . Created by main upon program start
*   . New bank accounts are added to the array contained within this class using addBankAccount
*/
public class Bank implements Serializable {

    /*
     ToDO: Optional extension:
     Improve account management in the Bank class:
     Refactor addBankAccount and login methods to leverage ArrayList.
    */

    // Instance variables storing bank information
    List<BankAccount> accounts = new ArrayList<BankAccount>();        // Array to hold BankAccount objects
    private BankAccount loggedInAccount = null;                                  // Currently logged-in account ('null' if no one is logged in)

    // A method to create new BankAccount - this is known as a 'factory method' and is a more
    // flexible way to do it than just using the 'new' keyword directly.
    public BankAccount makeBankAccount(String accNumber, String accPasswd, int balance) {
        return new BankAccount(accNumber, accPasswd, balance);
    }
    // A method to create a new StudentAccount -
    public BankAccount makeStudentAccount(String accNumber, String accPasswd, int balance){
        return new StudentAccount(accNumber, accPasswd, balance);
    }

    // A method to add a new bank account to the bank - it returns true if it succeeds
    // or false if it fails (because the bank is 'full')
    public boolean addBankAccount(BankAccount a) {
        if (a != null) {
            accounts.add(a); // adds new account
            Collections.sort(accounts,
                    (o1, o2) -> o1.getAccountNumber().compareTo(o2.getAccountNumber()));
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
                loggedInAccount.accessDate();
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
            loggedInAccount.deposit(amount);
            return true;
        } else {
            return false;
        }
    }


    // Attempt to withdraw money from the currently logged-in account
    // by calling the withdrawal method of the BankAccount object
    public boolean withdraw(int amount)
    {
        if (loggedIn()) {
            if(loggedInAccount.withdraw(amount)){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // Get the currently logged-in account's balance
    // by calling the getBalance method of the BankAccount object
    public int getBalance()
    {
        if (loggedIn()) {
            return loggedInAccount.getBalance();
        } else {
            return -1; // Use -1 as an indicator of an error
        }
    }
    // Get the currently logged-in account's withdrawn money today
    // by calling the getDailyCap method of the BankAccount object
    public int getDailyCap()
    {
        if (loggedIn()) {
            return loggedInAccount.getDailyCap();
        } else {
            return -1; // Use -1 as an indicator of an error
        }
    }
    // Get the currently logged-in account's withdrawal limit
    // by calling the getWithdrawalLimit method of the BankAccount object
    public int getWithdrawalLimit()
    {
        if (loggedIn()) {
            return loggedInAccount.getWithdrawalLimit();
        } else {
            return -1; // Use -1 as an indicator of an error
        }
    }
    // Get the remaining possible balance that can be withdrawn today from the currently logged-in account
    // by calling the getWithdrawalLimit and getDailyCap methods
    public int getLimit(){
        if (loggedIn()) {
            return (getWithdrawalLimit() - getDailyCap());
        } else {
            return 0;  // If error occurs, tell user that there is nothing left to take today
        }
    }

    // attempt to change password of the logged-in account
    public boolean changePassword(String p) {
        if (loggedIn()) {
            return loggedInAccount.changePassword(p);
        }
        else {
            return false;
        }
    }

    public boolean checkPassword(String p) {
        if (loggedIn()) {
            return loggedInAccount.checkPassword(p);
        }
        else {
            return false;
        }
    }

    public boolean validatePassword(String p) {
        if (loggedIn() && /*enter password rules here*/ true) {
            return true;
        }
        else {
            return false;
        }
    }
    // Used to read the array list of accounts in UIModel
    public List json() {
        return accounts;
    }

    /*public void load() {
        ObjectMapper mapper = new ObjectMapper();
        List<BankAccount> jsonList = mapper.readValue(new File("output.json"), new TypeReference<List>(){});
        accounts = jsonList;
        System.out.println(accounts);
    }*/
    public List load() {
        try{
            FileInputStream fis = new FileInputStream("bank.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<BankAccount> accounts1 = (List<BankAccount>) ois.readObject();
            ois.close();
            return accounts1;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
