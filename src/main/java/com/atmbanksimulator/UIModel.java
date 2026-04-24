package com.atmbanksimulator;

// ===== UIModel =====

// The UIModel represents all the actual content and functionality of the app
// For the ATM, it keeps track of the information shown in the display
// (the laMsg and two tfInput boxes), and the interaction with the bank,
// executes commands provided by the controller and tells the view to update when
// something changes

import tools.jackson.databind.ObjectMapper;

import java.io.*;

public class UIModel {
    View view; // Reference to the View (part of the MVC setup)
    private Bank bank; // The ATM communicates with this Bank

    // The ATM UIModel can be in one of three states:
    // We represent each state with a String constant.
    // The 'final' keyword ensures these values cannot be changed.
    private final String STATE_ACCOUNT_NO = "account_no"; // 1. Waiting for an account number
    private final String STATE_PASSWORD = "password";     // 2. Waiting for a password
    private final String STATE_LOGGED_IN = "logged_in";   // 3. Logged in (ready to process requests)
    // i added this state:
    private final String STATE_CHANGE_PASSWORD = "change_password";

    // Variables representing the state and data of the ATM UIModel
    private String state = STATE_ACCOUNT_NO;    // Current state of the ATM
    private String accNumber = "";              // Account number being typed
    private String accPasswd = "";              // Password being typed

    // Variables shown on the View display
    private String message;                // Message label text
    private String numberPadInput;         // Current number displayed in the TextField (as a string)
    private String result;                 // Contents of the TextArea (Could be multiple lines)

    // UIModel constructor: pass a Bank object that the ATM interacts with
    public UIModel(Bank bank) {
        this.bank = bank;
    }

    // Initialise the ATM UIModel: this method is called by Main when starting the app
    // - Set state to STATE_ACCOUNT_NO
    // - Clear the numberPadInput - numbers displayed in the TextField
    // - Display the welcome message and user instructions
    public void initialise() {
        setState(STATE_ACCOUNT_NO);
        numberPadInput = "";
        message = "Welcome to the ATM";
        result = "Enter your account number\nFollowed by \"Ent\"";
        update();
    }

    // Reset the ATM UIModel after an invalid action or logout:
    // - Set state to STATE_ACCOUNT_NO
    // - Clear the numberPadInput
    // - Display the provided message and user instructions
    private void reset(String msg) {
        setState(STATE_ACCOUNT_NO);
        numberPadInput = "";
        message = msg;
        result = "Enter your account number\nFollowed by \"Ent\"";
    }

    // Change the ATM state and print a debug message whenever the state changes
    private void setState(String newState)
    {
        if ( !state.equals(newState) )
        {
            String oldState = state;
            state = newState;
            System.out.println("UIModel::setState: changed state from "+ oldState + " to " + newState);
        }
    }

    // These process**** methods are called by the Controller
    // in response to specific button presses on the GUI.

    // Handle a number button press: append the digit to numberPadInput
    public void processNumber(String numberOnButton) {
        // Optional extension:
        // Improve feedback by showing what the number is being entered for based on the current state.
        // e.g.  if state is STATE_ACCOUNT_NO, display "Receiving Account Number, Beep 5 received"
        numberPadInput += numberOnButton;
        message = "Beep! " + numberOnButton + " received";
        update();
    }

    // Handle the Clear button: reset the current number stored in numberPadInput
    public void processClear() {
        // Optional extension:
        // Improve feedback by showing what was cleared depending on the current state.
        // e.g. if state is STATE_ACCOUNT_NO, display "Account Number cleared: 123"
        if (!numberPadInput.isEmpty()) {
            numberPadInput = "";
            message = "Input Cleared";
            update();
        }
    }

    // Handle the Enter button.
    // This is a more complex method: pressing Enter causes the ATM to change state,
    // progressing from STATE_ACCOUNT_NO → STATE_PASSWORD → STATE_LOGGED_IN,
    // and back to STATE_ACCOUNT_NO when logging out.
    public void processEnter()
    {
        // The action depends on the current ATM state
        switch ( state )
        {
            case STATE_ACCOUNT_NO:
                // Waiting for a complete account number
                // If nothing was entered, reset with "Invalid Account Number"
                if (numberPadInput.isEmpty()) {
                    message = "Invalid Account Number";
                    reset(message);
                }
                else{
                    // Save the entered number as accNumber, clear numberPadInput,
                    // update the state to expect password, and provide instructions
                    accNumber = numberPadInput;
                    numberPadInput = "";
                    setState(STATE_PASSWORD);
                    message = "Account Number Accepted";
                    result = "Now enter your password\nFollowed by \"Ent\"";
                }
                break;

            case STATE_PASSWORD:
                    // Waiting for a password
                    // Save the typed number as accPasswd, clear numberPadInput,
                    // then contact the bank to attempt login
                accPasswd = numberPadInput;
                numberPadInput = "";
                if ( bank.login(accNumber, accPasswd) )
                {
                    // Successful login: change state to STATE_LOGGED_IN and provide instructions
                    setState(STATE_LOGGED_IN);
                    message = "Logged In";
                    result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)";
                } else {
                    // Login failed: reset ATM and display error
                    message = "Login failed: Unknown Account/Password";
                    reset(message);
                }
                break;
            case STATE_CHANGE_PASSWORD:
                accPasswd = numberPadInput;
                numberPadInput = "";
                if (accPasswd.isEmpty()) {
                    message = "Input empty";
                    result = "Password not changed; type new password and press 'enter'"; // <- also needs to explain new password rules
                }
                else if (bank.validatePassword(accPasswd)) {
                    if (bank.changePassword(accPasswd)) {
                        message = "Password changed";
                        result = "Valid password entered; please keep track of your new password and use it to log in in future";
                    }
                    else {
                        message = "Password not changed";
                        result = "Input was valid but another error occurred; your previous password will still be used to log in";
                    }
                    setState(STATE_LOGGED_IN); // change to logged-in state if valid password was entered, whether or not password was changed
                    // if password wasn't changed for a different reason to the input not being valid, entering new input won't help
                }
                else {
                    message = "Password not changed";
                    result = "Invalid input; please enter new password and press 'enter'"; // also explain password rules
                }
                break;
            case STATE_LOGGED_IN:
            default:
                // Do nothing for other states (user is already logged in)
        }

        update(); // Refresh the GUI to show messages and input
    }

    /**
     * Parses a string into a valid transaction amount.
     * - If the string is empty, invalid, or consists only of zeros, returns 0.
     * - Otherwise, returns the integer value.
     * Purpose:
     * Helper method for validating user-entered amounts in transactions (Deposit, Withdraw, etc.).
     * Note: If you later add features like Transfer, this method can be reused.
     */
    private int parseValidAmount(String number) {
        if (number.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0; // Invalid input -> treated as 0
        }
    }

    // Handle the Balance button:
    // - If the user is logged in, retrieve the current balance and update messages/results accordingly
    // - Otherwise, reset the ATM and display an error message
    public void processBalance() {
        switch (state) {
            case STATE_CHANGE_PASSWORD: break;
            case STATE_LOGGED_IN:
                numberPadInput = "";
                message = "Balance Available";
                result = "Your Balance is: " + bank.getBalance();
                break;
            default:
                reset("You are not logged in");
        }
        update();
    }

    // Handle the Withdraw button:
    // - If the user is logged in, attempt to withdraw the amount entered;
    //  - If the user has exceeded their withdrawal limit for the day it will fail and notify them
    //  - If the user has insufficient funds it will fail and notify them
    // - otherwise, reset the ATM and display an error message.
    // - Reads the amount from numberPadInput, validates it, and updates messages/results accordingly.
    public void processWithdraw() {
        switch (state) {
            case STATE_CHANGE_PASSWORD: break;
            case STATE_LOGGED_IN:
                int amount = parseValidAmount(numberPadInput);
                if (amount > 0) {
                    if(bank.withdraw( amount )){
                        message = "Withdraw Successful";
                        result = "Withdrawn: " + numberPadInput;
                    }
                    else if(!bank.withdraw( amount)){
                        if(bank.getDailyCap() == bank.getWithdrawalLimit()){
                            message = "Withdraw Failed: You've reached your withdraw limit for the day";
                            result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)";
                        } else if(amount > (bank.getWithdrawalLimit() - bank.getDailyCap())){
                            message = "Withdraw Failed: You can only withdraw " + bank.getLimit() + " more today";
                            result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)";
                        } else {
                            message = "Withdraw Failed: Insufficient Funds";
                            result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)";
                        }
                    } else {
                        message = "Withdraw Failed: Insufficient Funds";
                        result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)";
                    }
                }
                else{
                    message = "Invalid Amount";
                    result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)";
                }
                numberPadInput = "";
                break;
            default:
            reset("You are not logged in");
        }
        save(); //- Will save data to a serialized file for loading
        saveRead(); // Will save data to a readable file for testing
        update();
    }

    // Handle the Deposit button:
    // - If the user is logged in, deposit the amount entered into the bank
    // - Reads the amount from numberPadInput, validates it, and updates messages/results accordingly
    // - Otherwise, reset the ATM and display an error message
    public void processDeposit() {
        switch (state) {
            case STATE_CHANGE_PASSWORD: break;
            case STATE_LOGGED_IN:
                int amount = parseValidAmount(numberPadInput);
                if (amount > 0) {
                    bank.deposit(amount);
                    message = "Deposit Successful";
                    result = "Deposited: " + numberPadInput;
                }
                else {
                    message = "Invalid Amount";
                    result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)";
                }
                numberPadInput = "";
                break;
            default:
                reset("You are not logged in");
        }
        save(); //- Will save data to a serialized file for loading
        saveRead(); // Will save data to a readable file for testing
        update();
    }

    // NOT WORKING YET
    // possibly change UI because the process of this is really confusing and could cause a lot of problems if a user messes it up
    // Handle the Change Password button:
    public void processChangePassword() {
        switch (state) {
            case STATE_CHANGE_PASSWORD: break;
            case STATE_LOGGED_IN:
                accPasswd = numberPadInput;
                numberPadInput = "";
                if ( bank.checkPassword(accPasswd) )
                {
                    // Correct password entered
                    setState(STATE_CHANGE_PASSWORD); // changes the state so that when enter is pressed again it will do validate password step
                    message = "Password correct";
                    result = "Type new password and press 'enter'"; // explain rules new password must follow somewhere
                } else {
                    // incorrect password entered - not sure whether to log out or not
                    // but bank.changePassword method won't change the state to allow changing password if it's returning false
                    // reset("Password incorrect, logging out for security reasons") // <- resets to asking for account number, logging the user out
                    message = "Password incorrect";
                    result = "Try again or contact the bank for help"; // <- keeps user logged in, lets them enter password again (maybe the better option because if they're logged in they obviously know their password so could be a typo)
                }
                break;
            default:
                reset("You are not logged in");
        }
        update();
    }

    // Handle the Finish button:
    // - If the user is logged in, log out
    // - Otherwise, reset the ATM and display an error message
    public void processFinish() {
        switch (state) {
            case STATE_CHANGE_PASSWORD: break;
            case STATE_LOGGED_IN:
                reset("Thank you for using the Bank ATM");
                bank.logout();
                break;
            default:
                reset("You are not logged in");
        }
        update();
    }

    // Handle unknown or invalid buttons for the current state:
    // - Reset the ATM and display an "Invalid Command" message
    public void processUnknownKey(String action) {
        reset("Invalid Command");
        update();
    }

    // Notify the View of changes by calling its update method
    private void update() {
        view.update(message,numberPadInput, result);
    }

    // Writes the accounts array list to JSON file - Readable for testing
    private void saveRead() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            // Writing the list directly into a JSON file
            objectMapper.writeValue(new File("output.json"), bank.json());
            System.out.println("JSON array has been written to output.json file.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Save the Bank object to a serialized file so it can be reloaded when program is next run
    public void save() {
        // Test serialization to local file
        try (
                FileOutputStream fileOut = new FileOutputStream("bank.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(bank.accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/*
TO DO
- maybe change reset function to work to reset to different states?
    - current version has it log out to asking for account number every time
    - which doesn't seem super helpful in situations like mis-typing the current password when trying to change it
    - although technically it would be more secure as if the password was mis-typed then  the user should know the information to log in again
- also maybe make the reset function clear all variables that were entered past a certain point
    - technically it should be secure as is cause the variables are only accessed via functions that overwrite them
    - but it just feels weird having the account number variable still be the account number of the last account that was logged in while the atm is idle
- add some kind of "cancel" button for multiple-step interactions
- change various if/elses in methods to switches
- also a problem where the reset() method doesn't log the current account out but does send it back to asking for an account number
    - so a user might assume it's logged them out after an invalid action and just leave
    - like with the previous variables you can't do anything on the logged in account directly but it feels weird to have the logged in account just hanging around like that
- add methods to validate amounts to withdraw and deposit for individual accounts/account types and use those in the processWithdraw() and processDeposit() methods
 */