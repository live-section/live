package com.example.live;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private View fragmentView;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        final TextView loginEmail = fragmentView.findViewById(R.id.loginEmail);
        final TextView loginPass = fragmentView.findViewById(R.id.loginPassword);

        Button loginButton = fragmentView.findViewById(R.id.loginButton);
        Button registerButton = fragmentView.findViewById(R.id.navigateToRegistrationButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence loginEmailText = loginEmail.getText();
                boolean isValidEmail = (!TextUtils.isEmpty(loginEmailText) && Patterns.EMAIL_ADDRESS.matcher(loginEmailText).matches());
                if (!isValidEmail) {
                    loginEmail.setError("The email you've entered is invalid!");
                } else {
                    // Validate the user's email & password.
                    if (validateUserLogin(loginEmailText, loginPass.getText())) {
                        mAuth.signInWithEmailAndPassword(loginEmailText.toString(), loginPass.getText().toString())
                                // TODO - SHOULD THIS BE this instead of getActivity()? should this be code placed in the fragment
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            // Navigate to the Home fragment.
                                            Navigation.findNavController(fragmentView).navigate(R.id.action_loginFragment_to_liveUserActivity);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(getActivity(), "Login failed. Ensure your email and password are correct.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(fragmentView).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        this.fragmentView = fragmentView;

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO - move user to inside to save time or w/e?
        if (mAuth.getCurrentUser() != null) {
            Navigation.findNavController(fragmentView).navigate(R.id.action_loginFragment_to_liveUserActivity);
        }
    }

    private boolean validateUserLogin(CharSequence email, CharSequence password) {
        return true;
    }

}
