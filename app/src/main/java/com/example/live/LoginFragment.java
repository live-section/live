package com.example.live;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                        // Navigate to the Home fragment.
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_liveUserActivity);
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

        return fragmentView;
    }

    private boolean validateUserLogin(CharSequence email, CharSequence password) {
        return true;
    }

}
