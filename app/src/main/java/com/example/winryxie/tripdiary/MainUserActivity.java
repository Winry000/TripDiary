package com.example.winryxie.tripdiary;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainUserActivity extends AppCompatActivity {

    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
