package com.idancealot.twostep;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class DanceDanceVideoPlayer extends Activity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener {

    public static int UNKNOWN = -1;
    public static int ERROR = 1;
    public static int OK = 0;

    public static int PLAY_VIDEO = 0;

    private VideoView mVV;
    private MediaController mMC;

    private int mVideoState = UNKNOWN;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        setContentView(R.layout.videoplayer);

        int fileRes=0;
        Bundle e = getIntent().getExtras();
        if (e!=null) {
            fileRes = e.getInt("fileRes");
        }

        mVV = (VideoView)findViewById(R.id.myvideoview);
        mVV.setOnCompletionListener(this);
        mVV.setOnPreparedListener(this);

        if (playFileRes(fileRes)) {
            mVV.start();
            mVideoState = OK;
        } else {
            mVideoState = ERROR;
            this.finish();
        }

        mMC = new MediaController(this) {
            @Override
            public void hide() {
                //Do not hide.
            }
            public boolean dispatchKeyEvent(KeyEvent event)
            {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                    ((Activity) getContext()).finish();

                return super.dispatchKeyEvent(event);
            }
        };
        mMC.setAnchorView(mVV);
        mVV.setMediaController(mMC);
        String title2 = e.getString("title2");
        ((TextView) findViewById(R.id.title)).setText(title2);

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int fileRes = 0;
        Bundle e = getIntent().getExtras();
        if (e != null) {
            fileRes = e.getInt("fileRes");
        }
        playFileRes(fileRes);
    }

    private boolean playFileRes(int fileRes) {
        if (fileRes==0) {
            mVV.stopPlayback();
            return false;
        } else {
            mVV.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + fileRes));
            return true;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Nothing to do here.
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
        mVV.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMC.show(0);
            }
        }, 100);
    }

    @Override
    public void onPause()  {
        super.onPause();
        this.setResult(mVideoState);
    }

}
