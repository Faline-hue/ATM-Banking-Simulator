module com.atmbanksimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires tools.jackson.core;
    requires tools.jackson.databind;


    opens com.atmbanksimulator to javafx.fxml;
    exports com.atmbanksimulator;
}