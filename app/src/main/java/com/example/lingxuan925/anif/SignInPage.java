package com.example.lingxuan925.anif;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class SignInPage extends AppCompatActivity {

    Intent signupWithGmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_screen);

        signupWithGmail = new Intent(getApplicationContext(), Signup.class);

        Button btnSignUp = findViewById(R.id.signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(signupWithGmail);
            }
        });
    }
}
