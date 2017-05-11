package com.example.winryxie.tripdiary;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary,container,false);
        //getActivity().setContentView(R.layout.diary_image);
        imgList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.listViewImage);
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Please wait load the diaries...");
        progressDialog.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference(CameraFragment.FB_DATABASE_PATH);


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
//
//        Intent i = new Intent(getActivity(),DiaryFragment.class);
//        startActivity(i);
        return view;
    }


}
