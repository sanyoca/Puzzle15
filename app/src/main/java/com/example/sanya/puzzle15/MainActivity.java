package com.example.sanya.puzzle15;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.R.attr.pivotX;
import static android.R.attr.pivotY;
import static com.example.sanya.puzzle15.R.id.button_highscores;
import static com.example.sanya.puzzle15.R.id.button_quitgame;
import static com.example.sanya.puzzle15.R.id.button_rules;
import static com.example.sanya.puzzle15.R.id.button_startholes;
import static com.example.sanya.puzzle15.R.id.button_startpicture;
import static com.example.sanya.puzzle15.R.id.button_startwalls;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Handles playback of all the sound files
     */
    private MediaPlayer mMediaPlayer;

    /**
     * Handles audio focus when playing a sound file
     */
    private AudioManager mAudioManager;
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
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // no lolligaggin with the screen !!!
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // import/set a font for the title
        Typeface fontHarrington = Typeface.createFromAsset(getAssets(), "fonts/harrington.TTF");
        TextView gameTitle = (TextView) findViewById(R.id.textview_gametitle);
        gameTitle.setTypeface(fontHarrington);
        // and for the buttons - also set the onclicklistener
        int[] buttons = {R.id.button_startclassical, R.id.button_startpicture, R.id.button_startholes, R.id.button_startwalls, R.id.button_highscores, R.id.button_rules, R.id.button_quitgame};
        Button fontChangeButton;
        for(int i = 0; i<=6; i++)   {
            fontChangeButton = (Button) findViewById(buttons[i]);
            fontChangeButton.setOnClickListener(this);
            fontChangeButton.setTypeface(fontHarrington);
        }
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // We have audio focus now.

            // Create and setup the {@link MediaPlayer} for the audio resource associated
            // with the current word
            mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.theme_vic);

            // Start the audio file
            mMediaPlayer.start();

            // Setup a listener on the media player, so that we can stop and release the
            // media player once the sound has finished playing.
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
        }
    }

    /**
     * handles the clicks on the game buttons
     *
     * @param v the view, that was clicked on
     */
    @Override
    public void onClick(View v) {
        int intWhatWasClicked = v.getId();
        Intent intentStart;
        switch (intWhatWasClicked) {
            // starts the classical game
            case R.id.button_startclassical: {
                intentStart = new Intent(MainActivity.this, Classicalpuzzle.class);
                startActivity(intentStart);
                break;
            }
            // starts the picture solve game
            case button_startpicture: {
                intentStart = new Intent(MainActivity.this, Picturepuzzle.class);
                startActivity(intentStart);
                break;
            }
            // starts the hole version game
            case button_startholes: {
                intentStart = new Intent(MainActivity.this, Holespuzzle.class);
                startActivity(intentStart);
                break;
            }
            // starts the walls version game
            case button_startwalls: {
                intentStart = new Intent(MainActivity.this, Wallspuzzle.class);
                startActivity(intentStart);
                break;
            }

            // shows the high scores
            case button_highscores: {
                intentStart = new Intent(MainActivity.this, Highscores.class);
                startActivity(intentStart);
                break;
            }

            // displays the rules and history
            case button_rules: {
                intentStart = new Intent(MainActivity.this, Showrules.class);
                startActivity(intentStart);
                break;
            }

            // quits the game
            case button_quitgame:  {
                this.finishAffinity();
            }
            default: {
                break;
            }
        }
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
