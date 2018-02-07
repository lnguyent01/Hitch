package chub45.benson.hitch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddPostActivity extends AppCompatActivity {

    private PostFactory postFactory = new DefaultPostFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        final EditText departingAreaText = (EditText) findViewById(R.id.departingAreaText);
        final EditText destinationText = (EditText) findViewById(R.id.destinationText);
        final EditText departureTimeText = (EditText) findViewById(R.id.departureTimeText);
        final EditText availableSpotsText = (EditText) findViewById(R.id.availableSpotsText);
        final EditText descriptionText = (EditText) findViewById(R.id.descriptionText);

        Button createPostButton = (Button) findViewById(R.id.createPostButton);
        //createPostButton.setOnClickListener(postButtonListener);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
                User author = getLoggedInUser(); //TODO: replace stub method
                try {
                    Post post = postFactory.createPost(departingAreaText.getText().toString(),
                            destinationText.getText().toString(),
                            formatter.parse(departureTimeText.getText().toString()), Integer.parseInt(availableSpotsText.getText().toString()),
                            author, descriptionText.getText().toString());
                    Intent navigationIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(navigationIntent);
                } catch (ParseException e) {
                    // eventually handle it
                }
            }
        });
    }
}