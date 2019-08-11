package com.example.live;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;


public class MyPostsFragment extends Fragment {

    public MyPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View fragmentView = inflater.inflate(R.layout.fragment_my_posts, container, false);

        // Initialize posts with temp values
        List<Post> posts = Post.createPostsList2(2);

        //inflater.inflate(R.layout.item_post, container, false);
        View rootView = inflater.inflate(R.layout.fragment_my_posts, container, false);

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.myPostsRecyclerView);

        // Create adapter passing in the sample user data
        MyPostsAdapter adapter = new MyPostsAdapter(posts);

        // Removes blinks
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        // 2. set layoutManger
        LinearLayoutManager layoutManager = new LinearLayoutManager((getActivity()));

        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);

        // Attach layout manager to the RecyclerView
        recyclerView.setLayoutManager(layoutManager);

        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "searchview BEGONE from my posts");
        super.onCreate(savedInstanceState); setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        Log.d("tag", "on preperation of my posts ydig");
        menu.findItem(R.id.post_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
