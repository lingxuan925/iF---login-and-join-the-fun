package com.example.lingxuan925.anif;

import android.content.Context;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class User extends Fragment {
    private ArrayList<Option> optionList = new ArrayList<>();

    public User() {
        // Required empty public constructor
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
        ListView listView = (ListView) view.findViewById(R.id.options_list);
        listView.setAdapter(adapter);
        return view;
    }

    private void initOptions(){
        Option option1 = new Option("Chang avatar", R.drawable.profile_icon);
        optionList.add(option1);
        Option option2 = new Option("Change nickname", R.drawable.pencil_icon);
        optionList.add(option2);
        Option option3 = new Option("Upcoming events", R.drawable.calendar_icon);
        optionList.add(option3);
        Option option4 = new Option("Details", R.drawable.details_icon);
        optionList.add(option4);
        Option option5 = new Option("Settings", R.drawable.wrench_icon);
        optionList.add(option5);
        Option option6 = new Option("Feedback",R.drawable.send_icon);
        optionList.add(option6);
    }

}
