package com.example.sanya.puzzle15;

import android.support.v7.app.AppCompatActivity;

public class Gameboard extends AppCompatActivity {
    private int[][] mPlayField = new int[9][9];
    private boolean inGame = false;
    private int mStepsToFlush = 10;
    private int mEmptySpotRow, mEmptySpotColoumn;
    private int maxRowCol;
    private int intHoleCol, intHoleRow;
    private boolean withHoles = false, withWalls = false;
    static final int NORMAL = 1;
    static final int HOLE = 2;
    static final int WALLS = 3;

    public Gameboard(int kindOfGame) {
        switch(kindOfGame)  {
            // normal game
            case NORMAL: {
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
     *
     * -1  -1  -1  -1  -1  -1  -1  -1 -1
     * -1   1 100   2 100   3 100   4 -1
     * -1 100 100 100 100 100 100 100 -1
     * -1   5 100   6 100   7 100   8 -1
     * -1 100 100 100 100 100 100 100 -1
     * -1   9 100  10 100  11 100  12 -1
     * -1 100 100 100 100 100 100 100 -1
     * -1  13 100  14 100  15 100   0 -1
     * -1  -1  -1  -1  -1  -1  -1  -1 -1
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
            mPlayField[4][3] = 999;
            mPlayField[4][4] = 999;
            mPlayField[4][5] = 999;
            mPlayField[3][4] = 999;
            mPlayField[5][4] = 999;

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

        if(withWalls)   {mStepsToFlush = mStepsToFlush * 10;}
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

        if(!withWalls) {
            for (row = 1; row <= 4; row++) {
                for (col = 1; col <= 4; col++) {
                    if (getBoardValue(col, row) == ((row - 1) * 4 + col)) {
                        counter++;
                    }
                }
            }
            return ((counter == 15 && inGame) || (counter == 14 && inGame && withHoles));
        }   else    {
            return false;
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
}
