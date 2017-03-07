package com.example.sanya.puzzle15;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Picturepuzzle extends AppCompatActivity {
    private board gameTable;
    private int mStepsToFlush = 10;
    private boolean inGame = false;
    private int[] intImageResources = {0, R.drawable.horse_1, R.drawable.horse_2, R.drawable.horse_3, R.drawable.horse_4, R.drawable.horse_5, R.drawable.horse_6, R.drawable.horse_7, R.drawable.horse_8, R.drawable.horse_9, R.drawable.horse_10,
            R.drawable.horse_11, R.drawable.horse_12, R.drawable.horse_13, R.drawable.horse_14, R.drawable.horse_15, R.drawable.horse_16};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gameTable = new board();

        findViewById(R.id.button10).setOnClickListener(gameTable);
        findViewById(R.id.button30).setOnClickListener(gameTable);
        findViewById(R.id.button50).setOnClickListener(gameTable);
        findViewById(R.id.shufflebutton).setOnClickListener(gameTable);

        showTable();
    }

    //
    // displays the table values in for linear(vertical) layouts
    //

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
                String e = String.valueOf(gameTable.getBoardValue(col, row));
                // If I left the commented lines in the code, it caused the app to crash on my phone (API 15)
                // only api 23+
                // int colorResId = getResources().getColor(R.color.tileColor, null);

                newTile.setPadding(3, 3, 3, 3);
                newTile.setImageResource(intImageResources[Integer.valueOf(e)]);

                LinearLayout ll = new LinearLayout(this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(110, 110);

                newTile.setLayoutParams(layoutParams);

                newTile.setTag(String.valueOf(e));
                newTile.setOnClickListener(gameTable);
                rowLayout.addView(newTile);
            }
        }

        if (gameTable.isGameWon()) {
            Toast.makeText(Picturepuzzle.this, R.string.youwon, Toast.LENGTH_LONG).show();
        }
    }

    private class board implements View.OnClickListener {
        int[][] mPlayField = new int[6][6];
        int mEmptySpotRow;
        int mEmptySpotColoumn;

        //
        // initializes the table, by calling resetTable()
        //

        private board() {
            resetTable();
        }

        //
        // the clicklistener that listens to the clicks, happen to tiles
        //

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

        //
        // resets the table, setting the mPlayField array to the default state
        // places 0 at 4,4
        // sets the mEmptySpotColoumn and mEmptySpotRow to 4
        //

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

        //
        // shuffles the table, by moving the empty spot around
        // @param steps for determining the number of moves
        //

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

        //
        // returns the value, found at the given position
        // @param whichCol the coloumn of the board
        // @param whichRow the row of the board
        //

        private int getBoardValue(int whichCol, int whichRow) {
            return mPlayField[whichCol][whichRow];
        }

        //
        // switch the position of the empty spot
        // and the spot, set by the @params
        // @param switchCol the coloumn which to switch to
        // @param switchRow the row which to switch to
        //

        private void change(int switchCol, int switchRow) {
            int store = getBoardValue(switchCol, switchRow);
            mPlayField[switchCol][switchRow] = 0;
            mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
            mEmptySpotColoumn = switchCol;
            mEmptySpotRow = switchRow;
        }

        //
        // returns true, if the game is won
        // all the tiles are in their places
        //

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
}
