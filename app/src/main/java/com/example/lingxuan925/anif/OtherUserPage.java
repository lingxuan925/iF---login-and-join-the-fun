package com.example.lingxuan925.anif;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserPage extends AppCompatActivity {

    CircleImageView avatar;
    TextView name;
    TextView age;
    ImageView gender;
    TextView whatsup;
    Button sendEmail;
    private DatabaseHelper dbHelper;
    private FirebaseAuth mAuth;

    public OtherUserPage() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_layout);

        String eventKey = "";
        Bundle extra = getIntent().getExtras();
        if (extra != null) eventKey = extra.getString("eventkey");

        dbHelper = new DatabaseHelper();
        mAuth = FirebaseAuth.getInstance();

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

        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        whatsup = findViewById(R.id.whatsUp);
        sendEmail = findViewById(R.id.sendEmail);

        dbHelper.getDatabaseEvents().child(eventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dbHelper.getDatabaseUsers().child(dataSnapshot.getValue(Event.class).getHostname()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                name.setText(dataSnapshot.getValue(AppUser.class).getName());
                                if (dataSnapshot.getValue(AppUser.class).getGender().equals("male")) {
                                    gender.setImageResource(R.drawable.icon_gender_man);
                                } else if (dataSnapshot.getValue(AppUser.class).getGender().equals("female")) {
                                    gender.setImageResource(R.drawable.icon_gender_woman);
                                } else {
                                    gender.setImageResource(R.drawable.icon_gender_unknown);
                                }
                                Glide.with(getApplicationContext()).load(dataSnapshot.getValue(AppUser.class).getImageUri()).apply(new RequestOptions().fitCenter()).into(avatar);
                                whatsup.setText(dataSnapshot.getValue(AppUser.class).getWhatsup());
                                age.setText("Age: "+dataSnapshot.getValue(AppUser.class).getAge());

                                sendEmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent data = new Intent(Intent.ACTION_SENDTO);

                                        //TODO: change the destination below to the email address of the user
                                        data.setData(Uri.parse("mailto:"+dataSnapshot.getValue(AppUser.class).getEmail()));
                                        startActivity(data);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}