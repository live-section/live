package com.example.live;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
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
    private MutableLiveData<List<Post>> allMyPosts;

    private FirebaseFirestore firestoreDb;
    private static PostRepository instance;
    private UserRepository userRepository;
    private AppDatabase appCacheDb;

    public static PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
        }

        return instance;
    }

    private PostRepository() {
        firestoreDb = FirebaseFirestore.getInstance();
        userRepository = UserRepository.getInstance();

        appCacheDb = Room.databaseBuilder(Live.getAppContext(),
                AppDatabase.class, "post").build();

//        allPosts = new MutableLiveData<>();
//        allPosts.setValue(appCacheDb.postDao().getAllPosts());
//
//        allMyPosts = new MutableLiveData<>();
//        allMyPosts.setValue(appCacheDb.postDao().getAllMyPosts(userRepository.getCurrentUser().getEmail()));
    }

    public LiveData<List<Post>> registerToAllMyPosts() {
        FirebaseUser user = userRepository.getCurrentUser();

        if (user == null) {
            return null;
        } else {
            return registerToAllPosts(user.getEmail());
        }
    }

    public MutableLiveData registerToAllPosts(@Nullable String userEmail) {
        MutableLiveData posts = new MutableLiveData<>();

        if (userEmail == null && allPosts != null) {
            return allPosts;
        }

        if (userEmail != null && allMyPosts != null) {
            return allMyPosts;
        }

        Query baseFirebaseQuery = firestoreDb.collection("posts")
                .orderBy("date", Query.Direction.DESCENDING);

        if (userEmail != null) {
            baseFirebaseQuery = baseFirebaseQuery
                    .whereEqualTo("user", userEmail);
        }

        final Query firebaseQuery = baseFirebaseQuery;

        if (userEmail != null && allMyPosts == null) {
            posts.setValue(appCacheDb.postDao().getAllMyPosts(userEmail));

            registerToChanges(firebaseQuery, posts);
            return posts;
        }

        if (userEmail == null && allPosts == null) {
            posts.setValue(appCacheDb.postDao().getAllPosts());

            registerToChanges(firebaseQuery, posts);
            return posts;
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

                            posts.setValue(currentPosts);

                            registerToChanges(firebaseQuery, posts);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // We make this very final differentiation.
        // If we requested posts for the currently logged user we store them in all posts variable.
        // Otherwise, we put it in another one that is used for the user.
        if (userEmail == null) {
            allPosts = posts;
        } else {
            allMyPosts = posts;
        }

        return posts;
    }

    private void registerToChanges(Query firebaseQuery, MutableLiveData posts) {
        firebaseQuery
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

                        posts.setValue(tempPosts);
                        appCacheDb.postDao().deleteAll();
                        appCacheDb.postDao().insertAll(tempPosts);
                    }
                });
    }

    public Task<Void> removePost(Post id) {
        if (id == null) {
            Log.w(TAG, "Why are you trying to delete null post, this isn't ideal.");
            return null;
        }
        else {
            return firestoreDb.collection("posts")
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
