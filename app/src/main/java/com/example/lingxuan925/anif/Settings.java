package com.example.lingxuan925.anif;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class Settings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    FirebaseAuth mAuth;
    private ArrayList<Option> optionList = new ArrayList<>();
    private OptionAdapter adapter;
    DatabaseHelper dbHelper;

    public Settings() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        dbHelper = new DatabaseHelper();
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

        adapter = new OptionAdapter(this, R.layout.option_item, optionList);
        ListView listView = findViewById(R.id.settings_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        initOptions();

        FirebaseUser current_user = mAuth.getCurrentUser();
        final String cur_user_key = current_user.getUid();

        dbHelper.getDatabaseUsers().orderByChild("email").equalTo(current_user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    optionList.get(0).setName(dataSnapshot.child(cur_user_key).getValue(AppUser.class).getRadius());
                    adapter.refreshList(optionList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initOptions() {
        Option option1 = new Option("Search Radius", R.drawable.ic_blur_circular_black_24dp);
        optionList.add(option1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = (String) ((TextView) view.findViewById(R.id.option_name)).getText();
        switch (position) {
            case 0:
                Toast.makeText(this, text + " is clicked!", Toast.LENGTH_SHORT).show();
                editRadius(position);
                break;
        }
    }

    public void editRadius(final int pos) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.change_radius, null);

        final EditText radiusField = mView.findViewById(R.id.change_radius);
        Button mSave = mView.findViewById(R.id.btn_save);
        mBuilder.setView(mView);

        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editRadius = radiusField.getText().toString();

                if (!editRadius.isEmpty()){
                    try {
                        int radius = Integer.parseInt(editRadius);
                        if (radius < 0 || radius > 500) {
                            radiusField.setError("Radius must be between 0 and 500!");
                        } else {
                            dbHelper.updateUser("radius", editRadius, mAuth);
                            optionList.get(pos).setName(editRadius);
                            adapter.refreshList(optionList);
                            dialog.dismiss();
                        }
                    } catch (NumberFormatException e) {
                        radiusField.setError("Radius must be a number!");
                    }
                } else {
                    radiusField.setError("Radius is required!");
                }
            }
        });
    }
}