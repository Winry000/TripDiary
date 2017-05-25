package com.example.winryxie.tripdiary.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.winryxie.tripdiary.AdapterExample;
import com.example.winryxie.tripdiary.ImageUpload;
import com.example.winryxie.tripdiary.Model.User;
import com.example.winryxie.tripdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static com.example.winryxie.tripdiary.MainUserActivity.UserPackage;
/**
 * Created by winryxie on 5/4/17.
 */

public class FavoriateFragment extends Fragment implements View.OnClickListener {
    DatabaseReference databaseReference;
    private AdapterExample adapter;
    public static List<ImageUpload> imgList_favor = new ArrayList<>();
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private Query queryUser;
    private DatabaseReference databaseReferenceUser;
    public static final String FB_DATABASE_PATH = "user";
    private User user;
    private Map<String, Boolean> favorite = new HashMap();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favoriate, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("image");
//        databaseReference = databaseReference.child(UserPackage);
        adapter = new AdapterExample(getContext(), imgList_favor);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_favor_view);
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        databaseReferenceUser = database.getReference(FB_DATABASE_PATH);
        String emailAddress = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        queryUser = databaseReferenceUser.orderByKey().equalTo(UserPackage);

        queryUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);
                    favorite = user.getFavoriteList();
                    if (!favorite.equals(null)) {
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                imgList_favor.clear();
                                int count = 0;
                                Log.i("DEBUG", "count is" + Integer.toString(count));
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    for (DataSnapshot data : snapshot.getChildren()) {
                                        if (count == 10) break;
                                        ImageUpload img = data.getValue(ImageUpload.class);
                                        if (favorite.containsKey(data.getKey())) {
                                            Log.i("DEBUG", "the key is " + data.getKey());
                                            img.id = data.getKey();
                                            imgList_favor.add(img);
                                            count++;
                                        }
                                        Log.i("DEBUG", "count is" + Integer.toString(count));
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                //recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }


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
