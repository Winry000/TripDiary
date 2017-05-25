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
import com.example.winryxie.tripdiary.DiarySearchActivity;
import com.example.winryxie.tripdiary.ImageListAdapter;
import com.example.winryxie.tripdiary.ImageUpload;
import com.example.winryxie.tripdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import static com.example.winryxie.tripdiary.MainUserActivity.imgList;

/**
 * Created by winryxie on 5/4/17.
 */

public class DiaryFragment extends Fragment {

    private ListView lv;
    private ImageListAdapter adapter;
    private BoomMenuButton bmb;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary, container, false);
        lv = (ListView) view.findViewById(R.id.listViewImage);

        initializeBmbButton(view);


        final List<ImageUpload> imgMapList = imgList;

        adapter = new ImageListAdapter(getActivity(), R.layout.diary_image, imgMapList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent myIntent = new Intent(getActivity(), AbstractDetailActivity.class);
                myIntent.putExtra("content", imgMapList.get(position).content);
                myIntent.putExtra("id", imgMapList.get(position).id);
                Log.i("DEBUG", imgMapList.get(position).id);
                myIntent.putExtra("url", imgMapList.get(position).url);
                myIntent.putExtra("name", imgMapList.get(position).name);
                myIntent.putExtra("like", imgMapList.get(position).like);
                myIntent.putExtra("likeflag", imgMapList.get(position).likeflag);
                myIntent.putExtra("location", imgMapList.get(position).location);
                myIntent.putExtra("diaryDate", imgMapList.get(position).diaryDate);
                myIntent.putExtra("index", position);
                myIntent.putExtra("from", "DiaryFragment");
                startActivity(myIntent);
            }
        });
//
//        Intent i = new Intent(getActivity(),DiaryFragment.class);
//        startActivity(i);
        return view;
    }


    private void initializeBmbButton(View view) {
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
