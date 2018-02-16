package chub45.benson.hitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;

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

        String TimeStatement = "Leaving at " + intent.getExtras().getString("departure_time");
        mDepartureTime.setText(TimeStatement);

        // Use this to find out which post this is
        String postID = intent.getExtras().getString("postID");

        String num = intent.getExtras().getString("available_spots");
        String price = "0";
        String seats_left_and_price = num + " seats left at $" + price + " each";
        mSeatsLeftAndPrice.setText(seats_left_and_price);




        mJoinButton.setOnClickListener(new View.OnClickListener() {
            boolean requested = false;

            @Override
            public void onClick(View view) {
                if (!requested) {
                    Toast.makeText(getBaseContext(), "You have requested to join this ride!", Toast.LENGTH_SHORT).show();
                    requested = true;
                }
                else {
                    Toast.makeText(getBaseContext(), "You have already requested to join this ride", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
