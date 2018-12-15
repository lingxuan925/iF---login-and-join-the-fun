package com.example.lingxuan925.anif;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private List<Fragment> fragments;
    private BottomNavigationView navigation;
    private ViewPager viewPager;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE_2 = 2;
    Intent searchIntent;
    TextView pickLocationButton;
    Place newAddedPlace;
    FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;
    boolean onMap = true;
    AlertDialog dialog;
    GoogleApiClient mGoogleApiClient;

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

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        try {
            searchIntent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
        } catch (GooglePlayServicesRepairableException e) {
            //todo
        } catch (GooglePlayServicesNotAvailableException e) {
            //todo
        }

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
                if (i != 0) onMap = false;
                if (i == 0) onMap = true;
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        dialog = setUpAddEventDialog();

        mGoogleApiClient.connect();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(onMap);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            startActivityForResult(searchIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            return true;
        }

        if (id == R.id.action_add) {
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                FragmentEvents events = (FragmentEvents) (getSupportFragmentManager()
                        .findFragmentByTag("android:switcher:" + R.id.fragment_frame + ":0"));
                events.addMarker(place.getName().toString(), place.getLatLng());
                events.moveCamera(place.getLatLng(), 15);
                events.refreshRadiusListAfterSearch(place.getLatLng());


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

    public AlertDialog setUpAddEventDialog() {
        View view = View.inflate(this, R.layout.add_event_page_layout, null);

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
                if (hasFocus) {
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
                if (hasFocus) {
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
                if (hasFocus) {
                    descriptionText.callOnClick();
                }
            }
        });

        final Spinner eventType = view.findViewById(R.id.event_type);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("Entertainment");
        categories.add("Foodies");
        categories.add("Sports");
        categories.add("Travel");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        eventType.setAdapter(dataAdapter);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                int capacity = 10; //default capacity of 10 for event
                String name = eventNameText.getText().toString();
                if (!participantsNumText.getText().toString().isEmpty() && !participantsNumText.getText().toString().equals("Enter participants number"))
                    capacity = Integer.parseInt(participantsNumText.getText().toString());
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
                String dateTime = formatter.format(calendar.getTime()).toString();
                String date = dateTime.split(" ")[0];
                String time = dateTime.split(" ")[1];
                String description = descriptionText.getText().toString();
                String location = pickLocationButton.getText().toString();
                LatLng latlng = new LatLng(37.419857, -122.078827);
                if (newAddedPlace != null) latlng = newAddedPlace.getLatLng();
                Event newEvent = new Event(name, location, description, mAuth.getCurrentUser().getUid(), date, time, capacity, latlng.latitude, latlng.longitude, eventType.getSelectedItem().toString());
                newEvent.getParticipants().add(mAuth.getCurrentUser().getUid());
                newEvent.setCurCnt(newEvent.getParticipants().size());
                dbHelper.createEvent(newEvent, mAuth);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_background);
        return dialog;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Google Api Client is connected", Toast.LENGTH_SHORT).show();
        FragmentEvents fragment = (FragmentEvents)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.fragment_frame + ":0");
        fragment.goToCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onResume(){
        mGoogleApiClient.connect();
        super.onResume();
    }
}
