package chub45.benson.hitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import android.util.Log;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    ImageView profilePicIV;
    TextView displayNameTV;
    TextView usernameTV;
    TextView emailTV;
    TextView countryTV;
    TextView stateTV;
    TextView cityTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePicIV = (ImageView)findViewById(R.id.profilePicIV);
        displayNameTV = (TextView)findViewById(R.id.displayNameTV);
        usernameTV = (TextView)findViewById(R.id.usernameTV);
        emailTV = (TextView)findViewById(R.id.emailTV);
        countryTV = (TextView)findViewById(R.id.countryTV);
        stateTV = (TextView)findViewById(R.id.stateTV);
        cityTV = (TextView)findViewById(R.id.cityTV);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            String displayName = user.getDisplayName();
            String email = user.getEmail();

            Log.d("displayName: ", displayName);
            displayNameTV.setText(displayName);
            emailTV.setText("Email: " + email);

        }
    }
}
