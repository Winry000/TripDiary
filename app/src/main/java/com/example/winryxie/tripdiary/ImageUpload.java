package com.example.winryxie.tripdiary;

/**
 * Created by winryxie on 5/7/17.
 */

public class ImageUpload {
    public String name;
    public String content;
    public String url;

    public ImageUpload(String name, String content, String url) {
        this.name = name;
        this.content = content;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getContent() {return content;}

    public String getUrl() {
        return url;
    }

    public ImageUpload() {

    }
}
