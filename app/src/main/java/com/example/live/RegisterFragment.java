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
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_register, container, false);

        Button button = fragmentView.findViewById(R.id.completeRegistrationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText fullNameEditText = fragmentView.findViewById(R.id.registrationFullName);
                EditText emailEditText = fragmentView.findViewById(R.id.registrationEmail);
                EditText passwordEditText = fragmentView.findViewById(R.id.registrationPassword);

                boolean errorFound = false;

                if (TextUtils.isEmpty(fullNameEditText.getText())) {
                    fullNameEditText.setError("Full name must be entered in order to register!");
                    errorFound = true;
                }

                if (TextUtils.isEmpty(emailEditText.getText())) {
                    emailEditText.setError("Email must be entered!");
                    errorFound = true;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches()) {
                    emailEditText.setError("The email you've entered is invalid!");
                    errorFound = true;
                }

                if (TextUtils.isEmpty(passwordEditText.getText())) {
                    passwordEditText.setError("Password must be entered!");
                    errorFound = true;
                }

                if (!errorFound) {
                    Navigation.findNavController(fragmentView).navigate(R.id.action_registerFragment_to_liveUserActivity);
                }
            }
        });

        return fragmentView;
    }

}
