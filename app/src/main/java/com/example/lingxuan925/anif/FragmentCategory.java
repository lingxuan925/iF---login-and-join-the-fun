package com.example.lingxuan925.anif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class FragmentCategory extends Fragment {

    View view;

    ArrayList<RelativeLayout> layoutList;
    static int currentCategory;

    public FragmentCategory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);

        layoutList = new ArrayList<>();
        layoutList.add(view.findViewById(R.id.entertainment));
        layoutList.add(view.findViewById(R.id.foodies));
        layoutList.add(view.findViewById(R.id.sports));
        layoutList.add(view.findViewById(R.id.travel));

        Intent categoryDetail = new Intent(getContext(), CategoryDetail.class);

        for (int i = 0; i < layoutList.size(); i++){
            layoutList.get(i).setOnClickListener(v -> {
                currentCategory = v.getId();
                startActivity(categoryDetail);
            });
        }

        return view;
    }

}
