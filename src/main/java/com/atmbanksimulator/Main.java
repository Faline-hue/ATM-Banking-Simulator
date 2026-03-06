package com.atmbanksimulator;

import javafx.application.Application;
import javafx.stage.Stage;

// Follows the MVC model - Model View Controller

public class Main extends Application {
    public static void main( String args[] ) {launch(args);}

    public void start(Stage window) {
        // Creates a Bank object and adds two bank accounts for test purposes
        Bank bank = new Bank();
        bank.addBankAccount("10001", "11111", 100, "Student");
        bank.addBankAccount("10002", "22222", 50, "Student");

        // UIModel-View-Controller structure setup
        // Create the UIModel, View and Controller objects and link them together
        UIModel UIModel = new UIModel(bank);   // the UIModel needs the Bank object to 'talk to' the bank
        View  view  = new View();
        Controller controller  = new Controller();

        // Link them together so they can talk to each other
        view.controller = controller;
        controller.UIModel = UIModel;
        UIModel.view = view;

        // start up the GUI (view), and then tell the UIModel to initialize itself
        view.start(window);
        UIModel.initialise();
    }
}
