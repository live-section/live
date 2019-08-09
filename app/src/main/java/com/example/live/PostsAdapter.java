package com.example.live;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    // Your holder should contain a member variable
    // for any view that will be set as you render a row


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView titleTextView;
        private TextView textTextView;
        private TextView userTextView;
        private TextView dateTextView;
        private ImageView imageView;
        private View subItem;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.post_title);
            textTextView = itemView.findViewById(R.id.sub_item_text);
            userTextView = itemView.findViewById(R.id.sub_item_user);
            dateTextView = itemView.findViewById(R.id.sub_item_date);
            imageView = itemView.findViewById(R.id.post_image);
            subItem = itemView.findViewById(R.id.sub_item);

        }

        public void setListeners() {
        }
    }

    private List<Post> mPosts;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostsAdapter(List<Post> data) {
        mPosts = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PostsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Post post = mPosts.get(position);


        // *** bind ***
        // Set item views based on your views and data model
        viewHolder.subItem.setVisibility(post.isExpanded() ? View.VISIBLE : View.GONE);
        viewHolder.titleTextView.setText(post.getTitle());
        viewHolder.textTextView.setText(post.getText());
        viewHolder.userTextView.setText("Created by : " + post.getuser());
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

        viewHolder.itemView.setOnClickListener(v -> {
            post.setExpanded(!post.isExpanded());
            notifyItemChanged(position);
        });

        //viewHolder.setListeners()

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mPosts == null)
            return 0;
        return mPosts.size();
    }


}