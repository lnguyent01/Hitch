package chub45.benson.hitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by gehri on 2/16/2018.
 */

public class ChoosePosts extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_choose_posts_menu);

        ImageButton myPosts = (ImageButton) findViewById(R.id.my_posts);
        myPosts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myPostsIntent = new Intent(getApplicationContext(), MyPostsActivity.class);
                startActivity(myPostsIntent);
            }
        });

        ImageButton acceptedPosts = (ImageButton) findViewById(R.id.accepted_posts);
        acceptedPosts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent acceptedPostsIntent = new Intent(getApplicationContext(), AcceptedPostsActivity.class);
                startActivity(acceptedPostsIntent);
            }
        });

        ImageButton RequestedPosts = (ImageButton) findViewById(R.id.requested_posts);
        RequestedPosts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent requestedPostsIntent = new Intent(getApplicationContext(), RequestedPostsActivity.class);
                startActivity(requestedPostsIntent);
            }
        });
    }



}
