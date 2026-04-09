package com.atmbanksimulator;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

// Follows the MVC model - Model View Controller

public class Main extends Application {
    public static void main( String args[] ) {launch(args);}

    public void start(Stage window) {
        // Creates a Bank
        Bank bank = new Bank();

        // Getting the file
        File f = new File("F:\\bank.ser");

        if (f.exists()){
            // Deserializing to local file
            try (FileInputStream fileIn = new FileInputStream("bank.ser");
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {

                bank = (Bank) in.readObject();
                System.out.println("Bank object deserialized successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("Bank class not found.");
                c.printStackTrace();
                return;
            }
        } else {
            // Used to assign the initial accounts before saving to serialised file
            bank.addBankAccount("10001", "11111", 100, "Basic");
            bank.addBankAccount("10002", "22222", 50, "Basic");
            bank.addBankAccount("10003", "33333", 300, "Student" );
        }



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
