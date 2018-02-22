package chub45.benson.hitch;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.net.Uri;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.database.Cursor;
import android.util.Log;

import java.util.HashMap;

import com.google.firebase.database.ChildEventListener;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 1;

    Uri imageHoldUri = null;

    ImageView profilePicIV;

    TextView fullNameTV, usernameTV, emailTV, stateTV, cityTV;

    final private HitchDatabase db = new HitchDatabase();
    DatabaseReference dbRef;

    String userIud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbRef = FirebaseDatabase.getInstance().getReference();

        profilePicIV = (ImageView)findViewById(R.id.profilePicIV);
        fullNameTV = (TextView)findViewById(R.id.fullNameTV);
        usernameTV = (TextView)findViewById(R.id.usernameTV);
        emailTV = (TextView)findViewById(R.id.emailTV);
        stateTV = (TextView)findViewById(R.id.stateTV);
        cityTV = (TextView)findViewById(R.id.cityTV);

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        //User user = get user from database using fbUser

        if (fbUser != null) {
            // Name, email address, and profile photo Url
            fullNameTV.setText(fbUser.getDisplayName());
            emailTV.setText("Email: " + fbUser.getEmail());

            //displayData(getQuery(dbRef, fbUser));
            Query query =  dbRef.child("users").child(fbUser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //User user = dataSnapshot.getValue(User.class);
                        Log.wtf("mytag", "dataSnapshot exists");
                        //HashMap<String, String> userQuery = (HashMap<String, String>) dataSnapshot.getValue();

                        getFullNameTV().setText(dataSnapshot.getValue(User.class).getFullName());
                        getStateTV().setText("State: " + dataSnapshot.getValue(User.class).getState());
                        getCityTV().setText("City: " + dataSnapshot.getValue(User.class).getCity());
                    }
                    else {
                        Log.wtf("hello", "dataSnapshot does not exist");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("[Database Error]", databaseError.getMessage());
                }
            });
        }

        /*
        profilePicIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePic();
            }
        });
        */

        findViewById(R.id.editProfileBtn).setOnClickListener(this);
    }

    private Query getQuery(DatabaseReference ref, FirebaseUser user) {
        Query query =  ref.child("uid").equalTo(user.getUid());
        return query;
    }

    private void displayData(Query query) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.wtf("hello", "inner class is called");
                if (dataSnapshot.exists()) {
                    //User user = dataSnapshot.getValue(User.class);
                    Log.wtf("mytag", "dataSnapshot exists");
                    //HashMap<String, String> userQuery = (HashMap<String, String>) dataSnapshot.getValue();
                    if (dataSnapshot.getValue(User.class) != null) {
                        Log.wtf("anothertag", "userQuery != null");
                        getFullNameTV().setText(dataSnapshot.getValue(User.class).getFullName());
                        getStateTV().setText(dataSnapshot.getValue(User.class).getState());
                        getCityTV().setText(dataSnapshot.getValue(User.class).getCity());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("[Database Error]", databaseError.getMessage());
            }

        });


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
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
        }
    }

    /*
    private void chooseProfilePic() {
        //Displays dialog to choose pic from camera or gallery
        final CharSequence[] choices = {"Take a Photo", "Choose From Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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

    private void cameraIntent() {
        //Camera chosen
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        //Gallery chosen
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Save uri from gallery
        if(requestCode == SELECT_FILE) //&& resultCode == RESULT_OK)
        {
            Uri selectedImg;
            if (data != null) {
                selectedImg = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImg,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                getProfileIV().setImageBitmap(BitmapFactory.decodeFile(picturePath));

            }


            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);


        }
        else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ) {
            //Save uri from camera
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
            Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();
        }

        //Image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                profilePicIV.setImageURI(imageHoldUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    */
}