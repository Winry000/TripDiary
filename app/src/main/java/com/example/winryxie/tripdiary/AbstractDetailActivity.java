package com.example.winryxie.tripdiary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

public class AbstractDetailActivity extends AppCompatActivity {

    private StorageReference storageReference;
    public View container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_detail);

        TextView imageTitle = (TextView) findViewById(R.id.title);
        TextView imageContent = (TextView) findViewById(R.id.description);
        ImageView imageView = (ImageView) findViewById(R.id.photo);
        container = findViewById(R.id.container);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imageTitle.setText(bundle.getString("name"));
            imageContent.setText(bundle.getString("content"));
            Glide.with(this).load(bundle.getString("url")).override(320, 300).into(imageView);
//            try {
//                URL myUrl = new URL(bundle.getString("url"));
//                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                colorize(myBitmap);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

//    private void colorize(Bitmap photo) {
//        Palette palette = Palette.generate(photo);
//        applyPalette(palette);
//    }
//
//    public void applyPalette(Palette palette) {
//        Resources res = getResources();
//
//        container.setBackgroundColor(palette.getDarkMutedColor(res.getColor(R.color.default_dark_muted)));
//
//        TextView titleView = (TextView) findViewById(R.id.title);
//        titleView.setTextColor(palette.getVibrantColor(res.getColor(R.color.default_vibrant)));
//
//        TextView descriptionView = (TextView) findViewById(R.id.description);
//        descriptionView.setTextColor(palette.getLightVibrantColor(res.getColor(R.color.default_light_vibrant)));
//
////        colorButton(R.id.info_button, palette.getDarkMutedColor(res.getColor(R.color.default_dark_muted)),
////                palette.getDarkVibrantColor(res.getColor(R.color.default_dark_vibrant)));
////        colorButton(R.id.star_button, palette.getMutedColor(res.getColor(R.color.default_muted)),
////                palette.getVibrantColor(res.getColor(R.color.default_vibrant)));
//
////        AnimatedPathView star = (AnimatedPathView) findViewById(R.id.star_container);
////        star.setFillColor(palette.getVibrantColor(R.color.default_vibrant));
////        star.setStrokeColor(palette.getLightVibrantColor(res.getColor(R.color.default_light_vibrant)));
//    }
}
