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
    private ArrayList<Post> posts;


    public HitchDatabase() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        usersRef = rootRef.child("users");
        postsRef = rootRef.child("posts");
        most_recent_post_id = -1; //placeholder
    }

    public void addUser(User user)
    {
        HashMap<String, String> userMap = makeUserMap(user);

        DatabaseReference currentChild = usersRef.child(user.toString());

        currentChild.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {                                  // if the data was successfully added to the database
                    // Make toast or whatever you want
                }
                else{                                                      // if the data was not successfully added
                    // Make burnt toast
                }
            }
        });

    }

    public void addPost(Post post)
    {
        HashMap<String, String> postMap = new HashMap<>();

        postMap.put("departing_area", post.getdeparting_area());
        postMap.put("destination", post.getdestination());
        postMap.put("departure_time", post.getdeparture_time().toString());
        postMap.put("available_spots", post.getavailable_spots().toString());
        postMap.put("description", post.getdescription());
        postMap.put("author_uid", post.getauthor());
        postMap.put("author_email", post.getauthor_email());
        postMap.put("post_id", post.get_post_id().toString());
        postMap.put("potential_passengers", post.getpotential_passengers());
        postMap.put("accepted_passengers", post.getaccepted_passengers());


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

    public ArrayList<Post> getPosts() {
        this.getAllPosts();
        return this.posts;
    }

    public void getAllPosts() {
    //DatabaseReference reference = postsRef;
        PostFactory factory = new DefaultPostFactory();
        /*postsRef.orderByChild("available_spots").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String departing_area = postSnapshot.child("departing_area").toString();
                        String destination = postSnapshot.child("destination").toString();
                        String departure_time = postSnapshot.child("departure_time").toString();
                        String num_spots = (String) postSnapshot.child("available_spots").getValue();
                        int available_spots = Integer.parseInt(num_spots);
                        String author_uid = postSnapshot.child("author_uid").toString();
                        String author_email = postSnapshot.child("author_email").toString();
                        String description = postSnapshot.child("description").toString();
                        String s_id = (String) postSnapshot.child("post_id").getValue();
                        int id = Integer.parseInt(s_id);
                        String potential_passengers = postSnapshot.child("potential_passengers").toString();
                        String accepted_passengers = postSnapshot.child("accepted_passengers").toString();
                        HitchDatabase.this.posts.add(factory.createPostFromDb(departing_area, destination, departure_time, available_spots, author_uid,
                                author_email, description, id, potential_passengers, accepted_passengers));
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String str) {
                // do nothing
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String str) {
                // do nothing
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // do nothing
            }
            @Override
            public void onCancelled(DatabaseError dbError) {
                // do nothing
            }
        });*/
        Query query = postsRef.orderByChild("available_spots");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    collectPosts((Map<String, Object>) postSnapshot.getValue());
                    String departing_area = postSnapshot.child("departing_area").toString();
                    String destination = postSnapshot.child("destination").toString();
                    String departure_time = postSnapshot.child("departure_time").toString();
                    String num_spots = (String) postSnapshot.child("available_spots").getValue();
                    int available_spots = Integer.parseInt(num_spots);
                    String author_uid = postSnapshot.child("author_uid").toString();
                    String author_email = postSnapshot.child("author_email").toString();
                    String description = postSnapshot.child("description").toString();
                    String s_id = (String) postSnapshot.child("post_id").getValue();
                    int id = Integer.parseInt(s_id);
                    String potential_passengers = postSnapshot.child("potential_passengers").toString();
                    String accepted_passengers = postSnapshot.child("accepted_passengers").toString();
                    HitchDatabase.this.addToPostsList(factory.createPostFromDb(departing_area, destination, departure_time, available_spots, author_uid,
                            author_email, description, id, potential_passengers, accepted_passengers));
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("[Database Error]", databaseError.getMessage());
        }
    });
    //return this.posts;
}

    private void collectPosts(Map<String, Object> posts) {
        String departing_area, destination, departure_time, num_spots, author_uid, author_email, description, s_id, potential_passengers, accepted_passengers;
        PostFactory factory = new DefaultPostFactory();
        int available_spots, id;
        for (Map.Entry<String, Object> entry: posts.entrySet()) {
            Map post = (Map) entry.getValue();
            departing_area = post.get("departing_area").toString();
            destination = post.get("destination").toString();
            departure_time = post.get("departure_time").toString();
            num_spots = (String) post.get("available_spots").toString();
            available_spots = Integer.parseInt(num_spots);
            author_uid = post.get("author_uid").toString();
            author_email = post.get("author_email").toString();
            description = post.get("description").toString();
            s_id = (String) post.get("post_id").toString();
            id = Integer.parseInt(s_id);
            potential_passengers = post.get("potential_passengers").toString();
            accepted_passengers = post.get("accepted_passengers").toString();
            this.addToPostsList(factory.createPostFromDb(departing_area, destination, departure_time, available_spots, author_uid,
                    author_email, description, id, potential_passengers, accepted_passengers));
        }
    }

    public void addToPostsList(Post post) {
        posts.add(post);
    }

    public int getnext_post_id() {
        DatabaseReference reference = this.getRoot().child("posts");
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

    private HashMap<String,String> makeUserMap(User user){
        HashMap<String, String> userMap = new HashMap<String, String>();
        userMap.put("username", user.toString());
        userMap.put("email", user.getEmail());
        userMap.put("fullName", user.getFullName());
        return userMap;
    }

//    public HitchPost getPost(String username) { // return type will be a post
//        /* query the database to find the postTitle and return the post object */
//    }

    public void addPassengerRequest(final String passengerUID, String postID){
        final DatabaseReference currentRef = postsRef.child(postID).child("potential_passengers");

        currentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String passengers = (String) dataSnapshot.getValue();
                    Log.d("CURRENT", "currval: " + passengers);
                     if (passengers.isEmpty() && !passengers.contains(passengerUID)) {
                         currentRef.setValue(passengerUID);
                      }
                      else if(!passengers.isEmpty() && !passengers.contains(passengerUID)){
                          currentRef.setValue(passengers + "|" + passengerUID);
                      }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                     Log.d("FAILURE", "Could not add passenger request because: " + databaseError.getCode());
            }
        });
    }

    public void acceptPassengers(String postID){
        final DatabaseReference currentRef = postsRef.child(postID);
        currentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String requestedPassengers = (String) dataSnapshot.child("potential_passengers").getValue();
                if(!requestedPassengers.isEmpty()) {
                    currentRef.child("accepted_passengers").setValue(requestedPassengers);
                    currentRef.child("potential_passengers").setValue("");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FAILURE", "Could not accept passengers because: " + databaseError.getCode());
            }
        });
    }

    public void removeUser(String username) {
        usersRef.child(username).removeValue();
    }

    public void removePost(String post_id){
        postsRef.child(post_id).removeValue();
    }

    public DatabaseReference getRoot(){
        return rootRef;
    }
}
