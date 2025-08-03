package com.browser.Controller;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Server {
    private static final int PORT = 5555;
    private final UserInfoStore store = new UserInfoStore();

    public void start() {
        try{
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("AuthServer listening on port " + PORT);
            while (true) {
                Socket client = server.accept();
                new Thread(() -> handleClient(client)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void handleClient(Socket sock) {
        System.out.println("Connection from " + sock.getInetAddress());
        try{
            BufferedReader in  = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter    out = new PrintWriter(sock.getOutputStream(), true);
            String line = in.readLine();
            if (line != null && line.startsWith("LOGIN ")) {
                //System.out.println("heheh");
                String[] parts = line.split(" ", 3);
                if (parts.length == 3) {
                    boolean ok = store.validate(parts[1], parts[2]);
                    out.println(ok ? "OK" : "FAIL");
                } else {
                    out.println("ERROR Invalid LOGIN syntax");
                }
            }
            else if (line != null && line.startsWith("REGISTER ")){
                String[] parts = line.split(" ", 3);
                if (parts.length == 3) {
                    int created = store.register(parts[1],parts[2]);
                    if(created==1){
                        out.println("REGISTERED");
                    }else if(created==2){
                        out.println("EXISTS");
                    }
                } else {
                    out.println("ERROR Invalid SIGNUP syntax");
                }
            } //server update
            else if (line != null && line.startsWith("GET_HISTORY ")) {
                String[] parts = line.split(" ", 2);
                sendUserData("history", parts[1], out);
            }
            else if (line != null && line.startsWith("ADD_HISTORY ")) {
                String[] parts = line.split(" ", 3);
                appendUserData("history", parts[1], parts[2]);
                out.println("OK");
            }
            else if (line != null && line.startsWith("GET_BOOKMARKS ")) {
                String[] parts = line.split(" ", 2);
                sendUserData("bookmarks", parts[1], out);
            }
            else if (line != null && line.startsWith("ADD_BOOKMARK ")) {
                String[] parts = line.split(" ", 3);
                appendUserData("bookmarks", parts[1], parts[2]);
                out.println("OK");
            }
            else if (line != null && line.startsWith("CLEAR_HISTORY ")) {
                String[] parts = line.split(" ", 2);
                clearUserData("history", parts[1]);
                out.println("OK");
            }
            else {
                out.println("ERROR Unrecognized command");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { sock.close(); } catch (IOException ignored) {}
        }
    }
    //Server update
    private void sendUserData(String type, String username, PrintWriter out) throws IOException {
        File file = new File("data/" + type + "/" + username + ".txt");
        if (!file.exists()) {
            out.println("0");
            return;
        }
        List<String> lines = Files.readAllLines(file.toPath());
        out.println(lines.size());
        for (String line : lines) {
            out.println(line);
        }
    }
    //server update
    private void appendUserData(String type, String username, String data) throws IOException {
        File file = new File("data/" + type + "/" + username + ".txt");
        file.getParentFile().mkdirs();
        Files.write(file.toPath(), (data + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    private void clearUserData(String type, String username) throws IOException {
        File file = new File("data/" + type + "/" + username + ".txt");
        if (file.exists()) {
            new PrintWriter(file).close();
        }
    }
    public static void main(String[] args) {
        new Server().start();
    }
}
