package com.example.recess;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class to store and manage user account data
 */
class Account implements Parcelable {
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

    protected Account(Parcel in) {
        email = in.readString();
        userID = in.readString();
        username = in.readString();
        password = in.readString();
        name = in.readString();
        baseURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(userID);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(baseURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}

class Course {
    String period;
    String teacher;
    String grade;
    String name;
    String score;
    String ID;

    public Course(String period, String teacher, String grade, String name, String score, String ID) {
        this.period = period;
        this.teacher = teacher;
        this.grade = grade;
        this.name = name;
        this.score = score;
        this.ID = ID;
    }

    public Course(JSONObject json) throws JSONException {
        this(json.getString("period"), json.getString("teacherName"), json.getString("grade"), json.getString("courseName"), json.getString("score"), json.getString("periodID"));
    }
}
