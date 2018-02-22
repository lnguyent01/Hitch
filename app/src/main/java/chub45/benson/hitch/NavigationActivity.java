package chub45.benson.hitch;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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
//                    Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
//                    startActivity(mapIntent);
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
<<<<<<< HEAD
        //Home Fragment Displayed when instance of activity class is created
        setTitle("Home");
        HomeFragment hFragment = new HomeFragment();
        FragmentTransaction hFragmentTransaction = getSupportFragmentManager().beginTransaction();
        hFragmentTransaction.replace(R.id.content, hFragment);
        hFragmentTransaction.commit();
=======

        //Create post creation button and wait until user clicks this button
        ImageButton addPostButton = (ImageButton) findViewById(R.id.addPostButton);
        addPostButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Opens add post activity for users to add a post by entering information
                Intent signUpIntent = new Intent(getApplicationContext(), AddPostActivity.class);
                startActivity(signUpIntent);
            }
        });


        // TEMPORARY way to look at your created posts and your "accepted to" posts
        Button posts_TEMP = (Button) findViewById(R.id.posts_TEMP);
        posts_TEMP.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Opens menu to choose how you want to view posts
                Intent testPostView = new Intent(getApplicationContext(), ChoosePosts.class);
                startActivity(testPostView);
            }
        });

        // Links mSearchView to the actual search bar
        mSearchView = findViewById(R.id.search_bar_name);
        // Enables use of the submit button to submit search queries
        mSearchView.setSubmitButtonEnabled(true);
        // Makes this class listen for queries from mSearchBar
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Enter your destination...");

        // Links mUserDatabase to the actual Firebase database, accessing everything stored under "posts"
        mUserDatabase = FirebaseDatabase.getInstance().getReference("posts");

        // Links mResultList to the actual result list in the .xml
        mResultList = (RecyclerView) findViewById(R.id.result_list);
        // Makes the result list have a constant size
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));
    }

    // This begins the section that includes the overridden methods from SearchView.OnQueryTextListener
    @Override
    public boolean onQueryTextSubmit(String query) {
        firebaseUserSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        firebaseUserSearch(newText);
        return true;
    }
    // This ends the section that includes the overridden methods from SearchView.OnQueryTextListener


    // This function does all the work when it comes to accessing the database and retrieving the relevant information
    private void firebaseUserSearch(String searchText) {

        // Accesses the Firebase database and includes all of the "posts" that have a "to" String that starts with searchText
        // This is case-sensitive, because Firebase does not natively support non-case-sensitive queries
        // The "\uf8ff" is necessary for reasons largely unknown (it's a Firebase thing)
        Query firebaseSearchQuery = mUserDatabase.orderByChild("destination").startAt(searchText).endAt(searchText + "\uf8ff");

        // The FirebaseRecyclerAdapter is from FirebaseUI, a third-party library. It accesses the Firebase database.
        FirebaseRecyclerAdapter<SearchDriverPost, postViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SearchDriverPost, postViewHolder>(

                // The information from the Firebase database in stored in this class
                SearchDriverPost.class,
                // This is how the post information is going to be displayed
                R.layout.list_layout,
                // This is what puts the post information from the post class into the list_layout
                postViewHolder.class,
                // This is required to correctly filter out all unnecessary information from the Firebase database
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(postViewHolder viewHolder, final SearchDriverPost model, int position) {

                // Passes the post information to the postViewHolder viewHolder
                viewHolder.setDetails(getApplicationContext(), model.getdeparting_area(),
                        model.getdestination(), String.valueOf(model.getavailable_spots()), model.getdeparture_time(),
                        model.getauthor_uid());

                // When a post is clicked, move to another activity - one that contains more details on the post that was clicked
                // Information is passed to that activity
                viewHolder.setOnClickListener(new postViewHolder.ClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), PostDetails.class);
                        intent.putExtra("destination", model.getdestination());
                        intent.putExtra("departing_area", model.getdeparting_area());
                        intent.putExtra("available_spots", String.valueOf(model.getavailable_spots()));
                        intent.putExtra("departure_time", model.getdeparture_time());
                        intent.putExtra("description", model.getdescription());
                        intent.putExtra("postID", model.getpost_id());
                        intent.putExtra("name", model.getauthor_email());
                        intent.putExtra("potential_passengers", model.getpotential_passengers());
                        intent.putExtra("accepted_passengers", model.getaccepted_passengers());
                        startActivity(intent);
                    }
                });
            }
        };

        // Links the result list to this firebaseRecyclerAdapter, so everything is properly displayed
        mResultList.setAdapter(firebaseRecyclerAdapter);
    }


    // Subclass that displays the information retrieved from the Firebase database by the firebaseRecyclerAdapter
    public static class postViewHolder extends RecyclerView.ViewHolder {

        View mView;

        // Get the current user's UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uID = currentUser.getUid();

        private postViewHolder.ClickListener mClickListener;

        // Enables callbacks to be sent
        public interface ClickListener{
            public void onItemClick(View view, int position);
        }

        // This method, when used, allows whatever used it to do something when this post is clicked
        // firebaseRecyclerAdapter uses this method to move to another activity when clicked
        public void setOnClickListener(postViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
        public postViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            // When a post is clicked, the program will know exactly which post was clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

        // This method is the one that does the work required to display all of the information retrieved
        // by the firebaseRecyclerAdapter
        public void setDetails(Context ctx, String postFrom, String postTo, String postSeats, String postTime, String postAuthor) {

            // Creates types of View object references and links them to every component of list_layout
            ImageView post_profile = (ImageView) mView.findViewById(R.id.profile);
            TextView post_from = (TextView) mView.findViewById(R.id.from);
            TextView post_to = (TextView) mView.findViewById(R.id.to);
            TextView post_time = (TextView) mView.findViewById(R.id.time);
            TextView post_seats = (TextView) mView.findViewById(R.id.seats_left_num);



            // Links the types of View object references to the information retrieved by firebaseRecyclerAdapter
            //if (postProfile != null) {
                // As long as the profile picture exists, display it
                // If it doesn't exist, the default profile picture will be displayed
              //  Glide.with(ctx).load(postProfile).into(post_profile);
            //}
            post_from.setText(postFrom);
            post_to.setText(postTo);
            post_time.setText(postTime);
            post_seats.setText(postSeats);

            // Posts by you won't show up when searching the database
            // Will add more later, like "accepted posts won't show up", etc.
            if (uID.equals(postAuthor)) {
                RelativeLayout listPart = (RelativeLayout) mView.findViewById(R.id.list_part);
                listPart.getLayoutParams().height = 0;
            }
        }

>>>>>>> parent of 9a09f56... Posts with no space won't show up when searching
    }
}
