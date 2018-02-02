package chub45.benson.hitch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Sign up button created
        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userText = (EditText) findViewById(R.id.userText);
                EditText passText = (EditText) findViewById(R.id.passText);
                EditText emailText = (EditText) findViewById(R.id.emailText);
                EditText countryText = (EditText) findViewById(R.id.countryText);
                EditText stateText = (EditText) findViewById(R.id.stateText);
                EditText cityText = (EditText) findViewById(R.id.cityText);
                //user input taken and sent to database via getText()
            }

        });

        //Cancel button created
        TextView cancelTView = (TextView) findViewById(R.id.cancelTView);
        cancelTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //if user clicks cancel, sends them to log in activity
                Intent sendBackIntent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(sendBackIntent);
            }
        });


    }
}
