package com.example.lingxuan925.anif;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

public class FragmentEvents extends Fragment implements AdapterView.OnItemClickListener, LocationListener {

    TextView textView;
    Button button;
    Boolean onMap = true, searching = false;
    MapView mMapView;
    private GoogleMap googleMap;
    Marker marker;
    private ArrayList<Event> radiusList;
    private EventsAdapter adapter;
    FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;
    private LatLng currentLatLng;
    AlertDialog dialog;
    LocationManager locationManager;
    private LocationCallback mLocationCallback;
    private String clickedEventKey;
    View viewJoin;

    public FragmentEvents() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        viewJoin = View.inflate(getContext(), R.layout.marker_popup_layout, null);

        final View view = inflater.inflate(R.layout.fragment_events, container, false);
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

        final FloatingActionButton btnMyLocation = view.findViewById(R.id.current_location);
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCurrentLocation();
            }
        });

        FloatingActionButton btnTransfer = view.findViewById(R.id.change_view);
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMap) {
                    layoutMap.startAnimation(mHiddenAction);
                    layoutMap.setVisibility(View.GONE);
                    btnMyLocation.hide();
                    layoutList.startAnimation(mShowAction);
                    layoutList.setVisibility(View.VISIBLE);

                    //FETCHING EVENTS TO LISTVIEW BY RADIUS IN SETTINGS
                    ListView listView = layoutList.findViewById(R.id.radius_list);

                    listView.setOnItemClickListener(FragmentEvents.this);
                    listView.setAdapter(adapter);

                    if (currentLatLng != null && !searching) refreshRadiusList();
                    onMap = false;
                } else {
                    layoutList.startAnimation(mHiddenAction);
                    layoutList.setVisibility(View.GONE);
                    btnMyLocation.show();
                    layoutMap.startAnimation(mShowAction);
                    layoutMap.setVisibility(View.VISIBLE);
                    if (currentLatLng != null && !searching) refreshRadiusList();
                    onMap = true;
                }
            }
        });

        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog = setUpMarkerPopupView();

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
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if (marker.getTitle().equals("Current Location")) marker.showInfoWindow();
                        else {
                            dialog.setTitle(marker.getTitle());
                            clickedEventKey = marker.getSnippet();
                            dbHelper.fetchSingleEventByID(clickedEventKey, viewJoin, mAuth, dialog);
                            dialog.show();
                        }
                        return true;
                    }
                });
            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                currentLatLng = new LatLng(locationResult.getLastLocation().getLatitude()
                        , locationResult.getLastLocation().getLongitude());
            }

            ;
        };

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
                    if (((MainActivity) getActivity()).mGoogleApiClient.isConnected())
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .getLastLocation()
                                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if (location != null) {
                                            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                            moveCamera(currentLatLng, 14);
                                            searching = false;
                                            refreshRadiusList();
                                        }
                                    }
                                });
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if (marker.getTitle().equals("Current Location"))
                                marker.showInfoWindow();
                            else {
                                dialog.setTitle(marker.getTitle());
                                dbHelper.fetchSingleEventByID(clickedEventKey, viewJoin, mAuth, dialog);
                                dialog.show();
                            }
                            return true;
                        }
                    });
                }
            }
        }
    }


    public void moveCamera(LatLng latlng, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(zoom).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void addMarker(String title, LatLng latLng) {
        if (marker != null)
            marker.remove();
        marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title));
    }

    public void addMarker(String title, LatLng latLng, BitmapDescriptor icon) {
        if (marker != null)
            marker.remove();
        marker = googleMap.addMarker(new MarkerOptions()
                .icon(icon)
                .position(latLng)
                .title(title));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clickedEventKey = radiusList.get(position).getId();
        dialog.setTitle(radiusList.get(position).getName());
        dbHelper.fetchSingleEventByID(clickedEventKey, viewJoin, mAuth, dialog);
        dialog.show();
    }

    public void goToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationRequest locationRequest = createLocationRequest();

            FusedLocationProviderClient mLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

            mLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);

            mLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                moveCamera(currentLatLng, 14);
                                searching = false;
                                refreshRadiusList();
                            } else {
                                if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                                    Toast.makeText(getContext(), "Please turn on location service and try again", Toast.LENGTH_SHORT).show();
                                else if (currentLatLng != null)
                                    moveCamera(currentLatLng, 14);
                            }
                        }
                    });
        }
    }

    public void refreshRadiusList() {
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

    public AlertDialog setUpMarkerPopupView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(viewJoin);
        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (!clickedEventKey.equals("")) {
                    dbHelper.updateUserEventList(clickedEventKey, mAuth);
                    dbHelper.updateEventParticipantList(clickedEventKey, mAuth);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_background);
        return dialog;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000);
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return mLocationRequest;
    }
}
