package com.example.sanya.puzzle15;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Wallspuzzle extends AppCompatActivity implements View.OnClickListener{
    Gameboard table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playboard);

        table = new Gameboard(this);
        showTable();
        findViewById(R.id.button10).setOnClickListener(this);
        findViewById(R.id.button30).setOnClickListener(this);
        findViewById(R.id.button50).setOnClickListener(this);
        findViewById(R.id.shufflebutton).setOnClickListener(this);
    }

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


                TextView newTile = new TextView(Wallspuzzle.this);
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
                newTile.setOnClickListener(this);
                if(!e.equals(" ")) {
                    newTile.setBackgroundResource(R.drawable.roundedcorners);
                }
                rowLayout.addView(newTile);
            }
        }

        if (table.isGameWon()) {
            Toast.makeText(Wallspuzzle.this, R.string.youwon, Toast.LENGTH_LONG).show();
        }
    }

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
        }   else    {
            switch (pushedView) {
                // radiobutton 10 steps
                case 100:   {
                    table.mStepsToFlush = 10;
                    break;
                }
                // radiobutton 30 steps
                case 300:   {
                    table.mStepsToFlush = 30;
                    break;
                }
                // radiobutton 50 steps
                case 500: {
                    table.mStepsToFlush = 50;
                    break;
                }
                // flush button
                case 700:   {
                    // reset the table
                    table.resetTable();
                    // shuffle the tiles
                    table.shuffleTable();
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
}
