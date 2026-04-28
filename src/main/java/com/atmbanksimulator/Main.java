package com.atmbanksimulator;

import javafx.application.Application;
import javafx.stage.Stage;
import tools.jackson.databind.ObjectMapper;

import java.io.File;

// Follows the MVC model - Model View Controller

public class Main extends Application {
    public static void main( String args[] ) {launch(args);}


    public void start(Stage window) {
        // Make a stage variable
        View.getWindow(window);

        // Creates a Bank
        Bank bank = new Bank();

        // Creates an ObjectMapper
        // Used to read/write JSON files
        ObjectMapper objectMapper = new ObjectMapper();

        // Getting the file
        File f = new File("bank.ser");

        // Reads the file only if it exists
        if (f.exists()){
            bank.accounts.addAll(bank.load());
        } else {
            // Used to assign the initial accounts before saving to JSON file
            bank.addBankAccount("10001", "11111", 100, "Basic");
            bank.addBankAccount("10002", "22222", 50, "Basic");
            bank.addBankAccount("10003", "33333", 300, "Student" );
            System.out.println("didn't read file");
        }
        System.out.println(bank.accounts);
        // UIModel-View-Controller structure setup
        // Create the UIModel, View and Controller objects and link them together
        UIModel UIModel = new UIModel(bank);   // the UIModel needs the Bank object to 'talk to' the bank
        View  view  = new View();
        Controller controller  = new Controller();

        // Link them together so they can talk to each other
        view.controller = controller;
        controller.UIModel = UIModel;
        UIModel.view = view;

        // start up the GUI (view), and then tell the UIModel to initialise itself
        view.start(window);
        UIModel.initialise();
    }
}
