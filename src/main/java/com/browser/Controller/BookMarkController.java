package com.browser.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class BookMarkController implements Initializable {

    @FXML
    private ListView<Hyperlink> bookmarkListView;
    private final static String server_host = "localhost";
    /// ///////////////////////////////////////////////////////////////////////////////////////////
    public void removeBookmark(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Hyperlink selectedUrl = bookmarkListView.getSelectionModel().getSelectedItem();
            if (selectedUrl != null) {
                SharedController.getInstance().removeBookmark(selectedUrl.getText());;
            }
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SharedController.getInstance().setBookMarkController(this);
        bookmarkListView.getItems().clear();

        try (Socket socket = new Socket(server_host, 5555);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String currentUser = SharedController.getInstance().getLogInController().getUsername();
            out.println("GET_BOOKMARKS " + currentUser);
            int size = Integer.parseInt(in.readLine());
            for (int i = 0; i < size; i++) {
                String line = in.readLine();
                String[] parts = line.split("<>");
                String label = parts[0].trim();
                String url = parts[1].trim();

                Hyperlink link = new Hyperlink(label);
                link.setOnAction(e -> {
                    try {
                        SharedController.getInstance().getTabController().openBrowserInNewTab(url, null);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                bookmarkListView.getItems().add(link);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
