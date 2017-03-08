package com.example.sanya.puzzle15;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Classicalpuzzle extends AppCompatActivity {
    private board gameTable;
    private int mStepsToFlush = 10;
    private boolean inGame = false;

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    /**
     * if the media player stopped playback, release its resources
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    /**
     * if focus changes, handle it properly
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gameTable = new board();
        // set up the OnClickListener for the 10-30-50 radiobuttons and the shufflebutton
        findViewById(R.id.button10).setOnClickListener(gameTable);
        findViewById(R.id.button30).setOnClickListener(gameTable);
        findViewById(R.id.button50).setOnClickListener(gameTable);
        findViewById(R.id.shufflebutton).setOnClickListener(gameTable);
        // set up the mediaplayer volume and the audiomanager
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMediaPlayer.setVolume(50, 50);
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


                TextView newTile = new TextView(Classicalpuzzle.this);
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
                String e = String.valueOf(gameTable.getBoardValue(col, row));
                if (Integer.valueOf(e) == 0) {
                    e = " ";
                }
                newTile.setText(e);
                newTile.setPadding(3, 3, 3, 3);
                newTile.setTextSize(30);
                LinearLayout ll = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(110, 110);
                layoutParams.setMargins(10, 0, 0, 10);
                newTile.setLayoutParams(layoutParams);
                newTile.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                newTile.setTag(String.valueOf(e));
                newTile.setOnClickListener(gameTable);
                if(!e.equals(" ")) {
                    newTile.setBackgroundResource(R.drawable.roundedcorners);
                }
                rowLayout.addView(newTile);
            }
        }

        if (gameTable.isGameWon()) {
            Toast.makeText(Classicalpuzzle.this, R.string.youwon, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * the class for the table and all other stuffs it handles
     */
    private class board implements View.OnClickListener {
        int[][] mPlayField = new int[6][6];
        int mEmptySpotRow;
        int mEmptySpotColoumn;

        /**
         * initializes the table, by calling resetTable()
         */
        private board() {
            resetTable();
        }

        /**
         * handles the clicks on radiobuttons and the tiles
         * @param v the view that was clicked on
         */
        public void onClick(View v) {
            int row = 1, col = 1;
            boolean gotCha = false;
            int tileNumber;
            int pushedView = Integer.valueOf(v.getTag().toString());

            if(pushedView <= 16) {
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
                        if (getBoardValue(col, row) == tileNumber) {
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

                if (row == mEmptySpotRow && col == mEmptySpotColoumn + 1) {
                    change(col, row);
                }

                if (row == mEmptySpotRow && col == mEmptySpotColoumn - 1) {
                    change(col, row);
                }

                if (row == mEmptySpotRow + 1 && col == mEmptySpotColoumn) {
                    change(col, row);
                }

                if (row == mEmptySpotRow - 1 && col == mEmptySpotColoumn) {
                    change(col, row);
                }

                // show the rearranged table
                showTable();
            }   else    {
                switch (pushedView) {
                    case 100:   {
                        mStepsToFlush = 10;
                        break;
                    }

                    case 300:   {
                        mStepsToFlush = 30;
                        break;
                    }

                    case 500: {
                        mStepsToFlush = 50;
                        break;
                    }

                    case 700:   {
                        // reset the table
                        gameTable.resetTable();
                        // shuffle the tiles
                        gameTable.shuffleTable(mStepsToFlush);
                        // show the table
                        showTable();
                        break;
                    }

                    default:    {
                        break;
                    }
                }
            }
        }

        /**
         * resets the table, setting the mPlayField array to the default state
         * places 0 at 4,4
         * sets the mEmptySpotColoumn and mEmptySpotRow to 4
         */
        private void resetTable() {
            int col, row;

            for (row = 0; row <= 5; row++) {
                for (col = 0; col <= 5; col++) {
                    mPlayField[col][row] = -1;
                }
            }

            for (row = 1; row <= 4; row++) {
                for (col = 1; col <= 4; col++) {
                    mPlayField[col][row] = (row - 1) * 4 + col;
                }
            }
            mPlayField[4][4] = 0;
            mEmptySpotRow = 4;
            mEmptySpotColoumn = 4;
        }

        /**
         * shuffles the table
         * @param steps the number of moves to shuffle the table
         */
        private void shuffleTable(int steps) {
            int previousMove = 0;
            boolean moved;
            int direction;
            int store;

            for (int move = 1; move <= steps; move++) {
                moved = false;
                // random direction
                direction = (int) (Math.random() * 4) + 1;

                while (!moved) {
                    // if previously was moved in the opposite direction, it'd be stupid to move it
                    //  back to that position again, so generate another random direction
                    if (previousMove == 5 - direction) {
                        direction = (int) (Math.random() * 4) + 1;
                        continue;
                    }

                    // moving up
                    if (direction == 1 && mEmptySpotRow > 1) {
                        store = mPlayField[mEmptySpotColoumn][mEmptySpotRow - 1];
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow - 1] = 0;
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                        mEmptySpotRow--;
                        moved = true;
                        previousMove = 1;
                        continue;
                    }

                    // moving right
                    if (direction == 2 && mEmptySpotColoumn < 4) {
                        store = mPlayField[mEmptySpotColoumn + 1][mEmptySpotRow];
                        mPlayField[mEmptySpotColoumn + 1][mEmptySpotRow] = 0;
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                        mEmptySpotColoumn++;
                        moved = true;
                        previousMove = 2;
                        continue;
                    }

                    // moving left
                    if (direction == 3 && mEmptySpotColoumn > 1) {
                        store = mPlayField[mEmptySpotColoumn - 1][mEmptySpotRow];
                        mPlayField[mEmptySpotColoumn - 1][mEmptySpotRow] = 0;
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                        mEmptySpotColoumn--;
                        moved = true;
                        previousMove = 3;
                        continue;
                    }

                    // moving down
                    if (direction == 4 && mEmptySpotRow < 4) {
                        store = mPlayField[mEmptySpotColoumn][mEmptySpotRow + 1];
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow + 1] = 0;
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                        mEmptySpotRow++;
                        moved = true;
                        previousMove = 4;
                        continue;
                    }

                    // if moving was not successful, generate another random direction
                    if (!moved) {
                        direction = (int) (Math.random() * 4) + 1;
                    }
                }
            }
            inGame = true;
        }

        /**
         *
         * @param whichCol the coloumn of the board
         * @param whichRow the row of the board
         * @return the value, foound at the given position
         */
        private int getBoardValue(int whichCol, int whichRow) {
            return mPlayField[whichCol][whichRow];
        }


        /**
         * switch the position of the empty spot, set by params
         * @param switchCol the coloumn which to switch to
         * @param switchRow the row which to switch to
         */
        private void change(int switchCol, int switchRow) {
            int store = getBoardValue(switchCol, switchRow);
            mPlayField[switchCol][switchRow] = 0;
            mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
            mEmptySpotColoumn = switchCol;
            mEmptySpotRow = switchRow;
            // whatever we do, release the media player first
            releaseMediaPlayer();
            // Can we get audio focus
            int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            // yes, we can, play the click sound
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mMediaPlayer = MediaPlayer.create(Classicalpuzzle.this, R.raw.click);
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
            }
        }

        /**
         * @return true, if the game is won
         */
        private boolean isGameWon() {
            int col, row;
            int counter = 0;

            for (row = 1; row <= 4; row++) {
                for (col = 1; col <= 4; col++) {
                    if (getBoardValue(col, row) == ((row - 1) * 4 + col)) {
                        counter++;
                    }
                }
            }
            return (counter == 15 && inGame);
        }
    }

    /**
     * If the app quits, release the media player resources
     */
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    public void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
