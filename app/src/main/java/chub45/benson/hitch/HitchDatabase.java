package chub45.benson.hitch;

/**
 * Created by Mattmitch on 2/4/18.
 */

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class HitchDatabase
{
    private DatabaseReference rootRef;
    private DatabaseReference usersRef;
    private DatabaseReference postsRef;
   // HashMap<String, String> returnedUserMap;


    public HitchDatabase() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        usersRef = rootRef.child("users");
        postsRef = rootRef.child("posts");
    //    returnedUserMap = new HashMap<>();
    }

    // This function takes a user object as an argument and add this user object to
    // the firebase database under the users collection
    // Can possibly change the function to return true if successful

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

    // This function takes a post object as an argument and add this post object to
    // the firebase database under the posts collection
    // Can possibly change the function to return true if successful

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
        //postsAuthor.setValue(userMap);
    }

    private HashMap<String,String> makeUserMap(User user){
        HashMap<String, String> userMap = new HashMap<String, String>();
        userMap.put("username", user.toString());
        userMap.put("email", user.getEmail());
        userMap.put("fullName", user.getFullName());
        return userMap;
    }

//    public User getUser(String username) {
//        returnedUserMap.clear();
//        Query query = usersRef.orderByChild("username").equalTo(username);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()) {
//                    for (DataSnapshot node : dataSnapshot.getChildren()) {
//                        String value = (String) node.getValue();
//                        String key = (String) node.getKey();
//                        returnedUserMap.put(key, value);
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return new User(returnedUserMap.get("email"), returnedUserMap.get("username"), returnedUserMap.get("fullName"));
//    }

//    public HitchPost getPost(String username) { // return type will be a post
//        /* query the database to find the postTitle and return the post object */
//    }

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
