package chub45.benson.hitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by gehri on 2/16/2018.
 */

public class ChoosePosts extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_choose_posts_menu);

        Button myPosts = (Button) findViewById(R.id.my_posts);
        myPosts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Opens add post activity for users to add a post by entering information
                Intent myPostsIntent = new Intent(getApplicationContext(), MyPostsActivity.class);
                startActivity(myPostsIntent);
            }
        });

        Button acceptedPosts = (Button) findViewById(R.id.accepted_posts);
        acceptedPosts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Opens add post activity for users to add a post by entering information
                Intent acceptedPostsIntent = new Intent(getApplicationContext(), ChoosePosts.class);
                startActivity(acceptedPostsIntent);
            }
        });
    }



}
