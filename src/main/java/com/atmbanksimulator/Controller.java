package com.atmbanksimulator;

// ===== ⚡ Controller (Nerves) =====

// The Controller receives user actions from the View and delegates the appropriate tasks to the UIModel.
// Its main job is to decide what to do based on the user input.
public class Controller {

    UIModel UIModel; // Reference to the UIModel (part of the MVC setup)

    // The process method is called by the View in response to user interface events.
    // It uses a switch statement to determine which UIModel method should be called,
    // and delegates the task accordingly.
    void process( String action ) {
        switch (action) {
            case "1" : case "2" : case "3" : case "4" : case "5" :
            case "6" : case "7" : case "8" : case "9" : case "0" :
                UIModel.processNumber(action);
                break;
            case "CLR":
                UIModel.processClear();
                break;
            case "Ent":
                UIModel.processEnter();
                break;
            case "Dep":
                UIModel.processDeposit();
                break;
            case "Bal":
                UIModel.processBalance();
                break;
            case "Sign-Out":
                UIModel.processFinish();
                break;
            case "Withdraw":
                UIModel.stageManager(action);
                break;
            case "Deposit":
                UIModel.stageManager(action);
                break;
            case "Balance":
                UIModel.stageManager(action);
                break;
            case "Change Password":
                UIModel.stageManager(action);
                break;
            default:
                UIModel.processUnknownKey(action);
                break;
        }
    }
    void processWithdraw( String action ) {
        switch (action) {
            case "£10" : case "£20" : case "£50" : case "£75" : case "£100" :
            case "£200" : case "£500":
                UIModel.processWithdraw(action);
                break;
            default:
                UIModel.processUnknownKey(action);
                break;
        }
    }


    void mouseClick( String action) {
        switch (action) {
            case "acc":
                UIModel.processClick("acc");
                break;
            case "pass":
                UIModel.processClick("pass");
                break;
        }
    }
}


