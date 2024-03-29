package com.example.live;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {
    private FirebaseAuth mAuth;

    private static UserRepository instance;

    private UserRepository() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }

        return instance;
    }

    public boolean isUserLogged() {
        return mAuth.getCurrentUser() != null;
    }

    public Task<AuthResult> loginUser(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> createUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
}
