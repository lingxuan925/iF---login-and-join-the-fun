package com.example.lingxuan925.anif;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

    /**
     * initialize constructor with respective nodes events and users
     */
    public DatabaseHelper() {
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        databaseEvents = FirebaseDatabase.getInstance().getReference("Events");
    }

    /**
     * added event to firebase event node
     * @param evt - the event object
     * @param mAuth - the authentication object used to get user uid to get the specific user node in firebase
     */
    public void createEvent(Event evt, FirebaseAuth mAuth) {
        String key = databaseEvents.push().getKey();
        evt.setId(key);
        updateUserEventList(key, mAuth);
        databaseEvents.child(key).setValue(evt);
    }

    /**
     * fetch upcoming events starting from tomorrow's date
     * @param mAuth
     * @param adapter - event adapter
     * @param upcomingEvents - arraylist of events
     */
    public void fetchUpcomingEvents(final FirebaseAuth mAuth, final EventsAdapter adapter, final ArrayList<Event> upcomingEvents) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);

        databaseEvents.orderByChild("date").startAt(modifiedDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                upcomingEvents.clear();
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

    /**
     * fetch events within specified radius in settings, it creates a location object for current
     * user location and event location and sets their respective lat and lng, then call the
     * method distance to to get difference in meters, then divide by 1000 and compare with radius,
     * which is in kilometers.
     * @param googleMap
     * @param currentLatLng - current user's location as latlng object
     * @param radius
     * @param mAuth
     * @param adapter
     * @param radiusEvents - arraylist of events
     */
    public void fetchEventsWithinRadius(final GoogleMap googleMap, final LatLng currentLatLng, final String radius, final FirebaseAuth mAuth, final EventsAdapter adapter, final ArrayList<Event> radiusEvents) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);

        databaseEvents.orderByChild("date").startAt(modifiedDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                radiusEvents.clear();
                googleMap.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Event anEvent = ds.getValue(Event.class);
                    Location currentLoc = new Location("current location");
                    currentLoc.setLatitude(currentLatLng.latitude);
                    currentLoc.setLongitude(currentLatLng.longitude);
                    Location eventLoc = new Location("event location");
                    eventLoc.setLatitude(anEvent.getLatitude());
                    eventLoc.setLongitude(anEvent.getLongitude());
                    if (currentLoc.distanceTo(eventLoc)/1000 < Integer.parseInt(radius)) {
                        googleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.event_flag))
                                .position(new LatLng(anEvent.getLatitude(), anEvent.getLongitude()))
                                .title(anEvent.getName())
                                .snippet(anEvent.getId()));
                        radiusEvents.add(anEvent);
                    }
                }
                adapter.refreshList(radiusEvents);
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
                    user.setImageUri("https://firebasestorage.googleapis.com/v0/b/iffirebaseproject-9e0ec.appspot.com/o/image%2Fdefault_avatar2.png?alt=media&token=9d9f088d-ff92-4ea5-a75c-d04cb5b3087f");
                    databaseUsers.child(mAuth.getCurrentUser().getUid()).setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * this is methods used to add the event key string to a user's eventID list, this happens when
     * the user host a event or join other events.
     * @param evtKey
     * @param mAuth
     */
    public void updateUserEventList(final String evtKey, final FirebaseAuth mAuth) {
        databaseUsers.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppUser curUser = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(AppUser.class);
                    curUser.getEventIDs().add(evtKey);
                    databaseUsers.child(mAuth.getCurrentUser().getUid()).child("eventIDs").setValue(curUser.getEventIDs());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateUser(String key, String val, FirebaseAuth mAuth) {
        databaseUsers.child(mAuth.getCurrentUser().getUid()).child(key).setValue(val);
    }

    public DatabaseReference getDatabaseUsers() {
        return databaseUsers;
    }
}
