package com.example.live;


import java.util.ArrayList;
import java.util.Date;

public class Post {
    private String postId;
    private String title;
    private String text;
    private String image;
    private String user;
    private Date date;
    private boolean expanded;

    public Post(String postId, String title, String text, String image, String user, Date date) {
        this.postId = postId;
        this.title = title;
        this.text = text;
        this.image = image;
        this.user = user;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getuser(){return user;}

    public Date getDate(){return date;}

    public String getImage() {
        return image;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public String getPostId() {
        return this.postId;
    }
}