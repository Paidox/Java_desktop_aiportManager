module main.lab8_javafx
{
    requires javafx.controls;
    requires javafx.fxml;

    opens main.controller to javafx.fxml;
    opens main.flight to javafx.base;
    opens main.ticket to javafx.base;
    exports main;
    exports main.controller;
    opens main.repository to javafx.base;
}
