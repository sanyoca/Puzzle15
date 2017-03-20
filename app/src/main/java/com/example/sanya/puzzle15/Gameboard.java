package com.example.sanya.puzzle15;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;

public class Gameboard extends AppCompatActivity {
    private int[][] mPlayField = new int[9][9];
    private boolean inGame = false;
    private int mStepsToFlush = 10;
    private int mEmptySpotRow, mEmptySpotColoumn;
    private int maxRowCol;
    private int intHoleCol, intHoleRow;
    private boolean withHoles = false, withWalls = false;
    static final int NORMAL = 1;
    static final int PICTURE = 2;
    static final int HOLE = 3;
    static final int WALLS = 4;
    Context mContext;

    public Gameboard(Context context, int kindOfGame) {
        mContext = context;
        switch(kindOfGame)  {
            // normal game
            case NORMAL: {
                maxRowCol = 4;
                break;
            }
            // picture game
            case PICTURE: {
                maxRowCol = 4;
                break;
            }
            // classical with a hole
            case HOLE: {
                maxRowCol = 4;
                do {
                    intHoleCol = (int) (Math.random() * 4) + 1;
                    intHoleRow = (int) (Math.random() * 4) + 1;
                } while (intHoleCol * intHoleCol == 16);
                withHoles = true;
                break;
            }
            // classical with walls
            case WALLS: {
                maxRowCol = 7;
                withWalls = true;
                break;
            }
            default:
                break;
        }
        resetTable();
    }

    /**
     * sets up the table (mPlayField matrix), like this - for classical and hole version
     * -1 -1 -1 -1 -1 -1
     * -1  1  2  3  4 -1
     * -1  5  6  7  8 -1
     * -1  9 10 11 12 -1
     * -1 13 14 15  0 -1
     * -1 -1 -1 -1 -1 -1
     *
     * or this - for the wall version
     *      0   1   2   3   4   5   6   7  8
     * 0   -1  -1  -1  -1  -1  -1  -1  -1 -1
     * 1   -1   1 100   2 100   3 100   4 -1
     * 2   -1 100 100 100 100 100 100 100 -1
     * 3   -1   5 100   6 100   7 100   8 -1
     * 4   -1 100 100 100 100 100 100 100 -1
     * 5   -1   9 100  10 100  11 100  12 -1
     * 6   -1 100 100 100 100 100 100 100 -1
     * 7   -1  13 100  14 100  15 100   0 -1
     * 8   -1  -1  -1  -1  -1  -1  -1  -1 -1
     */
    public void resetTable() {
        int col, row, count = 1;

        for (row = 0; row <= maxRowCol + 1; row++) {
            for (col = 0; col <= maxRowCol + 1; col++) {
                mPlayField[col][row] = -1;
            }
        }
        // filling up the normal (4x4) table
        if(maxRowCol == 4)  {
            for (row = 1; row <= 4; row++) {
                for (col = 1; col <= 4; col++) {
                    mPlayField[col][row] = (row - 1) * 4 + col;
                }
            }
            mPlayField[4][4] = 0;
            mEmptySpotRow = 4;
            mEmptySpotColoumn = 4;
            // if playing with hole, mark the hole's position back to '-1'
            if(withHoles)  mPlayField[intHoleCol][intHoleRow] = -1;
            // with walls, 100 marks the place where a piece of wall can be placed
        }   else    {
            for(row = 1; row <= 7; row++)    {
                for(col = 1; col <= 7; col++)   {
                    mPlayField[col][row] = 100;
                }
            }
            // with walls, filling up the bigger table with 1-15 tiles
            for (row = 1; row <= 7; row+=2)  {
                for(col = 1; col <= 7; col+=2)   {
                    mPlayField[col][row] = count;
                    count++;
                }
            }
            // 999 marks the places where there are walls
            mPlayField[2][1] = 999;
            mPlayField[3][2] = 999;
            mPlayField[4][3] = 999;
            mPlayField[5][4] = 999;
            mPlayField[6][5] = 999;

            mPlayField[7][7] = 0;
            mEmptySpotRow = 7;
            mEmptySpotColoumn = 7;
        }
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
                while(previousMove == 5 - direction && !withWalls && !withHoles) {
                    direction = (int) (Math.random() * 4) + 1;
                }

                // if playing classical or holes version, the shuffle goes like this
                if(!withWalls) {
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
                    if (direction == 4 && mEmptySpotRow < 4 && !isThisHole(mEmptySpotColoumn, mEmptySpotRow + 1)) {
                        store = mPlayField[mEmptySpotColoumn][mEmptySpotRow + 1];
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow + 1] = 0;
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                        mEmptySpotRow++;
                        moved = true;
                        previousMove = 4;
                        continue;
                    }
                }   else    {
                    // if playing with walls, then it's a bit complicated
                    // moving up
                    if (direction == 1 && mEmptySpotRow > 1 && getBoardValue(mEmptySpotColoumn, mEmptySpotRow - 1)!= 999) {
                        store = mPlayField[mEmptySpotColoumn][mEmptySpotRow - 2];
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow - 2] = 0;
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                        mEmptySpotRow-=2;
                        moved = true;
                        previousMove = 1;
                        continue;
                    }

                    // moving right
                    if (direction == 2 && mEmptySpotColoumn < 7 && getBoardValue(mEmptySpotColoumn + 1, mEmptySpotRow)!= 999) {
                        store = mPlayField[mEmptySpotColoumn + 2][mEmptySpotRow];
                        mPlayField[mEmptySpotColoumn + 2][mEmptySpotRow] = 0;
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                        mEmptySpotColoumn+=2;
                        moved = true;
                        previousMove = 2;
                        continue;
                    }

                    // moving left
                    if (direction == 3 && mEmptySpotColoumn > 1 && getBoardValue(mEmptySpotColoumn - 1, mEmptySpotRow)!= 999) {
                        store = mPlayField[mEmptySpotColoumn - 2][mEmptySpotRow];
                        mPlayField[mEmptySpotColoumn - 2][mEmptySpotRow] = 0;
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                        mEmptySpotColoumn-=2;
                        moved = true;
                        previousMove = 3;
                        continue;
                    }

                    // moving down
                    if (direction == 4 && mEmptySpotRow < 7 && getBoardValue(mEmptySpotColoumn, mEmptySpotRow + 1)!= 999) {
                        store = mPlayField[mEmptySpotColoumn][mEmptySpotRow + 2];
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow + 2] = 0;
                        mPlayField[mEmptySpotColoumn][mEmptySpotRow] = store;
                        mEmptySpotRow+=2;
                        moved = true;
                        previousMove = 4;
                        continue;
                    }
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
        int needed = 1;

        if(!withWalls) {
            for (row = 1; row <= 4; row++) {
                for (col = 1; col <= 4; col++) {
                    if (getBoardValue(col, row) == ((row - 1) * 4 + col)) {
                        counter++;
                    }
                }
            }
            return ((counter == 15 && inGame) || (counter == 14 && inGame && withHoles));
        }   else {
            for (row = 1; row <= 7; row += 2) {
                for (col = 1; col <= 7; col += 2) {
                    if (getBoardValue(col, row) == needed) {
                        counter++;
                        needed++;
                    }
                }
            }
            return (counter == 15 && inGame);
        }
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

        if(!withWalls) {
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
        }   else    {
            if (intRow == mEmptySpotRow && intCol == mEmptySpotColoumn + 2 && getBoardValue(mEmptySpotColoumn + 1,intRow)!=999) {
                change(intCol, intRow);
                return true;
            }

            if (intRow == mEmptySpotRow && intCol == mEmptySpotColoumn - 2 && getBoardValue(mEmptySpotColoumn - 1,intRow)!=999) {
                change(intCol, intRow);
                return true;
            }

            if (intRow == mEmptySpotRow + 2 && intCol == mEmptySpotColoumn && getBoardValue(intCol,mEmptySpotRow + 1)!=999) {
                change(intCol, intRow);
                return true;
            }

            if (intRow == mEmptySpotRow - 2 && intCol == mEmptySpotColoumn && getBoardValue(intCol,mEmptySpotRow - 1)!=999) {
                change(intCol, intRow);
                return true;
            }
            return false;
        }
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

    /**
     * @param gameKind what kind og puzzle we play (CLASSIC, PICTURE, HOLE)
     * @param moves how many steps were used to solve the puzzle
     *
     * this method does a lot of things:
     *              1. creates sharedpreferences for each of the puzzle types
     *              2. if there is a corresponding data, it reads it, then writes back
     */

    public void storeScore(int gameKind, int moves) {
        SharedPreferences highscoreSaves = null;

        switch (gameKind) {
            case NORMAL:
                switch (mStepsToFlush) {
                    case 30:
                        highscoreSaves = mContext.getSharedPreferences("classichighscore30", MODE_PRIVATE);
                        break;
                    case 50:
                        highscoreSaves = mContext.getSharedPreferences("classichighscore50", MODE_PRIVATE);
                        break;
                    case 100:
                        highscoreSaves = mContext.getSharedPreferences("classichighscore100", MODE_PRIVATE);
                        break;
                }
                break;
            case PICTURE:
                switch (mStepsToFlush) {
                    case 30:
                        highscoreSaves = mContext.getSharedPreferences("picturehighscore30", MODE_PRIVATE);
                        break;
                    case 50:
                        highscoreSaves = mContext.getSharedPreferences("picturehighscore50", MODE_PRIVATE);
                        break;
                    case 100:
                        highscoreSaves = mContext.getSharedPreferences("picturehighscore100", MODE_PRIVATE);
                        break;
                }
                break;
            case HOLE:
                switch (mStepsToFlush) {
                    case 30:
                        highscoreSaves = mContext.getSharedPreferences("holehighscore30", MODE_PRIVATE);
                        break;
                    case 50:
                        highscoreSaves = mContext.getSharedPreferences("holehighscore50", MODE_PRIVATE);
                        break;
                    case 100:
                        highscoreSaves = mContext.getSharedPreferences("holehighscore100", MODE_PRIVATE);
                        break;
                }
                break;
            case WALLS:
                highscoreSaves = mContext.getSharedPreferences("wallshighscore", MODE_PRIVATE);
                break;
            default:
                break;
        }

        SharedPreferences.Editor editSaveScores = highscoreSaves.edit();

        int [] bestMoves = {0, 0, 0, 0, 0, 0};
        bestMoves[0] = highscoreSaves.getInt("best1move", 1000000);
        bestMoves[1] = highscoreSaves.getInt("best2move", 1000000);
        bestMoves[2] = highscoreSaves.getInt("best3move", 1000000);
        bestMoves[3] = highscoreSaves.getInt("best4move", 1000000);
        bestMoves[4] = highscoreSaves.getInt("best5move", 1000000);
        bestMoves[5] = moves;
        Arrays.sort(bestMoves);

        editSaveScores.putInt("best1move", bestMoves[0]);
        editSaveScores.putInt("best2move", bestMoves[1]);
        editSaveScores.putInt("best3move", bestMoves[2]);
        editSaveScores.putInt("best4move", bestMoves[3]);
        editSaveScores.putInt("best5move", bestMoves[4]);
        editSaveScores.apply();
    }
}
