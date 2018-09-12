package com.example.lingxuan925.anif;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Events extends Fragment {

    TextView textView;
    Button button;

    public Events() {
        // Required empty public constructor
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        final LinearLayout layoutMap = view.findViewById(R.id.map);
        final LinearLayout layoutList = view.findViewById(R.id.event_list);
        layoutList.setVisibility(View.GONE);

        final TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        final TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(500);

        Button btnTransferToList = view.findViewById(R.id.to_event_list);
        btnTransferToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutMap.startAnimation(mHiddenAction);
                layoutMap.setVisibility(View.GONE);
                layoutList.startAnimation(mShowAction);
                layoutList.setVisibility(View.VISIBLE);
            }
        });

        Button btnTransferToMap = view.findViewById(R.id.to_map);
        btnTransferToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutList.startAnimation(mHiddenAction);
                layoutList.setVisibility(View.GONE);
                layoutMap.startAnimation(mShowAction);
                layoutMap.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView = (TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.textView1);
    }

}
