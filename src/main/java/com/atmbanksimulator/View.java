package com.atmbanksimulator;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.time.*;

// The View class creates the GUI for the application.
// It does not know anything about business logic;
// it only updates the display when notified by the UIModel.

class View {
    int H = 650;         // Height of window pixels
    int W = 750;         // Width  of window pixels

    Controller controller; // Reference to the Controller (part of the MVC setup)

    public static Stage getWindow(Stage window) {

        return stage = window;
    }
    public static Stage stage;

    // Getters
    public String tfSelect;            // Stores current selected field

    // Components (controls and layout) of the user interface
    private Label laMsg;        // Header at the top of the GUI


    private TextField tfInputA;    // view doesn't crae what's being entered; top field for all screens
    private TextField tfInputB;    // bottom field for screens with 2 fields

    private Button goBack;       // Send data
    private TextArea taResult;  // Output area where instructions and results are displayed
    private GridPane grid;      // Main layout container (grid-based)
    private TilePane buttonPane;// Container for ATM keypad buttons (tiled layout)
    private Label dateMsg;      // To display current date
    private Button btnWD;       // Send Withdraw data


    // start() is called from Main to set up the UI.
    // Important: Controls are created here so everything is initialised in the correct order
    public void start(Stage window) {
        signInPage(window);
    }
    public TilePane pinPad(){
        // Create pin pad
        buttonPane = new TilePane(); //
        buttonPane.setId("Buttons"); // CSS ID
        buttonPane.setPrefColumns(3);
        buttonPane.setPrefRows(4);
        buttonPane.setMaxWidth(250);
        GridPane.setHalignment(buttonPane, HPos.CENTER);
        // Define the button layout as a 2D array of text labels.
        // Empty strings ("") represent blank spaces in the grid.
        String buttonTexts[][] = {
                {"7",    "8",  "9"},
                {"4",    "5",  "6"},
                {"1",    "2",  "3"},
                {"CLR",  "0",  "Ent"} };

        // Build the button panel, loop through the array,
        // - For non-empty strings, create a Button
        // - For empty strings, add an empty Text element as a spacer
        // Add all elements to the buttonPane (a tiled pane),
        // then place the buttonPane into the main grid.
        for ( String[] row: buttonTexts ) {
            for (String text: row) {
                if ( !text.isEmpty() ) {
                    // non-empty string - make a button
                    Button btn = new Button( text );
                    btn.setOnAction( this::pinPadClicked );
                    // Register event handler: call buttonClicked() whenever this button is pressed
                    buttonPane.getChildren().add( btn );    // add this button to tiled pane
                } else {
                    // empty string - make an empty Text element as a spacer
                    buttonPane.getChildren().add( new Text() );
                }
            }
        }
        return buttonPane;
    }


    // This is how the View talks to the Controller
    // This method is called when a button is pressed
    // It fetches the label on the button and passes it to the controller's process method
    private void buttonClicked(ActionEvent event) {
        // This line asks the event to provide the actual Button object that was clicked
        Button b = ((Button) event.getSource());
        String text = b.getText();   // get the button label
        System.out.println( "View::buttonClicked: label = "+ text );
        controller.process( text );  // Pass it to the controller's process method
    }

    // This is how the View talks to the Controller
    // This method is called when the pin pad is pressed
    // It fetches the label on the button and passes it to the controller's process method
    private void pinPadClicked(ActionEvent event) {
        // This line asks the event to provide the actual Button object that was clicked
        Button b = ((Button) event.getSource());
        String text = b.getText();   // get the button label
        System.out.println( "View::buttonClicked: label = "+ text );
        controller.pinPad( text, tfSelect );  // Pass it to the controller's process method
    }

    public void welcomePage(Stage window){
        // Creates a Grid Pane
        grid = new GridPane();  // Page Layout
        grid.setId("layout");   // CSS ID

        // Setting the padding
        grid.setPadding(new Insets(25,25,25,25));

        // Setting the vert and hori gaps between the columns
        grid.setVgap(10);
        grid.setHgap(10);

        // Setting the grid alignment
        grid.setAlignment(Pos.CENTER);


        // Creates a label title
        laMsg = new Label("Welcome");   // Title bar at the top
        laMsg.setId("titleWelcome");   // CSS ID for designs
        grid.add(laMsg, 0, 0);         // Add to GUI at the top
        GridPane.setHalignment(laMsg, HPos.CENTER);

        // Creates a Text Area for instructions
        taResult = new TextArea("\"Welcome to Emalka Banking!\\nFree cash withdrawals, balance  enquires \\nand deposits.\\n\" +\n" +
                "                            \"Press enter to continue\";");   // Instructions
        taResult = new TextArea("Welcome");   // Instructions
        taResult.setId("welcomeText"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(175);       // Assign dimensions to the field

        //scrollPane  = new ScrollPane();    // create a scrolling window
        //scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        //scrollPane.setPrefHeight(100);     // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // add the scrolling window to GUI on third row


        // Create pin pad
        buttonPane = pinPad();

        grid.add(buttonPane,0,4); // add the tiled pane of buttons to the main grid

        // add the complete GUI to the window and display it
        Scene signIn = new Scene(grid, W, H);
        signIn.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(signIn);
        window.setTitle("ATM-Bank Simulator"); //set window title
        window.setResizable(false);
        window.show();

    }

    // This method is called when the user passes the welcome page.
    // Fills in the contents of the scene to create a sign-in page
    // - Takes the account number + password
    // - If the user's input isn't a valid account, it will alert them
    public void signInPage(Stage window){
        // Creates a Grid Pane
        grid = new GridPane();  // Page Layout
        grid.setId("layout");   // CSS ID

        // Setting the padding
        grid.setPadding(new Insets(25,25,25,25));

        // Setting the vert and hori gaps between the columns
        grid.setVgap(10);
        grid.setHgap(10);

        // Setting the grid alignment
        grid.setAlignment(Pos.CENTER);

        // Creates a label title
        laMsg = new Label("Sign-In");   // Title bar at the top
        laMsg.setId("title");   // CSS ID for designs
        grid.add(laMsg, 0, 0);         // Add to GUI at the top
        GridPane.setHalignment(laMsg, HPos.CENTER); // Centers the Title

        // Creates a Text Area for instructions
        taResult = new TextArea("Please enter your account number and password");   // Instructions
        taResult.setId("signinText"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(100);       // Assign dimensions to the field
        //scrollPane  = new ScrollPane();    // create a scrolling window
        //scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        //scrollPane.setPrefHeight(100);     // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // add the scrolling window to GUI on third row

        // Creates a Text field for inputting account number
        tfInputA = new TextField();     // text field for numbers
        tfInputA.setEditable(false);     // Read only
        grid.add(tfInputA, 0, 2);    // Add to GUI on second row
        tfInputA.setOnMouseClicked(event -> {
            tfSelect = /*"accountNum"*/ "A";
            controller.mouseClick("A");
        });

        // Creates a Text field for inputting account password
        tfInputB = new TextField();     // text field for numbers
        tfInputB.setEditable(false);     // Read only
        grid.add(tfInputB, 0, 3);    // Add to GUI on third row
        tfInputB.setOnMouseClicked(event -> {
            tfSelect = /*"password"*/ "B";
            controller.mouseClick("B");
        });

        // Create pin pad
        buttonPane = pinPad();

        grid.add(buttonPane,0,4); // add the tiled pane of buttons to the main grid

        // add the complete GUI to the window and display it
        Scene signIn = new Scene(grid, W, H);
        signIn.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(signIn);
        window.setTitle("ATM-Bank Simulator"); //set window title
        window.setResizable(false);
        window.show();
    }

    // This method is called when the user logs in.
    // Fills in the contents of the scene to create a main menu
    // - Presents options for the user to choose from
    // - Loads different scenes
    public void mainMenu(Stage window){
        // Creates a Grid Pane
        grid = new GridPane();  // Page Layout
        grid.setId("layout");   // CSS ID

        // Setting the padding
        grid.setPadding(new Insets(25,25,25,25));

        // Setting the vert and hori gaps between the columns
        grid.setVgap(10);
        grid.setHgap(10);

        // Setting the grid alignment
        grid.setAlignment(Pos.CENTER);

        // Creates a label title
        laMsg = new Label("Main Menu");     // Title bar at the top
        laMsg.setId("title");                   // CSS ID for designs
        grid.add(laMsg, 0, 0);         // Add to GUI at the top
        GridPane.setHalignment(laMsg, HPos.CENTER); // Centers the Title

        // Creates a Text Area for instructions
        taResult = new TextArea("Please select the option you need");   // Instructions
        taResult.setId("menuText"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(70);       // Assign dimensions to the field
        //scrollPane  = new ScrollPane();    // create a scrolling window
        //scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        //scrollPane.setPrefHeight(100);     // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // Add the scrolling window to GUI on third row

        // Create pin pad
        TilePane menuPane = new TilePane(); //
        menuPane.setId("menuButtons"); // CSS ID
        menuPane.setPrefColumns(2);
        menuPane.setPrefRows(4);
        menuPane.setMaxWidth(500);
        GridPane.setHalignment(menuPane, HPos.CENTER);
        // Define the button layout as a 2D array of text labels.
        // Empty strings ("") represent blank spaces in the grid.
        String buttonTexts[][] = {
                {"Withdraw",  "Deposit"},
                {"Balance", "Change Password"},
                {"", ""},
                {"", "Sign-Out"} };

        // Build the button panel, loop through the array,
        // - For non-empty strings, create a Button
        // - For empty strings, add an empty Text element as a spacer
        // Add all elements to the buttonPane (a tiled pane),
        // then place the buttonPane into the main grid.
        for ( String[] row: buttonTexts ) {
            for (String text: row) {
                if ( !text.isEmpty() ) {
                    // non-empty string - make a button
                    Button btn = new Button( text );
                    btn.setOnAction( this::buttonClicked );
                    // Register event handler: call buttonClicked() whenever this button is pressed
                    menuPane.getChildren().add( btn );    // add this button to tiled pane
                } else {
                    // empty string - make an empty Text element as a spacer
                    menuPane.getChildren().add( new Text() );
                }
            }
        }
        grid.add(menuPane,0,2); // Add the tiled pane of buttons to the main grid

        // Create pin pad
        //buttonPane = pinPad();
       // grid.add(buttonPane,0,4); // Add the tiled pane of buttons to the main grid

        // add the complete GUI to the window and display it
        Scene maMenu = new Scene(grid, W, H);
        maMenu.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(maMenu);
    }

    // This method is called when the user selects Withdraw.
    // Fills in the contents of the scene to create a Withdraw page
    // - Presents fast withdrawal options and a custom field
    // - Alerts the user if they will hit their withdrawal limit for the day
    public void withdraws(Stage window){
        // Creates a Grid Pane
        grid = new GridPane();  // Page Layout
        grid.setId("layout");   // CSS ID

        // Setting the padding
        grid.setPadding(new Insets(25,25,25,25));

        // Setting the vert and hori gaps between the columns
        grid.setVgap(10);
        grid.setHgap(10);

        // Setting the grid alignment
        grid.setAlignment(Pos.CENTER);

        // Creates a label title
        laMsg = new Label("Select Amount to Withdraw");     // Title bar at the top
        laMsg.setId("title");                   // CSS ID for designs
        grid.add(laMsg, 0, 0);         // Add to GUI at the top
        GridPane.setHalignment(laMsg, HPos.CENTER); // Centers the Title


        // Creates a Text Area for instructions
        taResult = new TextArea("Please select the amount");   // Instructions
        taResult.setId("withdrawText"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(100);       // Assign dimensions to the field
        //scrollPane  = new ScrollPane();    // create a scrolling window
        //scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        //scrollPane.setPrefHeight(100);     // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // Add the scrolling window to GUI on third row

        // Create withdrawal options
        TilePane menuPane = new TilePane(); //
        menuPane.setId("menuWithdraw"); // CSS ID
        menuPane.setPrefColumns(2);
        menuPane.setPrefRows(3);
        menuPane.setMaxWidth(500);
        menuPane.setHgap(60);
        GridPane.setHalignment(menuPane, HPos.CENTER);
        // Define the button layout as a 2D array of text labels.
        // Empty strings ("") represent blank spaces in the grid.
        String withdrawButtons[][] = {
                {"£10", "", "£20"},
                {"£50", "", "£75"},
                {"£100", "",  "£200"},
                {"£500", "",  "Custom"}};

        // Build the menu panel, loop through the array,
        // - For non-empty strings, create a Button
        // - For empty strings, add an empty Text element as a spacer
        // Add all elements to the menuPane (a tiled pane),
        // then place the menuPane into the main grid.
        for ( String[] row: withdrawButtons ) {
            for (String text: row) {
                if ( !text.isEmpty() ) {
                    // non-empty string - make a button
                    btnWD = new Button( text );
                    System.out.println(btnWD);
                    btnWD.setOnAction( this::buttonClicked );
                    // Register event handler: call buttonClicked() whenever this button is pressed
                    menuPane.getChildren().add( btnWD );    // add this button to tiled pane
                } else {
                    // empty string - make an empty Text element as a spacer
                    menuPane.getChildren().add( new Text() );
                }
            }
        }
        grid.add(menuPane,0,2); // Add the tiled pane of buttons to the main grid

        // Creates a return to menu button
        goBack = new Button("Return to menu");
        goBack.setId("returnButton");// CSS ID for designs
        goBack.setOnAction( this::buttonClicked );
        grid.add(goBack, 0, 3 );
        GridPane.setHalignment(goBack, HPos.CENTER);

        // Create pin pad
        //buttonPane = pinPad();
        //grid.add(buttonPane,0,4); // Add the tiled pane of buttons to the main grid

        // add the complete GUI to the window and display it
        Scene withdraws = new Scene(grid, W, H);
        withdraws.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(withdraws);
    }

    // This method is called when the user selects Balance
    public void balance(Stage window){
        // Creates a Grid Pane
        grid = new GridPane();  // Page Layout
        grid.setId("layout");   // CSS ID

        // Setting the padding
        grid.setPadding(new Insets(25,25,25,25));

        // Setting the vert and hori gaps between the columns
        grid.setVgap(10);
        grid.setHgap(10);

        // Setting the grid alignment
        grid.setAlignment(Pos.CENTER);

        // Creates a label title
        laMsg = new Label("Your account balance is:");     // Title bar at the top
        laMsg.setId("title");                   // CSS ID for designs
        grid.add(laMsg, 0, 0);         // Add to GUI at the top
        GridPane.setHalignment(laMsg, HPos.CENTER); // Centers the Title

        // Creates a Text Area for instructions
        //taResult = new TextArea();   // Instructions
        taResult.setId("balanceText"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(100);       // Assign dimensions to the field
        //scrollPane  = new ScrollPane();    // create a scrolling window
        //scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        //scrollPane.setPrefHeight(100);     // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // Add the scrolling window to GUI on third row

        // Creates a return to menu button
        goBack = new Button("Return to menu");
        goBack.setId("returnButton"); // CSS ID for designs
        goBack.setOnAction( this::buttonClicked );
        grid.add(goBack, 0, 2 );
        GridPane.setHalignment(goBack, HPos.CENTER);

        // Create pin pad
        buttonPane = pinPad();
        grid.add(buttonPane,0,4); // Add the tiled pane of buttons to the main grid

        // add the complete GUI to the window and display it
        Scene balance = new Scene(grid, W, H);
        balance.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(balance);
    }

    // This method is called when the user chooses to change their password.
    // Fills in the contents of the scene to create a change password page
    // - Presents 2 text fields, one to enter old password and another for new password
    public void chngPass(Stage window){
        // Creates a Grid Pane
        grid = new GridPane();  // Page Layout
        grid.setId("layout");   // CSS ID

        // Setting the padding
        grid.setPadding(new Insets(25,25,25,25));

        // Setting the vert and hori gaps between the columns
        grid.setVgap(10);
        grid.setHgap(10);

        // Setting the grid alignment
        grid.setAlignment(Pos.CENTER);

        // Creates a label title
        laMsg = new Label("Change Password");   // Title bar at the top
        laMsg.setId("title");   // CSS ID for designs
        grid.add(laMsg, 0, 0);         // Add to GUI at the top
        GridPane.setHalignment(laMsg, HPos.CENTER); // Centers the Title

        // Creates a Text Area for instructions
        taResult = new TextArea("Please enter your current password\nand new password");   // Instructions
        taResult.setId("passwordChangeText"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(100);       // Assign dimensions to the field
        //scrollPane  = new ScrollPane();    // create a scrolling window
        //scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        //scrollPane.setPrefHeight(100);     // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // add the scrolling window to GUI on third row

        // Creates a Text field for inputting account number
        tfInputA = new TextField();     // text field for numbers
        tfInputA.setEditable(false);     // Read only
        grid.add(tfInputA, 0, 2);    // Add to GUI on second row
        tfInputA.setOnMouseClicked(event -> {
            tfSelect = "A";
            controller.mouseClick("A");
        });

        // Creates a Text field for inputting account password
        tfInputB = new TextField();     // text field for numbers
        tfInputB.setEditable(false);     // Read only
        grid.add(tfInputB, 0, 3);    // Add to GUI on third row
        tfInputB.setOnMouseClicked(event -> {
            tfSelect = "B";
            controller.mouseClick("B");
        });

        // Creates a return to menu button
        goBack = new Button("Return to menu");
        goBack.setId("returnButton"); // CSS ID for designs
        goBack.setOnAction( this::buttonClicked );
        grid.add(goBack, 0, 4 );
        GridPane.setHalignment(goBack, HPos.CENTER);

        // Create pin pad
        buttonPane = pinPad();

        grid.add(buttonPane,0,5); // add the tiled pane of buttons to the main grid

        // add the complete GUI to the window and display it
        Scene chngPass = new Scene(grid, W, H);
        chngPass.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(chngPass);
    }

    // This method is called when the user selects Deposit
    // Fills in the contents of the scene to create a Deposit page
    //
    public void deposit(Stage window){
        // Creates a Grid Pane
        grid = new GridPane();  // Page Layout
        grid.setId("layout");   // CSS ID

        // Setting the padding
        grid.setPadding(new Insets(25,25,25,25));

        // Setting the vert and hori gaps between the columns
        grid.setVgap(10);
        grid.setHgap(10);

        // Setting the grid alignment
        grid.setAlignment(Pos.CENTER);

        // Creates a label title
        laMsg = new Label("Enter amount to deposit");     // Title bar at the top
        laMsg.setId("title");                   // CSS ID for designs
        grid.add(laMsg, 0, 0);         // Add to GUI at the top

        // Creates a Text Area for instructions
        taResult = new TextArea("Please enter the amount you wish to deposit");   // Instructions
        taResult.setId("depositText"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(100);       // Assign dimensions to the field
        //scrollPane  = new ScrollPane();    // create a scrolling window
        //scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        //scrollPane.setPrefHeight(100);     // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // Add the scrolling window to GUI on third row

        tfInputA = new TextField();
        tfInputA.setId("depositField"); // CSS ID for designs
        tfInputA.setEditable(false);     // Read only
        tfInputA.setOnMouseClicked(event -> {
            controller.mouseClick("A");
        });
        tfSelect = "A";
        grid.add(tfInputA, 0, 2);

        // Creates a return to menu button
        goBack = new Button("Return to menu");
        goBack.setId("return");
        goBack.setOnAction( this::buttonClicked );
        grid.add(goBack, 0, 3 );
        GridPane.setHalignment(goBack, HPos.CENTER);


        //Create pin pad
        buttonPane = pinPad();
        grid.add(buttonPane,0,4); // Add the tiled pane of buttons to the main grid

        // add the complete GUI to the window and display it
        Scene deposit = new Scene(grid, W, H);
        deposit.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(deposit);
    }
    // This method is called when the user selects Custom in the withdrawals page
    // Fills in the contents of the scene to create a Withdrawals page
    //
    public void withdrawsCustom(Stage window){
        // Creates a Grid Pane
        grid = new GridPane();  // Page Layout
        grid.setId("layout");   // CSS ID

        // Setting the padding
        grid.setPadding(new Insets(25,25,25,25));

        // Setting the vert and hori gaps between the columns
        grid.setVgap(10);
        grid.setHgap(10);

        // Setting the grid alignment
        grid.setAlignment(Pos.CENTER);

        // Creates a label title
        laMsg = new Label();     // Title bar at the top
        laMsg.setId("title");                   // CSS ID for designs
        grid.add(laMsg, 0, 0);         // Add to GUI at the top
        GridPane.setHalignment(laMsg, HPos.CENTER); // Centers the Title

        // Creates a Text Area for instructions
        taResult = new TextArea();   // Instructions
        taResult.setId("customWithdrawText"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(100);       // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // Add the scrolling window to GUI on third row

        tfInputA = new TextField();
        tfInputA.setId("withdrawField");
        tfInputA.setEditable(false);     // Read only
        grid.add(tfInputA, 0, 2);

        // Creates a return to menu button
        goBack = new Button("Return to menu");
        goBack.setId("return");
        goBack.setOnAction( this::buttonClicked );
        grid.add(goBack, 0, 3 );
        GridPane.setHalignment(goBack, HPos.CENTER);


        // Create pin pad
        buttonPane = pinPad();
        grid.add(buttonPane,0,4); // Add the tiled pane of buttons to the main grid

        // add the complete GUI to the window and display it
        Scene deposit = new Scene(grid, W, H);
        deposit.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(deposit);
    }

    // Hides previous scene
    public void hideScene() {
        grid.managedProperty().bind(grid.visibleProperty());
    }

    // This method is called by the UIModel whenever the UIModel changes.
    // It receives updated information from the UIModel and displays them in the GUI.
    // - msg → shown in the top message label
    // - tfInputMsg → shown in the text field (user input area)
    // - taResultMsg → shown in the text area (instructions / results)
    public void update(String msg,String tfInputMsg, String fieldB, String taResultMsg)
    {
        laMsg.setText(msg);
        tfInputA.setText(tfInputMsg);    // Number update
        tfInputB.setText(fieldB);
        taResult.setText(taResultMsg);

        // LocalDate currentDate = LocalDate.now(); // Creates a date object with the current date
        //dateMsg.setText(String.valueOf(currentDate)); // Assigns the current date to the label
    }
}