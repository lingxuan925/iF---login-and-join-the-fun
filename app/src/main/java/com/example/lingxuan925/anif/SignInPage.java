package com.example.lingxuan925.anif;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class SignInPage extends AppCompatActivity {

    Intent nextIntent;
    Intent signupWithGmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_screen);

        nextIntent = new Intent(getApplicationContext(), MainActivity.class);
        signupWithGmail = new Intent(getApplicationContext(), Signup.class);

        Button btnLogin = (Button) findViewById(R.id.login);
        Button btnSignUp = (Button) findViewById(R.id.signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(nextIntent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(signupWithGmail);
            }
        });
    }

//    private int getScreenHeight() {
//        DisplayMetrics dm = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int height = dm.heightPixels;
//        return height;
//    }

//    private void setMargin() {
//        ImageView image = (ImageView) findViewById(R.id.image_view);
//        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) image.getLayoutParams();
//        marginParams.setMargins(left, top, right, bottom);
//    }

}
