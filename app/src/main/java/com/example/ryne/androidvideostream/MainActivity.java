package com.example.ryne.androidvideostream;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //TODO: deprecated...change after tutorial done
    ProgressDialog mDialog;
    VideoView videoView;
    ImageButton btnPlayPause;

    //video to be streamed in videoview
    String videoURL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //binding button and video view
        videoView = findViewById(R.id.videoView);
        btnPlayPause = findViewById(R.id.btn_play_pause);
        btnPlayPause.setOnClickListener(this);
    }

    //setting dialog as video is retrieved
    @Override
    public void onClick(View v) {

        mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        try{
            if (!videoView.isPlaying()) {
                Uri uri = Uri.parse(videoURL);
                videoView.setVideoURI(uri);
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //button displays play icon img asset
                        btnPlayPause.setImageResource(R.drawable.ic_play);
                    }
                });
            }
            else{
                videoView.pause();
                btnPlayPause.setImageResource(R.drawable.ic_play);
            }
        }
        catch (Exception e){
            Log.e("Main", "Error with streaming");
        }
        //removing dialog and requesting video start
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mDialog.dismiss();
                mp.setLooping(true);
                //start video
                videoView.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause);
            }
        });
    }
}