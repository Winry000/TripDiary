package com.example.winryxie.tripdiary;

/**
 * Created by Dora on 5/11/17.
 */
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.winryxie.tripdiary.Fragments.SearchFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.example.winryxie.tripdiary.Model.User;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private Button SaveButton;
    private Button ChangePhoto;
    private EditText editTextNickName;
    private EditText editTextPhoneNumber;
    private EditText editTextSignature;
    private EditText editTextPassword;
    private EditText editTextPasswordAgain;
    private EditText editTextOldPassword;
    private TextView Email;
    private CircleImageView ProfileImage;
    private StorageReference storageReference;
    private DatabaseReference databaseReferenceUser;
    private String emailAddress;
    public static final String FB_DATABASE_PATH = "user";
    public static final String FB_STORAGE_PATH = "profile/";
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Query queryUser;
    private Uri headUri;
    public static final int REQUEST_CODE = 1234;
    private FirebaseUser currentFirebaseUser;
    public static final int UPDATE_PROFILE_REQUEST = 5555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        initToolbar();

        SaveButton = (Button) findViewById(R.id.button_save_profile);
        ChangePhoto = (Button) findViewById(R.id.btn_edit_profile_iamge);
        ProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        editTextNickName = (EditText) findViewById(R.id.profile_nick_name);
        editTextPhoneNumber = (EditText)findViewById(R.id.profile_phonenumber);
        editTextSignature = (EditText) findViewById(R.id.profile_signature);
        editTextPassword = (EditText) findViewById(R.id.profile_password);
        editTextPasswordAgain = (EditText) findViewById(R.id.profile_password_again);
        editTextOldPassword = (EditText) findViewById(R.id.profile_old_password);
        Email = (TextView) findViewById(R.id.profile_email);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        emailAddress = currentFirebaseUser.getEmail();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReferenceUser = database.getReference(FB_DATABASE_PATH);
        Email.setText(emailAddress);
        queryUser = databaseReferenceUser.orderByChild("emailAddress").equalTo(emailAddress);
        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    Log.i("DEBUG", user.getEmailAddress() + " name "+user.getName() +  "  sign " + user.getSignature() + " Url "+ user.getUrl() +" CityNumber "+user.getCityNumber() + " diaryNumber" + user.getDiaryNumber());
                    editTextNickName.setText(user.getName());
                    if(!user.getSignature().equals(""))
                        editTextSignature.setText(user.getSignature());
                    editTextPhoneNumber.setText(user.getPhoneNumber());
                    if(!user.getUrl().equals("")){
                        Glide.with(getApplicationContext()).load(user.getUrl()).into(ProfileImage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        SaveButton.setOnClickListener(this);
        ChangePhoto.setOnClickListener(this);

    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationIcon(R.drawable.btn_back_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ProfileActivity.this, MainUserActivity.class)); //Go back to home page
                Intent myIntent = new Intent(ProfileActivity.this, MainUserActivity.class);
                startActivityForResult(myIntent,UPDATE_PROFILE_REQUEST);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == SaveButton) {
            UpdateUser();
        }
        if (v == ChangePhoto) {
            changeHeader(v);
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void changeHeader(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    headUri = data.getData();

                    Log.i("DEBUG", "headUri " + headUri.toString());

                    CropImage.activity(headUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(this);
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    Bitmap bm = null;

                    if (resultCode == RESULT_OK) {
                        Uri resultUri = data.getData();
                        Log.i("DEBUG", "resultUri " + resultUri.toString());

                        try {
                            bm = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), resultUri);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i("DEBUG", "error in result_OK");
                        }
                        ProfileImage.setImageBitmap(bm);
                        storeProfile(resultUri);

                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                        Log.i("DEBUG", error.toString());
                    }

                }
        }

    }

    private void storeProfile(Uri resultUri){

        final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setTitle("Upload Profile Image");
        dialog.show();

        //get the storage reference
        Log.i("DEBUG","resultUri in storeProfile is "+resultUri.toString());
        StorageReference ref = storageReference.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(resultUri));
        Log.i("DEBUG","resultUri in storeProfile is "+resultUri.toString());
        ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //when success dismiss dialog;
                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                final String profileurl = downloadUrl.toString();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Profile Image Uploaded", Toast.LENGTH_SHORT).show();

                queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().child("url").setValue(profileurl);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Upload " + (int) progress + " %");
                    }
                });

    }
    private void UpdateUser() {
        final String nickname = editTextNickName.getText().toString().trim();
        final String phonenumber = editTextPhoneNumber.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String repassword = editTextPasswordAgain.getText().toString().trim();
        final String signature = editTextSignature.getText().toString().trim();
        final String oldPassword = editTextOldPassword.getText().toString().trim();

        if (!password.equals(repassword)) {
            Toast.makeText(this,"Password doesn't match, please try again..",Toast.LENGTH_SHORT).show();
            return;
        }

        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().child("nickname").setValue(nickname);
                        snapshot.getRef().child("phonenumber").setValue(phonenumber);
                        snapshot.getRef().child("signature").setValue(signature);
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(repassword)) {

            //Intent myIntent = new Intent(ProfileActivity.this, MainUserActivity.class);
            //startActivityForResult(myIntent,UPDATE_PROFILE_REQUEST);
            //startActivity(new Intent(ProfileActivity.this, MainUserActivity.class)); //Go back to home page
            Toast.makeText(this,"Profile updated successfully!",Toast.LENGTH_SHORT).show();
            //finish();
            return;

        }else{
            if(TextUtils.isEmpty(oldPassword)){
                Toast.makeText(this,"Please enter your old password",Toast.LENGTH_SHORT).show();
                return;
            }
// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
            AuthCredential credential = EmailAuthProvider
                    .getCredential(emailAddress, oldPassword);

// Prompt the user to re-provide their sign-in credentials
            currentFirebaseUser.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                currentFirebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(ProfileActivity.this, MainActivity.class)); //Go back to home page
                                            finish();
                                        } else {
                                            Log.d("DEBUG", "Error password not updated");
                                            Toast.makeText(getApplicationContext(),"Old password is incorrect!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Log.d("DEBUG", "Error auth failed");
                                Toast.makeText(getApplicationContext(),"Old password is incorrect!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }

    }
}
