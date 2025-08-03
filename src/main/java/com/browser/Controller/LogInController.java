package com.browser.Controller;

import com.browser.Home;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private ImageView image;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Label wrongLogIn;
    @FXML
    private AnchorPane mainPane;
    //server update
    @FXML
    private Button signupbutton;
    private static final String SERVER_HOST = "localhost";
    /// /////////////////////////////////////////////////////////////////////////////////////////
    private static final int    SERVER_PORT = 5555;

    public void userLogIn(ActionEvent event)throws IOException {
        checkLogIn();
    }
    private void checkLogIn(){
        Home h = new Home();
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (user.isEmpty() || pass.isEmpty()) {
            wrongLogIn.setText("Please enter both username & password");
            return;
        }
        login.setDisable(true);
        wrongLogIn.setText("Authenticating…");

        Task<Boolean> authTask = new Task<>() {
            @Override
            protected Boolean call() {
                return authenticateWithServer(user, pass);
            }
        };
        authTask.setOnSucceeded(e -> {
            login.setDisable(false);
            if (authTask.getValue()) {
                wrongLogIn.setText("Success");
                h.changeScene("/fxml/Home.fxml");
            } else {
                wrongLogIn.setText("Invalid credentials");
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid credentials");
                alert.showAndWait();
            }
        });
        authTask.setOnFailed(e -> {
            login.setDisable(false);
            wrongLogIn.setText("Network error");
            authTask.getException().printStackTrace();
        });

        new Thread(authTask).start();
        if(authenticateWithServer(user, pass)){
            wrongLogIn.setText("Success");
            h.changeScene("/fxml/Home.fxml");
        }else{
            wrongLogIn.setText("Wrong UserName or PassWord!!");
        }

    }
    private boolean authenticateWithServer(String user, String pass) {
        try (Socket sock = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()))) {

            out.println("LOGIN " + user + " " + pass);
            String response = in.readLine();
            return "OK".equals(response);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    @FXML
    public void showSignUpWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Signup.fxml")
            );
            Parent signupRoot = loader.load();
            Stage signupStage = new Stage();
            signupStage.setTitle("Sign Up");
            signupStage.initModality(Modality.APPLICATION_MODAL);
            signupStage.setResizable(false);
            signupStage.setScene(new Scene(signupRoot));
            signupStage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getUsername(){
        return username.getText().toString().trim();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
