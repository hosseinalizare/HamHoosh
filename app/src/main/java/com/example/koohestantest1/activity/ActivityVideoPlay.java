package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.example.koohestantest1.R;

import java.io.File;
import java.net.URI;

public class ActivityVideoPlay extends AppCompatActivity {
    private VideoView videoView;
    private AppCompatSeekBar seekBar;
    private ImageView imgPlay, imgPause, imgStop;
    private int pausePosition;
    private boolean play = false;
    private boolean stop = false;
    private File file;
    private Uri urie;
    private Handler handler ;
    int du;
    int mCurrentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar);
        imgPlay = findViewById(R.id.btnPlay);
        imgPause = findViewById(R.id.btnPause);
        imgStop = findViewById(R.id.btnStop);
        handler =new Handler();

        String videoName = getIntent().getExtras().getString("videoName");
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Dehkadeh/" + videoName);

    /*    videoView.setVideoPath(file.getAbsolutePath());
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();*/
        urie = Uri.parse(file.getAbsolutePath());
        videoView.setVideoURI(urie);

        videoView.start();


/*
        ActivityVideoPlay.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(videoView.isPlaying()){
                     mCurrentPosition = videoView.getCurrentPosition() / 1000;
                    du =  videoView.getDuration();
                    seekBar.setMax(du/1000);
                    seekBar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });
*/


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int duration = videoView.getDuration();
                float progressbarname = progress / 100.0f;
                float time = duration * progressbarname;
                videoView.seekTo((int) time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imgStop.setOnClickListener(v -> {
            videoView.stopPlayback();
            videoView.seekTo(0);
            seekBar.setProgress(0);
            stop = true;
        });

        imgPause.setOnClickListener(v -> {
            pausePosition = videoView.getCurrentPosition();
            videoView.pause();
            play = true;
        });

        imgPlay.setOnClickListener(v -> {
            if (play) {
                videoView.seekTo(pausePosition);
                videoView.start();
                play = false;
            }

            if (stop) {
                videoView.setVideoURI(urie);
                videoView.start();
            }
        });
    }




    public void getSeekBarStatus() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // mp is your MediaPlayer
                // progress is your ProgressBar

                int currentPosition = 0;
                int total = videoView.getDuration();
                seekBar.setMax(total);
                while (videoView != null && currentPosition < total) {
                    try {
                        Thread.sleep(1000);
                        currentPosition = videoView.getCurrentPosition();
                    } catch (InterruptedException e) {
                        return;
                    }
                    seekBar.setProgress(currentPosition);

                }
            }
        }).start();
    }
}