package com.example.live;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM post")
    List<Post> getAllPosts();

    @Query("SELECT * FROM post WHERE user = :user")
    List<Post> getAllMyPosts(String user);

    @Insert
    void insertAll(List<Post> posts);

    @Delete
    void delete(Post post);

    @Query("DELETE FROM post")
    void deleteAll();

    @Query("DELETE FROM post WHERE user = :user")
    void deleteAllMyPosts(String user);
}
