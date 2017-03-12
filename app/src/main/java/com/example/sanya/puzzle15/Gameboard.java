package com.example.sanya.puzzle15;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

import static java.lang.Boolean.FALSE;

public class Gameboard extends AppCompatActivity {
    private int[][] mPlayField = new int[6][6];
    private boolean inGame = false;
    private int mStepsToFlush = 10;
    private int mEmptySpotRow, mEmptySpotColoumn;

    private int intHoleCol, intHoleRow;
    private boolean withHoles = false;
    static final boolean HOLE = true;
    static final boolean NORMAL = false;

    public Gameboard(boolean isItAHoleGame) {
        // if it is a hole game, place the hole in a random position, except for (4,4), what is the empty spot's default spot
        if(isItAHoleGame)   {
            do {
                intHoleCol = (int) (Math.random() * 4) + 1;
                intHoleRow = (int) (Math.random() * 4) + 1;
            } while (intHoleCol * intHoleCol == 16);
            withHoles = true;
        }
        resetTable();
    }

    /**
     * sets up the table, like this
     * -1 -1 -1 -1 -1 -1
     * -1  1  2  3  4 -1
     * -1  5  6  7  8 -1
     * -1  9 10 11 12 -1
     * -1 13 14 15  0 -1
     * -1 -1 -1 -1 -1 -1
     */
    public void resetTable() {
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

        // if playing with hole, mark the hole' position back to '-1'

        if(withHoles)  mPlayField[intHoleCol][intHoleRow] = -1;

        mPlayField[4][4] = 0;
        mEmptySpotRow = 4;
        mEmptySpotColoumn = 4;
    }

    /**
     * shuffles the table
     */
    public void shuffleTable() {
        int previousMove = 0;
        boolean moved;
        int direction, store;

        for (int move = 1; move <= mStepsToFlush; move++) {
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
                if (direction == 1 && mEmptySpotRow > 1 && !isThisHole(mEmptySpotColoumn, mEmptySpotRow - 1)) {
                    store = mPlayField[mEmptySpotColoumn][mEmptySpotRow - 1];
                    mPlayField[mEmptySpotColoumn][mEmptySpotRow - 1] = 0;
                    mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                    mEmptySpotRow--;
                    moved = true;
                    previousMove = 1;
                    continue;
                }

                // moving right
                if (direction == 2 && mEmptySpotColoumn < 4 && !isThisHole(mEmptySpotColoumn + 1, mEmptySpotRow)) {
                    store = mPlayField[mEmptySpotColoumn + 1][mEmptySpotRow];
                    mPlayField[mEmptySpotColoumn + 1][mEmptySpotRow] = 0;
                    mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                    mEmptySpotColoumn++;
                    moved = true;
                    previousMove = 2;
                    continue;
                }

                // moving left
                if (direction == 3 && mEmptySpotColoumn > 1 && !isThisHole(mEmptySpotColoumn - 1, mEmptySpotRow)) {
                    store = mPlayField[mEmptySpotColoumn - 1][mEmptySpotRow];
                    mPlayField[mEmptySpotColoumn - 1][mEmptySpotRow] = 0;
                    mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                    mEmptySpotColoumn--;
                    moved = true;
                    previousMove = 3;
                    continue;
                }

                // moving down
                if (direction == 4 && mEmptySpotRow < 4 && !isThisHole(mEmptySpotColoumn, mEmptySpotRow +1)) {
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
        // we are in game, important to set it for the isGameWon method
        inGame = true;
    }

    /**
     * @param whichCol the coloumn of the board
     * @param whichRow the row of the board
     * @return the value, found at the given position
     */
    public int getBoardValue(int whichCol, int whichRow) {
        return mPlayField[whichCol][whichRow];
    }

    /**
     * @return true, if the game is won
     */
    public boolean isGameWon() {
        int col, row;
        int counter = 0;

        for (row = 1; row <= 4; row++) {
            for (col = 1; col <= 4; col++) {
                if (getBoardValue(col, row) == ((row - 1) * 4 + col)) {
                    counter++;
                }
            }
        }
        return ((counter == 15 && inGame) || (counter == 14 && inGame && withHoles));
    }

    /**
     * @param steps the moves to shuffle the table, used by shuffleTable method
     */
    public void setFlushStep(int steps) {
        mStepsToFlush = steps;
    }

    /**
     * @param intCol the coloumn
     * @param intRow the row
     *               of the tile to be moved to the empty spot, if possible
     */
    public boolean moveIfCan(int intCol, int intRow) {
        if (intRow == mEmptySpotRow && intCol == mEmptySpotColoumn + 1) {
            change(intCol, intRow);
            return true;
        }

        if (intRow == mEmptySpotRow && intCol == mEmptySpotColoumn - 1) {
            change(intCol, intRow);
            return true;
        }

        if (intRow == mEmptySpotRow + 1 && intCol == mEmptySpotColoumn) {
            change(intCol, intRow);
            return true;
        }

        if (intRow == mEmptySpotRow - 1 && intCol == mEmptySpotColoumn) {
            change(intCol, intRow);
            return true;
        }
        return false;
    }

    /**
     * switch the position of the empty spot, set by params
     * @param switchCol the coloumn which to switch to
     * @param switchRow the row which to switch to
     */
    public void change(int switchCol, int switchRow) {
        int store = getBoardValue(switchCol, switchRow);
        mPlayField[switchCol][switchRow] = 0;
        mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
        mEmptySpotColoumn = switchCol;
        mEmptySpotRow = switchRow;
    }

    /**
     * @param intHoleCol the coloumn
     * @param intHoleRow the row of the position
     * @return true, if the hole is at the given position
     */
    public boolean isThisHole(int intHoleCol, int intHoleRow)  {
        return (mPlayField[intHoleCol][intHoleRow] == -1);
    }
}
