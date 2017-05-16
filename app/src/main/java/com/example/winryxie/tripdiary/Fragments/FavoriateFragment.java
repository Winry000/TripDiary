package com.example.winryxie.tripdiary.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.winryxie.tripdiary.AdapterExample;
import com.example.winryxie.tripdiary.ImageUpload;
import com.example.winryxie.tripdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by winryxie on 5/4/17.
 */

public class FavoriateFragment extends Fragment implements View.OnClickListener {
    DatabaseReference databaseReference;
    private AdapterExample adapter;
    public static List<ImageUpload> imgList_favor = new ArrayList<>();
    private RecyclerView recyclerView;
    private String UserPackage;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favoriate, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("image");
        UserPackage = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
//        databaseReference = databaseReference.child(UserPackage);
        adapter = new AdapterExample(getContext(), imgList_favor);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_favor_view);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imgList_favor.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        ImageUpload img = data.getValue(ImageUpload.class);
                        if (img.likeflag == true) {
                            img.id = data.getKey();
                            imgList_favor.add(img);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {

    }
}
