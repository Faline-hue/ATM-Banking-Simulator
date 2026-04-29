package com.atmbanksimulator;

import javafx.event.ActionEvent;
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
    private TextField tfInput;  // Input field where numbers typed on the keypad appear
    private TextField accNum;   // Input field for account number
    private TextField paswrd;   // Input field for password
    private Button enter;       // Send data
    private TextArea taResult;  // Output area where instructions and results are displayed
    private ScrollPane scrollPane; // Provides scrollbars around the TextArea
    private GridPane grid;      // Main layout container (grid-based)
    private TilePane buttonPane;// Container for ATM keypad buttons (tiled layout)
    private Label dateMsg;      // To display current date



    // start() is called from Main to set up the UI.
    // Important: Controls are created here so everything is initialised in the correct order
    public void start(Stage window) {


        signInPage(window);
        /*
        // Create the user interface component objects.
        // The ATM UI is organised as a vertical grid with four main parts:
        // 1. A message label
        // 2. A text field showing numbers
        // 3. A text area showing transaction results, summaries, and user instructions
        // 4. A tiled panel of buttons

        window.setResizable(false); // Locks the window size
        grid = new GridPane(); // top layout
        grid.setId("Layout");  // CSS ID
        buttonPane = new TilePane(); //
        buttonPane.setId("Buttons"); // CSS ID

        // controls
        laMsg = new Label("Welcome to Bank-ATM");  // Message bar at the top
        grid.add(laMsg, 0, 0);         // Add to GUI at the top

        tfInput = new TextField();     // text field for numbers
        tfInput.setEditable(false);     // Read only
        grid.add(tfInput, 0, 1);    // Add to GUI on second row

        taResult = new TextArea();         // text area for instructions, transaction results
        taResult.setEditable(false);       // Read only
        scrollPane  = new ScrollPane();    // create a scrolling window
        scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        grid.add( scrollPane, 0, 2);    // add the scrolling window to GUI on third row

        // Define the button layout as a 2D array of text labels.
        // Empty strings ("") represent blank spaces in the grid.
        String buttonTexts[][] = {
                {"7",    "8",  "9",  "",  "Dep",  ""},
                {"4",    "5",  "6",  "",  "W/D",  ""},
                {"1",    "2",  "3",  "",  "Bal",  "Fin"},
                {"CLR",  "0",  "",   "",  "",     "Ent"} };

        // Build the button panel, loop through the array,
        // - For non-empty strings, create a Button
        // - For empty strings, add an empty Text element as a spacer
        // Add all elements to the buttonPane (a tiled pane),
        // then place the buttonPane into the main grid as the fourth row.
        for ( String[] row: buttonTexts ) {
            for (String text: row) {
                if ( !text.isEmpty() ) {
                    // non-empty string - make a button
                    Button btn = new Button( text );
                    btn.setOnAction( this::buttonClicked );
                              // Register event handler: call buttonClicked() whenever this button is pressed
                    buttonPane.getChildren().add( btn );    // add this button to tiled pane
                } else {
                    // empty string - make an empty Text element as a spacer
                    buttonPane.getChildren().add( new Text() );
                }
            }
        }
        grid.add(buttonPane,0,3); // add the tiled pane of buttons to the main grid

        dateMsg = new Label();
        grid.add(dateMsg, 0, 4);       // Add to GUI next to laMsg

        // add the complete GUI to the window and display it
        Scene scene = new Scene(grid, W, H);
        scene.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(scene);
        window.setTitle("ATM-Bank Simulator"); //set window title
        window.show();
        */
    }
    public TilePane pinPad(){
        // Create pin pad
        buttonPane = new TilePane(); //
        buttonPane.setId("Buttons"); // CSS ID
        buttonPane.setPrefColumns(3);
        buttonPane.setPrefRows(4);
        buttonPane.setMaxWidth(250);
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
                    btn.setOnAction( this::buttonClicked );
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

        // Creates a Text Area for instructions
        taResult = new TextArea("Please enter your account number and password");   // Instructions
        taResult.setId("instructions"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(100);       // Assign dimensions to the field
        //scrollPane  = new ScrollPane();    // create a scrolling window
        //scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        //scrollPane.setPrefHeight(100);     // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // add the scrolling window to GUI on third row

        // Creates a Text field for inputting account number
        accNum = new TextField();     // text field for numbers
        accNum.setEditable(false);     // Read only
        grid.add(accNum, 0, 2);    // Add to GUI on second row
        accNum.setOnMouseClicked(event -> {
            tfSelect = "accountNum";
            controller.mouseClick("acc");
        });

        tfInput = new TextField();  // To avoid errors from old code

        // Creates a Text field for inputting account password
        paswrd = new TextField();     // text field for numbers
        paswrd.setEditable(false);     // Read only
        grid.add(paswrd, 0, 3);    // Add to GUI on third row
        paswrd.setOnMouseClicked(event -> {
            tfSelect = "password";
            controller.mouseClick("pass");
        });

        // Create pin pad
        buttonPane = pinPad();

        grid.add(buttonPane,0,4); // add the tiled pane of buttons to the main grid

        // add the complete GUI to the window and display it
        Scene signIn = new Scene(grid, W, H);
        signIn.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(signIn);
        window.setTitle("ATM-Bank Simulator"); //set window title
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

        // Creates a Text Area for instructions
        taResult = new TextArea("Please select the option you need");   // Instructions
        taResult.setId("instructions"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(100);       // Assign dimensions to the field
        //scrollPane  = new ScrollPane();    // create a scrolling window
        //scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        //scrollPane.setPrefHeight(100);     // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // Add the scrolling window to GUI on third row

        // Create pin pad
        TilePane menuPane = new TilePane(); //
        menuPane.setId("menu"); // CSS ID
        menuPane.setPrefColumns(2);
        menuPane.setPrefRows(4);
        menuPane.setMaxWidth(500);
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
        buttonPane = pinPad();
        grid.add(buttonPane,0,4); // Add the tiled pane of buttons to the main grid

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

        // Creates a Text Area for instructions
        taResult = new TextArea("Please select the amount");   // Instructions
        taResult.setId("instructions"); // CSS ID for designs
        taResult.setEditable(false);       // Read only
        taResult.setPrefHeight(100);       // Assign dimensions to the field
        //scrollPane  = new ScrollPane();    // create a scrolling window
        //scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        //scrollPane.setPrefHeight(100);     // Assign dimensions to the field
        grid.add( taResult, 0, 1);    // Add the scrolling window to GUI on third row

        // Create withdrawal options
        TilePane menuPane = new TilePane(); //
        menuPane.setId("menu"); // CSS ID
        menuPane.setPrefColumns(2);
        menuPane.setPrefRows(3);
        menuPane.setMaxWidth(500);
        menuPane.setHgap(60);
        // Define the button layout as a 2D array of text labels.
        // Empty strings ("") represent blank spaces in the grid.
        String buttonTexts[][] = {
                {"£10", "", "£20"},
                {"£50", "", "£75"},
                {"£100", "",  "£200"},
                {"£500", "",  "Custom"}};

        // Build the menu panel, loop through the array,
        // - For non-empty strings, create a Button
        // - For empty strings, add an empty Text element as a spacer
        // Add all elements to the menuPane (a tiled pane),
        // then place the menuPane into the main grid.
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
        buttonPane = pinPad();
        grid.add(buttonPane,0,4); // Add the tiled pane of buttons to the main grid

        // add the complete GUI to the window and display it
        Scene withdraws = new Scene(grid, W, H);
        withdraws.getStylesheets().add("atm.css"); // tell to use our CSS file
        window.setScene(withdraws);
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
    public void update(String msg,String tfInputMsg,String taResultMsg)
    {
        laMsg.setText(msg);
        if (tfSelect == "accountNum"){
            accNum.setText(tfInputMsg);     // Account number update
        } else if (tfSelect == "password"){
            paswrd.setText(tfInputMsg);     // Password update
        } else {
            tfInput.setText(tfInputMsg);    // Number update
        }
        taResult.setText(taResultMsg);

        LocalDate currentDate = LocalDate.now(); // Creates a date object with the current date
        //dateMsg.setText(String.valueOf(currentDate)); // Assigns the current date to the label
    }
}