package chub45.benson.hitch;

/**
 * Created by Mattmitch on 2/4/18.
 */

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class HitchDatabase
{
    private DatabaseReference rootRef;
    private DatabaseReference usersRef;
    private DatabaseReference postsRef;


    public HitchDatabase() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        usersRef = rootRef.child("users");
        postsRef = rootRef.child("posts");
    }

    // This function takes a user object as an argument and add this user object to
    // the firebase database under the users collection
    // Can possibly change the function to return true if successful

    public void addUser(User user)
    {
        HashMap<String, String> userMap = new HashMap<String, String>();                // hashmap that contains the key for
                                                                           // each piece of data as well as the data <K,D>
        userMap.put("Username", user.toString());                              // Just a default name field (to be updated)
        userMap.put("Email", user.getEmail());                                  // Just a default password field (to be updated)

        DatabaseReference currentChild = usersRef.child(user.toString());   // creates a child of "users" that has the
                                                                            // username (should be returned by toString())

        // the following code adds the hashmap before the node previously created with username
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
        postMap.put("description", post.get_description());

        DatabaseReference currentChild = postsRef.child(post.toString());

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

    public void getUser(String username) { // return type will be a user
        /* query the database to find the username and return the user object */
    }

    public void getPost(String postTitle) { // return type will be a post
        /* query the database to find the postTitle and return the post object */
    }

    public void removeUser(String username) {
        usersRef.child(username).removeValue();
    }

    public void removePost(String postTitle){
        postsRef.child(postTitle).removeValue();
    }

    public DatabaseReference getRoot(){
        return rootRef;
    }
}
