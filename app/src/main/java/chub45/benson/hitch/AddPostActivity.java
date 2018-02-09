package chub45.benson.hitch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPostActivity extends AppCompatActivity {

    private static PostFactory postFactory = new DefaultPostFactory();
    private static HitchDatabase db = new HitchDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DateFormat formatter = new SimpleDateFormat("hh:mm");

        final EditText departingAreaText = (EditText) findViewById(R.id.departingAreaText);
        final EditText destinationText = (EditText) findViewById(R.id.destinationText);
        final EditText departureTimeText = (EditText) findViewById(R.id.departureTimeText);
        final EditText availableSpotsText = (EditText) findViewById(R.id.availableSpotsText);
        final EditText descriptionText = (EditText) findViewById(R.id.descriptionText);

        Button createPostButton = (Button) findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat formatter = new SimpleDateFormat("hh:mm");
                try {
                    Date date = new Date();
                    date.setTime(formatter.parse(departureTimeText.getText().toString()).getTime());
                    Post post = postFactory.createPost(departingAreaText.getText().toString(),
                            destinationText.getText().toString(),
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
}