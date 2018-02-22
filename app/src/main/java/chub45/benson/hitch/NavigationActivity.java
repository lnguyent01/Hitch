package chub45.benson.hitch;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;

public class NavigationActivity extends AppCompatActivity{
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("Home");
                    HomeFragment homeFragment = new HomeFragment();
                    android.support.v4.app.FragmentTransaction homeFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    homeFragmentTransaction.replace(R.id.content, homeFragment);
                    homeFragmentTransaction.commit();
                    return true;
                case R.id.navigation_map:
                    Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(mapIntent);
                    setTitle("Map");
                    MapFragment mapFragment = new MapFragment();
                    android.support.v4.app.FragmentTransaction mapFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mapFragmentTransaction.replace(R.id.content, mapFragment);
                    mapFragmentTransaction.commit();
                    return true;
                case R.id.navigation_posts:
                    setTitle("Posts");
                    PostsFragment postsFragment = new PostsFragment();
                    android.support.v4.app.FragmentTransaction postsFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    postsFragmentTransaction.replace(R.id.content, postsFragment);
                    postsFragmentTransaction.commit();
                    return true;
                case R.id.navigation_profile:
                    setTitle("Profile");
                    ProfileFragment profileFragment = new ProfileFragment();
                    android.support.v4.app.FragmentTransaction profileFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    profileFragmentTransaction.replace(R.id.content, profileFragment);
                    profileFragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Home Fragment Displayed when instance of activity class is created
        setTitle("Home");
        HomeFragment hFragment = new HomeFragment();
        FragmentTransaction hFragmentTransaction = getSupportFragmentManager().beginTransaction();
        hFragmentTransaction.replace(R.id.content, hFragment);
        hFragmentTransaction.commit();
    }
}
