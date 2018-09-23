package com.example.lingxuan925.anif;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;


public class User extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ArrayList<Option> optionList = new ArrayList<>();
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    public User() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOptions();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        OptionAdapter adapter = new OptionAdapter(view.getContext(), R.layout.option_item, optionList);
        ListView listView = view.findViewById(R.id.options_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser current_user = mAuth.getCurrentUser();
        Button logoutBtn = view.findViewById(R.id.signout);
        logoutBtn.setOnClickListener(this);

        TextView name = view.findViewById(R.id.user_name);
        TextView user_email = view.findViewById(R.id.user_id);
        name.setText(Objects.requireNonNull(current_user).getDisplayName());
        user_email.setText(current_user.getEmail());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(getActivity(), SignInPage.class));
                }
            }
        };
        return view;
    }

    private void initOptions() {
        Option option1 = new Option("Change avatar", R.drawable.profile_icon);
        optionList.add(option1);
        Option option2 = new Option("Change nickname", R.drawable.pencil_icon);
        optionList.add(option2);
        Option option3 = new Option("Upcoming events", R.drawable.calendar_icon);
        optionList.add(option3);
        Option option4 = new Option("Details", R.drawable.details_icon);
        optionList.add(option4);
        Option option5 = new Option("Settings", R.drawable.wrench_icon);
        optionList.add(option5);
        Option option6 = new Option("Feedback", R.drawable.send_icon);
        optionList.add(option6);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signout) mAuth.signOut();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String text = (String) ((TextView) view.findViewById(R.id.option_name)).getText();
        switch (text) {
            case "Change avatar":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                break;
            case "Change nickname":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                break;
            case "Upcoming events":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                break;
            case "Details":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                break;
            case "Settings":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                break;
            case "Feedback":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

