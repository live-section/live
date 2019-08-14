package com.example.live;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    public static PostsAdapter adapter;
    private PostViewModel viewModel;
    private RecyclerView recyclerView;
    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public
    void onActivityCreated(
            @Nullable Bundle savedInstanceState
            ) {
        super.onActivityCreated(savedInstanceState);
        // String userId = getArguments().getString(UID_KEY);
        viewModel = ViewModelProviders.of(this).get(PostViewModel.class);

        viewModel.getPosts().observe(this, posts -> {
            this.replaceRecyclerAdapter(posts, this.rootView);

            Toast.makeText(getContext(), "New posts just came live!!!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflater.inflate(R.layout.item_post, container, false);
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        this.rootView = rootView;
        return rootView;
    }

    public void replaceRecyclerAdapter(List<Post> posts, View rootView) {
        // 1. get a reference to recyclerView
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // Create adapter passing in the sample user data
        adapter = new PostsAdapter(posts);

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
    }
}
