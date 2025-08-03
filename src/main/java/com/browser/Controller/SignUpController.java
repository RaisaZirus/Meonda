package com.browser.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SignUpController {
    @FXML
    private TextField username;
    @FXML
    private TextField emailid;
    @FXML
    private PasswordField password;
    @FXML
    private Button signup;
    @FXML
    private Label Info;
    private static final String server_host = "localhost";
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final int    server_port = 5555;

    public void SignUp (ActionEvent event)throws IOException{
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (user.isEmpty() || pass.isEmpty()) {
            Info.setText("Please enter both username & password");
            return;
        }
        signup.setDisable(true);
        Info.setText("Authenticating…");

        try{
            Socket sock = new Socket(server_host, server_port);
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out.println("REGISTER " + user + " " + pass);
            String response = in.readLine();
            if("REGISTERED".equals(response)){

                Stage thisStage = (Stage) signup.getScene().getWindow();
                thisStage.close();
            } else if ("EXISTS".equals(response)) {
                Info.setText("You're already signed up! Login instead");
            }
        } catch (IOException ex) {
            ex.printStackTrace();

            
        }
    }
}
