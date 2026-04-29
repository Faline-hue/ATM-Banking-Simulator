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

    // New states:
    // The ATM UIModel can be in one of these states:
    private final String STATE_WELCOME_PAGE = "welcome";    // 1. Default page upon start up, returns here after goodbye page
    private final String STATE_SIGNIN_PAGE = "Sign_In";     // 2. Sign-In page, waiting for account number and password
    private final String STATE_MAINMENU_PAGE = "Main_Menu"; // 3. Logged in (awaiting choice)
    private final String STATE_WITHDRAW_PAGE = "Withdraw";  // 4. Waiting for user to select amount to withdraw
    private final String STATE_DEPOSIT_PAGE = "Deposit";    // 5. Waiting for user to enter amount to deposit
    private final String STATE_BALANCE_PAGE = "Balance";    // 6. Showing balance of currently logged in account
    private final String STATE_CHANGE_PASS = "Change_Pass"; // 7. Sign-In page, allows user to change password
    private final String STATE_WELCOME_PG = "Welcome";    // 1. Default page when atm is accessed
    private final String STATE_LOGOUT_PG = "Goodbye" ;   // 5. Logging out, used as a transition page
    // Variables representing the state and data of the ATM UIModel
    private String state = STATE_SIGNIN_PAGE;    // Current state of the ATM
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

    // Opened on Startup, and recalled upon after logout.
    public void welcomePage() {
        setState(STATE_WELCOME_PG);
        numberPadInput = "";
        message = "Welcome to Emalka Banking";
        result = "Welcome to Emalka Banking!\nFree cash withdrawls,\n balanace enquires\nand deposits!\n" +
                "Press enter to continue";

        update();
    }
    // Displays the logout page
    public void logoutPage() {
        numberPadInput = "";
        message = "Logging you out!";
        result = "Thankyou for banking with Emalka\nPress Enter to return to the menu";

        update();
    }




    // Reset the ATM UIModel after an invalid action or logout:
    // - Set state to STATE_ACCOUNT_NO
    // - Clear the numberPadInput
    // - Display the provided message and user instructions
    private void signIn(String msg) {
        setState(STATE_SIGNIN_PAGE);
        numberPadInput = "";
        message = "Sign-In";
        result = msg;
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
        //message = "Beep! " + numberOnButton + " received";
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
            /*case STATE_ACCOUNT_NO:
                // Waiting for a complete account number
                // If nothing was entered, reset with "Invalid Account Number"
                if (numberPadInput.equals("")) {
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
                break;*/
            case STATE_WELCOME_PG:
                // Loaded on program launch and after logout
                // waiting for any input
                if (numberPadInput.equals("")){
                    setState(STATE_SIGNIN_PAGE);
                    message = "Sign-In";
                    result = "Enter your Account Number and Password";
                }
                break;

            case STATE_SIGNIN_PAGE:
                    // Waiting for user's account details
                    // Will attempt to log in with given details
                accPasswd = numberPadInput;
                numberPadInput = "";
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
                    signIn(message);
                }
                break;
            case STATE_LOGOUT_PG:
                // Loaded on program launch and after logout
                // waiting for any input
                if (numberPadInput.equals("")){
                    setState(STATE_WELCOME_PG);
                    welcomePage();
                }
                break;


            case STATE_MAINMENU_PAGE:
                    // Waiting for user to select option
                if (numberPadInput.equals("Sign-Out")){
                    setState(STATE_LOGOUT_PG);

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
        if (state.equals(STATE_LOGGED_IN) ) {
            numberPadInput = "";
            message = "Balance Available";
            result = "Your Balance is: " + bank.getBalance();
        } else {
            signIn("You are not logged in");
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
        if (state.equals(STATE_LOGGED_IN)) {
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
        }
        else {
            signIn("You are not logged in");
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
        if (state.equals(STATE_LOGGED_IN)) {
            int amount = parseValidAmount(numberPadInput);
            if (amount > 0) {
                bank.deposit( amount );
                message = "Deposit Successful";
                result = "Deposited: " + numberPadInput;
            }
            else {
                message = "Invalid Amount";
                result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)";
            }
            numberPadInput = "";
        }
        else {
            signIn("You are not logged in");
        }
        save(); //- Will save data to a serialized file for loading
        saveRead(); // Will save data to a readable file for testing
        update();
    }

    // NOT WORKING YET
    // possibly change UI because the process of this is really confusing and could cause a lot of problems if a user messes it up
    // Handle the Change Password button:
    public void processPasswordChange() {
        if (state.equals(STATE_LOGGED_IN)) {
            accPasswd = numberPadInput;
            numberPadInput = "";
            // at this point the user needs to press enter so possibly need new state to add an effect in processEnter
            if ( bank.changePassword(accPasswd, numberPadInput) )
            {
                // Correct password entered
                message = "Password correct";
                result = "Enter new password";
            } else {
                // incorrect password entered - not sure whether to log out or not
                // but bank.changePassword method won't change the password if it's returning false
                signIn("Incorrect password");
            }
        }
        else {
            signIn("You are not logged in");
        }
        update();
    }

    // Handle the Finish button:
    // - If the user is logged in, log out
    // - Otherwise, reset the ATM and display an error message
    public void processFinish() {
        if (state.equals(STATE_MAINMENU_PAGE) ) {
            setState(STATE_LOGOUT_PG);
            logoutPage();
            bank.logout();

            view.signInPage(View.stage);

        } else {
            signIn("You are not logged in");
        }
        update();
    }

    // Handle unknown or invalid buttons for the current state:
    // - Reset the ATM and display an "Invalid Command" message
    public void processUnknownKey(String action) {
        signIn("Invalid Command");
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

