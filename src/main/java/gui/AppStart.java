package gui;

import gui.controllers.MasterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppStart extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println(getClass());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/BaseScene.fxml"));
        try {
            Parent parent = loader.load();

            primaryStage.setScene(new Scene(parent, 1000, 700));
            primaryStage.show();

            MasterController.setPrimaryStage(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
