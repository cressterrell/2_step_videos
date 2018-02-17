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
package com.example.android.miwok;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.net.Uri;
import java.util.ArrayList;

public class MovesActivity extends AppCompatActivity {

    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    private Word mSelectedItem = null;

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

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word(1,"1. Basic", "Beginner", R.raw.number_one, R.raw.video_number_one));
        words.add(new Word(2,"2. Promenade", "Beginner", R.raw.number_two, R.raw.video_number_one));
        words.add(new Word(3,"3. Right Turning Basic (Natural)", "Beginner",R.raw.number_three, R.raw.video_number_one));
        words.add(new Word(4,"4. Right Turning Basic (Cross-Body)", "Beginner", R.raw.number_four, R.raw.video_number_one));
        words.add(new Word(5,"5. Promenade Pivot", "Beginner", R.raw.number_five, R.raw.video_number_one));
        words.add(new Word(6,"6. Underarm Turn (Left)", "Beginner", R.raw.number_five, R.raw.video_number_one));
        words.add(new Word(7,"7. Underarm Turn (Right)", "Beginner", R.raw.number_six, R.raw.video_number_one));
        words.add(new Word(8,"8. Wrap (Walkout)", "Beginner",R.raw.number_seven, R.raw.video_number_one));
        words.add(new Word(9,"9. Wrap (Check Turn)", "Beginner", R.raw.number_eight, R.raw.video_number_one));
        words.add(new Word(10,"10. Sweetheart (Check Turn Left)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
        words.add(new Word(11,"11. Sweetheart (Check Turn Right)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
        words.add(new Word(12,"12. Grapevine (Closed)", "Beginner", R.raw.number_nine, R.raw.video_number_one));
        words.add(new Word(13,"13. Grapevine (Backward Hands)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
        words.add(new Word(14,"14. Grapevine (Forward Hands)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
        words.add(new Word(15,"15. Basket Whip", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
        words.add(new Word(16,"16. Shoulder Catch", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
        words.add(new Word(17,"17. Weave (Inside)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
        words.add(new Word(18,"18. Weave (Outside)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
        words.add(new Word(19,"19. Weave (Outside/Inside)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
        words.add(new Word(20,"20. Side-by-Side Freespins", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));


        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(this, words, R.color.moves);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();

                // Get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);

                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(MovesActivity.this, word.getAudioResourceId());

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }

                mSelectedItem = word;
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
