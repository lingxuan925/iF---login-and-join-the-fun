package com.example.lingxuan925.anif;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CategoryDetail extends AppCompatActivity {

    public CategoryDetail() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        TextView title = findViewById(R.id.title_toolbar);
        switch(FragmentCategory.currentCategory){
            case R.id.entertainment:
                title.setText("Entertainment");
                break;
            case R.id.sports:
                title.setText("Sports");
                break;
            case R.id.foodies:
                title.setText("Foodies");
                break;
            case R.id.travel:
                title.setText("Travel");
                break;
        }

        Button backButton = mToolbar.findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}