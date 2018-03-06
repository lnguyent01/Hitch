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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RequestedPostDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requested_post_details);


        ImageView mProfile = (ImageView) findViewById(R.id.profile);
        ImageButton mCancelButton = (ImageButton) findViewById(R.id.CancelButton);
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

        // Get the current user's UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uID = currentUser.getUid();

        String num = intent.getExtras().getString("available_spots");
        String seats_left = num + " seats left";
        mSeatsLeftAndPrice.setText(seats_left);

        String potential_passengers_all = intent.getExtras().getString("potential_passengers");
        String [] potential_passengers_list = potential_passengers_all.split(Pattern.quote("|"));

        mCancelButton.setOnClickListener(new View.OnClickListener() {

            boolean Cancelled = false;

            @Override
            public void onClick(View view) {



                if (Cancelled == true) {
                    Toast.makeText(getBaseContext(), "You have already cancelled your request to join this ride!", Toast.LENGTH_SHORT).show();
                }
                else {
                    HitchDatabase CancelIt = new HitchDatabase();
                    CancelIt.removePassengerRequest(uID, postID);

                    Toast.makeText(getBaseContext(), "You have cancelled your request to join this ride!", Toast.LENGTH_SHORT).show();
                    Cancelled = true;
                }
            }

        });

    }
}
