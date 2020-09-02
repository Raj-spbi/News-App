package com.aaratechnologies.lawtrend.models;

public class ModelShowSearchData {
    String title;
    String content;
    String imageres;
    String time;
    String id;
    String sharelink;

    public ModelShowSearchData(String title, String content, String imageres, String time, String id, String sharelink) {
        this.title = title;
        this.content = content;
        this.imageres = imageres;
        this.time = time;
        this.id = id;
        this.sharelink = sharelink;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageres() {
        return imageres;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public String getSharelink() {
        return sharelink;
    }
}
