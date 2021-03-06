package chub45.benson.hitch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class AcceptMyPostsActivity extends AppCompatActivity{

    // This is what displays all posts relevant to what the user searched
    private RecyclerView mResultList;

    // This is what accesses the Firebase Database
    private DatabaseReference mUserDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passengers_list);

        // Links mUserDatabase to the actual Firebase database, accessing everything stored under "users"
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Links mResultList to the actual result list in the .xml
        mResultList = (RecyclerView) findViewById(R.id.passengers_list);
        // Makes the result list have a constant size
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String postID = intent.getExtras().getString("postID");
        String potential_passengers_all = intent.getExtras().getString("potential_passengers");
        String spotsLeft = intent.getExtras().getString("spots_left");



        firebaseUserSearch(potential_passengers_all, postID, spotsLeft);
    }


    // This function does all the work when it comes to accessing the database and retrieving the relevant information
    private void firebaseUserSearch(String potential_passengers, String postid, String spots_left) {


        // The FirebaseRecyclerAdapter is from FirebaseUI, a third-party library. It accesses the Firebase database.
        FirebaseRecyclerAdapter<User, postViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, postViewHolder>(

                // The information from the Firebase database in stored in this class
                User.class,
                // This is how the post information is going to be displayed
                R.layout.user_list_layout,
                // This is what puts the post information from the post class into the list_layout
                postViewHolder.class,
                // This loads all the posts from the Firebase database
                mUserDatabase
        ) {
            @Override
            protected void populateViewHolder(postViewHolder viewHolder, final User model, int position) {


                // Passes the post information to the postViewHolder viewHolder
                viewHolder.setDetails(getApplicationContext(), model.getUid(), model.getFullName(), model.getUsername(),
                        model.getProfilePicUrl(), model.getPhoneNo(), potential_passengers);

                // When a post is clicked, move to another activity - one that contains more details on the post that was clicked
                // Information is passed to that activity
                viewHolder.setOnClickListener(new postViewHolder.ClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        HitchDatabase AcceptIt = new HitchDatabase();
                        AcceptIt.acceptPassengers(model.getUid(), postid, getBaseContext());

                    }
                });
            }
        };

        // Links the result list to this firebaseRecyclerAdapter, so everything is properly displayed
        mResultList.setAdapter(firebaseRecyclerAdapter);
    }


    // Subclass that displays the information retrieved from the Firebase database by the firebaseRecyclerAdapter
    public static class postViewHolder extends RecyclerView.ViewHolder {

        View mView;

        // Get the current user's UID
        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
       // String uID = currentUser.getUid();

        private postViewHolder.ClickListener mClickListener;

        // Enables callbacks to be sent
        public interface ClickListener{
            public void onItemClick(View view, int position);
        }

        // This method, when used, allows whatever used it to do something when this post is clicked
        // firebaseRecyclerAdapter uses this method to move to another activity when clicked
        public void setOnClickListener(postViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
        public postViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            // When a post is clicked, the program will know exactly which post was clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

        // This method is the one that does the work required to display all of the information retrieved
        // by the firebaseRecyclerAdapter
        public void setDetails(Context ctx, String uID, String name, String username, String profilePic, String PhoneNum, String potential_passengers_all) {

            // Creates types of View object references and links them to every component of list_layout
            ImageView user_profile = (ImageView) mView.findViewById(R.id.user_pic);
            TextView userName = (TextView) mView.findViewById(R.id.userName);
            TextView phone_number = (TextView) mView.findViewById(R.id.phone_number);


            // Displaying profile picture
            // As long as the profile picture exists, display it
            // If it doesn't exist, the default profile picture will be displayed
            if (!profilePic.isEmpty()) {
                Glide.with(ctx)
                        .load(profilePic)
                        .apply(new RequestOptions().placeholder(R.drawable.default_pic))
                        .into(user_profile);
            }


            // Displays full name if available
            if (!name.isEmpty()) {
                userName.setText(name);
            }
            // If full name is not available, displays username
            else if (!username.isEmpty()) {
                userName.setText(username);
            }
            // Displays phone number if available
            if (!PhoneNum.isEmpty()) {
                phone_number.setText(PhoneNum);
            }

            String [] potential_passengers_list = potential_passengers_all.split(Pattern.quote("|"));

            boolean is_requested = false;

            for (int i = 0; i < potential_passengers_list.length; i++) {
                String temp = potential_passengers_list[i];

                if (uID.equals(temp)) {
                    is_requested = true;
                }
            }

            if (!is_requested) {
                RelativeLayout listPart = (RelativeLayout) mView.findViewById(R.id.list_part);
                listPart.getLayoutParams().height = 0;
            }

        }

    }
}
