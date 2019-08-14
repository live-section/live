package com.example.live;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity
public class Post {
    @PrimaryKey
    @NonNull
    private String postId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "user")
    private String user;

    @ColumnInfo(name = "date")
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

    public String getUser(){return user;}

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