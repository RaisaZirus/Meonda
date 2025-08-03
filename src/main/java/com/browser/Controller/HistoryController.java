package com.browser.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HistoryController {

    @FXML private ListView<String> historyListView;
    private final static String server_host = "localhost";
    /// ///////////////////////////////////////////////////////////////////////
    @FXML
    public void initialize() {
        SharedController.getInstance().setHistoryController(this);
        historyListView.getItems().clear();

        try (Socket socket = new Socket(server_host, 5555);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String currentUser = SharedController.getInstance().getLogInController().getUsername();
            out.println("GET_HISTORY " + currentUser);
            int size = Integer.parseInt(in.readLine());
            for (int i = 0; i < size; i++) {
                String line = in.readLine();
                historyListView.getItems().add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleHistoryClick(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            String selected = historyListView.getSelectionModel().getSelectedItem();
            String[] parts = selected.split("<>");
            String time = parts[0];
            String url = parts[1];
            if (url != null) {
                SharedController.getInstance().getTabController().loadPageFromHistory(url);
            }
        }
    }

    @FXML
    public void clearHistory() {
        String currentUser = SharedController.getInstance().getLogInController().getUsername();
        try (Socket socket = new Socket(server_host, 5555);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("CLEAR_HISTORY " + currentUser);
            String response = in.readLine();
            if ("OK".equals(response)) {
                historyListView.getItems().clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
