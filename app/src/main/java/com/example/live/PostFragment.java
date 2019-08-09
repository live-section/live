package com.example.live;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {


    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize posts with temp values
        List<Post> posts = Post.createPostsList(20);

        //inflater.inflate(R.layout.item_post, container, false);
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // Create adapter passing in the sample user data
        PostsAdapter adapter = new PostsAdapter(posts);

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

        Button newPostButton = rootView.findViewById(R.id.navigateToNewPostButton);

        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(rootView).navigate(R.id.action_PostsFragment_to_newPostFragment);
            }
        });

        Button myPostsButton = rootView.findViewById(R.id.navigateToMyPostsButton);

        myPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(rootView).navigate(R.id.action_PostsFragment_to_myPostsFragment);
            }
        });

        return rootView;
    }

}