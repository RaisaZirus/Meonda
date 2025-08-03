package com.browser.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BrowserController implements Initializable {


    @FXML
    private MenuButton menu;


    @FXML
    private Button homeIcon,forward,backward,refresh;
    @FXML
    private Button loadbtn,google,bookmark;

    @FXML
    private WebView webView;

    @FXML
    private TextField textField;

    private WebEngine engine;
    @FXML
    MenuItem history;

    @FXML
    private MenuItem bookmarkmenuitem;

    @FXML
    private MenuItem logoutmenuitem;


    @FXML
    public void loadPage(ActionEvent event) {

        String input = textField.getText();
        if (input != null && input.startsWith("http")) {
            engine.load(input);
            engine.locationProperty().addListener(new Downloadagent(webView));
            addContextMenu(webView, engine);
            engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    engine.executeScript("document.addEventListener('contextmenu', function(e) { e.preventDefault(); });");
                }
            });
        } else if(input!=null) {
            input = "https://www.bing.com/search?q=" + input;
            engine.load(input);
            engine.locationProperty().addListener(new Downloadagent(webView));
            addContextMenu(webView, engine);
            engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    engine.executeScript("document.addEventListener('contextmenu', function(e) { e.preventDefault(); });");
                }
            });
        }
        SharedController.getInstance().addToHistory(input);
    }
    public void loadFromStringURL(String input) {

        if (input != null && input.startsWith("http")) {
            engine.load(input);
            engine.locationProperty().addListener(new Downloadagent(webView));
            addContextMenu(webView, engine);
            engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    engine.executeScript("document.addEventListener('contextmenu', function(e) { e.preventDefault(); });");
                }
            });
        } else if(input!=null) {
            input = "https://www.bing.com/search?q=" + input;
            engine.load(input);
            engine.locationProperty().addListener(new Downloadagent(webView));
            addContextMenu(webView, engine);
            engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    engine.executeScript("document.addEventListener('contextmenu', function(e) { e.preventDefault(); });");
                }
            });
        }
        SharedController.getInstance().addToHistory(input);

    }
    private static class Downloadagent implements ChangeListener<String> {

        private final WebView webView;

        public Downloadagent(WebView webView) {
            this.webView = webView;
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            System.out.println("Navigated to: " + newValue);
            if (isDownloadable(newValue)) {
                System.out.println("Detected downloadable URL: " + newValue);
                Downloader dm = new Downloader(webView.getScene().getWindow());
                dm.download(newValue);
                webView.getEngine().getLoadWorker().cancel();
            }
        }

        private boolean isDownloadable(String url) {
            if (url == null || url.isEmpty()) {
                return false;
            }

            url = url.toLowerCase();

            if (url.endsWith(".pdf")) return true;
            else if (url.endsWith(".zip")) return true;
            else if (url.endsWith(".docx")) return true;
            else if (url.endsWith(".xlsx")) return true;
            else if (url.endsWith(".pptx")) return true;
            else if (url.endsWith(".exe")) return true;
            else if (url.endsWith(".jpg")) return true;
            else if (url.endsWith(".jpeg")) return true;
            else if (url.endsWith(".png")) return true;
            else if (url.endsWith(".mp3")) return true;
            else if (url.endsWith(".mp4")) return true;
            else if (url.endsWith(".rar")) return true;
            else return false;
        }
    }

    public void goToHome(ActionEvent e) throws IOException {
        SharedController.getInstance().getTabController().OpenHomeinSameTab(e);
    }

    @FXML
    public void openMenu(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        if (source == history) {
            SharedController.getInstance().getTabController().openHistoryTab();
        }
        else if(source == logoutmenuitem){
            SharedController.getInstance().getTabController().logOutNow();
        } else if (source==bookmarkmenuitem) {
            SharedController.getInstance().getTabController().openBookMarkTab();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engine = webView.getEngine();

        //engine.load("https://www.google.com");
    }

    public void backward(ActionEvent e) throws IOException {
        WebHistory history = engine.getHistory();
        if (history.getCurrentIndex() > 0) {
            history.go(-1);
        }
        else if(history.getCurrentIndex()==0){
            goToHome(e);
        }
    }

    public void forward(ActionEvent e)  {
        WebHistory history = engine.getHistory();
        if (history.getCurrentIndex() >0) {
            history.go(1);
        }

    }
    public void refresh(ActionEvent e){
        engine.reload();
    }
    public WebView getWebView() {
        return webView;
    }

    //context menu update???
    private void addContextMenu(WebView webView, WebEngine webEngine){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem downloadImage = new MenuItem("Download Image");
        MenuItem copyLinkAddress = new MenuItem("Copy Link Address");
        MenuItem openInNewTab = new MenuItem("Open in New Tab");

        final String[] imageUrl = {null};
        final String[] linkUrl = {null};

        webView.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                webEngine.executeScript("""
                document.addEventListener('contextmenu', function(e) {
                    window.imageToDownload = (e.target.tagName === 'IMG') ? e.target.src : null;
                    window.linkToHandle = (e.target.closest('a')) ? e.target.closest('a').href : null;
                }, { once: true });
            """);
            }
        });

        webView.setOnContextMenuRequested(e -> {
            try {
                e.consume();
                imageUrl[0] = (String) webEngine.executeScript("window.imageToDownload");
                linkUrl[0] = (String) webEngine.executeScript("window.linkToHandle");

                contextMenu.getItems().clear();

                if (imageUrl[0] != null && imageUrl[0].startsWith("http")) {
                    contextMenu.getItems().add(downloadImage);
                }

                if (linkUrl[0] != null && linkUrl[0].startsWith("http")) {
                    contextMenu.getItems().addAll(copyLinkAddress, openInNewTab);
                }

                if (!contextMenu.getItems().isEmpty()) {
                    contextMenu.show(webView, e.getScreenX(), e.getScreenY());
                } else {
                    contextMenu.hide();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        downloadImage.setOnAction(e -> {
            if (imageUrl[0] != null) {
                Downloader dm = new Downloader(webView.getScene().getWindow());
                dm.download(imageUrl[0]);
            }
        });

        copyLinkAddress.setOnAction(e -> {
            if (linkUrl[0] != null) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(linkUrl[0]);
                clipboard.setContent(content);
            }
        });

        openInNewTab.setOnAction(e -> {
            if (linkUrl[0] != null) {
                try {
                    SharedController.getInstance().getTabController().openBrowserInNewTab(linkUrl[0],e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void showBookmarkPopup(ActionEvent event) {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Add Bookmark");

        Label labelPrompt = new Label("Enter a label for the bookmark:");

        TextField labelInput = new TextField();
        labelInput.setPromptText(" add bookmark name");

        Button confirmBtn = new Button("Add");
        Button cancelBtn = new Button("Cancel");

        HBox buttons = new HBox(10, confirmBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, labelPrompt, labelInput, buttons);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        popup.setScene(scene);
        popup.setResizable(false);

        String url = webView.getEngine().getLocation();

        confirmBtn.setOnAction(e -> {
            String label = labelInput.getText().trim();

            if (!label.isEmpty()) {
                SharedController.getInstance().addBookmark(label, url);
                popup.close();
            }
        });

        cancelBtn.setOnAction(e -> popup.close());

        popup.showAndWait();
    }


    public void searchFromGoogle(ActionEvent actionEvent) {
        engine.load("https://www.google.com");
        SharedController.getInstance().addToHistory("https://www.google.com");
    }

}
