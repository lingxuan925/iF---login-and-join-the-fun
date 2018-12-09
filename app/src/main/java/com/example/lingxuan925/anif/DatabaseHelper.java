package com.example.lingxuan925.anif;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper {

    DatabaseReference databaseUsers, databaseEvents;

    public DatabaseHelper() {
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        databaseEvents = FirebaseDatabase.getInstance().getReference("Events");
    }

    public void createEvent(Event evt, FirebaseAuth mAuth) {
        String key = databaseEvents.push().getKey();
        updateUserEventList(key, mAuth);
        databaseEvents.child(key).setValue(evt);
    }

    public void fetchUpcomingEvents(final FirebaseAuth mAuth, final EventsAdapter adapter, final ArrayList<Event> upcomingEvents) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);

        databaseEvents.orderByChild("date").startAt(modifiedDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Event anEvent = ds.getValue(Event.class);
                    if (anEvent.getParticipants().contains(mAuth.getCurrentUser().getUid())) upcomingEvents.add(anEvent);
                }
                adapter.refreshList(upcomingEvents);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createUser(final AppUser user, final FirebaseAuth mAuth) {
        databaseUsers.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    user.getEventIDs().add("placeholder");
                    databaseUsers.child(mAuth.getCurrentUser().getUid()).setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateUserEventList(final String evtKey, final FirebaseAuth mAuth) {
        databaseUsers.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppUser curUser = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(AppUser.class);
                    System.out.println(curUser.getEventIDs());
                    curUser.getEventIDs().add(evtKey);
                    databaseUsers.child(mAuth.getCurrentUser().getUid()).child("eventIDs").setValue(curUser.getEventIDs());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
