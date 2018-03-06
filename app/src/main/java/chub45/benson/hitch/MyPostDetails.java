package chub45.benson.hitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class MyPostDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_post_details);


        ImageView mProfile = (ImageView) findViewById(R.id.profile);
        ImageButton mAcceptButton = (ImageButton) findViewById(R.id.AcceptButton);
        ImageButton mPassengersButton = (ImageButton) findViewById(R.id.PassengersButton);
        TextView mDriverName = (TextView) findViewById(R.id.DriverName);
        TextView mDriverIs = (TextView) findViewById(R.id.yourDriverIs);
        TextView mFrom = (TextView) findViewById(R.id.From);
        TextView mDepartingFrom = (TextView) findViewById(R.id.departingFrom);
        TextView mTo = (TextView) findViewById(R.id.To);
        TextView mGoingTo = (TextView) findViewById(R.id.goingTo);
        TextView mDescriptionHeader = (TextView) findViewById(R.id.descriptionHeader);
        TextView mDescriptionText = (TextView) findViewById(R.id.displayDescriptionText);
        TextView mDepartureTime = (TextView) findViewById(R.id.DepartureTime);
        TextView mSeatsLeftAndPrice = (TextView) findViewById(R.id.SeatsLeftandPrice);

        Intent intent = getIntent();

        // Displaying profile picture and name

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        Query query =  dbRef.child("users").child(intent.getExtras().getString("uID"));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // As long as the profile picture exists, display it
                    // If it doesn't exist, the default profile picture will be displayed
                    if ((dataSnapshot.getValue(User.class).getProfilePicUrl() != "")) {
                        Glide.with(getApplicationContext())
                                .load(dataSnapshot.getValue(User.class).getProfilePicUrl())
                                .apply(new RequestOptions().placeholder(R.drawable.default_pic))
                                .into(mProfile);
                    }

                    // Display name if the user has one
                    if ((!dataSnapshot.getValue(User.class).getFullName().isEmpty())) {
                        mDriverName.setText(dataSnapshot.getValue(User.class).getFullName());
                    }

                    // If the user has no full name, display their username
                    else if ((!dataSnapshot.getValue(User.class).getUsername().isEmpty())) {
                        mDriverName.setText(dataSnapshot.getValue(User.class).getUsername());
                    }

                    // If the user has no username or full name, display their email address
                    else {
                        mDriverName.setText(intent.getExtras().getString("email"));
                    }
                }
                else {
                    Log.wtf("mytag", "dataSnapshot does not exists");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("[Database Error]", databaseError.getMessage());
            }
        });

        mDepartingFrom.setText(intent.getExtras().getString("departing_area"));
        mGoingTo.setText(intent.getExtras().getString("destination"));
        mDescriptionText.setText(intent.getExtras().getString("description"));

        String TimeStatement = "Leaving at " + intent.getExtras().getString("departure_time");
        mDepartureTime.setText(TimeStatement);

        // Use this to find out which post this is
        final String postID = intent.getExtras().getString("postID");

        String num = intent.getExtras().getString("available_spots");
        String seats_left = num + " seats left";
        mSeatsLeftAndPrice.setText(seats_left);

        // Parsing the potential_passengers data
        final String potential_passengers_all = intent.getExtras().getString("potential_passengers");

        boolean potential_passengers_is_empty = false;

        // If the list isn't empty
        if (!potential_passengers_all.equals("")) {
            String [] potential_passengers_list = potential_passengers_all.split(Pattern.quote("|"));
        }
        else {
            potential_passengers_is_empty = true;
        }


        // Parsing the accepted_passengers data
        String accepted_passengers_all = intent.getExtras().getString("accepted_passengers");

        boolean accepted_passengers_is_empty = false;

        // If the list isn't empty
        if (!accepted_passengers_all.equals("")) {
            String [] accepted_passengers_list = accepted_passengers_all.split(Pattern.quote("|"));
        }
        else {
            accepted_passengers_is_empty = true;
        }


        final String finalNum = num;
        final boolean finalPotential_passengers_is_empty = potential_passengers_is_empty;
        final boolean finalAccepted_passengers_is_empty = accepted_passengers_is_empty;
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            boolean no_more_potential_passengers = false;

            @Override
            public void onClick(View view) {

                if (finalNum.equals("0")) {
                    Toast.makeText(getBaseContext(), "There is no more space to accept passengers on this ride!", Toast.LENGTH_SHORT).show();
                }

                else if ((!finalPotential_passengers_is_empty) && (!no_more_potential_passengers)) {

                    Intent acceptThem = new Intent(getApplicationContext(), AcceptMyPostsActivity.class);
                    acceptThem.putExtra("postID", postID);
                    acceptThem.putExtra("potential_passengers", potential_passengers_all);
                    acceptThem.putExtra("spots_left", finalNum);
                    startActivity(acceptThem);


                }

                else if (finalPotential_passengers_is_empty || no_more_potential_passengers) {
                    Toast.makeText(getBaseContext(), "There are no passengers to accept!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mPassengersButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!finalAccepted_passengers_is_empty) {

                    Intent viewThem = new Intent(getApplicationContext(), ViewMyPassengersActivity.class);
                    viewThem.putExtra("accepted_passengers", accepted_passengers_all);
                    startActivity(viewThem);

                }

                else {
                    Toast.makeText(getBaseContext(), "You haven't accepted any requests!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
