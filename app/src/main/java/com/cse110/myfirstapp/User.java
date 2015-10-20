package com.cse110.myfirstapp;

/**
 * Created by Jonathan on 10/19/2015.
 */
public class User {
    String name, username, password;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    public User(String username, String password) {
        this.name = "";
        this.username = username;
        this.password = password;
    }
}
