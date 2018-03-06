package chub45.benson.hitch;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class HitchDatabase {
    private DatabaseReference rootRef;
    private DatabaseReference usersRef;
    private DatabaseReference postsRef;
    private DatabaseReference countRef;
    private PostFactory factory;
    private Post triggerQuery;
    private int postCount;

    public HitchDatabase() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        countRef = rootRef.child("postCount");
        usersRef = rootRef.child("users");
        postsRef = rootRef.child("posts");
        factory = new DefaultPostFactory();
        triggerQuery = factory.createPostFromDb("","", "", "","", 0, "","","", -1, "", "");
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
        userMap.put("username", user.getUsername());
        userMap.put("profilePicUrl", user.getProfilePicUrl());
        userMap.put("fullName", user.getFullName());
        userMap.put("phoneNo", user.getPhoneNo());
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


    public void removePassengerRequest(final String passengerUid, final String postID){
        final DatabaseReference currentPostRef = postsRef.child(postID).child("potential_passengers");
        //final DatabaseReference currentUserRef = usersRef.child(passengerUid).child("rideRequests");

        currentPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String passengers = (String) dataSnapshot.getValue();

                if (passengers.indexOf("|") > 0) {
                    String[] potential_passengers_list = passengers.split(Pattern.quote("|"));

                    String final_passengers = "";


                    for (int i = 0; i < potential_passengers_list.length; i++) {
                        String temp = potential_passengers_list[i];

                        if (passengerUid.equals(temp) && i == 0) {
                            if (potential_passengers_list.length < 2) {
                            } else {
                                final_passengers = potential_passengers_list[1] + "|";
                                i = 1;
                            }
                        } else if (passengerUid.equals(temp)) {
                        } else {
                            final_passengers = final_passengers + potential_passengers_list[i] + "|";
                        }


                    }

                    if (final_passengers.substring(final_passengers.length() - 1).equals("|") && final_passengers.length() > 0) {
                        final_passengers = final_passengers.substring(0, final_passengers.length() - 1);
                    }

                    currentPostRef.setValue(final_passengers);
                }



                else {
                    currentPostRef.setValue("");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FAILURE", "Could not cancel request because: " + databaseError.getCode());
            }
        });


        ///currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
           // @Override
          //  public void onDataChange(DataSnapshot dataSnapshot) {
           //     String rides = (String) dataSnapshot.getValue();
           //     if(rides.isEmpty()){
           //         currentUserRef.setValue(postID);
           //     }
           //     else if(!rides.isEmpty() && !rides.contains(postID)){
           //         currentUserRef.setValue(rides + "|" + postID);
           //     }
           // }
           // @Override
           // public void onCancelled(DatabaseError databaseError) {
            //    Log.d("FAILURE", "Could not add to request to user because: " + databaseError.getCode());
           // }
       // });
    }

    public void acceptPassengers(final String passengerUid, final String postID, Context context){
        final DatabaseReference currentPostRef = postsRef.child(postID);
        currentPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String potentialPassengers = (String) dataSnapshot.child("potential_passengers").getValue();
                String acceptedPassengers = (String) dataSnapshot.child("accepted_passengers").getValue();
                Integer availSpots = Integer.parseInt((String) dataSnapshot.child("available_spots").getValue());


                // If this is true, then the current user is on the list of accepted passengers
                boolean isUserAccepted = false;

                // If the list isn't empty
                if (!(acceptedPassengers.equals(""))) {


                    String [] accepted_passengers_list = acceptedPassengers.split(Pattern.quote("|"));

                    for (String accepted_passenger : accepted_passengers_list) {

                        if (passengerUid.equals(accepted_passenger)) {
                            isUserAccepted = true;
                        }
                    }

                }

                if (!isUserAccepted && availSpots > 0) {
                if (potentialPassengers.indexOf("|") > 0) {
                    String[] potential_passengers_list = potentialPassengers.split(Pattern.quote("|"));

                    String final_passengers = "";

                    for (int i = 0; i < potential_passengers_list.length; i++) {
                        String temp = potential_passengers_list[i];

                        if (passengerUid.equals(temp) && i == 0) {
                            if (potential_passengers_list.length < 2) {
                            } else {
                                final_passengers = potential_passengers_list[1] + "|";
                                i = 1;
                            }
                        } else if (passengerUid.equals(temp)) {
                        } else {
                            final_passengers = final_passengers + potential_passengers_list[i] + "|";
                        }


                    }

                    if (final_passengers.substring(final_passengers.length() - 1).equals("|") && final_passengers.length() > 0) {
                        final_passengers = final_passengers.substring(0, final_passengers.length() - 1);
                    }

                    currentPostRef.child("potential_passengers").setValue(final_passengers);
                } else {
                    currentPostRef.child("potential_passengers").setValue("");
                }


                if (!acceptedPassengers.equals("")) {
                    String final_accepted = acceptedPassengers + "|" + passengerUid;
                    currentPostRef.child("accepted_passengers").setValue(final_accepted);
                } else {
                    currentPostRef.child("accepted_passengers").setValue(passengerUid);
                }

                int temp_spots = availSpots - 1;
                String final_spots = "" + temp_spots;
                currentPostRef.child("available_spots").setValue(final_spots);
                Toast.makeText(context, "This passengers has been accepted! They will remain on the list until you refresh by backing out to the 'My Posts' menu.", Toast.LENGTH_LONG).show();
            }

            else if (availSpots <= 0) {
                    Toast.makeText(context, "There are no more spots available on this ride. Please refresh by backing out to the 'My Posts' menu.", Toast.LENGTH_LONG).show();
                }

            else {
                    Toast.makeText(context, "This passenger has already been accepted! They will remain on the list until you refresh by backing out to the 'My Posts' menu.", Toast.LENGTH_LONG).show();
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

    public void deleteUser(String uid) {
        Query query = postsRef.orderByChild("author_uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String postToDelete = data.child("post_id").getValue().toString();
//                    Log.d("Matthew", "Post to delete: " + postToDelete);
                    deletePost(postToDelete);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("[Database Error]", "Database Error: " + databaseError.getCode());
            }
        });

        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String reqRides = dataSnapshot.child("rideRequests").getValue().toString();
                String actRides = dataSnapshot.child("activeRides").getValue().toString();
                ArrayList<String> reqRidesList = parseListWithDelimiter(reqRides);
                ArrayList<String> actRidesList = parseListWithDelimiter(actRides);
                removeUserFromPost(reqRidesList, actRidesList, uid);
                usersRef.child(uid).removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("[Database Error]", "Database Error: " + databaseError.getCode());
            }
        });
    }

    private void removeUserFromPost(ArrayList<String> reqRidesList, ArrayList<String> actRidesList, String uid) {
        for (int i = 0; i < reqRidesList.size(); i++) {
            postsRef.child(reqRidesList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String passengers = dataSnapshot.child("potential_passengers").getValue().toString();
                    String postID = dataSnapshot.child("post_id").getValue().toString();
                    String newList = removeFromListWithDelimiter(passengers, uid);
                    postsRef.child(postID).child("potential_passengers").setValue(newList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("[Database Error]", "Database Error: " + databaseError.getCode());
                }
            });
        }
        for (int i = 0; i < actRidesList.size(); i++) {
            postsRef.child(actRidesList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String passengers = dataSnapshot.child("accepted_passengers").getValue().toString();
                    String postID = dataSnapshot.child("post_id").getValue().toString();
                    String newList = removeFromListWithDelimiter(passengers, uid);
                    postsRef.child(postID).child("accepted_passengers").setValue(newList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("[Database Error]", "Database Error: " + databaseError.getCode());
                }
            });
        }
    }

    public void deletePost(String post_id) {
        postsRef.child(post_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String potPass = dataSnapshot.child("potential_passengers").getValue().toString();
                String accPass = dataSnapshot.child("accepted_passengers").getValue().toString();
                ArrayList<String> potPassList = parseListWithDelimiter(potPass);
                ArrayList<String> accPassList = parseListWithDelimiter(accPass);
                removePostFromUser(potPassList, accPassList, post_id);
                postsRef.child(post_id).removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("[Database Error]", "Database Error: " + databaseError.getCode());
            }
        });
    }

    private void removePostFromUser(ArrayList<String> potPassList, ArrayList<String> accPassList, String post_id){
        for (int i = 0; i < potPassList.size(); i++) {
            usersRef.child(potPassList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String rideReq = dataSnapshot.child("rideRequests").getValue().toString();
                    String uid = dataSnapshot.child("uid").getValue().toString();
                    String newList = removeFromListWithDelimiter(rideReq, post_id);
                    usersRef.child(uid).child("rideRequests").setValue(newList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("[Database Error]", "Database Error: " + databaseError.getCode());
                }
            });
        }
        for (int i = 0; i < accPassList.size(); i++) {
            usersRef.child(accPassList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String actRides = dataSnapshot.child("activeRides").getValue().toString();
                    String uid = dataSnapshot.child("uid").getValue().toString();
                    String newList = removeFromListWithDelimiter(actRides, post_id);
                    usersRef.child(uid).child("activeRides").setValue(newList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("[Database Error]", "Database Error: " + databaseError.getCode());
                }
            });
        }
    }

    public DatabaseReference getCountRef() { return countRef; }

    public DatabaseReference getRoot() { return rootRef; }

}
