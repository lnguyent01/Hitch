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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class ViewMyPassengersActivity extends AppCompatActivity{

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
        String accepted_passengers_all = intent.getExtras().getString("accepted_passengers");



        firebaseUserSearch(accepted_passengers_all);
    }


    // This function does all the work when it comes to accessing the database and retrieving the relevant information
    private void firebaseUserSearch(String accepted_passengers) {


        // The FirebaseRecyclerAdapter is from FirebaseUI, a third-party library. It accesses the Firebase database.
        FirebaseRecyclerAdapter<User, postViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, postViewHolder>(

                // The information from the Firebase database in stored in this class
                User.class,
                // This is how the user information is going to be displayed
                R.layout.user_list_layout,
                // This is what puts the user information from the user class into the user_list_layout
                postViewHolder.class,
                // This loads all the users from the Firebase database
                mUserDatabase
        ) {
            @Override
            protected void populateViewHolder(postViewHolder viewHolder, final User model, int position) {


                // Passes the user information to the postViewHolder viewHolder
                viewHolder.setDetails(getApplicationContext(), model.getUid(), model.getUsername(), accepted_passengers);


                viewHolder.setOnClickListener(new postViewHolder.ClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

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


        private postViewHolder.ClickListener mClickListener;

        // Enables callbacks to be sent
        public interface ClickListener{
            public void onItemClick(View view, int position);
        }

        // This method, when used, allows whatever used it to do something when this user is clicked
        public void setOnClickListener(postViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
        public postViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            // When a user is clicked, the program will know exactly which user was clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

        // This method is the one that does the work required to display all of the information retrieved
        // by the firebaseRecyclerAdapter
        public void setDetails(Context ctx, String uID, String username, String accepted_passengers_all) {

            // Creates types of View object references and links them to every component of list_layout
            ImageView user_profile = (ImageView) mView.findViewById(R.id.profile);
            TextView userName = (TextView) mView.findViewById(R.id.userName);



            // Links the types of View object references to the information retrieved by firebaseRecyclerAdapter
            //if (postProfile != null) {
                // As long as the profile picture exists, display it
                // If it doesn't exist, the default profile picture will be displayed
              //  Glide.with(ctx).load(postProfile).into(userprofile);
            //}
            userName.setText(username);

            String [] potential_passengers_list = accepted_passengers_all.split(Pattern.quote("|"));

            boolean is_accepted = false;

            for (int i = 0; i < potential_passengers_list.length; i++) {
                String temp = potential_passengers_list[i];

                if (uID.equals(temp)) {
                    is_accepted = true;
                }
            }

            if (!is_accepted) {
                RelativeLayout listPart = (RelativeLayout) mView.findViewById(R.id.list_part);
                listPart.getLayoutParams().height = 0;
            }

        }

    }
}
