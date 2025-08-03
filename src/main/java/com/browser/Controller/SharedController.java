package com.browser.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class SharedController {

    private static final String HISTORY_FILE = "history.txt";
    private static final String BOOKMARK_FILE = "bookmarks.txt";

    private static SharedController instance;
    private HistoryController historyController;
    private TabController tabController;
    private BrowserController browserController;
    private LogInController logInController;

    private TabPane tabPane;

    public ObservableList<String> historyList = FXCollections.observableArrayList();

    public ObservableList<String> bookmarkList = FXCollections.observableArrayList();
    private BookMarkController BookMarkController;

    private final static String server_host = "localhost";
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////

    private SharedController() {
        loadHistoryFromFile();
        loadBookmarksFromFile();
    }

    public static SharedController getInstance() {
        if (instance == null) {
            instance = new SharedController();
        }
        return instance;
    }

    public void setTabController(TabController hc) {
        this.tabController = hc;
    }
    public TabController getTabController() {
        return tabController;
    }

    public void setBrowserController(BrowserController bc){
        this.browserController=bc;
    }
    public BrowserController getBrowserController(){
        return browserController;
    }

    public void setTabPane(TabPane tp){
        this.tabPane=tp;
    }
    public TabPane getTabPane() {
        return tabPane;
    }

    public void setLogInController(LogInController hc) {
        this.logInController = hc;
    }/////login update
    public LogInController getLogInController() {
        return logInController;
    }//login update


    //History Management
    private void loadHistoryFromFile() {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    historyList.add(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHistoryInFile(){

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE));
            for (String url : historyList) {
                writer.write(url+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void clearList()
    {
        historyList.clear();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //Server Update
    public void addToHistory(String url) {
        String currentUser = getLogInController().getUsername();

        LocalDateTime time = LocalDateTime.now();
        String CurrentTime = time.toString();
        String entry = CurrentTime + "<>"+ url;
        try (Socket socket = new Socket(server_host , 5555);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println("ADD_HISTORY " + currentUser + " " + entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //historyList.add(0,entry);

        //writeHistoryInFile();
    }


    public void setHistoryController(HistoryController hc) {
        this.historyController = hc;
    }
    public HistoryController getHistoryController( ){return historyController;}

    //end of history management

    //bookmark management

    private void loadBookmarksFromFile(){

        try{
            BufferedReader reader = new BufferedReader(new FileReader(BOOKMARK_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    bookmarkList.add(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeBookmarksInFile(){

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKMARK_FILE));
            for (String url : bookmarkList) {
                writer.write(url+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Server Update
    public void addBookmark(String label, String url) {
        String entry = label + " <> "+ url;
        String currentUser = getLogInController().getUsername();
        try (Socket socket = new Socket(server_host, 5555);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println("ADD_BOOKMARK " + currentUser + " " + entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //bookmarkList.add(entry);
        //writeBookmarksInFile();
    }
    public void removeBookmark(String url) {
        bookmarkList.remove(url);
        writeBookmarksInFile();
    }


    public void setBookMarkController(BookMarkController bc) {
        this.BookMarkController=bc;
    }

    public BookMarkController getBookMarkController() {
        return BookMarkController;
    }

    //end of bookmark management
}



