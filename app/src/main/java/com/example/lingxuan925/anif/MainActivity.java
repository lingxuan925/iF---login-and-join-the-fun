package com.example.lingxuan925.anif;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> fragments;
    private BottomNavigationView navigation;
    private ViewPager viewPager;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE_2 = 2;
    Intent searchIntent;
    TextView pickLocationButton;
    ImageButton searchButton;
    Place newAddedPlace;
    FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;
    boolean onMap = true;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_friends:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_user:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper();

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        searchButton = mToolbar.findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(searchIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        try {
            searchIntent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
        } catch (GooglePlayServicesRepairableException e) {
            //todo
        } catch (GooglePlayServicesNotAvailableException e) {
            //todo
        }
        ImageButton addButton = mToolbar.findViewById(R.id.button_add_event);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(v.getContext(), R.layout.add_event_page_layout, null);

                final EditText eventNameText = view.findViewById(R.id.new_act_add_name);
                eventNameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eventNameText.setText("");
                    }
                });
                eventNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
                        {
                            eventNameText.callOnClick();
                        }
                    }
                });


                pickLocationButton = view.findViewById(R.id.new_act_choose_location);
                pickLocationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent locationIntent;
                        try {
                            locationIntent =
                                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                            .build(MainActivity.this);
                            startActivityForResult(locationIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE_2);
                        } catch (GooglePlayServicesRepairableException e) {
                            //todo
                        } catch (GooglePlayServicesNotAvailableException e) {
                            //todo
                        }
                    }
                });

                final EditText participantsNumText = view.findViewById(R.id.new_act_add_capacity);
                participantsNumText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        participantsNumText.setText("");
                    }
                });
                participantsNumText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
                        {
                            participantsNumText.callOnClick();
                        }
                    }
                });


                final EditText descriptionText = view.findViewById(R.id.new_act_add_description);
                descriptionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (descriptionText.getText().toString().equals("Enter brief description"))
                            descriptionText.setText("");
                    }
                });
                descriptionText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
                        {
                            descriptionText.callOnClick();
                        }
                    }
                });


                final DatePicker datePicker = view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker = view.findViewById(R.id.new_act_time_picker);

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                int hour = calendar.get(Calendar.HOUR);
                datePicker.init(year, month, day, null);

                timePicker.setIs24HourView(true);
                timePicker.setHour(hour);
                timePicker.setMinute(0);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(view);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    int capacity = 10; //default capacity of 10 for event
                    String name = eventNameText.getText().toString();
                    if (!participantsNumText.getText().toString().isEmpty() && !participantsNumText.getText().toString().equals("Enter participants number")) capacity = Integer.parseInt(participantsNumText.getText().toString());
                    calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
                    String dateTime = formatter.format(calendar.getTime()).toString();
                    String date = dateTime.split(" ")[0];
                    String time = dateTime.split(" ")[1];
                    String description = descriptionText.getText().toString();
                    String location = pickLocationButton.getText().toString();
                    LatLng latlng = new LatLng(37.419857, -122.078827);
                    if (newAddedPlace != null) latlng = newAddedPlace.getLatLng();

                    Event newEvent = new Event(name, location, description, mAuth.getCurrentUser().getUid(), date, time, capacity, latlng.latitude, latlng.longitude);
                    newEvent.getParticipants().add(mAuth.getCurrentUser().getUid());
                    dbHelper.createEvent(newEvent, mAuth);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });

        initFragment();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = (MyViewPager) findViewById(R.id.fragment_frame);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.setList(fragments);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i != 0)
                    searchButton.setVisibility(View.GONE);
                else searchButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initFragment() {
        Fragment fragment1 = new FragmentEvents();
        Fragment fragment2 = new FragmentFriends();
        Fragment fragment3 = new FragmentUser();
        fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (onMap) {
                    FragmentEvents events = (FragmentEvents) (getSupportFragmentManager()
                            .findFragmentByTag("android:switcher:" + R.id.fragment_frame + ":0"));
                    events.addMarker(place.getName().toString(), place.getLatLng());
                    events.moveCamera(place.getLatLng(), 15);
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_2) {
            if (resultCode == RESULT_OK) {
                newAddedPlace = PlaceAutocomplete.getPlace(this, data);
                pickLocationButton.setText(newAddedPlace.getName());
                //TODO: you can get the place chosen by user from here
            }
        }
    }
}
