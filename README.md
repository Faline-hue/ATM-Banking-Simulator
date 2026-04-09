An ATM simulator designed to test UI/UX features and design them 
aligned to ethical and legal standards in the industry

Features of the ATM: 
-
 - Premade test bank accounts can be logged in to
 - Bank account changes are retained ***during*** use of program, when closed they are not saved
 - Can check the balance of the logged in account
 - Can withdraw from and deposit into the logged in account
   - When withdrawing, program checks if the user is attempting to withdraw more than their daily limit
   - Alerts user if they cannot withdraw any more today
 - Current date is read into program for multiple checks
 


Additions to make:
-
 - Account types and the different effects they have
   - Prime Account, Savings Account
   - Override the behaviour where needed (withdraw, deposit, etc)
 - 
 - Limit on cash deposit
   - Legal requirement to limit fraud
   - Daily limit
   - Annual (Rolling 12 months) limit
 -
 - Change Password feature
   - Validate old password,
   - Check new password follows rules
 -
 - Create a new account feature
   - Legally this wouldn't be a feature that can be done in the app, 
     as following regulations a background check needs to be done
   - 
   - Validate account number,
   - Prevent duplicate accounts,
   - Initialise balance and account type
   - Switch to arraylist for storing bank accounts so there is no limit on bank accounts
 -
 - Navigation Pages and Flow
   - Add a welcome page before logging in
   - Add a goodbye page after logging out
     - Loop back to log-in screen, skipping the welcome page
   - Improve navigation between states/pages
   - Ensure the user always knows what to do next
 - 
 - Login Control
   - Limit login attempts to 3 tries before locking account
 - 
 - Account Transfers
   - Select a target account
   - Validate the balance of the current account
   - Update both accounts safely
 - 
 - Mini Statement
   - Show recent transactions
   - Store data in files
   - Load saved data when the program starts
 - 
 - Restyle the GUI to resemble a modern ATM
 - 
 - Documentation and Testing
   - Write documentation explaining:
     - Classes
     - Key methods
   - Apply testing
     - JUnit tests