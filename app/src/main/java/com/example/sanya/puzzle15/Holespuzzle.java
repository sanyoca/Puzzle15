package com.example.sanya.puzzle15;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Holespuzzle extends AppCompatActivity implements View.OnClickListener {
    Gameboard table;
    private int[] intImageResources = {0, R.drawable.tile_1, R.drawable.tile_2, R.drawable.tile_3, R.drawable.tile_4, R.drawable.tile_5, R.drawable.tile_6, R.drawable.tile_7, R.drawable.tile_8, R.drawable.tile_9, R.drawable.tile_10, R.drawable.tile_11, R.drawable.tile_12, R.drawable.tile_13, R.drawable.tile_14, R.drawable.tile_15, R.drawable.tile_1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playboard);
        // no lolligaggin with the screen !!!
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        table = new Gameboard(Gameboard.HOLE);

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
                ImageView newTile = new ImageView(Holespuzzle.this);
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
                if(table.isThisHole(col, row))   {
                    newTile.setImageResource(R.drawable.hole);
                }   else    {
                    newTile.setImageResource(intImageResources[Integer.valueOf(e)]);
                }
                LinearLayout ll = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(110, 110);
                newTile.setLayoutParams(layoutParams);
                newTile.setTag(String.valueOf(e));
                newTile.setOnClickListener(this);
                rowLayout.addView(newTile);
            }
        }
        if (table.isGameWon()) {
            Toast.makeText(Holespuzzle.this, R.string.youwon, Toast.LENGTH_LONG).show();
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
        // a tile was pushed?
        if (pushedView <= 16 && pushedView > 0) {
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

            table.moveIfCan(col, row);

            // show the rearranged table
            showTable();
            // not a tile was clicked, but a radiobutton or the flush button
        } else {
            switch (pushedView) {
                case 100: {
                    // set the flush steps to 10
                    table.setFlushStep(10);
                    break;
                }
                case 300: {
                    // set the flush steps to 30
                    table.setFlushStep(30);
                    break;
                }
                case 500: {
                    // set the flush steps to 50
                    table.setFlushStep(50);
                    break;
                }
                case 700: {
                    // the flush button
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