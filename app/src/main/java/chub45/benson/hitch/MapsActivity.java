package chub45.benson.hitch;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private HitchDatabase db;
    private LatLng current_location;
    private static LatLng mDefaultLocation = new LatLng(34.41073, -119.86352);
    private static float DEFAULT_ZOOM = 15;

    /**
     * Default radius for displaying posts
     * Users only see posts with a departing area within this distance from
     * the user's current location
     * Units: meters
     */
    private static double DEFAULT_POST_DISTANCE = 10000;
    private Location current_loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        db = new HitchDatabase();
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Intent intent = new Intent(getApplicationContext(), PostDetails.class);
                LatLng position = marker.getPosition();
//                Post post = db.lookup(position.toString());
//                intent.putExtra("destination", post.getdestination());
//                intent.putExtra("departing_area", post.getdeparting_area());
//                intent.putExtra("available_spots", String.valueOf(post.getavailable_spots()));
//                intent.putExtra("departure_time", post.getdeparture_time().toString());
//                intent.putExtra("description", post.getdescription());
                //startActivity(intent);
            }
        });
        // Add a marker in Isla Vista and move the camera
        LatLng isla_vista = new LatLng(34.41073, -119.86352);
        LatLng debug = new LatLng(34.3, -119.9);
        current_location = debug;
        try {
            if (locationPermissionGranted()) {
                Task locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    // Set the map's camera position to the current location of the device.
                                    current_loc = (Location) task.getResult();
                                    current_location = new LatLng(current_loc.getLatitude(), current_loc.getLongitude());
                                    MapsActivity.this.addPosts(current_location);
                                }
                            }
                        });

            }

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

        mMap.addMarker(new MarkerOptions().position(isla_vista).title("Marker in Isla Vista"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(isla_vista));
    }

    private boolean locationPermissionGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void addPosts(LatLng current_location) {
        ArrayList<Post> posts = getClosePosts(current_location);
        LatLng post_location;
        for (int i = 0; i < posts.size(); i++) {
            post_location =  Post.getPlaceFromId(this, posts.get(i).getdestination()).getLatLng();
            addPostMarker(post_location, posts.get(i));
        }
    }

    private void addPostMarker(LatLng location, Post post) {
        MarkerOptions markerOptions = new MarkerOptions().position(location).title(getPlaceFromId(this, post.getdestination())).snippet(post.getdescription());
        Marker marker = mMap.addMarker(markerOptions);
    }

    private ArrayList<Post> getClosePosts(LatLng current_location) {
        ArrayList<Post> posts = new ArrayList<Post>();
        Place post_location;
        LatLng post_coordinates;
        //posts = db.getAllPosts();

        for (int i = 0; i < posts.size(); i++) {
            post_location = Post.getPlaceFromId(this, posts.get(i).getdeparting_area());
            post_coordinates = post_location.getLatLng();
            if (distanceBetween(current_location, post_coordinates) > DEFAULT_POST_DISTANCE) {
                posts.remove(posts.get(i));
            }
        }
        return posts;
    }

    private float distanceBetween(LatLng point1, LatLng point2) {
        float[] results = new float[3];
        Location.distanceBetween(point1.latitude, point1.longitude, point2.latitude, point2.longitude, results);

        if (results[0] != 0) {
            return results[0];
        }
        else if (results[1] != 0) {
            return results[1];
        }
        return results[2];
    }

    private String getPlaceFromId(Context context, String id) {
        GoogleApiClient client = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).build();
        client.connect();

        PendingResult<PlaceBuffer> departing_result = Places.GeoDataApi.getPlaceById(client, id);
        PlaceBuffer place = departing_result.await();
        return place.get(0).toString();
    }
}
