package com.example.lingxuan925.anif;

import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;

public class SignInPage extends AppCompatActivity {

    Intent nextIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_screen);

        nextIntent = new Intent(getApplicationContext(), MainActivity.class);

        Button btnLogin = (Button) findViewById(R.id.login);
        Button btnSignUp = (Button) findViewById(R.id.signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(nextIntent);
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
