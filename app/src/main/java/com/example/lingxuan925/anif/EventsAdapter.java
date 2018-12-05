package com.example.lingxuan925.anif;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.support.v7.widget.CardView;

import java.util.ArrayList;

public class EventsAdapter extends ArrayAdapter<Event> {

    CardView cardView;
    TextView textViewTitle, textViewLocation, textViewStartTime, textViewMonth, textViewDay;
    private ArrayList<Event> eventList;

    public EventsAdapter(Context context, int resource, ArrayList<Event> events) {
        super(context, resource, events);
        eventList = events;
        System.out.println(events.get(0).getName());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cardview, parent, false);
        }
        System.out.println(eventList.get(0).getName()+"...................");

        cardView = convertView.findViewById(R.id.cardViewMeeting);
        textViewTitle = convertView.findViewById(R.id.tViewTitle);
        textViewLocation = convertView.findViewById(R.id.tViewLocation);
        textViewStartTime = convertView.findViewById(R.id.tViewStartTime);
        textViewMonth = convertView.findViewById(R.id.textViewMonth);
        textViewDay = convertView.findViewById(R.id.textViewDay);

        textViewTitle.setText(eventList.get(position).getName());
//        textViewStartTime.setText(eventList.get(position).);
//        textViewMonth.setText(eventList.get(position).getDate().split("-")[1]);
//        textViewDay.setText(eventList.get(position).getDate().split("-")[2]);
//        textViewLocation.setText(eventList.get(position).getLocation());

        return convertView;
    }

    public void refreshList(ArrayList<Event> events){
        eventList = events;
        notifyDataSetChanged();
    }
}
