package com.example.sanya.puzzle15;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;

public class Picturepuzzle extends AppCompatActivity implements View.OnClickListener {
    Gameboard table;
    int moves = 0;
    Chronometer timer;
    private int whichImage = 0;
    private int[][] intImageResources = {{0, R.drawable.horse_1, R.drawable.horse_2, R.drawable.horse_3, R.drawable.horse_4, R.drawable.horse_5, R.drawable.horse_6, R.drawable.horse_7, R.drawable.horse_8, R.drawable.horse_9, R.drawable.horse_10, R.drawable.horse_11, R.drawable.horse_12, R.drawable.horse_13, R.drawable.horse_14, R.drawable.horse_15, R.drawable.horse_16},{0, R.drawable.thor_1, R.drawable.thor_2, R.drawable.thor_3, R.drawable.thor_4, R.drawable.thor_5, R.drawable.thor_6, R.drawable.thor_7, R.drawable.thor_8, R.drawable.thor_9, R.drawable.thor_10, R.drawable.thor_11, R.drawable.thor_12, R.drawable.thor_13, R.drawable.thor_14, R.drawable.thor_15, R.drawable.thor_16}};

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
        Spinner pictureChooser = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Horse", "Thor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        pictureChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 switch (position) {
                     case 0:
                         whichImage = 0;
                         showTable();
                         break;
                     case 1:
                         whichImage = 1;
                         showTable();
                         break;
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {
                 // TODO Auto-generated method stub
             }
        });
        pictureChooser.setVisibility(View.VISIBLE);
        pictureChooser.setAdapter(adapter);

        // still no lolligaggin with the orientation !!!
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        table = new Gameboard(this, Gameboard.NORMAL);
        // set up the OnClickListener for the 10-30-50 radiobuttons and the shufflebutton
        findViewById(R.id.button30).setOnClickListener(this);
        findViewById(R.id.button50).setOnClickListener(this);
        findViewById(R.id.button100).setOnClickListener(this);
        findViewById(R.id.shufflebutton).setOnClickListener(this);
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


                ImageView newTile = new ImageView(Picturepuzzle.this);
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
                String e = String.valueOf(table.getBoardValue(col, row));
                newTile.setPadding(pxToDp(6),pxToDp(6), pxToDp(6), pxToDp(6));
                newTile.setImageResource(intImageResources[whichImage][Integer.valueOf(e)]);
                LinearLayout ll = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxToDp(160), pxToDp(160));
                newTile.setLayoutParams(layoutParams);
                newTile.setTag(String.valueOf(e));
                newTile.setOnClickListener(this);
                rowLayout.addView(newTile);
            }
        }
        if (table.isGameWon()) {
            Toast.makeText(Picturepuzzle.this, R.string.youwon, Toast.LENGTH_LONG).show();
            table.storeScore(table.PICTURE, moves, timer.getText().toString());
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
            // now, if possible, swap the tile with the empty spot

            if (table.moveIfCan(col, row)) {

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(Picturepuzzle.this, R.raw.click);

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
                // show the rearranged table
                showTable();
                moves ++;
                TextView movesText = (TextView) findViewById(R.id.moves_textview);
                movesText.setText(": "+String.valueOf(moves));
            }

            // not a tile was clicked, but a radiobutton or the flush button
        } else {
            switch (pushedView) {
                // set the flush steps to 30
                case 30: {
                    table.setFlushStep(30);
                    break;
                }
                // set the flush steps to 50
                case 50: {
                    table.setFlushStep(50);
                    break;
                }
                // set the flush steps to 100
                case 100: {
                    table.setFlushStep(100);
                    break;
                }
                case 700: {
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
        return (int) ((px/scale)+0.5f);
    }

}