package chub45.benson.hitch;

import android.content.Context;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.regex.Pattern;

public class PostDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details);


        ImageView mProfile = (ImageView) findViewById(R.id.profile);
        ImageButton mJoinButton = (ImageButton) findViewById(R.id.JoinButton);
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

                    // If not, display their email address
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


        // Get the current user's UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uID = currentUser.getUid();


        // If this is true, then the current user is on the list of potential passengers,
        // meaning that they have requested to join this ride but have not been accepted or declined yet
        boolean isUserRequested = false;

        String potential_passengers_all = intent.getExtras().getString("potential_passengers");

        // If the list isn't empty
        if (!(potential_passengers_all.equals(""))) {

            // If there is more than one person on the requested list, see if the current user is there
            if (potential_passengers_all.indexOf("|") > 0) {
                String [] potential_passengers_list = potential_passengers_all.split(Pattern.quote("|"));

                for (int i = 0; i < potential_passengers_list.length; i++) {
                    String temp = potential_passengers_list[i];

                    if (uID.equals(temp)) {
                        isUserRequested = true;
                    }
                }
            }

            // If there is one person on the requested list, see if the current user is them
            else {
                if (uID.equals(potential_passengers_all)) {
                    isUserRequested = true;
                }
            }
        }

        // If this is true, then the current user is on the list of accepted passengers
        boolean isUserAccepted = false;

        String accepted_passengers_all = intent.getExtras().getString("accepted_passengers");

        // If the list isn't empty
        if (!(accepted_passengers_all.equals(""))) {


                String [] accepted_passengers_list = accepted_passengers_all.split(Pattern.quote("|"));

                for (String accepted_passenger : accepted_passengers_list) {

                    if (uID.equals(accepted_passenger)) {
                        isUserAccepted = true;
                   }
                }

            }



        final boolean finalIsUserRequested = isUserRequested;
        final boolean finalIsUserAccepted = isUserAccepted;
        mJoinButton.setOnClickListener(new View.OnClickListener() {

            boolean Requested = false;

            @Override
            public void onClick(View view) {
                if ((!finalIsUserRequested) && (!finalIsUserAccepted) && (!Requested)) {

                    HitchDatabase requestIt = new HitchDatabase();
                    requestIt.addPassengerRequest(uID, postID);

                    Toast.makeText(getBaseContext(), "You have requested to join this ride!", Toast.LENGTH_SHORT).show();
                    Requested = true;
                }
                else if (finalIsUserRequested || Requested){
                    Toast.makeText(getBaseContext(), "You have already requested to join this ride!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "You have already been accepted to this ride!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
