package chub45.benson.hitch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    ListView listViewPosts;
    HitchDatabase<Student> hitchDatabase;
    List<Student> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        listViewPosts = (ListView) findViewById(R.id.listViewPosts);
        Student s1 = new Student("Matt", "12345");
        Student s2 = new Student("Jay", "98765");
        hitchDatabase.addPost(s1);
        hitchDatabase.addPost(s2);
        postList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        hitchDatabase.getRoot().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    Student post = postSnapshot.getValue(Student.class);
                    postList.add(post);
                }
                PostList adapter = new PostList(PostActivity.this, postList);
                listViewPosts.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
