package com.example.test1.VolunteerConnect;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private Database localDatabase, getterDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        localDatabase  = new Database();
        //All objs from server
        getterDatabase = new Database();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        addEvents();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clear the previous data on screen

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Event event = postSnapshot.getValue(Event.class);
                    //adding artist to the list
                    getterDatabase.fillFromServer(event);
                }

                //creating adapter
                //ArtistList artistAdapter = new ArtistList(MainActivity.this, artists);
                //attaching adapter to the listview
                //listViewArtists.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveEventInformation(){

    }

    public void addEvents(){
        int numEvents = 3;

        Event [] eventArray = new Event[numEvents];
        eventArray[0] = new Event("Tree Planting", new Address("95070", "Saratoga", "CA", "18060 King Court"), 10, 10, 25, 2017);
        eventArray[1] = new Event("Volunteering", new Address("95054", "Santa Clara", "CA", "4900 Marie P DeBartolo Way"), 20, 8, 12, 2018);
        eventArray[2] = new Event("Helping", new Address("95014", "Cupertino", "CA", "22500 Cristo Rey Dr"), 5, 4, 4, 2024);

        for(int i = 0; i<numEvents; i++) {
            localDatabase.addEvent(databaseReference, eventArray[i]);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at present position
        boolean mLocationPermissionGranted = false;
        Toast.makeText(EventMap.this, "Reached", Toast.LENGTH_LONG).show();

        while (mLocationPermissionGranted == false) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        99);
            }
        }

        mMap.setMyLocationEnabled(true);

        /*
        LatLng currPosition = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        Toast.makeText(EventMap.this, "Test", Toast.LENGTH_LONG).show();
        Toast.makeText(EventMap.this,mMap.getMyLocation().getLatitude() + " " + mMap.getMyLocation().getLongitude(),Toast.LENGTH_LONG).show();

        mMap.addMarker(new MarkerOptions().position(currPosition).title("Your Current Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currPosition));
        */

        // Add a marker at current location and move the camera
        LatLng currLocation = new LatLng(37.38, -121.98);
        mMap.addMarker(new MarkerOptions().position(currLocation).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));


    }
}
