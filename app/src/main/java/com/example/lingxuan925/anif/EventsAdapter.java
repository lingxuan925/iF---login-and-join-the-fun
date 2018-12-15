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
    TextView textViewTitle, textViewLocation, textViewStartTime, textViewMonth, textViewDay, textViewHost, textViewDesc, textViewCurrent, textViewMax, textViewType;
    private ArrayList<Event> eventList;

    public EventsAdapter(Context context, int resource, ArrayList<Event> events) {
        super(context, resource, events);
        eventList = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cardview, parent, false);
        }

        cardView = convertView.findViewById(R.id.cardViewMeeting);
        textViewTitle = convertView.findViewById(R.id.tViewTitle);
        textViewLocation = convertView.findViewById(R.id.tViewLocation);
        textViewStartTime = convertView.findViewById(R.id.tViewStartTime);
        textViewMonth = convertView.findViewById(R.id.textViewMonth);
        textViewDay = convertView.findViewById(R.id.textViewDay);
        textViewDesc = convertView.findViewById(R.id.tViewDesc);
        textViewHost = convertView.findViewById(R.id.tViewOwner);
        textViewCurrent = convertView.findViewById(R.id.tViewCurrent);
        textViewMax = convertView.findViewById(R.id.tViewMaxCapacity);
        textViewType = convertView.findViewById(R.id.tEventType);

        textViewTitle.setText(eventList.get(position).getName());
        textViewMax.setText(Integer.toString(eventList.get(position).getNumLimit()));
        textViewCurrent.setText(Integer.toString(eventList.get(position).getParticipants().size()));
        textViewStartTime.setText(eventList.get(position).getStartTime());
        textViewMonth.setText(eventList.get(position).getDate().split("-")[1]);
        textViewDay.setText(eventList.get(position).getDate().split("-")[2]);
        textViewLocation.setText(eventList.get(position).getLocation());
        textViewDesc.setText(eventList.get(position).getDescription());
        textViewType.setText(eventList.get(position).getType());

        return convertView;
    }

    public void refreshList(ArrayList<Event> events){
        eventList = events;
        notifyDataSetChanged();
    }
}
