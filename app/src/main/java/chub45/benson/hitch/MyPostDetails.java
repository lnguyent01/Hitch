package chub45.benson.hitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyPostDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_post_details);


        ImageView mProfile = (ImageView) findViewById(R.id.profile);
        ImageButton mAcceptButton = (ImageButton) findViewById(R.id.AcceptButton);
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
        String postID = intent.getExtras().getString("postID");

        String num = intent.getExtras().getString("available_spots");
        String price = "0";
        String seats_left_and_price = num + " seats left at $" + price + " each";
        mSeatsLeftAndPrice.setText(seats_left_and_price);




        mAcceptButton.setOnClickListener(new View.OnClickListener() {

            boolean PLACEHOLDER = false;
            //Remove PLACEHOLDER

            @Override
            public void onClick(View view) {


                if (!PLACEHOLDER) {
                    Toast.makeText(getBaseContext(), "You have accepted these passengers!", Toast.LENGTH_SHORT).show();
                    PLACEHOLDER = true;

                    // Put code here
                    // Every single passenger (Represented by UID) in the "pending" String gets moved to the "accepted" String
                    // If there is not enough space, just don't accept the last ones
                    //   Example: 3 spaces, 4 pending. The first 3 usernames get accepted, the 4th one doesn't
                    // Space is decremented accordingly
                    // Use the post's unique ID, stored in postID, to find the correct post


                }
                else {

                    //If space = 0, do this
                    Toast.makeText(getBaseContext(), "There is no more space on this ride!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
