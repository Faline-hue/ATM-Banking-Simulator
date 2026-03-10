An ATM simulator designed to test UI/UX features and design them 
aligned to ethical and legal standards in the industry

Features of the ATM: 
-
 - Premade test bank accounts can be logged in to
 - Bank account changes are retained ***during*** use of program, when closed they are not saved
 - Can check the balance of the logged in account
 - Can withdraw from and deposit into the logged in account


Additions to make:
-
 - Account types and the different effects they have
   - Objects that inherit attributes and methods from BankAccount
   - Student Account, Prime Account, Savings Account
   - Override the behaviour where needed (withdraw, deposit, etc)
   - Check daily cap on withdrawal feature

 - Change Password feature
   - Validate old password,
   - Check new password follows rules

 - Create a new account feature
   - Validate account number,
   - Prevent duplicate accounts,
   - Initialise balance and account type

   - If the account limit is reached, needs to not allow the user to make a new account and flag this

 - Navigation Pages and Flow
   - Add a welcome page before logging in
   - Add a goodbye page after logging out
     - Loop back to log-in screen, skipping the welcome page
   - Improve navigation between states/pages
   - Ensure the user always knows what to do next

 - Date/time functionality on ATM 
   - Utilised by multiple features
     - Withdrawal limit daily reset
     - Interest on savings account
     - Home page date?
   - Needs to be coded in early to check for date change