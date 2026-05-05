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

    // The ATM UIModel can be in one of eight states:
    // We represent each state with a String constant.
    // The 'final' keyword ensures these values cannot be changed.

    // New states:
    private final String STATE_WELCOME_PAGE = "welcome";    // 1. Default page upon start up, returns here after goodbye page
    private final String STATE_SIGNIN_PAGE = "Sign_In";     // 2. Sign-In page, waiting for account number and password
    private final String STATE_MAINMENU_PAGE = "Main_Menu"; // 3. Logged in (awaiting choice)
    private final String STATE_WITHDRAW_PAGE = "Withdraw";  // 4. Waiting for user to select amount to withdraw
    private final String STATE_DEPOSIT_PAGE = "Deposit";    // 5. Waiting for user to enter amount to deposit
    private final String STATE_BALANCE_PAGE = "Balance";    // 6. Showing balance of currently logged in account
    private final String STATE_CHANGE_PASS = "Change_Pass"; // 7. Sign-In page, allows user to change password
    private final String STATE_GOODBYE_PAGE = "Change_Pass"; // 8. Goodbye page, says goodbye to user, loops back to welcome page


    // Variables representing the state and data of the ATM UIModel
    private String state = STATE_SIGNIN_PAGE;    // Current state of the ATM
    private String accNumber = "";              // Account number being typed
    private String accPasswd = "";              // Password being typed
    private String curPasswd = "";              // Current password being type
    private String newPasswd = "";              // New password being typed

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
        setState(STATE_SIGNIN_PAGE);
        numberPadInput = "";
        message = "Sign-In";
        result = "Enter your Account Number and Password";
        update();
    }

    // Reset the ATM UIModel after an invalid action or logout:
    // - Set state to STATE_ACCOUNT_NO
    // - Clear the numberPadInput
    // - Display the provided message and user instructions
    private void reset(String msg) {
        setState(STATE_SIGNIN_PAGE);
        view.signInPage(View.stage);
        numberPadInput = "";
        message = "Sign-In";
        result = "An error has occured, \n please Sign-in again";
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
        numberPadInput += numberOnButton;
        update();
    }

    // Handle the Clear button: reset the current number stored in numberPadInput
    public void processClear() {
        if (!numberPadInput.isEmpty()) {
            numberPadInput = "";
            message = "Input Cleared";
            update();
        }
    }

    // Handle the Enter button.
    // This is a more complex method: pressing Enter causes the ATM to change state,
    // progressing from STATE_SIGNIN_PAGE → STATE_MAINMENU_PAGE,
    public void processEnter()
    {
        // The action depends on the current ATM state
        switch ( state )
        {
            case STATE_SIGNIN_PAGE:
                    // Waiting for user's account details
                    // Will attempt to log in with given details
                accPasswd = numberPadInput;
                numberPadInput = "";
                if ( bank.ch)
                if ( bank.login(accNumber, accPasswd) )
                {
                    // Successful login: change state to STATE_MAINMENU_PAGE and provide instructions
                    setState(STATE_MAINMENU_PAGE);
                    hideScene();
                    view.mainMenu(View.stage);
                    message = "Main Menu";
                    result = "Please select the option you would like to access";

                } else {
                    // Login failed: reset ATM and display error
                    message = "Login failed: Unknown Account/Password";
                    System.out.println(accNumber + " " + accNumber);
                    reset(message);
                }
                break;
            case STATE_CHANGE_PASS:
                    // Waiting for user's password details
                    // Will confirm password details
                newPasswd = numberPadInput;
                numberPadInput = "";
                if (bank.checkPassword(curPasswd)){
                    // Current password entered correctly
                    bank.changePassword(curPasswd, newPasswd);
                    result = "Password successfully updated\nYou may now return to menu";
                    saveRead();
                    save();
                } else if(curPasswd == newPasswd){
                    // Password isn't new
                    result = "New password is identical to current password\nPlease enter a new password";
                }else {
                    // Current password entered incorrectly
                    result = "Password incorrect\nPlease ensure current password is correct";
                }
                break;
            case STATE_DEPOSIT_PAGE:
                    // Waiting for user's deposit
                    // Will confirm daily deposit limit
                processDeposit();

            default:
                // Do nothing for other states (user is already logged in)
        }

        update(); // Refresh the GUI to show messages and input
    }

    // Handle Main Menu selection
    // Complex method: selecting a menu option causes the ATM to change state,
    // progressing from STATE_MAINMENU_PAGE → a process stage, or back to Main Menu from process
    public void stageManager(String action){
        // The action is the button input
        switch (action) {
            case "Withdraw":
                setState(STATE_WITHDRAW_PAGE);
                hideScene();
                view.withdraws(View.stage);
                message = "Select Amount";
                result = "How much would you like to withdraw?";
                break;
            case "Deposit":
                setState(STATE_DEPOSIT_PAGE);
                hideScene();
                view.deposit(View.stage);
                message = "Enter amount to deposit";
                result = "Please enter the amount you want to deposit";
                break;
            case "Balance":
                setState(STATE_BALANCE_PAGE);
                hideScene();
                view.balance(View.stage);
                message = "Your account balance is:";
                result = Integer.toString(bank.getBalance());
                break;
            case "Change Password":
                setState(STATE_CHANGE_PASS);
                hideScene();
                view.chngPass(View.stage);
                message = "Changing Password";
                result = "Please enter your current password\nand your new password";
                break;
            case "Return to menu":
                setState(STATE_MAINMENU_PAGE);
                hideScene();
                view.mainMenu(View.stage);
                message = "Main Menu";
                result = "Please select the option you would like to access";
                break;
            case "Sign-Out":
                setState(STATE_GOODBYE_PAGE);
                // INSERT GOODBYE PAGE SCENE AND THEN RETURN TO WELCOME PAGE
                break;

        }
        update();
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

    /*
    // Handle the Balance button:
    // - If the user is logged in, retrieve the current balance and update messages/results accordingly
    // - Otherwise, reset the ATM and display an error message
    public void processBalance() {
        if (state.equals(STATE_LOGGED_IN) ) {
            numberPadInput = "";
            message = "Balance Available";
            result = "Your Balance is: " + bank.getBalance();
        } else {
            reset("You are not logged in");
        }
        update();
    }
    */

    // Handle the Withdraw button:
    // - If the user is logged in, attempt to withdraw the amount entered;
    //  - If the user has exceeded their withdrawal limit for the day it will fail and notify them
    //  - If the user has insufficient funds it will fail and notify them
    // - otherwise, reset the ATM and display an error message.
    // - Reads the amount from numberPadInput, validates it, and updates messages/results accordingly.
    public void processWithdraw(String action) {
        if (state.equals(STATE_WITHDRAW_PAGE)) {
            int amount = 0;
            if (action != "Custom") {
                System.out.println(action);
                amount = Integer.parseInt(action.replace("£", ""));
            }
            if (amount > 0) {
                if (bank.withdraw(amount)) {
                    // If balance allows, will return true
                    message = "Withdraw Successful";
                    result = "Withdrawn: " + amount;
                } else if (!bank.withdraw(amount)) {
                    // If balance doesn't allow, will return false
                    if (amount > bank.getBalance() ){
                        // Amount is larger than user's balance
                        message = "Withdraw Failed";
                        result = "Insufficient Funds";
                    } else {
                        if (bank.getDailyCapWD() == bank.getWithdrawalLimit()) {
                            // Daily withdraw limit is reached
                            message = "Withdraw Failed";
                            result = "You've reached your withdraw limit for the day";
                        } else if (amount > (bank.getWithdrawalLimit() - bank.getDailyCapWD())) {
                            // Not enough left on user's withdraw limit
                            message = "Withdraw Failed";
                            result = "You can only withdraw " + bank.getWDLimit() + " more today";
                        } else {
                            // Fallback statement
                            message = "Withdraw Failed";
                            result = "Insufficient Funds";
                        }
                    }
                } else {
                    // Fallback statement
                    message = "Withdraw Failed";
                    result = "Insufficient Funds";
                }
            } else {
                // Fallback in event of error
                message = "Invalid Amount";
                result = "An error has occured \nPlease reselect how much you want to withdraw";
            }
            numberPadInput = "";
        }
        else {
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
        if (state.equals(STATE_DEPOSIT_PAGE)) {
            int amount = parseValidAmount(numberPadInput);
            if (amount > 0) {
                if (bank.deposit(amount)) {
                    // If balance allows, will return true
                    message = "Deposit Successful";
                    result = "Deposited: " + amount;
                } else if (!bank.deposit(amount)) {
                    // If balance doesn't allow, will return false
                    if (bank.getYearlyLimit() == bank.getYearlyCapD()) {
                        // Reached yearly limit
                        message = "Deposit Failed";
                        result = "You have reached your deposit limit for the year \nYour deposit limit will reset on " + bank.getResetDate();
                    } else if (amount > (bank.getYearlyLimit() - bank.getYearlyCapD())) {
                        // Not enough left on user's yearly limit
                        message = "Deposit Failed";
                        result = "You can only deposit " + (bank.getYearlyLimit() - bank.getYearlyCapD()) + " more this year";
                    }else {
                        if (bank.getDailyCapD() == bank.getDlyDepositLimit()) {
                            // Daily deposit limit is reached
                            message = "Deposit Failed";
                            result = "You've reached your deposit limit for the day";
                        } else if (amount > (bank.getDlyDepositLimit() - bank.getDailyCapD())) {
                            // Not enough left on user's deposit limit
                            message = "Deposit Failed";
                            result = "You can only deposit " + bank.getDepLimit() + " more today";
                        } else {
                            // Fallback statement
                            message = "Deposit Failed";
                            result = "";
                        }
                    }
                } else {
                    // Fallback statement
                    message = "Deposit Failed";
                    result = "";
                }
            } else {
                // Fallback in event of error
                message = "Invalid Amount";
                result = "An error has occured \nPlease reenter how much you want to deposit";
            }
            numberPadInput = "";
        } else{
            reset("You are not logged in");
        }
        save(); //- Will save data to a serialized file for loading
        saveRead(); // Will save data to a readable file for testing
        update();
    }

    // Handle the Finish button:
    // - If the user is logged in, log out
    // - Otherwise, reset the ATM and display an error message
    public void processFinish() {
        if (state.equals(STATE_MAINMENU_PAGE) ) {
            reset("Thank you for using the Bank ATM");
            bank.logout();
            view.signInPage(View.stage);
        } else {
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

    // Handle clicking on text field during sign-in:
    public void processClick(String action){
        switch (action){
            case "acc":
                accPasswd = numberPadInput;
                break;
            case "pass":
                accNumber = numberPadInput;
                break;
            case "curpass":
                curPasswd = numberPadInput;
                break;
            case "newpass":
                newPasswd = numberPadInput;
                break;
        }
        numberPadInput = "";
        update();
    }

    // Notify the View of changes by calling its update method
    private void update() {
        view.update(message,numberPadInput, result);
    }

    // Hide previous scene
    private void hideScene() {
        view.hideScene();
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

