package com.example.lingxuan925.anif;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Feedback extends AppCompatActivity {

    EditText summaryText, descriptionText;
    Button btnSend;

    public Feedback() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        Button backButton = mToolbar.findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        summaryText = findViewById(R.id.feedback_summary);
        descriptionText = findViewById(R.id.feedback_description);
        btnSend = findViewById(R.id.feedback_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (summaryText.getText() != null && descriptionText.getText() != null) {
                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:zhatiayua59@gmail.com"));
                    data.putExtra(Intent.EXTRA_SUBJECT, summaryText.getText().toString());
                    data.putExtra(Intent.EXTRA_TEXT, descriptionText.getText().toString());
                    startActivity(data);
                }
            }
        });
    }
}