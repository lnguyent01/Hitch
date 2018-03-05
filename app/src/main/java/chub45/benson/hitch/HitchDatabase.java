package chub45.benson.hitch;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class HitchDatabase {
    private DatabaseReference rootRef;
    private DatabaseReference usersRef;
    private DatabaseReference postsRef;
    private DatabaseReference countRef;

    public HitchDatabase() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        countRef = rootRef.child("postCount");
        usersRef = rootRef.child("users");
        postsRef = rootRef.child("posts");
    }

    public void addUser(User user) {
        HashMap<String, String> userMap = makeUserMap(user);
        DatabaseReference currentChild = usersRef.child(user.getUid());

        currentChild.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Make toast or whatever you want
                } else {
                    // Make burnt toast
                }
            }
        });
    }

    private HashMap<String, String> makeUserMap(User user) {
        HashMap<String, String> userMap = new HashMap<String, String>();
        userMap.put("uid", user.getUid());
        userMap.put("fullName", user.getFullName());
        userMap.put("city", user.getCity());
        userMap.put("state", user.getState());
        userMap.put("rideRequests", user.getRideRequests());
        userMap.put("activeRides", user.getActiveRides());
        return userMap;
    }

    public void addPost(Post post) {
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
        updatePostCount();
    }

    public HashMap<String, String> makePostMap(Post post) {
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

    public void addPassengerRequest(final String passengerUid, final String postID) {
        final DatabaseReference currentPostRef = postsRef.child(postID).child("potential_passengers");
        final DatabaseReference currentUserRef = usersRef.child(passengerUid).child("rideRequests");

        currentPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String passengers = (String) dataSnapshot.getValue();
                if (passengers.isEmpty() && !passengers.contains(passengerUid)) {
                    currentPostRef.setValue(passengerUid);
                } else if (!passengers.isEmpty() && !passengers.contains(passengerUid)) {
                    currentPostRef.setValue(passengers + "|" + passengerUid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("[Database Error]", "Could not add to request to post because: " + databaseError.getCode());
            }
        });

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String rides = (String) dataSnapshot.getValue();
                if (rides.isEmpty()) {
                    currentUserRef.setValue(postID);
                } else if (!rides.isEmpty() && !rides.contains(postID)) {
                    currentUserRef.setValue(rides + "|" + postID);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("[Database Error]", "Could not add to request to user because: " + databaseError.getCode());
            }
        });
    }

    public void acceptPassengers(final String postID) {
        final DatabaseReference currentPostRef = postsRef.child(postID);
        currentPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String requestedPassengers = (String) dataSnapshot.child("potential_passengers").getValue();
                Integer availSpots = Integer.parseInt((String) dataSnapshot.child("available_spots").getValue());

                if (!requestedPassengers.isEmpty()) {
                    ArrayList<String> reqPassengersArray = parseListWithDelimiter(requestedPassengers);
                    availSpots -= reqPassengersArray.size();

                    currentPostRef.child("accepted_passengers").setValue(requestedPassengers);
                    currentPostRef.child("potential_passengers").setValue("");
                    currentPostRef.child("available_spots").setValue(availSpots.toString());

                    for (int k = 0; k < reqPassengersArray.size(); k++) {
                        final DatabaseReference currentUserRef = usersRef.child(reqPassengersArray.get(k));
                        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String currentRideRequests = (String) dataSnapshot.child("rideRequests").getValue();
                                String currentActiveRides = (String) dataSnapshot.child("activeRides").getValue();

                                String temp = removeFromListWithDelimiter(currentRideRequests, postID);
                                currentUserRef.child("rideRequests").setValue(temp);

                                String temp2 = addToListWithDelimiter(currentActiveRides, postID);
                                currentUserRef.child("activeRides").setValue(temp2);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("[Database Error]", "Could not accept passengers because: " + databaseError.getCode());
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("[Database Error]", "Could not accept passengers because: " + databaseError.getCode());
            }
        });
    }

    private ArrayList<String> parseListWithDelimiter(String list){
        int j = 0;
        ArrayList<String> reqPassengersArray = new ArrayList<>(10);
        String temp = "";
        for (int i = 0; i < list.length(); i++) {
            if (list.charAt(i) != '|') {
                temp += list.charAt(i);
            } else {
                reqPassengersArray.add(j, temp);
                temp = "";
                j++;
            }
            if (i == list.length() - 1) {
                reqPassengersArray.add(j, temp);
            }
        }
        return reqPassengersArray;
    }

    private String removeFromListWithDelimiter(String list, String postID){
        String temp0 = "";
        String temp1 = "";
        for (int i = 0; i < list.length(); i++) {
            if (list.contains(postID)) {
                int index = list.indexOf(postID);
                if (index == 0 && list.length() != postID.length()) {
                    temp1 = list.substring(postID.length() + 1);
                } else if (index == list.length() - postID.length()) {
                    temp1 = list.substring(0, index);
                } else {
                    temp0 = list.substring(0, index);
                    temp1 = list.substring(index + postID.length() + 1);
                }
            }
        }
        return temp0 + temp1;
    }

    private String addToListWithDelimiter(String list, String ID){
        String temp2 = "";
        if (list.isEmpty()) {
            temp2 = ID;
        } else {
            temp2 = list + "|" + ID;
        }
        return temp2;
    }

    public void updatePostCount() {
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer greatest = -1;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String str = child.getKey();
                    Integer i = Integer.parseInt(str);
                    if (i > greatest)
                        greatest = i;
                }
                countRef.setValue(greatest + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("[Database Error]", "Could not update post count: " + databaseError.getCode());
            }
        });
    }

    public DatabaseReference getRoot() {
        return rootRef;
    }


    /* working on code to delete a user from all posts when a user is deleted
        right now the app is deleting the user, but crashing shortly after
     */

    public void deleteUser(String uid) {
//        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String reqRides = dataSnapshot.child("rideRequests").getValue().toString();
//                String actRides = dataSnapshot.child("activeRides").getValue().toString();
//                ArrayList<String> reqRidesList = parseListWithDelimiter(reqRides);
//                ArrayList<String> actRidesList = parseListWithDelimiter(actRides);
//                removeUserFromPost(reqRidesList, actRidesList, uid);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("[Database Error]", "Database Error: " + databaseError.getCode());
//            }
//        });
        usersRef.child(uid).removeValue();
    }

    private void removeUserFromPost(ArrayList<String> reqRidesList, ArrayList<String> actRidesList, String uid){
//        int i;
//        for(i = 0; i < reqRidesList.size(); i++){
//            postsRef.child(reqRidesList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String passengers = dataSnapshot.child("potential_passengers").getValue().toString();
//                    String postID = dataSnapshot.child("post_id").getValue().toString();
//                    String newList = removeFromListWithDelimiter(passengers, uid);
//                    postsRef.child(postID).child("potential_passengers").setValue(newList);
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.d("[Database Error]", "Database Error: " + databaseError.getCode());
//                }
//            });
//        }
//        for(i = 0; i < actRidesList.size(); i++){
//            postsRef.child(actRidesList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String passengers = dataSnapshot.child("accepted_passengers").getValue().toString();
//                    String postID = dataSnapshot.child("post_id").getValue().toString();
//                    String newList = removeFromListWithDelimiter(passengers, uid);
//                    postsRef.child(postID).child("accepted_passengers").setValue(newList);
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.d("[Database Error]", "Database Error: " + databaseError.getCode());
//                }
//            });
//        }
    }

    public void deletePost(String post_id) {
        postsRef.child(post_id).removeValue();
    }

//    public int getnext_post_id() {
//        DatabaseReference reference = this.getRoot().child("postCount");
//        Query query = reference.orderByChild("post_id");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    int key = Integer.parseInt(postSnapshot.getKey());
//                    if (HitchDatabase.this.most_recent_post_id < key) {
//                        HitchDatabase.this.most_recent_post_id = key;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return most_recent_post_id + 1;
//    }

}