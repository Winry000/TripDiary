package com.example.winryxie.tripdiary.Model;

/**
 * Created by Dora on 5/11/17.
 */

public class User {
    public String nickname;
    public String emailAddress;
    public String phoneNumber;
    public String url;

    public User(String nickname, String emailAddress, String phoneNumber, String url) {
        this.nickname = nickname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.url = url;
    }

    public String getName() {
        return nickname;
    }

    public String getEmailAddress() {return emailAddress;}

    public String getUrl() {
        return url;
    }

    public String getPhoneNumber(){return phoneNumber;}

    public User() {

    }
}
