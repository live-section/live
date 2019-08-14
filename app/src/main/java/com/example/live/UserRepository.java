package com.example.live;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {
    // Initialize Firebase Auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public boolean isUserLogged() {
        return mAuth.getCurrentUser() != null;
    }

    public Task<AuthResult> loginUser(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> createUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }
}
