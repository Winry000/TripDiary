package com.example.winryxie.tripdiary;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.winryxie.tripdiary.Fragments.SearchFragment;

import java.io.Serializable;
import java.util.List;

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
            myIntent.putExtra("content", albumList.get(getPosition()).content);
            myIntent.putExtra("url", albumList.get(getPosition()).url);
            myIntent.putExtra("name", albumList.get(getPosition()).name);
            myIntent.putExtra("like", albumList.get(getPosition()).like);
            myIntent.putExtra("likeflag", albumList.get(getPosition()).likeflag);
            myIntent.putExtra("location", albumList.get(getPosition()).location);
            myIntent.putExtra("diaryDate", albumList.get(getPosition()).diaryDate);
            myIntent.putExtra("id", albumList.get(getPosition()).id);
            myIntent.putExtra("index",getPosition());
            myIntent.putExtra("from", "AlbumsAdapter");
            myIntent.putExtra("favoriteBy", (Serializable) albumList.get(getPosition()).favoriteBy);
            context.startActivity(myIntent);
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
        //itemView.setOnClickListener(new SearchFragment());
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
