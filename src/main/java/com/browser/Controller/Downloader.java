package com.browser.Controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
public class Downloader {
    private final Window thatWindow;
    public Downloader(Window window){
        thatWindow = window;
    }
    public void download(String fileurl){
        Platform.runLater(() ->{
            try {
                URL url = new URL(fileurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "JavaFX-Browser");
                conn.connect();
                String downfileName = extractFileName(conn, fileurl);
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName(downfileName);
                File targetFile = fileChooser.showSaveDialog(thatWindow);
                if (targetFile == null) return;

                try (InputStream in = conn.getInputStream();
                     FileOutputStream out = new FileOutputStream(targetFile)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1)
                        out.write(buffer, 0, bytesRead);
                }
                showSuccess(targetFile.getAbsolutePath());

            }catch (Exception e){
                showError(e.getMessage());
            }
        });

    }
    private String extractFileName(HttpURLConnection conn, String fileurl){
        String cont = conn.getHeaderField("Content-Disposition");
        if(cont != null && cont.contains("filename=")){
            String ans[] = cont.split("filename=");
            ans[1] = ans[1].replace("\"", "").trim();
            return ans[1];
        }
        return fileurl.substring(fileurl.lastIndexOf("/") + 1);
    }
    private void showSuccess(String path) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Download Complete");
            alert.setHeaderText(null);
            alert.setContentText("Downloaded to:\n" + path);
            alert.showAndWait();
        });
    }
    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Download Failed");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + message);
            alert.showAndWait();
        });
    }
}
