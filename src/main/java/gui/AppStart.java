package gui;

import gui.controllers.MasterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AppStart extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(Object.class.getResource("/FXML/BaseScene.fxml"));
        try {
            Parent parent = loader.load();
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setScene(new Scene(parent, bounds.getWidth() , bounds.getHeight()- 100));
            primaryStage.show();

            MasterController.setPrimaryStage(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
