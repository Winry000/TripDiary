package com.example.winryxie.tripdiary;

/**
 * Created by winryxie on 5/7/17.
 */

public class ImageUpload {
    public String name;
    public String content;
    public String url;
    public double log;
    public double lat;
    public String location;
    public String diaryDate;
    public int likes;
    public boolean likeflag;

    public ImageUpload(String name, String content, String url, String location,double log, double lat, String diaryDate, int likes, boolean likeflag) {
        this.name = name;
        this.content = content;
        this.url = url;
        this.log = log;
        this.lat = lat;
        this.location = location;
        this.diaryDate = diaryDate;
        this.likes = likes;
        this.likeflag = likeflag;
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
    public int getLike () {
        return likes;
    }

    public boolean getLikeflag() {
        return likeflag;
    }

    public String getLocation(){return location;}
    public String getDiaryDate(){return diaryDate;}

    public ImageUpload() {

    }
}
