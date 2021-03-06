package com.example.winryxie.tripdiary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import com.example.winryxie.tripdiary.Model.User;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;

import static com.example.winryxie.tripdiary.Fragments.SearchFragment.user;
import static com.example.winryxie.tripdiary.MainUserActivity.UserPackage;


public class AbstractDetailActivity extends AppCompatActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {


    public View container;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    private int num = 0;
    private String UserPackage;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceUser;
    private String fromFragment;
    private int currentLikenumber = 0;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_abstract_detail);
        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initMenuFragment();
        TextView imageTitle = (TextView) findViewById(R.id.title);
        TextView imageContent = (TextView) findViewById(R.id.description);
        ImageView imageView = (ImageView) findViewById(R.id.photo);
        TextView diarydate = (TextView) findViewById(R.id.diary_date);
        TextView diarylocation = (TextView) findViewById(R.id.diary_location);
        final ImageButton likeButton =(ImageButton) findViewById(R.id.like_button);
        final TextView likeCount = (TextView) findViewById(R.id.like_count);
        database = FirebaseDatabase.getInstance();
        UserPackage = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        container = findViewById(R.id.container);

        databaseReference = database.getReference("image");
        //databaseReference = databaseReference.child(UserPackage);
        databaseReferenceUser = database.getReference("user");
        HashMap<String, Boolean> favoritePeople = new HashMap();

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imageTitle.setText(bundle.getString("name"));
            imageContent.setText(bundle.getString("content"));
            diarydate.setText(bundle.getString("diaryDate"));
            diarylocation.setText(bundle.getString("location"));
            Glide.with(this).load(bundle.getString("url")).override(320, 300).into(imageView);
            fromFragment = bundle.getString("from");
            favoritePeople = (HashMap<String, Boolean>) getIntent().getSerializableExtra("favoriteBy");
            num = bundle.getInt("like");
            likeCount.setText(Integer.toString(num));

        }

        if(!favoritePeople.equals(null)){
            if(favoritePeople.containsKey(UserPackage)){
                likeButton.setImageResource(R.drawable.like);
                likeButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {
                        likeButton.setImageResource(R.drawable.likebefore);
                        num = bundle.getInt("like") - 1;
                        likeCount.setText(Integer.toString(num));

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    for (DataSnapshot data: snapshot.getChildren()) {
                                        if (data.getKey().equals(bundle.getString("id"))) {
                                            DatabaseReference ref = databaseReference.child(snapshot.getKey()).child(data.getKey());
                                            int likenumber = data.child("like").getValue(Integer.class);
                                            Log.i("DEBUG", Integer.toString(likenumber));
                                            ref.child("like").setValue(likenumber - 1);
                                            ref.child("favoriteBy").child(UserPackage).removeValue();
                                            updateLikeNumberMinus(snapshot.getKey(), data.getKey());
                                            break;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });

                    }
                });


            }else{
                likeButton.setImageResource(R.drawable.likebefore);
                likeButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {
                        likeButton.setImageResource(R.drawable.like);
                        num = bundle.getInt("like") + 1;
                        likeCount.setText(Integer.toString(num));

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    for (DataSnapshot data: snapshot.getChildren()) {
                                        if (data.getKey().equals(bundle.getString("id"))) {
                                            DatabaseReference ref = databaseReference.child(snapshot.getKey()).child(data.getKey());
                                            int likenumber = data.child("like").getValue(Integer.class);
                                            Log.i("DEBUG", Integer.toString(likenumber));
                                            ref.child("like").setValue(likenumber + 1);
                                            ref.child("favoriteBy").child(UserPackage).setValue(true);
                                            updateLikeNumberPlus(snapshot.getKey(),data.getKey());
                                            break;
                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });


                    }
                });
            }
        }else{
            likeButton.setImageResource(R.drawable.likebefore);
            likeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    likeButton.setImageResource(R.drawable.like);
                    num = bundle.getInt("like") + 1;
                    likeCount.setText(Integer.toString(num));

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                for (DataSnapshot data: snapshot.getChildren()) {
                                    if (data.getKey().equals(bundle.getString("id"))) {
                                        DatabaseReference ref = databaseReference.child(snapshot.getKey()).child(data.getKey());
                                        int likenumber = data.child("like").getValue(Integer.class);
                                        Log.i("DEBUG", Integer.toString(likenumber));
                                        ref.child("like").setValue(likenumber + 1);
                                        ref.child("favoriteBy").child(UserPackage).setValue(true);
                                        updateLikeNumberPlus(snapshot.getKey(),data.getKey());
                                        break;
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });


                }
            });
        }


    }


    private void updateLikeNumberPlus(String userid, String iamgeid){

        Query queryUser = databaseReferenceUser.orderByKey().equalTo(userid);
        final String imageID = iamgeid;

        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    currentLikenumber = user.getCityNumber();
                    snapshot.child("cityNumber").getRef().setValue(currentLikenumber + 1);
                    databaseReferenceUser.child(UserPackage).child("favoriteList").child(imageID).setValue(true);
                   //databaseReferenceUser.child(userID).child("cityNumber").setValue(currentLikenumber + 1);
                    //
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateLikeNumberMinus(String userid, String iamgeid){

        Query queryUser = databaseReferenceUser.orderByKey().equalTo(userid);
        final String imageID = iamgeid;
        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    currentLikenumber = user.getCityNumber();
                    snapshot.child("cityNumber").getRef().setValue(currentLikenumber - 1);

                    //databaseReferenceUser.child(snapshot.getKey()).child("cityNumber").setValue(currentLikenumber - 1);
                    databaseReferenceUser.child(UserPackage).child("favoriteList").child(imageID).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {


        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject like = new MenuObject("Share");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icn_2);
        like.setBitmap(b);

        MenuObject addFav = new MenuObject("Add to favorites");
        addFav.setResource(R.drawable.icn_4);

        MenuObject block = new MenuObject("Sign out");
        block.setResource(R.drawable.icn_5);

        menuObjects.add(close);
        menuObjects.add(like);
        menuObjects.add(addFav);
        menuObjects.add(block);
        return menuObjects;
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setLogo(R.drawable.logotitle);
        mToolbar.setNavigationIcon(R.drawable.btn_back_toolbar);
        mToolbar.setNavigationIcon(R.drawable.btn_back_toolbar);



        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (fromFragment){
                    case "CameraFragment":
                        Intent myIntent2 = new Intent(AbstractDetailActivity.this, MainUserActivity.class);
                        startActivity(myIntent2);
                        finish();
                    default:
                        onBackPressed();
                }

            }
        });
        
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            finish();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if (position == 3){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AbstractDetailActivity.this, MainActivity.class)); //Go back to home page
            finish();
        }

    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }



//    private void colorize(Bitmap photo) {
//        Palette palette = Palette.generate(photo);
//        applyPalette(palette);
//    }
//
//    public void applyPalette(Palette palette) {
//        Resources res = getResources();
//
//        container.setBackgroundColor(palette.getDarkMutedColor(res.getColor(R.color.default_dark_muted)));
//
//        TextView titleView = (TextView) findViewById(R.id.title);
//        titleView.setTextColor(palette.getVibrantColor(res.getColor(R.color.default_vibrant)));
//
//        TextView descriptionView = (TextView) findViewById(R.id.description);
//        descriptionView.setTextColor(palette.getLightVibrantColor(res.getColor(R.color.default_light_vibrant)));
//
////        colorButton(R.id.info_button, palette.getDarkMutedColor(res.getColor(R.color.default_dark_muted)),
////                palette.getDarkVibrantColor(res.getColor(R.color.default_dark_vibrant)));
////        colorButton(R.id.star_button, palette.getMutedColor(res.getColor(R.color.default_muted)),
////                palette.getVibrantColor(res.getColor(R.color.default_vibrant)));
//
////        AnimatedPathView star = (AnimatedPathView) findViewById(R.id.star_container);
////        star.setFillColor(palette.getVibrantColor(R.color.default_vibrant));
////        star.setStrokeColor(palette.getLightVibrantColor(res.getColor(R.color.default_light_vibrant)));
//    }
}


