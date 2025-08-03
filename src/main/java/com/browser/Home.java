package com.browser;

import com.browser.Controller.LogInController;
import com.browser.Controller.SharedController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Home extends Application {
    private static Stage stg;
    @Override
    public void start(Stage primaryStage) {
        stg = primaryStage;
        try {
            FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
            Parent mainRoot = homeLoader.load();
            //HomeController mainController = homeLoader.getController();
            LogInController logInController = homeLoader.getController();
            //SharedController.getInstance().setHomeController(mainController);
            SharedController.getInstance().setLogInController(logInController);
            Scene scene = new Scene(mainRoot);
            scene.getStylesheets().add(getClass().getResource("/darktheme.css").toExternalForm());

            primaryStage.setTitle("JavaFX Browser");
            primaryStage.setScene(scene);
            Image img = new Image(getClass().getResource("/png/logo.png").toExternalForm());
            stg.getIcons().add(img);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeScene(String fxml){
        try{
            Parent pane = FXMLLoader.load(getClass().getResource(fxml));
            stg.getScene().setRoot(pane);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
