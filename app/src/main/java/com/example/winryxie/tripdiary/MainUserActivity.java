package com.example.winryxie.tripdiary;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.winryxie.tripdiary.Fragments.CameraFragment;
import com.example.winryxie.tripdiary.Fragments.DiaryFragment;
import com.example.winryxie.tripdiary.Fragments.FavoriateFragment;
import com.example.winryxie.tripdiary.Fragments.MapFragment;
import com.example.winryxie.tripdiary.Fragments.SearchFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initMenuFragment();

//        bottomBar = BottomBar.attach(this,savedInstanceState);
        bottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.nav_search) {
                    SearchFragment f = new SearchFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_view, f).commit();
                } else if (menuItemId == R.id.nav_map) {
                    MapFragment f = new MapFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_view, f).commit();
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

        MenuObject like = new MenuObject("Edit profile");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icn_2);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("Add to friends");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.icn_3));
        addFr.setDrawable(bd);

        MenuObject addFav = new MenuObject("Share");
        addFav.setResource(R.drawable.icn_4);

        MenuObject block = new MenuObject("Sign out");
        block.setResource(R.drawable.icn_5);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        menuObjects.add(block);
        return menuObjects;
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
       // mToolbar.setNavigationIcon(R.drawable.btn_back);

        /*mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        mToolBarTextView.setText("Trip Diary");
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
        if (position == 2){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainUserActivity.this, MainActivity.class)); //Go back to home page
            finish();
        }
        if (position == 5){
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
