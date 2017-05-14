package com.example.winryxie.tripdiary.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.winryxie.tripdiary.AbstractDetailActivity;
import com.example.winryxie.tripdiary.ImageListAdapter;
import com.example.winryxie.tripdiary.ImageUpload;
import com.example.winryxie.tripdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class DiaryFragment extends Fragment {
    private DatabaseReference databaseReference;
    private List<ImageUpload> imgList;
    private ListView lv;
    private ImageListAdapter adapter;
    private ProgressDialog progressDialog;
    private String UserPackage;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary,container,false);
        //getActivity().setContentView(R.layout.diary_image);
        UserPackage = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        imgList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.listViewImage);
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Please wait，loading the diaries...");
        progressDialog.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserPackage = currentFirebaseUser.getUid().toString();

        databaseReference = database.getReference("image");
        databaseReference = databaseReference.child(UserPackage);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    imgList.add(img);
                }
                adapter = new ImageListAdapter(getActivity(), R.layout.diary_image, imgList);
                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent myIntent = new Intent(getActivity(), AbstractDetailActivity.class);
                myIntent.putExtra("content",imgList.get(position).content);
                myIntent.putExtra("url",imgList.get(position).url);
                myIntent.putExtra("name",imgList.get(position).name);
                myIntent.putExtra("likes", imgList.get(position).likes);
                myIntent.putExtra("likeflag", imgList.get(position).likeflag);
                myIntent.putExtra("index",position);
                startActivity(myIntent);
            }
        });
//
//        Intent i = new Intent(getActivity(),DiaryFragment.class);
//        startActivity(i);
        return view;
    }


}
