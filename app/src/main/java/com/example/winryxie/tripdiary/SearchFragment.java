package com.example.winryxie.tripdiary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.winryxie.tripdiary.CameraFragment.REQUEST_CODE;

/**
 * Created by winryxie on 5/4/17.
 */

public class SearchFragment extends Fragment implements View.OnClickListener{
    private ImageButton imageButton;
    public final static String CROP_IMAGE_ACTIVITY_REQUEST_CODE = "1234";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search,container,false);
        TextView username = (TextView)view.findViewById(R.id.username);
        username.setText("xxx");
        imageButton = (ImageButton) view.findViewById(R.id.image_header);
        imageButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v == imageButton) {
            changeHeader(v);
        }
    }
    public void changeHeader(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"),REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri headUri = data.getData();
            CropImage.activity(headUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getActivity());
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Bitmap bm = null;
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageButton.setImageBitmap(bm);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }
}
