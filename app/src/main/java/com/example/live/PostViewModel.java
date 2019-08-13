package com.example.live;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostViewModel extends ViewModel {
    private MutableLiveData<List<Post>> posts;

    public LiveData<List<Post>> getPosts() {
        if (posts == null) {
            posts = new MutableLiveData<List<Post>>();
            loadPosts();
        }
        return posts;
    }

    private void loadPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Post> currentPosts = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> objectData = document.getData();
                                Timestamp ts = (Timestamp)objectData.get("date");
                                currentPosts.add(new Post((String)(objectData.get("title")), (String)(objectData.get("text")), (String)(objectData.get("image")), (String)(objectData.get("user")), ts.toDate()));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }

                            posts.setValue(currentPosts);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
