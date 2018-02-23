package chub45.benson.hitch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // This is where users type in what they want to search
    private SearchView mSearchView;

    // This is what displays all posts relevant to what the user searched
    private RecyclerView mResultList;

    // This is what accesses the Firebase Database
    private DatabaseReference mUserDatabase;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageButton addPostButton = (ImageButton) view.findViewById(R.id.addPostButton);
        addPostButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Opens add post activity for users to add a post by entering information
                Intent addPostIntent = new Intent(getContext(), AddPostActivity.class);
                startActivity(addPostIntent);
            }
        });

        // Way to look at your created posts and your "accepted to" posts
        Button ridesButton = (Button) view.findViewById(R.id.rides_button);
        ridesButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Opens menu to choose how you want to view posts
                Intent testPostView = new Intent(getContext(), ChoosePosts.class);
                startActivity(testPostView);
            }
        });

// Links mSearchView to the actual search bar
        mSearchView = view.findViewById(R.id.search_bar_name);
// Enables use of the submit button to submit search queries
        mSearchView.setSubmitButtonEnabled(true);
// Makes this class listen for queries from mSearchBar
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Enter your destination...");

// Links mUserDatabase to the actual Firebase database, accessing everything stored under "posts"
        mUserDatabase = FirebaseDatabase.getInstance().getReference("posts");

// Links mResultList to the actual result list in the .xml
        mResultList = (RecyclerView) view.findViewById(R.id.result_list);
// Makes the result list have a constant size
        mResultList.setHasFixedSize(true);
        LinearLayoutManager mLayout= new LinearLayoutManager(getContext());
        mResultList.setLayoutManager(mLayout);
        mLayout.setOrientation(LinearLayoutManager.VERTICAL);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                viewHolder.setDetails(getActivity().getApplicationContext(), model.getdeparting_area(),
                        model.getdestination(), String.valueOf(model.getavailable_spots()), model.getdeparture_time(),
                        model.getauthor_uid());

                // When a post is clicked, move to another activity - one that contains more details on the post that was clicked
                // Information is passed to that activity
                viewHolder.setOnClickListener(new postViewHolder.ClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, PostDetails.class);
                        intent.putExtra("destination", model.getdestination());
                        intent.putExtra("departing_area", model.getdeparting_area());
                        intent.putExtra("available_spots", String.valueOf(model.getavailable_spots()));
                        intent.putExtra("departure_time", model.getdeparture_time());
                        intent.putExtra("description", model.getdescription());
                        intent.putExtra("postID", model.getpost_id());
                        intent.putExtra("name", model.getauthor_email());
                        intent.putExtra("potential_passengers", model.getpotential_passengers());
                        intent.putExtra("accepted_passengers", model.getaccepted_passengers());
                        context.startActivity(intent);
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

            // Posts with a size of 0 (no space) won't show up when searching the database
            if (postSeats.equals("0")) {
                RelativeLayout listPart = (RelativeLayout) mView.findViewById(R.id.list_part);
                listPart.getLayoutParams().height = 0;
            }
        }
    }
}
