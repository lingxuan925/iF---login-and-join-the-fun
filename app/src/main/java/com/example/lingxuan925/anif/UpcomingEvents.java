package com.example.lingxuan925.anif;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpcomingEvents extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Event> upcomingEvents;
    DatabaseReference databaseRef;
    FirebaseAuth mAuth;
    ListView listView;
    EventsAdapter adapter;

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
        upcomingEvents = new ArrayList<>();

        databaseRef = FirebaseDatabase.getInstance().getReference("Events");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser current_user = mAuth.getCurrentUser();
        final String cur_user_key = current_user.getUid();

        listView = findViewById(R.id.upcoming_events_list);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Event anEvent = ds.getValue(Event.class);
                    upcomingEvents.add(anEvent);
                }
                adapter = new EventsAdapter(UpcomingEvents.this, 0, upcomingEvents);
                listView.setOnItemClickListener(UpcomingEvents.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
