package com.example.winryxie.tripdiary.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.winryxie.tripdiary.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by winryxie on 5/4/17.
 */

public class MapFragment extends android.support.v4.app.Fragment
        implements OnMapReadyCallback{

    MapView mapView;
    GoogleMap map;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.map, container, false);

        mapView = (MapView) v.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
      /*  map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
*/
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls

        MapsInitializer.initialize(this.getActivity());


        // Updates the location and zoom of the MapView
       /* CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        map.animateCamera(cameraUpdate);*/
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
