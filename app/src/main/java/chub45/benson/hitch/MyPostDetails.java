package chub45.benson.hitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

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
        final String postID = intent.getExtras().getString("postID");

        String num = intent.getExtras().getString("available_spots");
        String price = "0";
        String seats_left_and_price = num + " seats left at $" + price + " each";
        mSeatsLeftAndPrice.setText(seats_left_and_price);

        // Parsing the potential_passengers data
        String potential_passengers_all = intent.getExtras().getString("potential_passengers");

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
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            boolean no_more_potential_passengers = false;

            @Override
            public void onClick(View view) {

                if (finalNum.equals("0")) {
                    Toast.makeText(getBaseContext(), "There is no more space to accept passengers on this ride!", Toast.LENGTH_SHORT).show();
                }

                else if ((!finalPotential_passengers_is_empty) && (!no_more_potential_passengers)) {

                    HitchDatabase acceptThem = new HitchDatabase();
                    acceptThem.acceptPassengers(postID);

                    Toast.makeText(getBaseContext(), "You have accepted these passengers!", Toast.LENGTH_SHORT).show();
                    no_more_potential_passengers = true;


                }

                else if (finalPotential_passengers_is_empty || no_more_potential_passengers) {
                    Toast.makeText(getBaseContext(), "There are no passengers to accept!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
