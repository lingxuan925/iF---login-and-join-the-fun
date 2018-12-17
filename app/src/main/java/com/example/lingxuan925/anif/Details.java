package com.example.lingxuan925.anif;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class Details extends AppCompatActivity implements AdapterView.OnItemClickListener {

    FirebaseAuth mAuth;
    DatabaseHelper dbHelper;
    private ArrayList<Option> optionList = new ArrayList<>();
    private OptionAdapter adapter;

    public Details() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper();
        setContentView(R.layout.details);

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

        adapter = new OptionAdapter(this, R.layout.option_item, optionList);
        ListView listView = findViewById(R.id.detail_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        initOptions();

        FirebaseUser current_user = mAuth.getCurrentUser();
        final String cur_user_key = current_user.getUid();

        dbHelper.getDatabaseUsers().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(cur_user_key)) {
                        optionList.get(0).setName(ds.getValue(AppUser.class).getGender());
                        optionList.get(1).setName(ds.getValue(AppUser.class).getWhatsup());
                        optionList.get(2).setName(ds.getValue(AppUser.class).getAge());
                        adapter.refreshList(optionList);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initOptions() {
        Option option1 = new Option("Gender", R.drawable.ic_assignment_ind_black_24dp);
        optionList.add(option1);
        Option option2 = new Option("Message", R.drawable.ic_message_black_24dp);
        optionList.add(option2);
        Option option3 = new Option("Age", R.drawable.ic_cake_black_24dp);
        optionList.add(option3);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String text = (String) ((TextView) view.findViewById(R.id.option_name)).getText();
        switch (i) {
            case 0:
                editGender(i);
                break;
            case 1:
                editWhatsup(i);
                break;
            case 2:
                editAge(i);
                break;
        }
    }

    public void editAge(final int pos) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.change_age, null);

        final EditText ageField = mView.findViewById(R.id.change_age);
        Button mSave = mView.findViewById(R.id.btn_save);
        mBuilder.setView(mView);

        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editAge = ageField.getText().toString();

                if (!editAge.isEmpty()) {
                    try {
                        int radius = Integer.parseInt(editAge);
                        update(editAge, pos);
                        dialog.dismiss();
                    } catch (NumberFormatException e) {
                        ageField.setError("Age must be a number!");
                    }
                } else {
                    ageField.setError("Age is required!");
                }
            }
        });
    }

    public void editGender(final int pos) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                new String[]{"unknown", "male", "female"});
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        update("unknown", pos);

                        break;
                    case 1:
                        update("male", pos);
                        break;
                    case 2:
                        update("female", pos);
                        break;
                }
            }
        });
        builder.create().show();
    }

    public void editWhatsup(final int pos) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.change_whatsup_popup, null);

        final EditText ageField = mView.findViewById(R.id.change_whatsup);
        Button mSave = mView.findViewById(R.id.btn_save);
        mBuilder.setView(mView);

        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String editWhatsup = ageField.getText().toString();
                if (!editWhatsup.isEmpty()) {

                    update(editWhatsup, pos);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "field is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void update(String val, int pos) {
        try {
            switch (pos) {
                case 0:
                    dbHelper.getDatabaseUsers().child(mAuth.getCurrentUser().getUid()).child("gender").setValue(val);
                    optionList.get(pos).setName(val);
                    adapter.refreshList(optionList);
                    break;
                case 1:
                    dbHelper.getDatabaseUsers().child(mAuth.getCurrentUser().getUid()).child("whatsup").setValue(val);
                    optionList.get(pos).setName(val);
                    adapter.refreshList(optionList);
                    break;
                case 2:
                    dbHelper.getDatabaseUsers().child(mAuth.getCurrentUser().getUid()).child("age").setValue(val);
                    optionList.get(pos).setName(val);
                    adapter.refreshList(optionList);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
