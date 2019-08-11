package com.example.live;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder> {

    // Your holder should contain a member variable
    // for any view that will be set as you render a row


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView titleTextView;
        private TextView textTextView;
        private TextView dateTextView;
        private ImageView imageView, imageDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.post_title);
            textTextView = itemView.findViewById(R.id.sub_item_text);
            dateTextView = itemView.findViewById(R.id.sub_item_date);
            imageView = itemView.findViewById(R.id.post_image);
            imageDelete = itemView.findViewById(R.id.img_delete);
        }
    }

    private List<Post> mPosts;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyPostsAdapter(List<Post> data) {
        mPosts = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyPostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_my_post, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyPostsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Post post = mPosts.get(position);

        // *** bind ***
        // Set item views based on your views and data model
        viewHolder.titleTextView.setText(post.getTitle());
        viewHolder.textTextView.setText(post.getText());
        viewHolder.dateTextView.setText("Posted on : " + post.getDate());

        if (post.getImage() != null) {
            Log.d("tag", "Trying to load image of post " + post.getImage());
            Picasso.get().setLoggingEnabled(true);
            Picasso.get().setIndicatorsEnabled(true);
            Picasso.get()
                    .load(post.getImage())
                    .placeholder(R.drawable.logo)
                    .into(viewHolder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("tag", "Failed to load image of post " + e.getMessage());
                        }
                    });
        }

        viewHolder.imageDelete.setOnClickListener(v -> {
             removeItem(position);
        });

        viewHolder.itemView.setOnClickListener(v -> {
            post.setExpanded(!post.isExpanded());
            notifyItemChanged(position);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mPosts == null)
            return 0;
        return mPosts.size();
    }

    public void removeItem(int position)
    {
        mPosts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mPosts.size());
//		notifyDataSetChanged();
    }

}