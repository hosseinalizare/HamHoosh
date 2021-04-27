package com.example.koohestantest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ActivityShowFullScreenImage extends AppCompatActivity {
    ImageView fullScreenImage;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_screen_image);
        fullScreenImage = findViewById(R.id.fullScreenImage);
        url = getIntent().getStringExtra("image_url");
        Glide.with(this).load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(fullScreenImage);

    }
}