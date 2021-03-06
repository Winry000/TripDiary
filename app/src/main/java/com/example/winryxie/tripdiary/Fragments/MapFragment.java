package com.example.winryxie.tripdiary.Fragments;

import android.Manifest;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.winryxie.tripdiary.ImageUpload;
import com.example.winryxie.tripdiary.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.*;
import com.bumptech.glide.request.target.Target;

import static com.example.winryxie.tripdiary.MainUserActivity.imgList;


/**
 * Created by winryxie on 5/4/17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;
    private LayoutInflater mInflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        mInflater = inflater;
        View v = inflater.inflate(R.layout.map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                setUpMap();
            }
        });

        return v;
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setUpMap();

    }

    public void setUpMap() {

        final View marker = mInflater.inflate(R.layout.map_view_marker, null);
        List<ImageUpload> imgMapList = imgList;

//        ImageView imageView = (ImageView) marker.findViewById(R.id.num_txt);
//        Glide.with(getContext()).load("").into(imageView);
//        Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(imageView);
//        ImageView imageView = (ImageView) marker.findViewById(R.id.imageView2);
//        Glide.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        for (int i = 0; i < imgMapList.size(); i++) {
            final double latitude = imgMapList.get(i).getLat();
            final double longitude = imgMapList.get(i).getLog();
            final String img_title = imgMapList.get(i).getName();
            final String img_url = imgMapList.get(i).getUrl();
//            TextView textView = (TextView) marker.findViewById(R.id.num_txt);
//            textView.setText(img_title);
//            ImageButton imageButton = (ImageButton) marker.findViewById(R.id.num_txt);
//            imageButton.setImageURI(Uri.parse(img_url));
//
            Glide.with(getContext())
                    .load(img_url)
                    .asBitmap()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title(img_title)
                                    .snippet("Snippet")
                                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), marker, resource)))
                            );
                            return true;
                        }
                    })
                    .centerCrop()
                    .preload();

  /*          googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(img_title)
                    .snippet("Snippet")
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), marker, img_url)))); // transparent image*/

            // adding zoom for the lastest image
            double lat = imgMapList.get(imgMapList.size() - 1).getLat();
            double log = imgMapList.get(imgMapList.size() - 1).getLog();
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, log)).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
    }



    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view, Bitmap bm) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ImageView imageView = (ImageView) view.findViewById(R.id.map_photo);
        //Glide.with(view.getContext()).load(url).into(imageView);
        imageView.setImageBitmap(bm);
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


}
