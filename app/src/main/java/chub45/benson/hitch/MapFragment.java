package chub45.benson.hitch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.internal.kx;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    MapView mMapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private HitchDatabase db;
    private PostFactory factory;
    private ArrayList<Post> posts;
    private LatLng current_location;
    private Place post_location;
    private static LatLng mDefaultLocation = new LatLng(34.41073, -119.86352);
    private static float DEFAULT_ZOOM = 15;
    private Post triggerQuery, current_post;

    /**
     * Default radius for displaying posts
     * Users only see posts with a departing area within this distance from
     * the user's current location
     * Units: meters
     */
    private static double DEFAULT_POST_DISTANCE = 10000;
    private Location current_loc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity().getApplicationContext()).addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API).build();
        mGoogleApiClient.connect();
        db = new HitchDatabase();
        factory = new DefaultPostFactory();
        posts = new ArrayList<>();
        Date date = new Date();
        FirebaseUser user = new FirebaseUser() {
            @NonNull
            @Override
            public String getUid() {
                return null;
            }

            @NonNull
            @Override
            public String getProviderId() {
                return null;
            }

            @Override
            public boolean isAnonymous() {
                return false;
            }

            @Nullable
            @Override
            public List<String> getProviders() {
                return null;
            }

            @NonNull
            @Override
            public List<? extends UserInfo> getProviderData() {
                return null;
            }

            @NonNull
            @Override
            public FirebaseUser zzP(@NonNull List<? extends UserInfo> list) {
                return null;
            }

            @Override
            public FirebaseUser zzax(boolean b) {
                return null;
            }

            @NonNull
            @Override
            public FirebaseApp zzEF() {
                return null;
            }

            @Nullable
            @Override
            public String getDisplayName() {
                return null;
            }

            @Nullable
            @Override
            public Uri getPhotoUrl() {
                return null;
            }

            @Nullable
            @Override
            public String getEmail() {
                return null;
            }

            @Nullable
            @Override
            public String getPhoneNumber() {
                return null;
            }

            @NonNull
            @Override
            public kx zzEG() {
                return null;
            }

            @Override
            public void zza(@NonNull kx kx) {

            }

            @NonNull
            @Override
            public String zzEH() {
                return null;
            }

            @NonNull
            @Override
            public String zzEI() {
                return null;
            }

            @Override
            public boolean isEmailVerified() {
                return false;
            }
        };
        triggerQuery = factory.createPostFromDb("","", "", 0, "","","", -1, "", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), PostDetails.class);
                        LatLng position = marker.getPosition();
                        Post post = MapFragment.this.getPostByDestination(marker.getId());
                        intent.putExtra("destination", MapFragment.this.current_post.getdestination());
                        intent.putExtra("departing_area", MapFragment.this.current_post.getdeparting_area());
                        intent.putExtra("available_spots", String.valueOf(MapFragment.this.current_post.getavailable_spots()));
                        intent.putExtra("departure_time", MapFragment.this.current_post.getdeparture_time().toString());
                        intent.putExtra("description", MapFragment.this.current_post.getdescription());
                        startActivity(intent);
                    }
                });
                // Add a marker in Isla Vista and move the camera
                LatLng isla_vista = new LatLng(34.41073, -119.86352);
                LatLng debug = new LatLng(34.3, -119.9);
                current_location = debug;
                try {
                    if (locationPermissionGranted()) {
                        Task locationResult = mFusedLocationClient.getLastLocation();
                        locationResult.addOnCompleteListener(MapFragment.this.getActivity(), new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    // Set the map's camera position to the current location of the device.
                                    current_loc = (Location) task.getResult();
                                    current_location = new LatLng(current_loc.getLatitude(), current_loc.getLongitude());
                                    MapFragment.this.mMap.addMarker(new MarkerOptions().position(current_location).title("Current location worked"));
                                    //MapFragment.this.addPosts(current_location);
                                    MapFragment.this.getAllPosts();
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
        });
        return rootView;
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
                Intent intent = new Intent(getActivity().getApplicationContext(), PostDetails.class);
                LatLng position = marker.getPosition();
                Post post = MapFragment.this.getPostByDestination(marker.getId());
                intent.putExtra("destination", MapFragment.this.current_post.getdestination());
                intent.putExtra("departing_area", MapFragment.this.current_post.getdeparting_area());
                intent.putExtra("available_spots", String.valueOf(MapFragment.this.current_post.getavailable_spots()));
                intent.putExtra("departure_time", MapFragment.this.current_post.getdeparture_time().toString());
                intent.putExtra("description", MapFragment.this.current_post.getdescription());
                startActivity(intent);
            }
        });
        // Add a marker in Isla Vista and move the camera
        LatLng isla_vista = new LatLng(34.41073, -119.86352);
        LatLng debug = new LatLng(34.3, -119.9);
        current_location = debug;
        try {
            if (locationPermissionGranted()) {
                Task locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this.getActivity(), new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            current_loc = (Location) task.getResult();
                            current_location = new LatLng(current_loc.getLatitude(), current_loc.getLongitude());
                            MapFragment.this.mMap.addMarker(new MarkerOptions().position(current_location).title("Current location worked"));
                            //MapFragment.this.addPosts(current_location);
                            MapFragment.this.getAllPosts();
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
        return (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private ArrayList<Post> getClosePosts(LatLng current_location) {
        ArrayList<Post> close_posts = new ArrayList<>();
        getAllPosts();
        Place post_location;
        LatLng post_coordinates;

        for (int i = 0; i < this.posts.size(); i++) {
            post_location = Post.getPlaceFromId(this.getActivity().getApplicationContext(), posts.get(i).getdeparting_area());
            post_coordinates = post_location.getLatLng();
            if (distanceBetween(current_location, post_coordinates) < DEFAULT_POST_DISTANCE * 1000000) {
                close_posts.add(this.posts.get(i));
            }
        }
        return close_posts;
    }

    public Post getPostByDestination(String destination) {
        Query query = db.getRoot().child("posts").child(destination).orderByChild("destination").equalTo(destination);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    collectPost((HashMap<String, HashMap<String, String>>) dataSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("[Database Error]", databaseError.getMessage());
            }
        });
        db.addPost(triggerQuery);
        return current_post;
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

    public void getAllPosts() {
        Query query = db.getRoot().child("posts").orderByChild("available_spots");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    collectPosts((HashMap<String, HashMap<String, String>>) dataSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("[Database Error]", databaseError.getMessage());
            }
        });
        db.addPost(triggerQuery);
    }

    private void collectPost(HashMap<String, HashMap<String, String>> all_posts) {
        String departing_area, destination, departure_time, num_spots, author_uid, author_email, description, s_id, potential_passengers, accepted_passengers;
        PostFactory factory = new DefaultPostFactory();
        HashMap<String, String> post;
        Post tempPost;
        LatLng post_coordinates;
        Place current_post_location;
        int available_spots, post_id;
        for (int i = 0; i < all_posts.size(); i++) {
            post = all_posts.get(String.valueOf(i));
            if (post != null) {
                departing_area = post.get("departing_area");
                destination = post.get("destination");
                departure_time = post.get("departure_time");
                num_spots = post.get("available_spots");
                available_spots = Integer.parseInt(num_spots);
                author_uid = post.get("author_uid");
                author_email = post.get("author_email");
                description = post.get("description");
                s_id = post.get("post_id");
                post_id = Integer.parseInt(s_id);
                potential_passengers = post.get("potential_passengers");
                accepted_passengers = post.get("accepted_passengers");
                this.current_post = factory.createPostFromDb(departing_area, destination, departure_time, available_spots, author_uid,
                        author_email, description, post_id, potential_passengers, accepted_passengers);
            }
        }
    }

    private void collectPosts(HashMap<String, HashMap<String, String>> all_posts) {
        String departing_area, destination, departure_time, num_spots, author_uid, author_email, description, s_id, potential_passengers, accepted_passengers;
        PostFactory factory = new DefaultPostFactory();
        HashMap<String, String> post;
        Post tempPost;
        LatLng post_coordinates;
        Place current_post_location;
        int available_spots, id;
        for (int i = 0; i < all_posts.size(); i++) {
            post = all_posts.get(String.valueOf(i));
            if ((post != null) && (!post.get("post_id").equals("-1")) && (!post.get("destination").equals(""))){
                tempPost = addMarkerandCreatePost(post.get("destination"), post);
            }
        }
    }

    public Post addMarkerandCreatePost(String place_id, HashMap<String, String> post) {
        final Post[] result = new Post[1];

        Places.GeoDataApi.getPlaceById(mGoogleApiClient, place_id)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        String departing_area, destination, departure_time, num_spots, author_uid, author_email, description, s_id, potential_passengers, accepted_passengers;
                        int available_spots, post_id;
                        LatLng post_coordinates;
                        MarkerOptions markerOptions;
                        Marker marker;
                        if ((places.getStatus().isSuccess()) && (places.getCount() > 0)) {
                            final Place myPlace = places.get(0);
                            post_coordinates = places.get(0).getLatLng();
                            //MapFragment.this.setPost_location(myPlace);
                            departing_area = post.get("departing_area");
                            destination = post.get("destination");
                            departure_time = post.get("departure_time");
                            num_spots = post.get("available_spots");
                            available_spots = Integer.parseInt(num_spots);
                            author_uid = post.get("author_uid");
                            author_email = post.get("author_email");
                            description = post.get("description");
                            s_id = post.get("post_id");
                            post_id = Integer.parseInt(s_id);
                            potential_passengers = post.get("potential_passengers");
                            accepted_passengers = post.get("accepted_passengers");
                            result[0] = factory.createPostFromDb(departing_area, destination, departure_time, available_spots, author_uid,
                                    author_email, description, post_id, potential_passengers, accepted_passengers);
                            if (distanceBetween(current_location, post_coordinates) < DEFAULT_POST_DISTANCE * 1000000) {
                                markerOptions = new MarkerOptions().position(post_coordinates).title(myPlace.getAddress().toString()).snippet(result[0].getdescription());
                                mMap.addMarker(markerOptions);
                                //MapFragment.this.addPostMarker(post_coordinates, result[0]);
                            }
                        }
                        places.release();
                    }
                });
        return result[0];
    }

    public void setPost_location(Place loc) {
        this.post_location = loc;
    }

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
