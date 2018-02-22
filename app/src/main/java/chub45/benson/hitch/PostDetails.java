package chub45.benson.hitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        mDepartingFrom.setText(intent.getExtras().getString("departing_area"));
        mGoingTo.setText(intent.getExtras().getString("destination"));
        mDescriptionText.setText(intent.getExtras().getString("description"));
        mDriverName.setText(intent.getExtras().getString("name"));

        String TimeStatement = "Leaving at " + intent.getExtras().getString("departure_time");
        mDepartureTime.setText(TimeStatement);

        // Use this to find out which post this is
        final String postID = intent.getExtras().getString("postID");

        String num = intent.getExtras().getString("available_spots");
        String price = "0";
        String seats_left_and_price = num + " seats left at $" + price + " each";
        mSeatsLeftAndPrice.setText(seats_left_and_price);


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
