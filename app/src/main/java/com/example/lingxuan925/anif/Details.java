package com.example.lingxuan925.anif;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Details extends AppCompatActivity implements AdapterView.OnItemClickListener {

    FirebaseAuth mAuth;
    DatabaseReference databaseRef;
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
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        setContentView(R.layout.fragment_details);

        adapter = new OptionAdapter(this, R.layout.option_item, optionList);
        ListView listView = findViewById(R.id.detail_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        initOptions();

        FirebaseUser current_user = mAuth.getCurrentUser();
        final String cur_user_key = current_user.getUid();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(cur_user_key)) {
                        optionList.get(0).setName(ds.getValue(AppUser.class).getBirthDate());
                        optionList.get(1).setName(ds.getValue(AppUser.class).getWhatsup());
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
        Option option1 = new Option("Birth Date", R.drawable.ic_cake_black_24dp);
        optionList.add(option1);
        Option option2 = new Option("Message", R.drawable.ic_message_black_24dp);
        optionList.add(option2);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String text = (String) ((TextView) view.findViewById(R.id.option_name)).getText();
        switch (i) {
            case 0:
                Toast.makeText(this, text + " is clicked!", Toast.LENGTH_SHORT).show();
                editBirthDate(i);
                break;
            case 1:
                Toast.makeText(this, text + " is clicked!", Toast.LENGTH_SHORT).show();
                editWhatsup(i);
                break;
        }
    }

    public void editBirthDate(final int pos) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.change_birthdate, null);

        final EditText birthdateField = mView.findViewById(R.id.change_birthdate);
        Button mSave = mView.findViewById(R.id.btn_save);
        mBuilder.setView(mView);

        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String editBirthDate = birthdateField.getText().toString();
                if (!editBirthDate.isEmpty()){

                    update(editBirthDate, pos);
                    dialog.dismiss();
                } else{
                    Toast.makeText(getApplicationContext(), "field is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                if (!editWhatsup.isEmpty()){

                    update(editWhatsup, pos);
                    dialog.dismiss();
                } else{
                    Toast.makeText(getApplicationContext(), "field is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void update(String val, int pos) {
        try {
            switch (pos) {
                case 0:
                    databaseRef.child(mAuth.getCurrentUser().getUid()).child("birthDate").setValue(val);
                    optionList.get(pos).setName(val);
                    adapter.refreshList(optionList);
                    break;
                case 1:
                    databaseRef.child(mAuth.getCurrentUser().getUid()).child("whatsup").setValue(val);
                    optionList.get(pos).setName(val);
                    adapter.refreshList(optionList);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
