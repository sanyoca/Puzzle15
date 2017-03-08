package com.example.sanya.puzzle15;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Gameboard extends AppCompatActivity implements View.OnClickListener {
    private int[][] mPlayField = new int[6][6];
    protected int mEmptySpotRow;
    protected int mEmptySpotColoumn;
    boolean inGame = false;
    private Context contextThat;

    public Gameboard(Context that)  {
        contextThat = that;
        resetTable();
        showTable();
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
    private void resetTable()    {
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

    public void showTable() {

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

    public void onClick(View v) {

    }
}
