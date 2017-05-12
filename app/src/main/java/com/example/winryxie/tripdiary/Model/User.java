package com.example.winryxie.tripdiary.Model;

/**
 * Created by Dora on 5/11/17.
 */

public class User {
    public String nickname;
    public String emailAddress;
    public String phoneNumber;
    public String url;
    public int diaryNumber;
    public int cityNumber;
    public int countryNumber;
    public String signature;

    public User(String nickname, String emailAddress) {
        this.nickname = nickname;
        this.emailAddress = emailAddress;
        this.countryNumber = 0;
        this.diaryNumber = 0;
        this.cityNumber = 0;
        this.signature = "";
        this.phoneNumber = "";
        this.url = "";
    }

    public String getName() {
        return nickname;
    }
    public String getEmailAddress() {return emailAddress;}
    public String getUrl() {
        return url;
    }
    public int getDiaryNumber() {return diaryNumber;}
    public int getCityNumber() {return cityNumber;}
    public int getCountryNumber() {return countryNumber;}
    public String getPhoneNumber(){return phoneNumber;}
    public String getSignature(){return signature;}

    public User() {

    }
}
