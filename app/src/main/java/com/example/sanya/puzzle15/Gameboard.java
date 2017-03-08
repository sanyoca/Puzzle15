package com.example.sanya.puzzle15;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Gameboard extends AppCompatActivity {
    private int[][] mPlayField = new int[6][6];
    protected int mEmptySpotRow;
    protected int mEmptySpotColoumn;
    private boolean inGame = false;
    int mStepsToFlush = 10;

    public Gameboard(Context that)  {
        resetTable();
    }

    /**
     * sets up the table, like this
     * 0  0  0  0  0  0
     * 0  1  2  3  4  0
     * 0  5  6  7  8  0
     * 0  9 10 11 12  0
     * 0 13 14 15  0  0
     * 0  0  0  0  0  0
     */
    public void resetTable()    {
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
    public void shuffleTable() {
        int previousMove = 0;
        boolean moved;
        int direction;
        int store;

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
        return (counter == 15 && inGame);
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

}
