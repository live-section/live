package com.example.live;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostRepository {
    private MutableLiveData<List<Post>> allPosts;
    private CollectionReference postsCollectionRef = FirebaseFirestore.getInstance().collection("posts");

    public LiveData<List<Post>> registerToAllPosts() {
        if (allPosts != null) {
            return allPosts;
        }

        allPosts = new MutableLiveData<>();

        postsCollectionRef
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Post> currentPosts = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currentPosts.add(DeserializeToPost(document));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }

                            allPosts.setValue(currentPosts);

                            postsCollectionRef
                                    .orderBy("date", Query.Direction.DESCENDING)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            if (e != null) {
                                                Log.w(TAG, "Listen failed.", e);
                                                return;
                                            }

                                            List<Post> tempPosts = new ArrayList<>();

                                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                tempPosts.add(DeserializeToPost(document));
                                            }

                                            allPosts.setValue(tempPosts);
                                        }
                                    });
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return allPosts;
    }

    public static Post DeserializeToPost(QueryDocumentSnapshot document) {
        Map<String, Object> objectData = document.getData();
        Timestamp ts = (Timestamp)objectData.get("date");

        return (new Post((String)(objectData.get("title")), (String)(objectData.get("text")), (String)(objectData.get("image")), (String)(objectData.get("user")), ts.toDate()));
    }
}
