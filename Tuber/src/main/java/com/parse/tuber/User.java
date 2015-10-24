package com.parse.tuber;

public class User {

    String email, username, password;


    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this("", username, password);
    }
}
