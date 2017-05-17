package com.example.winryxie.tripdiary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.winryxie.tripdiary.Fragments.FavoriateFragment;

import java.util.List;

//import static com.example.winryxie.tripdiary.Fragments..imgList;

/**
 * Created by winryxie on 5/4/17.
 */

public class AdapterExample extends RecyclerView.Adapter<AdapterExample.ViewHolder> {

    private Context context;
    private List<ImageUpload> favorList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private ImageView favor;

        public ViewHolder(View view) {
            super(view);
            favor = (ImageView) view.findViewById(R.id.imageView_favor);
            title = (TextView) view.findViewById(R.id.txt_title);

            favor.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(context, AbstractDetailActivity.class);
            myIntent.putExtra("content", favorList.get(getPosition()).content);
            myIntent.putExtra("url", favorList.get(getPosition()).url);
            myIntent.putExtra("name",favorList.get(getPosition()).name);
            myIntent.putExtra("like", favorList.get(getPosition()).like);
            myIntent.putExtra("likeflag", favorList.get(getPosition()).likeflag);
            myIntent.putExtra("location", favorList.get(getPosition()).location);
            myIntent.putExtra("diaryDate", favorList.get(getPosition()).diaryDate);
            myIntent.putExtra("id",favorList.get(getPosition()).id);
            myIntent.putExtra("from", "Adapterxample");
            myIntent.putExtra("index",getPosition());

            context.startActivity(myIntent);
        }
    }

    public AdapterExample(Context context, List<ImageUpload> imgList) {
        this.context = context;
        this.favorList = imgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favor_card, parent, false);
        itemView.setOnClickListener(new FavoriateFragment());
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUpload item = favorList.get(position);
        holder.title.setText(item.getLocation());
        Glide.with(context).load(item.getUrl()).into(holder.favor);

    }

    @Override
    public int getItemCount() {
        int m = 0;
        if (favorList != null) {
            m = favorList.size();
        }
        return m;
    }

}
