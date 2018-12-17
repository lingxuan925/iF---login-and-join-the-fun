package com.example.lingxuan925.anif;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryDetail extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Event> eventsByType;
    FirebaseAuth mAuth;
    ListView listView;
    EventsAdapter adapter;
    DatabaseHelper dbHelper;
    AlertDialog dialog;
    View viewJoin;
    private String clickedEventKey;
    private LinearLayout emptyBackground;

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
        eventsByType = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper();
        viewJoin = View.inflate(this, R.layout.marker_popup_layout, null);
        dialog = setUpMarkerPopupView();

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

        emptyBackground = findViewById(R.id.empty_view);
        listView = findViewById(R.id.detail_list);
        adapter = new EventsAdapter(CategoryDetail.this, 0, eventsByType);
        listView.setOnItemClickListener(CategoryDetail.this);
        listView.setAdapter(adapter);
        dbHelper.getDatabaseUsers().child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dbHelper.fetchEventsByType(emptyBackground, mAuth, adapter, eventsByType, title.getText().toString(), dataSnapshot.getValue(AppUser.class).getRadius());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button backButton = mToolbar.findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clickedEventKey = eventsByType.get(position).getId();
//        dialog.setTitle(eventsByType.get(position).getName());
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