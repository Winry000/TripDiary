package com.example.winryxie.tripdiary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.winryxie.tripdiary.Fragments.SearchFragment;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context context;
    private List<ImageUpload> albumList;
    private String x;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            thumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(context, AbstractDetailActivity.class);
            myIntent.putExtra("content", SearchFragment.imgList.get(getPosition()).content);
            myIntent.putExtra("url", SearchFragment.imgList.get(getPosition()).url);
            myIntent.putExtra("name",  SearchFragment.imgList.get(getPosition()).name);
            myIntent.putExtra("diaryDate", SearchFragment.imgList.get(getPosition()).getDiaryDate());
            myIntent.putExtra("location", SearchFragment.imgList.get(getPosition()).getLocation());
            myIntent.putExtra("name", SearchFragment.imgList.get(getPosition()).name);
            myIntent.putExtra("like", SearchFragment.imgList.get(getPosition()).like);
            myIntent.putExtra("like", SearchFragment.imgList.get(getPosition()).like);
            myIntent.putExtra("likeflag", SearchFragment.imgList.get(getPosition()).likeflag);
            myIntent.putExtra("index",getPosition());
            myIntent.putExtra("from", "AlbumsAdapter");
            context.startActivity(myIntent);
            //        int position = recyclerView.indexOfChild(v);
//        Intent myIntent = new Intent(getActivity(), AbstractDetailActivity.class);
//        myIntent.putExtra("content",imgList.get(position).content);
//        myIntent.putExtra("url",imgList.get(position).url);
//        myIntent.putExtra("name",imgList.get(position).name);
//        startActivity(myIntent);
        }
    }

    public AlbumsAdapter(Context context, List<ImageUpload> albumList) {
        this.context = context;
        this.albumList = albumList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);
        itemView.setOnClickListener(new SearchFragment());
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
            ImageUpload album = albumList.get(position);
        // loading album cover using Glide library
        Glide.with(context).load(album.getUrl()).into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
