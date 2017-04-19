package com.example.sanya.puzzle15;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;

public class Classicalpuzzle extends AppCompatActivity implements View.OnClickListener {
    Gameboard table;
    int moves = 0;
    Chronometer timer;
    boolean isSound = true;
    private int[] intImageResources_vic = {0, R.drawable.tile_1, R.drawable.tile_2, R.drawable.tile_3, R.drawable.tile_4, R.drawable.tile_5, R.drawable.tile_6, R.drawable.tile_7, R.drawable.tile_8, R.drawable.tile_9, R.drawable.tile_10, R.drawable.tile_11, R.drawable.tile_12, R.drawable.tile_13, R.drawable.tile_14, R.drawable.tile_15, R.drawable.tile_1};
    private int[] intImageResources_sp = {0, R.drawable.sp_tile_1, R.drawable.sp_tile_2, R.drawable.sp_tile_3, R.drawable.sp_tile_4, R.drawable.sp_tile_5, R.drawable.sp_tile_6, R.drawable.sp_tile_7, R.drawable.sp_tile_8, R.drawable.sp_tile_9, R.drawable.sp_tile_10, R.drawable.sp_tile_11, R.drawable.sp_tile_12, R.drawable.sp_tile_13, R.drawable.sp_tile_14, R.drawable.sp_tile_15, R.drawable.sp_tile_1};
    private int[] intImageResources = {0, R.drawable.tile_1, R.drawable.tile_2, R.drawable.tile_3, R.drawable.tile_4, R.drawable.tile_5, R.drawable.tile_6, R.drawable.tile_7, R.drawable.tile_8, R.drawable.tile_9, R.drawable.tile_10, R.drawable.tile_11, R.drawable.tile_12, R.drawable.tile_13, R.drawable.tile_14, R.drawable.tile_15, R.drawable.tile_1};

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
        setContentView(R.layout.playboard);
        // no lolligaggin with the screen !!!
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // read the theme  and sound settings from the 'config'
        SharedPreferences configuration = getApplication().getSharedPreferences("config", MODE_PRIVATE);
        String stringTheme = configuration.getString("theme", "Victorian");
        boolean isSound = configuration.getBoolean("sound", true);
        // apply the background, fontset and tileset, what theme is set in config
        Typeface themeFontStyle;
        if (stringTheme.equals("Victorian")) {
            findViewById(R.id.frame).setBackgroundResource(R.drawable.vic_background_frame);
            themeFontStyle = Typeface.createFromAsset(getAssets(), "fonts/harrington.TTF");
            intImageResources = intImageResources_vic;
        } else  {
            themeFontStyle = Typeface.createFromAsset(getAssets(), "fonts/SancreekRegular.ttf");
            findViewById(R.id.frame).setBackgroundResource(R.drawable.sp_background_frame);
            intImageResources = intImageResources_sp;
        }
        // set the shuffle and radio buttons onclicklistener
        RadioButton b30 = (RadioButton) findViewById(R.id.button30);
        b30.setTypeface(themeFontStyle);
        b30.setOnClickListener(this);
        RadioButton b50 = (RadioButton) findViewById(R.id.button50);
        b50.setTypeface(themeFontStyle);
        b50.setOnClickListener(this);
        RadioButton b100 = (RadioButton) findViewById(R.id.button100);
        b100.setTypeface(themeFontStyle);
        b100.setOnClickListener(this);
        Button shuffle = (Button) findViewById(R.id.shufflebutton);
        shuffle.setOnClickListener(this);
        shuffle.setTypeface(themeFontStyle);
        ((TextView)findViewById(R.id.moves)).setTypeface(themeFontStyle);
        ((TextView)findViewById(R.id.moves_textview)).setTypeface(themeFontStyle);
        ((TextView)findViewById(R.id.timer)).setTypeface(themeFontStyle);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        table = new Gameboard(this, Gameboard.NORMAL);
        showTable();
    }

    /**
     * displays the table values for linear(vertical) layouts
     */
    public void showTable() {
        int col, row;
        LinearLayout rowLayout;
        // erase all previous children from the layouts
        rowLayout = (LinearLayout) findViewById(R.id.row1);
        rowLayout.removeAllViewsInLayout();
        rowLayout = (LinearLayout) findViewById(R.id.row2);
        rowLayout.removeAllViewsInLayout();
        rowLayout = (LinearLayout) findViewById(R.id.row3);
        rowLayout.removeAllViewsInLayout();
        rowLayout = (LinearLayout) findViewById(R.id.row4);
        rowLayout.removeAllViewsInLayout();

        for (row = 1; row <= 4; row++) {
            for (col = 1; col <= 4; col++) {
                ImageView newTile = new ImageView(Classicalpuzzle.this);
                switch (row) {
                    case 1:
                        rowLayout = (LinearLayout) findViewById(R.id.row1);
                        break;
                    case 2:
                        rowLayout = (LinearLayout) findViewById(R.id.row2);
                        break;
                    case 3:
                        rowLayout = (LinearLayout) findViewById(R.id.row3);
                        break;
                    case 4:
                        rowLayout = (LinearLayout) findViewById(R.id.row4);
                        break;
                    default:
                        rowLayout = (LinearLayout) findViewById(R.id.row4);
                }
                // put a tile, to the col, row position
                String e = String.valueOf(table.getBoardValue(col, row));
                newTile.setPadding(pxToDp(0), pxToDp(2), pxToDp(2), pxToDp(2));
                newTile.setImageResource(intImageResources[Integer.valueOf(e)]);
                LinearLayout ll = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxToDp(70), pxToDp(70));
                newTile.setLayoutParams(layoutParams);
                newTile.setTag(String.valueOf(e));
                newTile.setOnClickListener(this);
                rowLayout.addView(newTile);
            }
        }
        // if the player won
        if (table.isGameWon()) {
            Toast.makeText(Classicalpuzzle.this, R.string.youwon, Toast.LENGTH_LONG).show();
            table.storeScore(Gameboard.NORMAL, moves, timer.getText().toString());
            timer.stop();
        }
    }

    /**
     * handles the clicks on 'tiles' and the radio buttons
     *
     * @param v the view that was clicked on
     */
    public void onClick(View v) {
        int row = 1, col = 1;
        boolean gotCha = false;
        int tileNumber;
        int pushedView = Integer.valueOf(v.getTag().toString());
        // a tile was pushed?
        if (pushedView <= 16) {
            // the tag of the tile clicked on
            String clickedTile = (String) v.getTag();
            if (clickedTile.equals(" ")) {
                tileNumber = 0;
            } else {
                tileNumber = pushedView;
            }

            // where is the clicked tile in the matrix?
            while (row < 5 && !gotCha) {
                while (col < 5 && !gotCha) {
                    if (table.getBoardValue(col, row) == tileNumber) {
                        gotCha = true;
                    }
                    col++;
                }
                row++;
                if (!gotCha) col = 1;
            }

            col--;
            row--;

            // col, row position shows to the clicked tile's position
            // now, if possible, swap the tile with the empty spot and play the click sound

            if (table.moveIfCan(col, row)) {

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(Classicalpuzzle.this, R.raw.click);

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
                // show the rearranged table
                showTable();
                moves++;
                TextView movesText = (TextView) findViewById(R.id.moves_textview);
                String ph = ": " + String.valueOf(moves);
                movesText.setText(ph);
            }

            // not a tile was clicked, but a radiobutton or the flush button
        } else {
            switch (pushedView) {
                case 30: {
                    // set the flush steps to 30
                    table.setFlushStep(30);
                    break;
                }
                case 50: {
                    // set the flush steps to 50
                    table.setFlushStep(50);
                    break;
                }
                case 100: {
                    // set the flush steps to 100
                    table.setFlushStep(100);
                    break;
                }
                case 700: {
                    // the flush button
                    // reset the table
                    table.resetTable();
                    moves = 0;
                    // shuffle the tiles
                    table.shuffleTable();
                    // show the table
                    showTable();
                    findViewById(R.id.radionshuffle).setVisibility(GONE);
                    findViewById(R.id.movesandtime).setVisibility(View.VISIBLE);
                    timer = (Chronometer) findViewById(R.id.timer);
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.stop();
                    timer.start();
                    break;
                }
                default: {
                    break;
                }
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

    // converts px values to dp
    public int pxToDp(int px) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) ((px * scale) + 0.5f);
    }
}