package com.example.winryxie.tripdiary;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by winryxie on 5/7/17.
 */

public class ImageListAdapter extends ArrayAdapter<ImageUpload> {
    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;
    public ImageListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ImageUpload> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @Override
    public int getCount(){
        return listImage.size();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);
        TextView imageTitle = (TextView) v.findViewById(R.id.tvimage_title);
        TextView imageContent = (TextView) v.findViewById(R.id.tvimage_content);
        ImageView imageView = (ImageView) v.findViewById(R.id.imgView);



        imageTitle.setText(listImage.get(position).getName());
        imageContent.setText(listImage.get(position).getContent());
        Glide.with(context).load(listImage.get(position).getUrl()).override(320, 300).into(imageView);
        return v;
    }



}
