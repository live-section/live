package com.example.live;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    public LiveData<List<Post>> registerToAllMyPosts() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return null;
        } else {
            return registerToAllPosts(user.getEmail());
        }
    }

    public LiveData<List<Post>> registerToAllPosts(@Nullable String userEmail) {
        if (allPosts != null) {
            return allPosts;
        }

        allPosts = new MutableLiveData<>();

        Query firebaseQuery = postsCollectionRef
                .orderBy("date", Query.Direction.DESCENDING);

        if (userEmail != null) {
            firebaseQuery = firebaseQuery
                    .whereEqualTo("user", userEmail);
        }

        firebaseQuery
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

    public Task<Void> removePost(Post id) {
        if (id == null) {
            Log.w(TAG, "Why are you trying to delete null post, this isn't ideal.");
            return null;
        }
        else {
            return postsCollectionRef
                    .document(id.getPostId())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "post id " + id.getPostId() + " BEGONE!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error deleting document.", e);
                }
            });
        }
    }

    public static Post DeserializeToPost(QueryDocumentSnapshot document) {
        Map<String, Object> objectData = document.getData();
        Timestamp ts = (Timestamp)objectData.get("date");

        return (new Post((String)(objectData.get("postId")),(String)(objectData.get("title")), (String)(objectData.get("text")), (String)(objectData.get("image")), (String)(objectData.get("user")), ts.toDate()));
    }
}
