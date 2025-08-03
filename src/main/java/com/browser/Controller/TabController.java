package com.browser.Controller;

import com.browser.Home;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.scene.control.MenuItem;

public class TabController implements Initializable {

    @FXML
    private Hyperlink fb;
    @FXML
    private Button homeIcon;

    @FXML
    private Hyperlink gfg;

    @FXML
    private Button homebtn;

    @FXML
    private ImageView image;

    @FXML
    private Button loadbtn,loadbtn2;

    @FXML
    private MenuButton menu;

    @FXML
    private Hyperlink moodle;

    //@FXML
    //private Button newTab;

    @FXML
    private ImageView img,img2;//= new ImageView("/png/free-search-icon-2911-thumb.png");
    @FXML
    private TextField textField,textField2;

    @FXML
    private Hyperlink wiki;

    @FXML
    private Hyperlink yt;

    @FXML
    private  TabPane tabPane;

    private BrowserController currentBrowserController;

    @FXML
    private MenuItem historyMenuItem;
    @FXML
    private MenuItem logoutmenuitem;


    //opening tabs

    void openBrowserInNewTab(String url, Event event) throws IOException {
        FXMLLoader browserLoader = new FXMLLoader(getClass().getResource("/fxml/browser.fxml"));
        Parent browserContent = browserLoader.load();
        BrowserController newBrowserController = browserLoader.getController();
        SharedController.getInstance().setBrowserController(newBrowserController);
        ((BorderPane) browserContent).setUserData(newBrowserController);

        Tab newBrowserTab = new Tab(getDomainNameFromUrl(url));
        newBrowserTab.setContent(browserContent);
        newBrowserTab.setClosable(true);
        int insertPos = tabPane.getTabs().size() - 1;
        tabPane.getTabs().add(insertPos,newBrowserTab);
        tabPane.getSelectionModel().select(newBrowserTab);

        newBrowserController.loadFromStringURL(url);
    }
    public void openBrowserInCurrTab(String url,Event event) throws IOException {
        if (url == null || url.trim().isEmpty()) {
            return;
        }

        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();

        if (currentTab == null || !currentTab.isClosable()) {
            openBrowserInNewTab(url,event);
            return;
        }

        Node content = currentTab.getContent();
        BrowserController existingBrowserController = null;

        if (content instanceof BorderPane) {
            existingBrowserController = (BrowserController) ((BorderPane) content).getUserData();
        }
        if (existingBrowserController != null) {
            existingBrowserController.loadFromStringURL(url);
            SharedController.getInstance().setBrowserController(existingBrowserController);
            currentTab.setText(getDomainNameFromUrl(url));
            existingBrowserController.getWebView().getEngine().titleProperty().addListener((obs, oldTitle, newTitle) -> {
                if (newTitle != null && !newTitle.trim().isEmpty()) {
                    currentTab.setText(newTitle);
                } else {
                    currentTab.setText(getDomainNameFromUrl(url));
                }
            });
        } else {
            try {
                FXMLLoader browserLoader = new FXMLLoader(getClass().getResource("/fxml/browser.fxml"));
                Parent browserContent = browserLoader.load();
                BrowserController newBrowserController = browserLoader.getController();

                ((BorderPane) browserContent).setUserData(newBrowserController);

                currentTab.setContent(browserContent);
                currentTab.setText(getDomainNameFromUrl(url));

                newBrowserController.loadFromStringURL(url);
                SharedController.getInstance().setBrowserController(newBrowserController);

                newBrowserController.getWebView().getEngine().titleProperty().addListener((obs, oldTitle, newTitle) -> {
                    if (newTitle != null && !newTitle.trim().isEmpty()) {
                        currentTab.setText(newTitle);
                    } else {
                        currentTab.setText(getDomainNameFromUrl(url));
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void OpenHomeinSameTab(ActionEvent event) throws IOException {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        FXMLLoader homeViewLoader = new FXMLLoader(getClass().getResource("/fxml/HomeView.fxml"));
        Parent homeViewContent = homeViewLoader.load();

        currentTab.setContent(homeViewContent);
        currentTab.setText("Home");

    }    public void openNewHomeTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HomeView.fxml"));
            Parent content = loader.load();

            Tab homeTab = new Tab("Home");
            homeTab.setContent(content);
            homeTab.setClosable(true);

            int insertPos = tabPane.getTabs().size() - 1;
            tabPane.getTabs().add(insertPos, homeTab);
            tabPane.getSelectionModel().select(homeTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openHistoryTab() {
        try {

            Tab historyTab = null;

            for (Tab tab : tabPane.getTabs()) {
                if ("History".equals(tab.getText())) {
                    historyTab = tab;
                    break;
                }
            }

            FXMLLoader historyLoader = new FXMLLoader(getClass().getResource("/fxml/History.fxml"));
            Parent historyContent = historyLoader.load();
            historyTab = new Tab("History");
            historyTab.setContent(historyContent);
            historyTab.setClosable(true);
            int insertPos = tabPane.getTabs().size() - 1;
            tabPane.getTabs().add(insertPos,historyTab);
            tabPane.getSelectionModel().select(historyTab);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading history view: " + e.getMessage());
            alert.showAndWait();
        }
    }
    public void openBookMarkTab(){
        try {

            Tab bookamrkTab = null;

            for (Tab tab : tabPane.getTabs()) {
                if ("Bookmark".equals(tab.getText())) {
                    bookamrkTab = tab;
                    break;
                }
            }

            FXMLLoader bookmarkLoader = new FXMLLoader(getClass().getResource("/fxml/Bookmark.fxml"));
            Parent bookmarkContent = bookmarkLoader.load();
            bookamrkTab = new Tab("Bookmark");
            bookamrkTab.setContent(bookmarkContent);
            bookamrkTab.setClosable(true);
            int insertPos = tabPane.getTabs().size() - 1;
            tabPane.getTabs().add(insertPos,bookamrkTab);
            tabPane.getSelectionModel().select(bookamrkTab);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading Bookmark view: " + e.getMessage());
            alert.showAndWait();
        }

    }
    // end of opening tab

    //helper
    @FXML
    void loadPage(ActionEvent event) throws IOException {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if(textField.getText()!=null)openBrowserInCurrTab(textField.getText(),event);
    }
    public void logOutNow(){
        Home h = new Home();
        h.changeScene("/fxml/LoginPage.fxml");
    }
    public void loadPageFromHistory(String selectedUrl) throws IOException {
        textField.setText(selectedUrl);
        openBrowserInNewTab(selectedUrl,null);
    }

    private String getDomainNameFromUrl(String url) {
        try {
            URL javaUrl = new URL(url);
            String host = javaUrl.getHost();
            if (host != null && host.startsWith("www.")) {
                return host.substring(4);
            }
            return host;
        } catch (Exception e) {
            return url;
        }
    }
    //end of helper
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SharedController.getInstance().setTabController(this);

        Tab newTab = new Tab("+");
        tabPane.getTabs().add(newTab);
        newTab.setClosable(false);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        newTab.setOnSelectionChanged(e -> {
            if (!newTab.isSelected()) return;
            openNewHomeTab();
        });

        tabPane.getTabs().remove(0);
    }

}
