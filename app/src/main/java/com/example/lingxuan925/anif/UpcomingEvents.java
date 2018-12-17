package com.example.lingxuan925.anif;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UpcomingEvents extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Event> upcomingEvents;
    FirebaseAuth mAuth;
    ListView listView;
    EventsAdapter adapter;
    DatabaseHelper dbHelper;
    AlertDialog dialog;
    View viewJoin;
    private String clickedEventKey;
    private LinearLayout emptyBackground;

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
        emptyBackground = findViewById(R.id.empty_view);
        dbHelper = new DatabaseHelper();
        upcomingEvents = new ArrayList<>();
        viewJoin = View.inflate(this, R.layout.marker_popup_layout, null);
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
        dbHelper.fetchUpcomingEvents(emptyBackground, mAuth, adapter, upcomingEvents);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clickedEventKey = upcomingEvents.get(position).getId();
        dbHelper.fetchSingleEventByID(clickedEventKey, viewJoin, mAuth, dialog);
        dialog.show();
    }

    public AlertDialog setUpMarkerPopupView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewJoin);
        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (!clickedEventKey.equals("")) {
                    dbHelper.updateUserEventList(clickedEventKey, mAuth);
                    dbHelper.updateEventParticipantList(clickedEventKey, mAuth);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_background);
        return dialog;
    }
}
