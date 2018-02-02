package chub45.benson.hitch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Create log in button and wait until user clicks button with entered information
        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sends information to server and saves info, as long as username
                EditText userNameText = (EditText) findViewById(R.id.usernameText);
                EditText passwordText = (EditText) findViewById(R.id.passwordText);
                if(true){ //if user is identified from database, sends user to navigation activity
                    Intent navigationIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(navigationIntent);
                }


            }
        });

        //Create sign up button and wait until user clicks this button
        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Opens sign up activity for users to sign up by entering information
                Intent signUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
}
