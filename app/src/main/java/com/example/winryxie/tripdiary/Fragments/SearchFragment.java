package com.example.winryxie.tripdiary.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.example.winryxie.tripdiary.AlbumsAdapter;
import com.example.winryxie.tripdiary.ImageUpload;
import com.example.winryxie.tripdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.winryxie.tripdiary.Fragments.CameraFragment.REQUEST_CODE;
import com.google.firebase.database.Query;
import com.example.winryxie.tripdiary.Model.User;
import android.widget.ImageView;
import android.util.Log;

/**
 * Created by winryxie on 5/4/17.
 */

public class SearchFragment extends Fragment implements View.OnClickListener{
    private ImageButton imageButton;
    public final static String CROP_IMAGE_ACTIVITY_REQUEST_CODE = "1234";
    private String UserPackage;
    public static List<ImageUpload> imgList;
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceUser;
    private String emailAddress;
    private TextView username;
    private TextView usersign;
    private TextView userdiaryNo;
    private TextView usercityNo;
    private TextView usercountryNo;
    private ImageView userProfile;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search,container,false);
/*      TextView username = (TextView)view.findViewById(R.id.username);
        UserPackage = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        username.setText(user.getEmail());
        imageButton = (ImageButton) view.findViewById(R.id.image_header);
        imageButton.setOnClickListener(this);*/

        username = (TextView) view.findViewById(R.id.UserProfileName);
        usersign = (TextView) view.findViewById(R.id.UserProfileSign);
        userdiaryNo = (TextView) view.findViewById(R.id.UserProfileDiaryNo);
        usercityNo = (TextView) view.findViewById(R.id.UserProfileCity);
        usercountryNo = (TextView) view.findViewById(R.id.UserProfileCountry);
        userProfile = (ImageView) view.findViewById(R.id.ivUserProfilePhoto);
        imgList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserPackage = currentFirebaseUser.getUid().toString();
        emailAddress = currentFirebaseUser.getEmail();
        databaseReferenceUser = database.getReference("user");
        Query queryUser = databaseReferenceUser.orderByChild("emailAddress").equalTo(emailAddress);
        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    Log.i("DEBUG", user.getEmailAddress() + " name "+user.getName() +  "  sign " + user.getSignature() + " Sign "+ user.getUrl() +" CityNumber "+user.getCityNumber() + " diaryNumber" + user.getDiaryNumber());
                    username.setText(user.getName());
                    if(user.getSignature() != "")
                        usersign.setText(user.getSignature());
                    else usersign.setText("What do you think?");
                    userdiaryNo.setText(Integer.toString(user.getDiaryNumber()));
                    usercityNo.setText(Integer.toString(user.getCityNumber()));
                    usercountryNo.setText(Integer.toString(user.getCountryNumber()));
                    if(user.url != ""){
                        Glide.with(getContext()).load(user.getUrl()).override(60, 60).into(userProfile);
                    }
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference = database.getReference("image");
        databaseReference = databaseReference.child(UserPackage);
        adapter = new AlbumsAdapter(getContext(), imgList);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    imgList.add(img);
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    //change image header
    @Override
    public void onClick(View v) {
        if (v == imageButton) {
            changeHeader(v);
        }
    }
        //recyclerview listen to click
//        int position = recyclerView.indexOfChild(v);
//        Intent myIntent = new Intent(getActivity(), AbstractDetailActivity.class);
//        myIntent.putExtra("content",imgList.get(position).content);
//        myIntent.putExtra("url",imgList.get(position).url);
//        myIntent.putExtra("name",imgList.get(position).name);
//        startActivity(myIntent);
// }
   public void changeHeader(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"),REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri headUri = data.getData();
            CropImage.activity(headUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getActivity());
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Bitmap bm = null;
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageButton.setImageBitmap(bm);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }






}
