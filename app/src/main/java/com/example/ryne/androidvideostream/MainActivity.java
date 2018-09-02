package com.example.ryne.androidvideostream;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity{

    //TODO: deprecated...change after tutorial done
    //ProgressDialog mDialog;
    private ProgressBar progressBar;
    private VideoView videoView;
    private ImageButton btnPlayPause;
    private TextView mLoadingText;

    //progress update configuration using background thread
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    //video to be streamed in videoview
    String videoURL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //binding button and video view
        videoView = findViewById(R.id.videoView);
        btnPlayPause = findViewById(R.id.btn_play_pause);
        mLoadingText = findViewById(R.id.tvText);
        progressBar = findViewById(R.id.progressBar);
        btnPlayPause.setOnClickListener((View.OnClickListener) this); //cast View.OnClickListener for not using implementation

        //separate thread updating progress bar and using handler to communicate with ui
        new Thread(new Runnable() {
            @Override
            public void run() {

                //TODO: btn object setListener in thread
                btnPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressBar = new ProgressBar(MainActivity.this);

                        while (mProgressStatus < 100) {
                            mProgressStatus++;
                            android.os.SystemClock.sleep(100);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(mProgressStatus);
                                }
                            });
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                mLoadingText.setVisibility(View.VISIBLE);

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
                                        mp.setLooping(true);
                                        //start video
                                        videoView.start();
                                        btnPlayPause.setImageResource(R.drawable.ic_pause);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }).start();
    }
}