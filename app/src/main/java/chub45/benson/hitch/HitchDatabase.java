package chub45.benson.hitch;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HitchDatabase
{
    private DatabaseReference rootRef;
    private DatabaseReference usersRef;
    private DatabaseReference postsRef;
    private int most_recent_post_id;

    public HitchDatabase() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        usersRef = rootRef.child("users");
        postsRef = rootRef.child("posts");
        most_recent_post_id = -1; //placeholder
    }

    public void addUser(User user)
    {
        HashMap<String, String> userMap = makeUserMap(user);
        DatabaseReference currentChild = usersRef.child(user.getUid());

        currentChild.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    // Make toast or whatever you want
                }
                else{
                    // Make burnt toast
                }
            }
        });
    }

    private HashMap<String,String> makeUserMap(User user){
        HashMap<String, String> userMap = new HashMap<String, String>();
        userMap.put("uid", user.getUid());
        userMap.put("fullName", user.getFullName());
        userMap.put("city", user.getCity());
        userMap.put("state", user.getState());
        userMap.put("rideRequests", user.getRideRequests());
        userMap.put("activeRides", user.getActiveRides());
        return userMap;
    }

    public void addPost(Post post)
    {
        HashMap<String, String> postMap = makePostMap(post);
        DatabaseReference currentChild = postsRef.child(post.get_post_id().toString());

        currentChild.setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Make toast
                } else {
                    // Make burnt toast
                }
            }
        });
    }

    public int getnext_post_id() {
        DatabaseReference reference = this.getRoot().child("postCount");
        Query query = reference.orderByChild("post_id");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    int key = Integer.parseInt(postSnapshot.getKey());
                    if (HitchDatabase.this.most_recent_post_id < key) {
                        HitchDatabase.this.most_recent_post_id = key;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return most_recent_post_id + 1;
    }

    public HashMap<String, String> makePostMap(Post post){
        HashMap<String, String> postMap = new HashMap<>();
        postMap.put("departing_area", post.getdeparting_area());
        postMap.put("destination", post.getdestination());
        postMap.put("departing_area_id", post.getdeparting_area_id());
        postMap.put("destination_id", post.getdestination_id());
        postMap.put("departure_time", post.getdeparture_time().toString());
        postMap.put("available_spots", post.getavailable_spots().toString());
        postMap.put("description", post.getdescription());
        postMap.put("author_uid", post.getauthor());
        postMap.put("author_email", post.getauthor_email());
        postMap.put("post_id", post.get_post_id().toString());
        postMap.put("potential_passengers", post.getpotential_passengers());
        postMap.put("accepted_passengers", post.getaccepted_passengers());
        return postMap;
    }

    public void addPassengerRequest(final String passengerUid, final String postID){
        final DatabaseReference currentPostRef = postsRef.child(postID).child("potential_passengers");
        final DatabaseReference currentUserRef = usersRef.child(passengerUid).child("rideRequests");

        currentPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String passengers = (String) dataSnapshot.getValue();
                     if (passengers.isEmpty() && !passengers.contains(passengerUid)) {
                         currentPostRef.setValue(passengerUid);
                      }
                      else if(!passengers.isEmpty() && !passengers.contains(passengerUid)){
                          currentPostRef.setValue(passengers + "|" + passengerUid);
                      }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                     Log.d("FAILURE", "Could not add to request to post because: " + databaseError.getCode());
            }
        });

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String rides = (String) dataSnapshot.getValue();
                if(rides.isEmpty()){
                    currentUserRef.setValue(postID);
                }
                else if(!rides.isEmpty() && !rides.contains(postID)){
                    currentUserRef.setValue(rides + "|" + postID);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FAILURE", "Could not add to request to user because: " + databaseError.getCode());
            }
        });
    }

    public void acceptPassengers(final String postID){
        final DatabaseReference currentPostRef = postsRef.child(postID);
        currentPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String requestedPassengers = (String) dataSnapshot.child("potential_passengers").getValue();
                Integer availSpots = Integer.parseInt((String) dataSnapshot.child("available_spots").getValue());

                if(!requestedPassengers.isEmpty()) {
                    int j = 0;
                    ArrayList<String> passengers = new ArrayList<>(10);
                    String temp = "";
                    for(int i = 0; i < requestedPassengers.length(); i++){
                        if(requestedPassengers.charAt(i) != '|'){
                            temp += requestedPassengers.charAt(i);
                        }
                        else{
                            passengers.add(j, temp);
                            temp = "";
                            j++;
                        }
                        if(i == requestedPassengers.length() - 1){
                            passengers.add(j, temp);
                        }
                    }
                    availSpots -= passengers.size();
                    currentPostRef.child("accepted_passengers").setValue(requestedPassengers);
                    currentPostRef.child("potential_passengers").setValue("");
                    currentPostRef.child("available_spots").setValue(availSpots.toString());

                    for(int k = 0; k < passengers.size(); k++){
                        final DatabaseReference currentUserRef = usersRef.child(passengers.get(k));
                        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String currentRideRequests = (String) dataSnapshot.child("rideRequests").getValue();
                                String currentActiveRides = (String) dataSnapshot.child("activeRides").getValue();

                                String temp0 = "";
                                String temp1 = "";
                                for(int i = 0; i < currentRideRequests.length(); i++){
                                    if(currentRideRequests.contains(postID)){
                                        int index = currentRideRequests.indexOf(postID);
                                        if(index == 0 && currentRideRequests.length() != postID.length()){
                                            temp1 = currentRideRequests.substring(postID.length()+1);
                                        }
                                        else if(index == currentRideRequests.length()-postID.length()){
                                            temp1 = currentRideRequests.substring(0, index);
                                        }
                                        else{
                                            temp0 = currentRideRequests.substring(0, index);
                                            temp1 = currentRideRequests.substring(index + postID.length()+1);
                                        }
                                    }
                                }
                                currentUserRef.child("rideRequests").setValue(temp0 + temp1);

                                String temp2 = "";
                                if(currentActiveRides.isEmpty()){
                                    temp2 = postID;
                                }
                                else{
                                    temp2 = currentActiveRides + "|" + postID;
                                }
                                currentUserRef.child("activeRides").setValue(postID);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("FAILURE", "Could not accept passengers because: " + databaseError.getCode());
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FAILURE", "Could not accept passengers because: " + databaseError.getCode());
            }
        });
    }

    public DatabaseReference getRoot() {
        return rootRef;
    }

    public void removeUser(String uid) {
        usersRef.child(uid).removeValue();
    }

    public void removePost(String post_id){
        postsRef.child(post_id).removeValue();
    }
}
