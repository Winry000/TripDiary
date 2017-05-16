package com.example.winryxie.tripdiary;

import java.security.Timestamp;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Exclude;
import java.util.HashMap;
/**
 * Created by winryxie on 5/7/17.
 */

public class ImageUpload {
    public String id;
    public String name;
    public String content;
    public String url;
    public double log;
    public double lat;
    public String location;
    public String diaryDate;
    public int like;
    public boolean likeflag;
    public String country;
    public String city;
    public HashMap<String, Object> timestampCreated;

    public ImageUpload(String name, String content, String url, String location, String country, String city, double log, double lat, String diaryDate, int like, boolean likeflag) {
        this.name = name;
        this.content = content;
        this.url = url;
        this.log = log;
        this.lat = lat;
        this.location = location;
        this.diaryDate = diaryDate;
        this.like = like;
        this.likeflag = likeflag;
        this.city = city;
        this.country = country;
        HashMap<String, Object> timestampNow = new HashMap<>();
        timestampNow.put("timestamp", ServerValue.TIMESTAMP);
        this.timestampCreated = timestampNow;
    }

    public String getName() {
        return name;
    }

    public String getContent() {return content;}

    public String getUrl() {
        return url;
    }

    public double getLog(){return log;}
    public double getLat(){return lat;}

    public String getLocation(){return location;}

    public String getCity(){return city;}

    public String getCountry(){return country;}

    public String getDiaryDate(){return diaryDate;}
    public HashMap<String, Object> getTimestampCreated(){
        return timestampCreated;
    }

    @Exclude
    public long getTimestampCreatedLong(){
        return (long)timestampCreated.get("timestamp");
    }

    public ImageUpload() {

    }
}
