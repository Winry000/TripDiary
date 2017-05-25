package com.example.winryxie.tripdiary;


import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.example.winryxie.tripdiary.Fragments.CameraFragment;
import com.example.winryxie.tripdiary.Fragments.DiaryFragment;
import com.example.winryxie.tripdiary.Fragments.FavoriateFragment;
import com.example.winryxie.tripdiary.Fragments.MapFragment;
import com.example.winryxie.tripdiary.Fragments.SearchFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainUserActivity extends AppCompatActivity  implements OnMenuItemClickListener, OnMenuItemLongClickListener {

    BottomBar bottomBar;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    public static List<ImageUpload> imgList;
    private DatabaseReference databaseReferenceImage;
    public static String UserPackage;
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final int NUMBER_OF_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initMenuFragment();

        UserPackage = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        imgList = new ArrayList<ImageUpload>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        databaseReferenceImage = database.getReference("image").child(UserPackage);

        databaseReferenceImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imgList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    img.id = snapshot.getKey();
                    imgList.add(img);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


//        bottomBar = BottomBar.attach(this,savedInstanceState);
        bottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.nav_search) {
                    SearchFragment f = new SearchFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_view, f).commit();
                } else if (menuItemId == R.id.nav_map) {
                    safeOpenMap();
                } else if (menuItemId == R.id.nav_camera) {
                    CameraFragment f = new CameraFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_view, f).commit();
                } else if (menuItemId == R.id.nav_favoriate) {
                    FavoriateFragment f = new FavoriateFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_view, f).commit();
                } else if (menuItemId == R.id.nav_diary) {
                    DiaryFragment f = new DiaryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_view, f).commit();
                }
            }
        });
//        bottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
//            @Override
//            public void onMenuTabSelected(@IdRes int menuItemId) {
//
//            }
//
//            @Override
//            public void onMenuTabReSelected(@IdRes int menuItemId) {
//
//            }
//        });

    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length == NUMBER_OF_PERMISSIONS
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openMap();
                } else {
                    showToastNeedPermissions();
                }
                break;
            default:
                showToastNeedPermissions();
                break;
        }
    }

    private void showToastNeedPermissions() {
        Toast.makeText(this, getString(R.string.need_permissions),
                Toast.LENGTH_SHORT).show();
    }


    private boolean permissionNotGranted() {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {
                android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION
        }, PERMISSIONS_REQUEST_CODE);
    }

    private void checkRuntimePermissions() {
        if (permissionNotGranted()) {
            requestPermission();
        } else {
            openMap();
        }
    }
    private void safeOpenMap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkRuntimePermissions();
        } else {
            openMap();

        }
    }

    private void openMap()
    {
        MapFragment f = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_view, f).commit();
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
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("Friends");
        send.setResource(R.drawable.icn_1);

        MenuObject addFr = new MenuObject("Edit profile");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.icn_3));
        addFr.setDrawable(bd);

        MenuObject block = new MenuObject("Sign out");
        block.setResource(R.drawable.icn_5);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(addFr);
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

        /*mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case com.example.winryxie.tripdiary.ProfileActivity.UPDATE_PROFILE_REQUEST:
                if (resultCode == RESULT_OK) {
                    SearchFragment f = new SearchFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_view, f).commit();
                }
        }


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
        if (position == 2){
            startActivity(new Intent(MainUserActivity.this, ProfileActivity.class)); //Go back to home page
            finish();
        }
        if (position == 3){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainUserActivity.this, MainActivity.class)); //Go back to home page
            finish();
        }

    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

}
