package com.example.lingxuan925.anif;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.v7.widget.CardView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsAdapter extends ArrayAdapter<Event> {

    CardView cardView;
    TextView textViewTitle, textViewLocation, textViewStartTime, textViewMonth, textViewDay, textViewHost, textViewDesc, textViewCurrent, textViewMax, textViewType;
    private ArrayList<Event> eventList;
    ImageView imageViewEventType;

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
        imageViewEventType = convertView.findViewById(R.id.event_type_image);

        textViewTitle.setText(eventList.get(position).getName());
        textViewHost.setText(eventList.get(position).getHost());
        textViewMax.setText("Max: "+Integer.toString(eventList.get(position).getNumLimit()));
        textViewCurrent.setText("Current: "+Integer.toString(eventList.get(position).getParticipants().size()));
        textViewStartTime.setText(eventList.get(position).getStartTime());
        textViewMonth.setText(eventList.get(position).getDate().split("-")[1]);
        textViewDay.setText(eventList.get(position).getDate().split("-")[2]);
        textViewLocation.setText(eventList.get(position).getLocation());
        textViewDesc.setText(eventList.get(position).getDescription());
        textViewType.setText(eventList.get(position).getType());

        switch (eventList.get(position).getType()) {
            case "Entertainment":
                imageViewEventType.setImageResource(R.drawable.icon_large_entertainment);
                break;
            case "Sports":
                imageViewEventType.setImageResource(R.drawable.icon_large_sports);
                break;
            case "Travel":
                imageViewEventType.setImageResource(R.drawable.icon_large_travel);
                break;
            case "Foodies":
                imageViewEventType.setImageResource(R.drawable.icon_large_foodies);
                break;
        }

        return convertView;
    }

    public void refreshList(ArrayList<Event> events) {
        eventList = events;
        notifyDataSetChanged();
    }
}
