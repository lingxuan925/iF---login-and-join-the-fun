package com.example.lingxuan925.anif;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentEvents extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemClickListener {

    TextView textView;
    Button button;
    Boolean onMap = true, searching = false;
    MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    boolean isConnected = false;
    Marker marker;
    private ArrayList<Event> radiusList;
    private EventsAdapter adapter;
    FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;
    private LatLng currentLatLng;
    AlertDialog dialog;

    public FragmentEvents() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        View view = inflater.inflate(R.layout.fragment_events, container, false);
        final LinearLayout layoutMap = view.findViewById(R.id.map);
        final LinearLayout layoutList = view.findViewById(R.id.event_list);
        layoutList.setVisibility(View.GONE);
        radiusList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper();
        adapter = new EventsAdapter(getContext(), 0, radiusList);

        final TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        final TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(500);

        FloatingActionButton btnTransfer = view.findViewById(R.id.fab);
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMap) {
                    layoutMap.startAnimation(mHiddenAction);
                    layoutMap.setVisibility(View.GONE);
                    layoutList.startAnimation(mShowAction);
                    layoutList.setVisibility(View.VISIBLE);

                    //FETCHING EVENTS TO LISTVIEW BY RADIUS IN SETTINGS
                    ListView listView = layoutList.findViewById(R.id.radius_list);

                    listView.setOnItemClickListener(FragmentEvents.this);
                    listView.setAdapter(adapter);

                    if (currentLatLng != null) refreshRadiusList();
                    onMap = false;
                } else {
                    layoutList.startAnimation(mHiddenAction);
                    layoutList.setVisibility(View.GONE);
                    layoutMap.startAnimation(mShowAction);
                    layoutMap.setVisibility(View.VISIBLE);
                    if (currentLatLng != null) refreshRadiusList();
                    onMap = true;
                }
            }
        });

        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                        dialog.setTitle(marker.getTitle());
                        dialog.show();
                        return true;
                    }
                });
            }
        });

        dialog = setUpMarkerPopupView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (currentLatLng != null && !searching) refreshRadiusList();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),
                        "Please enable LOCATION ACCESS in settings to show your current location.",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                    if (isConnected)
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .getLastLocation()
                                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if (location != null) {
                                            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                            moveCamera(currentLatLng, 14);
                                            refreshRadiusList();
                                        }
                                    }
                                });
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnected = true;
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.getFusedLocationProviderClient(getActivity())
                    .getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                moveCamera(currentLatLng, 14);
                                refreshRadiusList();
                            }
                        }
                    });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    public void moveCamera(LatLng latlng, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(zoom).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void addMarker(String title, LatLng latLng){
        if (marker != null)
            marker.remove();
        marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void refreshRadiusList() {
        System.out.println(currentLatLng);
        dbHelper.getDatabaseUsers().orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dbHelper.fetchEventsWithinRadius(googleMap, currentLatLng, dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(AppUser.class).getRadius(), mAuth, adapter, radiusList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void refreshRadiusListAfterSearch(final LatLng searchLatLng) {
        searching = true;
        System.out.println(searchLatLng);
        dbHelper.getDatabaseUsers().orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dbHelper.fetchEventsWithinRadius(googleMap, searchLatLng, dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(AppUser.class).getRadius(), mAuth, adapter, radiusList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public AlertDialog setUpMarkerPopupView(){
        View view = View.inflate(getContext(), R.layout.marker_popup_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Join in this event
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_background);
        return dialog;
    }
}
