package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsLetterViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> posterList;

    public NewsLetterViewPagerAdapter(Context context, List<String> posterList) {
        this.context = context;
        this.posterList = posterList;

    }

    @Override
    public int getCount() {
        return posterList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if (posterList.get(position).contains(".jpg") || posterList.get(position).contains(".png")) {
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(posterList.get(position)).into(imageView);
            container.addView(imageView);
            return imageView;
        } else {
            VideoView videoView = new VideoView(context);

            videoView.setVideoURI(Uri.parse(posterList.get(position)));
            videoView.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                videoView.start();
            });
            container.addView(videoView);
            return videoView;
        }
    }
}
