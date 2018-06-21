package com.awesome.adams.com.Model;

public class Blog {

    public String desc,title,image,timestamp,userid;

    public Blog() {

    }

    public Blog(String desc, String title, String image, String timestamp, String userid) {
        this.desc = desc;
        this.title = title;
        this.image = image;
        this.timestamp = timestamp;
        this.userid = userid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
