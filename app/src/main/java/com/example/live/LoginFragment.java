package com.example.live;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

    final CharSequence invalidEmailMessage = "That's not a real email...";

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final TextView loginEmail = view.findViewById(R.id.loginEmail);
        TextView loginPass = view.findViewById(R.id.loginPassword);

        Button loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidEmail = isValidEmail(loginEmail.getText());
                if (!isValidEmail) {
                    Toast invalidEmailToast = Toast.makeText(getActivity().getApplicationContext(), invalidEmailMessage, Toast.LENGTH_LONG);
                    invalidEmailToast.show();
                } else {
                    Toast zafigPleaseNotice = Toast.makeText(getActivity().getApplicationContext(), "Zafig if you got this far remove this line and keep working lmao", Toast.LENGTH_LONG);
                }
            }
        });

        return view;
    }

}
