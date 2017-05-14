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
    public int like;
    public boolean likeflag;

    public ImageUpload(String name, String content, String url, String location,double log, double lat, String diaryDate, int like, boolean likeflag) {
        this.name = name;
        this.content = content;
        this.url = url;
        this.log = log;
        this.lat = lat;
        this.location = location;
        this.diaryDate = diaryDate;
        this.like = like;
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

    public String getLocation(){return location;}
    public String getDiaryDate(){return diaryDate;}

    public ImageUpload() {

    }
}
