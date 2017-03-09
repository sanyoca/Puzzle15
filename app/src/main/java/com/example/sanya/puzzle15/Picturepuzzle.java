package com.example.sanya.puzzle15;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Picturepuzzle extends AppCompatActivity implements View.OnClickListener{
    Gameboard table;
    private int mStepsToFlush = 10;
    private boolean inGame = false;
    private int[] intImageResources = {0, R.drawable.horse_1, R.drawable.horse_2, R.drawable.horse_3, R.drawable.horse_4, R.drawable.horse_5, R.drawable.horse_6, R.drawable.horse_7, R.drawable.horse_8, R.drawable.horse_9, R.drawable.horse_10,
            R.drawable.horse_11, R.drawable.horse_12, R.drawable.horse_13, R.drawable.horse_14, R.drawable.horse_15, R.drawable.horse_16};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        table = new Gameboard();
        // set up the OnClickListener for the 10-30-50 radiobuttons and the shufflebutton
        findViewById(R.id.button10).setOnClickListener(this);
        findViewById(R.id.button30).setOnClickListener(this);
        findViewById(R.id.button50).setOnClickListener(this);
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
                newTile.setPadding(3, 3, 3, 3);
                newTile.setImageResource(intImageResources[Integer.valueOf(e)]);
                LinearLayout ll = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(110, 110);
                newTile.setLayoutParams(layoutParams);
                newTile.setTag(String.valueOf(e));
                newTile.setOnClickListener(this);
                rowLayout.addView(newTile);
            }
        }
        if (table.isGameWon()) {
            Toast.makeText(Picturepuzzle.this, R.string.youwon, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * handles the clicks on 'tiles' and the radio buttons
     * @param v the view that was clicked on
     */
    public void onClick(View v) {
        int row = 1, col = 1;
        boolean gotCha = false;
        int tileNumber;
        int pushedView = Integer.valueOf(v.getTag().toString());

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

            if (row == table.mEmptySpotRow && col == table.mEmptySpotColoumn + 1) {
                table.change(col, row);
            }

            if (row == table.mEmptySpotRow && col == table.mEmptySpotColoumn - 1) {
                table.change(col, row);
            }

            if (row == table.mEmptySpotRow + 1 && col == table.mEmptySpotColoumn) {
                table.change(col, row);
            }

            if (row == table.mEmptySpotRow - 1 && col == table.mEmptySpotColoumn) {
                table.change(col, row);
            }

            // show the rearranged table
            showTable();
        } else {
            switch (pushedView) {
                case 100: {
                    mStepsToFlush = 10;
                    break;
                }
                case 300: {
                    mStepsToFlush = 30;
                    break;
                }
                case 500: {
                    mStepsToFlush = 50;
                    break;
                }
                case 700: {
                    // reset the table
                    table.resetTable();
                    // shuffle the tiles
                    table.shuffleTable();
                    // show the table
                    showTable();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}