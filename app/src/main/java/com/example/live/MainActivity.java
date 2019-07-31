package com.example.live;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final CharSequence invalidEmailMessage = "That's not a real email...";

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView loginEmail = this.findViewById(R.id.loginEmail);
        TextView loginPass = this.findViewById(R.id.loginPassword);

        Button loginButton = this.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidEmail = isValidEmail(loginEmail.getText());
                if (!isValidEmail) {
                    Toast invalidEmailToast = Toast.makeText(getApplicationContext(), invalidEmailMessage, Toast.LENGTH_LONG);
                    invalidEmailToast.show();
                } else {
                    Toast zafigPleaseNotice = Toast.makeText(getApplicationContext(), "Zafig if you got this far remove this line and keep working lmao", Toast.LENGTH_LONG);
                }
            }
        });
    }
}
