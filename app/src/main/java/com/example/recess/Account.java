package com.example.recess;

/**
 * A class to store and manage user account data
 */
public class Account {
    String email;
    String userID;
    String username;
    String password;
    String name;
    String baseURL;

    public Account(String email, String userID, String username, String password, String name, String baseURL) {
        this.email = email;
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.baseURL = baseURL;
    }

    public Account (String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account() { }

    public void copy(Account other) {
        this.email = other.email;
        this.userID = other.userID;
        this.username = other.username;
        this.password = other.password;
        this.name = other.name;
        this.baseURL = other.baseURL;
    }

    public String toString() {
        return String.format("Name: %s\nEmail: %s\nUserID: %s\n", name, email, userID);
    }
}
