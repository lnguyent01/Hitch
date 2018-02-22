package chub45.benson.hitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileActivity extends AppCompatActivity {

    EditText emailText, fullNameText, usernameText, cityText, stateText;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        emailText = (EditText) findViewById(R.id.emailText);
        fullNameText = (EditText) findViewById(R.id.fullNameText);
        usernameText = (EditText) findViewById(R.id.usernameText);
        cityText = (EditText) findViewById(R.id.cityText);
        stateText = (EditText) findViewById(R.id.stateText);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();



    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

}
