package chub45.benson.hitch;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Map;
import java.util.HashMap;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    EditText emailText, passText, fullNameText, usernameText, cityText, stateText;

    final private HitchDatabase db = new HitchDatabase();
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

        findViewById(R.id.finishBtn).setOnClickListener(this);
        findViewById(R.id.cancelBtn).setOnClickListener(this);

    }

    private void updateUserInfo() {
        String email = emailText.getText().toString().trim();
        String password = passText.getText().toString().trim();
        String fullName = fullNameText.getText().toString().trim();
        String username = usernameText.getText().toString().trim();
        String city = cityText.getText().toString().trim();
        String state = stateText.getText().toString().trim();

        Map<String, Object> userUpdates = new HashMap<>();
        if (email.length() > 0)
            currentUser.updateEmail(email);
        if (password.length() > 0)
            currentUser.updatePassword(password);
        if (fullName.length() > 0)
            userUpdates.put("fullName", fullName);
        if (username.length() > 0)
            userUpdates.put("username", username);
        if (city.length() > 0)
            userUpdates.put("city", city);
        if (state.length() > 0)
            userUpdates.put("state", state);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        dbRef.updateChildren(userUpdates);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.finishBtn:
                updateUserInfo();
                startActivity(new Intent(this, ProfileActivity.class));

                break;
            case R.id.cancelBtn:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
        }
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

}