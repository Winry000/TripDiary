package com.example.winryxie.tripdiary.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.winryxie.tripdiary.GPSTracker;
import com.example.winryxie.tripdiary.ImageUpload;
import com.example.winryxie.tripdiary.PlaceSearchDialog;
import com.example.winryxie.tripdiary.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mapzen.places.api.LatLng;
import com.mapzen.places.api.Place;
import com.mapzen.places.api.ui.PlacePicker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * Created by winryxie on 5/4/17.
 */

public class CameraFragment extends Fragment implements View.OnClickListener,  GoogleApiClient.OnConnectionFailedListener{

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ImageView imageView;
    private EditText editText;
    private EditText editContent;
    private EditText editLocation;
    private Uri imgUri;
    private ImageButton selectButton;
    private Button shareButton;
    private Button pickPlaceButton;
    private double log = 0;
    private double lat = 0;
    private double currentlog = 0;
    private double currentlat = 0;
    private String location;
    protected GoogleApiClient mGoogleApiClient;

    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static boolean isPickingPlace = false;

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final int NUMBER_OF_PERMISSIONS = 1;
    private String UserPackage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .build();

        Log.d("DEBUG", "onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.camera,container,false);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        editText =(EditText) view.findViewById(R.id.text_diary);
        editContent = (EditText) view.findViewById(R.id.text_diary_content);
        editLocation = (EditText) view.findViewById(R.id.diary_location);

        selectButton = (ImageButton)view.findViewById(R.id.image_diary);
        shareButton = (Button) view.findViewById(R.id.button_share);
        pickPlaceButton = (Button) view.findViewById(R.id.location_pick);
        selectButton.setOnClickListener(this);
        pickPlaceButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        UserPackage = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        editLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    showPlacePickerDialog();
                }
                return false;
            }
        });

        return view;
    }

    private void showPlacePickerDialog() {

        PlaceSearchDialog placeSearchDialog = new PlaceSearchDialog.Builder(this.getContext())
                //.setHeaderImage(R.drawable.dialog_header)
                .setLocationNameListener(new PlaceSearchDialog.LocationNameListener() {
                    @Override
                    public void locationName(String locationName) {
                        editLocation.setText(locationName);

                    }
                    public void placeId(String placeId) {
                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                        Log.i("DEBUG", "Called getPlaceById to get Place details for " + placeId);
                    }

                })
                .build();
        placeSearchDialog.show();
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("DEBUG", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final com.google.android.gms.location.places.Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            lat = place.getLatLng().latitude;
            log = place.getLatLng().longitude;
            location = place.getName().toString();

            Log.i("DEBUG", "Place details received: " + place.getName());

            places.release();
        }
    };

    public void Select_Click(View v) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"),REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        isPickingPlace = false;
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this.getContext(), data);
                    Log.i("DEBUG", Double.toString(place.getLatLng().getLatitude()) + " " +Double.toString(place.getLatLng().getLongitude()));
                    log = place.getLatLng().getLongitude();
                    lat = place.getLatLng().getLatitude();
                    location = place.getName().toString();
                    editLocation.setText(location);

                }
                break;
            case REQUEST_CODE:
                if ( resultCode == RESULT_OK) {
                    imgUri = data.getData();
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imgUri);
                        selectButton.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @SuppressWarnings("VisibleForTests")
    public void Share_Click (View v) {
        if (imgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this.getContext());
            dialog.setTitle("Share Diary...");
            dialog.show();

            //get the storage reference
            StorageReference ref = storageReference.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //when success dismiss dialog;
                    dialog.dismiss();
                    Toast.makeText(getContext().getApplicationContext(), "Diary Uploaded", Toast.LENGTH_SHORT).show();
                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());

                    ImageUpload imageupload = new ImageUpload(editText.getText().toString(), editContent.getText().toString(), taskSnapshot.getDownloadUrl().toString(), editLocation.getText().toString(), log,lat,formattedDate );
                    //save the imginfo to firedatabase

                    databaseReference = databaseReference.child(UserPackage);
                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(imageupload);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getContext().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Upload " + (int)progress + " %" );
                        }
                    });


        }
        else {
            Toast.makeText(getContext().getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
        }
    }

    public void pick_place (View v) {

        if (!isPickingPlace) {
            safeLaunchPicker();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == selectButton) {
            Select_Click(v);
        }
        if(v == shareButton) {
            Share_Click(v);
        }
        if(v == pickPlaceButton) {
            pick_place(v);
        }
    }


    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length == NUMBER_OF_PERMISSIONS
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchPlacePicker();
                } else {
                    showToastNeedPermissions();
                }
                break;
            default:
                showToastNeedPermissions();
                break;
        }
    }



    private boolean permissionNotGranted() {
        return (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this.getActivity(), new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        }, PERMISSIONS_REQUEST_CODE);
    }

    private void checkRuntimePermissions() {
        if (permissionNotGranted()) {
            requestPermission();
        } else {
            launchPlacePicker();
        }
    }
    private void safeLaunchPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkRuntimePermissions();
        } else {
            launchPlacePicker();
        }
    }

    private void launchPlacePicker() {
        GPSTracker gps = new GPSTracker(this.getContext());
        if(gps.canGetLocation()){
            currentlat = gps.getLatitude(); // returns latitude
            currentlog = gps.getLongitude(); // returns longitude
        }
        LatLng southwest = new LatLng(currentlat + 1.0000,currentlog + 1.0000);
        LatLng northeast = new LatLng(currentlat - 1.0000, currentlog - 1.0000);

        Intent intent = new PlacePicker.IntentBuilder()
                //.setLatLngBounds(new LatLngBounds(southwest, northeast))
                .build(this.getActivity());

        isPickingPlace = true;
        gps.stopUsingGPS();
        startActivityForResult(intent, PLACE_PICKER_REQUEST);

    }

    private void showToastNeedPermissions() {
        Toast.makeText(this.getContext(), getString(R.string.need_permissions),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e("DEBUG", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this.getContext(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

}


