package com.aaratechnologies.lawtrend.models;

public class ModelDataWithLabel {
    String title;
    String content;
    String imageres;
    String time;
    String id;
    String shortContent;
    String label;
    String sharelink;

    public ModelDataWithLabel(String title, String content, String imageres, String time, String id, String shortContent, String label, String sharelink) {
        this.title = title;
        this.content = content;
        this.imageres = imageres;
        this.time = time;
        this.id = id;
        this.shortContent = shortContent;
        this.label = label;
        this.sharelink = sharelink;
    }

    public ModelDataWithLabel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageres() {
        return imageres;
    }

    public void setImageres(String imageres) {
        this.imageres = imageres;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSharelink() {
        return sharelink;
    }

    public void setSharelink(String sharelink) {
        this.sharelink = sharelink;
    }
}
