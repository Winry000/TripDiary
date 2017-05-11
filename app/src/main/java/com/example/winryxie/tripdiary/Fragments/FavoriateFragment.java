package com.example.winryxie.tripdiary.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.winryxie.tripdiary.R;

/**
 * Created by winryxie on 5/4/17.
 */

public class FavoriateFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favoriate,container,false);
        return view;
    }

}
