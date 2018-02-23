package chub45.benson.hitch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RequestedPostsActivity extends AppCompatActivity{

    // This is what displays all posts relevant to what the user searched
    private RecyclerView mResultList;

    // This is what accesses the Firebase Database
    private DatabaseReference mUserDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accepted_post_list);

        // Links mUserDatabase to the actual Firebase database, accessing everything stored under "posts"
        mUserDatabase = FirebaseDatabase.getInstance().getReference("posts");

        // Links mResultList to the actual result list in the .xml
        mResultList = (RecyclerView) findViewById(R.id.accepted_post_list);
        // Makes the result list have a constant size
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));



        firebaseUserSearch("");
    }


    // This function does all the work when it comes to accessing the database and retrieving the relevant information
    private void firebaseUserSearch(String searchText) {


        // The FirebaseRecyclerAdapter is from FirebaseUI, a third-party library. It accesses the Firebase database.
        FirebaseRecyclerAdapter<SearchDriverPost, postViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SearchDriverPost, postViewHolder>(

                // The information from the Firebase database in stored in this class
                SearchDriverPost.class,
                // This is how the post information is going to be displayed
                R.layout.list_layout,
                // This is what puts the post information from the post class into the list_layout
                postViewHolder.class,
                // This loads all the posts from the Firebase database
                mUserDatabase
        ) {
            @Override
            protected void populateViewHolder(postViewHolder viewHolder, final SearchDriverPost model, int position) {

                // Passes the post information to the postViewHolder viewHolder
                viewHolder.setDetails(getApplicationContext(), model.getdeparting_area(),
                        model.getdestination(), String.valueOf(model.getavailable_spots()), model.getdeparture_time(),
                        model.getpotential_passengers());

                // When a post is clicked, move to another activity - one that contains more details on the post that was clicked
                // Information is passed to that activity
                viewHolder.setOnClickListener(new postViewHolder.ClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), AcceptedPostDetails.class);
                        intent.putExtra("destination", model.getdestination());
                        intent.putExtra("departing_area", model.getdeparting_area());
                        intent.putExtra("available_spots", String.valueOf(model.getavailable_spots()));
                        intent.putExtra("departure_time", model.getdeparture_time());
                        intent.putExtra("description", model.getdescription());
                        intent.putExtra("postID", model.getpost_id());
                        intent.putExtra("name", model.getauthor_email());
                        intent.putExtra("potential_passengers", model.getpotential_passengers());
                        intent.putExtra("accepted_passengers", model.getaccepted_passengers());
                        startActivity(intent);
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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uID = currentUser.getUid();

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
        public void setDetails(Context ctx, String postFrom, String postTo, String postSeats, String postTime, String postRequested) {

            // Creates types of View object references and links them to every component of list_layout
            ImageView post_profile = (ImageView) mView.findViewById(R.id.profile);
            TextView post_from = (TextView) mView.findViewById(R.id.from);
            TextView post_to = (TextView) mView.findViewById(R.id.to);
            TextView post_time = (TextView) mView.findViewById(R.id.time);
            TextView post_seats = (TextView) mView.findViewById(R.id.seats_left_num);



            // Links the types of View object references to the information retrieved by firebaseRecyclerAdapter
            //if (postProfile != null) {
                // As long as the profile picture exists, display it
                // If it doesn't exist, the default profile picture will be displayed
              //  Glide.with(ctx).load(postProfile).into(post_profile);
            //}
            post_from.setText(postFrom);
            post_to.setText(postTo);
            post_time.setText(postTime);
            post_seats.setText(postSeats);

            // If this is true, then the current user is on the list of accepted passengers
            boolean isUserAccepted = false;

            String requested_passengers_all = postRequested;

            // If the list isn't empty
            if (!(requested_passengers_all.equals(""))) {


                String [] requested_passengers_list = requested_passengers_all.split(Pattern.quote("|"));

                for (String requested_passenger : requested_passengers_list) {

                    if (uID.equals(requested_passenger)) {
                        isUserAccepted = true;
                    }
                }

            }
            if (!(isUserAccepted)) {
                RelativeLayout listPart = (RelativeLayout) mView.findViewById(R.id.list_part);
                listPart.getLayoutParams().height = 0;
            }
        }

    }
}
