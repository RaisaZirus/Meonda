package com.browser.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.io.IOException;

public class HomeViewController {
    @FXML
    private Button homeIcon;
    @FXML private TextField textField, textField2;
    @FXML private Button loadbtn, loadbtn2;
    @FXML private Hyperlink yt, fb, moodle, gfg, wiki;
    @FXML
    private MenuItem historyMenuItem;
    @FXML
    private MenuItem logoutmenuitem;
    @FXML
    private MenuItem bookmarkmenuitem;

    @FXML
    void loadPage(ActionEvent event) throws IOException {
        String url = textField.getText();
        if (url != null && !url.isEmpty()) {
            SharedController.getInstance().getTabController().openBrowserInCurrTab(url,event);
        }
    }
    @FXML
    void loadPage2(ActionEvent event) throws IOException {
        String url = textField2.getText();
        if (url != null && !url.isEmpty()) {
            SharedController.getInstance().getTabController().openBrowserInCurrTab(url,event);
        }
    }
    public void goToHome(ActionEvent e) throws IOException {
        SharedController.getInstance().getTabController().OpenHomeinSameTab(e);
    }
    public void searchFromGoogle(ActionEvent actionEvent) throws IOException {
        SharedController.getInstance().getTabController().openBrowserInCurrTab("https://www.google.com",null);
    }
    @FXML
    public void openMenu(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        if (source == historyMenuItem) {
            SharedController.getInstance().getTabController().openHistoryTab();
        } else if (source == logoutmenuitem) {
            SharedController.getInstance().getTabController().logOutNow();
        } else if (source== bookmarkmenuitem) {
            SharedController.getInstance().getTabController().openBookMarkTab();
        }
    }

    @FXML
    void OpenTab(ActionEvent event) throws IOException {
        Hyperlink source = (Hyperlink) event.getSource();
        String url = switch (source.getId()) {
            case "yt" -> "https://www.youtube.com";
            case "fb" -> "https://www.facebook.com";
            case "gfg" -> "https://www.twitter.com";
            case "wiki" -> "https://www.wikipedia.org";
            case "moodle" -> "https://cse.buet.ac.bd/moodle";
            default -> null;
        };

        if (url != null) {
            SharedController.getInstance().getTabController().openBrowserInCurrTab(url,event);
        }
    }

}
