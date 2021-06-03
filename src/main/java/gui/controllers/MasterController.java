package gui.controllers;

import javafx.stage.Stage;

public class MasterController {
    public static Stage primaryStage = null;

    public static void setPrimaryStage(Stage primaryStage) {
        MasterController.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
