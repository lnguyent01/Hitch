package chub45.benson.hitch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.net.Uri;
import android.widget.Toast;

import android.database.Cursor;
import android.util.Log;
import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 1;

    Uri imageHoldUri = null;

    ImageView profilePicIV;

    TextView fullNameTV, usernameTV, emailTV, stateTV, cityTV;

    final private HitchDatabase db = new HitchDatabase();
    DatabaseReference dbRef;

    String userIud;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference();

        profilePicIV = (ImageView)view.findViewById(R.id.profilePicIV);
        fullNameTV = (TextView)view.findViewById(R.id.fullNameTV);
        usernameTV = (TextView)view.findViewById(R.id.usernameTV);
        emailTV = (TextView)view.findViewById(R.id.emailTV);
        stateTV = (TextView)view.findViewById(R.id.stateTV);
        cityTV = (TextView)view.findViewById(R.id.cityTV);
        view.findViewById(R.id.editProfileBtn).setOnClickListener(this);

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        if (fbUser != null) {
            Query query =  dbRef.child("users").child(fbUser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        fullNameTV.setText(dataSnapshot.getValue(User.class).getFullName());
                        stateTV.setText("State: " + dataSnapshot.getValue(User.class).getState());
                        cityTV.setText("City: " + dataSnapshot.getValue(User.class).getCity());
                        usernameTV.setText("@"+ dataSnapshot.getValue(User.class).getUsername());
                        emailTV.setText("Email: " + fbUser.getEmail());
                    }
                    else {
                        Log.wtf("mytag", "dataSnapshot does not exists");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("[Database Error]", databaseError.getMessage());
                }
            });
        }

        profilePicIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chooseProfilePic();
            }
        });

        return view;
    }

    ImageView getProfileIV() {
        return profilePicIV;
    }

    public String getUserIud() { return userIud; }

    public void setUserIud(String userIud) { this.userIud = userIud; }

    public TextView getFullNameTV() { return fullNameTV; }

    public TextView getStateTV() { return stateTV; }

    public TextView getCityTV() { return cityTV; }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.editProfileBtn:
                ProfileFragment.this.startActivity(new Intent(getActivity(), EditProfileActivity.class));
                break;
        }
    }

    private void chooseProfilePic() {
        //Displays dialog to choose pic from camera or gallery
        final CharSequence[] choices = {"Take a Photo", "Choose From Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");

        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                if (choices[choice].equals("Take a Photo")) {
                    cameraIntent();
                }
                else if (choices[choice].equals("Choose from Library")) {
                    galleryIntent();
                }
                else if (choices[choice].equals("Cancel")); {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    //Camera chosen
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //Gallery chosen
    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setFlags(0);
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.wtf("TEST", "ON ACTIVITY CALLED");
        //Save uri from gallery
        if(requestCode == SELECT_FILE) //&& resultCode == RESULT_OK)
        {
            Log.wtf("TEST1", "SELECT FILE RUNS");
            Uri selectedImg;
            if (data != null) {
                Log.wtf("TEST2", "DATA != NULL");
                selectedImg = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImg,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                getProfileIV().setImageBitmap(BitmapFactory.decodeFile(picturePath));

            }
            else
                Log.wtf("TEST2", "DATA == NULL");


            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getActivity());


        }
        else if ( requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK ) {
            //Save uri from camera
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getActivity());
            Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
        }

        //Image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                imageHoldUri = result.getUri();

                profilePicIV.setImageURI(imageHoldUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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
}
