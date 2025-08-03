package com.browser.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserInfoStore {
    private static final String FILE = "user.properties";
    private final Properties props = new Properties();

    public UserInfoStore(){
        try {
            FileInputStream in = new FileInputStream(FILE);
            props.load(in);
            System.out.println("Loaded " + props.size() + " user(s) from " + FILE);

        }catch (IOException e){
            System.out.println("No existing user file");
        }
    }

    public boolean validate(String username, String password) {
        String stored = props.getProperty(username);
        return stored != null && stored.equals(password);
    }
    public int register(String username, String password) {
        if(props.containsKey(username))return 2;
        props.setProperty(username, password);
        try (FileOutputStream out = new FileOutputStream(FILE)) {
            props.store(out, "User credentials");
            System.out.println("Registered user: " + username);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
