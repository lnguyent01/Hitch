package chub45.benson.hitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPostActivity extends AppCompatActivity {

    private static PostFactory postFactory = new DefaultPostFactory();
    private static HitchDatabase db = new HitchDatabase();
    private String departing_area, destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        departing_area = "";
        destination = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DateFormat formatter = new SimpleDateFormat("hh:mm a");

        final PlaceAutocompleteFragment departingAreaText = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.departingAreaText);
        departingAreaText.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        AddPostActivity.this.setDeparting_area(place.getAddress().toString());
                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        final PlaceAutocompleteFragment destinationText = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.destinationText);
        destinationText.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        AddPostActivity.this.setDestination(place.getAddress().toString());
                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        final EditText departureTimeText = (EditText) findViewById(R.id.departureTimeText);
        final EditText availableSpotsText = (EditText) findViewById(R.id.availableSpotsText);
        final EditText descriptionText = (EditText) findViewById(R.id.displayDescriptionText);

        Button createPostButton = (Button) findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat formatter = new SimpleDateFormat("hh:mm a");
                try {
                    //Date date = new Date();
                    //date.setTime(formatter.parse(departureTimeText.getText().toString()).getTime());
                    Date date = formatter.parse(departureTimeText.getText().toString());
                    /*Post post = postFactory.createPost(departingAreaText.getText().toString(),
                            destinationText.getText().toString(),
                            date, Integer.parseInt(availableSpotsText.getText().toString()),
                            user, descriptionText.getText().toString());*/
                    Post post = postFactory.createPost(departing_area, destination,
                            date, Integer.parseInt(availableSpotsText.getText().toString()),
                            user, descriptionText.getText().toString());
                    db.addPost(post);
                    //Intent navigationIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                    //startActivity(navigationIntent);
                    finish();
                } catch (ParseException e) {
                    /*Date d = new Date();
                    Post post = postFactory.createPost(departingAreaText.getText().toString(),
                            destinationText.getText().toString(),
                            d, Integer.parseInt(availableSpotsText.getText().toString()),
                            user, descriptionText.getText().toString());
                    db.addPost(post);*/
                    finish();
                }
            }
        });
    }

    public void setDeparting_area(String departing_area) { this.departing_area = departing_area; }

    public void setDestination(String destination) { this.destination = destination; }
}