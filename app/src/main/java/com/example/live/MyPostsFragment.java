package com.example.live;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class MyPostsFragment extends Fragment {

    public MyPostsAdapter adapter;
    private MyPostViewModel viewModel;
    private RecyclerView recyclerView;
    private View rootView;

    public MyPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public
    void onActivityCreated(
            @Nullable Bundle savedInstanceState
    ) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MyPostViewModel.class);

        viewModel.getPosts().observe(this, posts -> {
            this.replaceRecyclerAdapter(posts, this.rootView);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_posts, container, false);

        this.rootView = rootView;
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

    public void replaceRecyclerAdapter(List<Post> posts, View rootView) {
        // 1. get a reference to recyclerView
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.myPostsRecyclerView);

        // Create adapter passing in the sample user data
        adapter = new MyPostsAdapter(posts);

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


        Drawable icon = rootView.getResources().getDrawable(R.drawable.baseline_delete_black_36dp);
        ColorDrawable background = new ColorDrawable(Color.LTGRAY);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteMyPostCallback(adapter, icon, background, viewModel));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        recyclerView.setHasFixedSize(true);
    }
}
