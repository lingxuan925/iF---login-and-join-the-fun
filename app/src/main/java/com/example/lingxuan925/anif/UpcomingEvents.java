package com.example.lingxuan925.anif;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UpcomingEvents extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Event> upcomingEvents;
    FirebaseAuth mAuth;
    ListView listView;
    EventsAdapter adapter;
    DatabaseHelper dbHelper;

    public UpcomingEvents() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_events);
        dbHelper = new DatabaseHelper();
        upcomingEvents = new ArrayList<>();
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

        mAuth = FirebaseAuth.getInstance();

        listView = findViewById(R.id.upcoming_events_list);
        adapter = new EventsAdapter(UpcomingEvents.this, 0, upcomingEvents);
        listView.setOnItemClickListener(UpcomingEvents.this);
        listView.setAdapter(adapter);
        dbHelper.fetchUpcomingEvents(mAuth, adapter, upcomingEvents);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
