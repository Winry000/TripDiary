package com.example.winryxie.tripdiary.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.winryxie.tripdiary.AbstractDetailActivity;
import com.example.winryxie.tripdiary.DiarySearchActivity;
import com.example.winryxie.tripdiary.ImageListAdapter;
import com.example.winryxie.tripdiary.ImageUpload;
import com.example.winryxie.tripdiary.MainUserActivity;
import com.example.winryxie.tripdiary.ProfileActivity;
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

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


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

    private BoomMenuButton bmb;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary,container,false);
        Log.d("DEBUG", "DiaryFragment view on create");
        //getActivity().setContentView(R.layout.diary_image);
        UserPackage = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        imgList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.listViewImage);
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Please wait，loading the diaries...");
        progressDialog.show();

        initializeBmbButton(view);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserPackage = currentFirebaseUser.getUid().toString();

        databaseReference = database.getReference("image");
        databaseReference = databaseReference.child(UserPackage);


        adapter = new ImageListAdapter(this.getActivity(), R.layout.diary_image, imgList);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    imgList.add(img);
                }
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
                myIntent.putExtra("like", imgList.get(position).like);
                myIntent.putExtra("likeflag", imgList.get(position).likeflag);
                myIntent.putExtra("diaryDate",imgList.get(position).getDiaryDate());
                myIntent.putExtra("location",imgList.get(position).getLocation());
                myIntent.putExtra("index",position);
                myIntent.putExtra("from", "DiaryFragment");
                startActivity(myIntent);
            }
        });
//
//        Intent i = new Intent(getActivity(),DiaryFragment.class);
//        startActivity(i);
        return view;
    }


    private void initializeBmbButton(View view){
        bmb = (BoomMenuButton) view.findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_4_1);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_1);

        TextOutsideCircleButton.Builder builder1 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.butterfly)
                .normalText("Diaries with Most Likes")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent myIntent = new Intent(getActivity(), DiarySearchActivity.class);
                        myIntent.putExtra("from", "mostLike");
                        startActivity(myIntent);
                        // When the boom-button corresponding this builder is clicked.
                    }
                 });
        bmb.addBuilder(builder1);

        TextOutsideCircleButton.Builder builder2 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.butterfly)
                .normalText("Most Recent Diaries")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        // When the boom-button corresponding this builder is clicked.
                        Intent myIntent = new Intent(getActivity(), DiarySearchActivity.class);
                        myIntent.putExtra("from", "mostRecent");
                        startActivity(myIntent);
                    }
                });
        bmb.addBuilder(builder2);

        TextOutsideCircleButton.Builder builder3 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.butterfly)
                .normalText("Most Visited Country")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        // When the boom-button corresponding this builder is clicked.
                        Intent myIntent = new Intent(getActivity(), DiarySearchActivity.class);
                        myIntent.putExtra("from", "mostVisitedCountry");
                        startActivity(myIntent);
                    }
                });
        bmb.addBuilder(builder3);

        TextOutsideCircleButton.Builder builder4 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.butterfly)
                .normalText("Favorite")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        // When the boom-button corresponding this builder is clicked.
                        Intent myIntent = new Intent(getActivity(), DiarySearchActivity.class);
                        myIntent.putExtra("from", "Favorite");
                        startActivity(myIntent);
                    }
                });
        bmb.addBuilder(builder4);


    }

}