/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.idancealot.twostep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.widget.VideoView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import static com.android.billingclient.api.BillingClient.BillingResponse;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingClientStateListener;

public class MovesActivity extends AppCompatActivity {

    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    private Word mSelectedItem = null;

    private Context mContext;

    private BillingClient mBillingClient;
    private PurchasesUpdatedListener mPurchasesUpdatedListener;

    private boolean mGooglePlayStoreReady=false;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
            //Launch the video here
            launchVideo();
        }
    };

    private boolean launchVideo() {
        Intent videoPlaybackActivity = new Intent(this, DanceDanceVideoPlayer.class);
        videoPlaybackActivity.putExtra("fileRes", mSelectedItem.getVideoResourceId());
        videoPlaybackActivity.putExtra("title1", mSelectedItem.getMiwokTranslation());
        videoPlaybackActivity.putExtra("title2", mSelectedItem.getDefaultTranslation());
        startActivityForResult(videoPlaybackActivity, DanceDanceVideoPlayer.PLAY_VIDEO);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        mContext = this;

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(this, MovesManager.getInstance().getMoves(), R.color.moves);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        final ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SharedPreferences prefs = mContext.getApplicationContext().getSharedPreferences(MovesManager.PREF_FILE_KEY, MODE_PRIVATE);
                boolean isUserSubscribed = prefs.getBoolean("is_subscriber", false);

                // Release the media player if it currently exists because we are about to
                // play a different sound file
                //releaseMediaPlayer();

                // Get the {@link Word} object at the given position the user clicked on
                Word word = MovesManager.getInstance().getMoves().get(position);

                if (isUserSubscribed || word.getItemId()<=5) {
                    mSelectedItem = word;
                    launchVideo();
                } else {
                    if (mGooglePlayStoreReady) {
                        BillingFlowParams.Builder builder = BillingFlowParams
                                .newBuilder()
                                .setSku("premium_subscription").setType(BillingClient.SkuType.SUBS);
                        int result = mBillingClient.launchBillingFlow((AppCompatActivity)mContext, builder.build());
                    } else {
                        Toast.makeText(mContext, "Unable to contact Google Play Store--Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mPurchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@BillingResponse int responseCode,
                                    List<Purchase> purchases) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    //Toast.makeText(mContext,"purchase flow", Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences(MovesManager.PREF_FILE_KEY, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("is_subscriber", false);
                    editor.commit();
                    if (purchases!= null) {
                        for (Purchase purchase : purchases) {
                           //Toast.makeText(mContext,purchase.getSku(), Toast.LENGTH_LONG).show();
                            if (purchase.getSku().compareTo("premium_subscription")==0) {
                                editor.putBoolean("is_subscriber", true);
                                editor.commit();
                            }
                        }
                    }
                } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                    // Handle an error caused by a user canceling the purchase flow.
                } else {
                    // Handle any other error codes.
                }
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((WordAdapter)(listView.getAdapter())).refresh();
                    }
                }, 250);
            }
        };

        mBillingClient = BillingClient.newBuilder(this)
                .setListener(mPurchasesUpdatedListener)
                .build();

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingResponse int billingResponseCode) {

                if (billingResponseCode == BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                    mGooglePlayStoreReady = true;
                    //Toast.makeText(mContext, "Google Play Store activated", Toast.LENGTH_SHORT).show();
                    Purchase.PurchasesResult purchases = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
                    if (purchases.getPurchasesList()!=null) {
                        mPurchasesUpdatedListener.onPurchasesUpdated(BillingClient.BillingResponse.OK, purchases.getPurchasesList());
                    }
                } else {
                    mGooglePlayStoreReady = false;
                    Toast.makeText(mContext, "Unable to contact Google Play Store", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                //Toast.makeText(mContext, "Google Play Store Disconnect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
