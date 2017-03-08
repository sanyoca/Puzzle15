package com.example.sanya.puzzle15;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Wallspuzzle extends AppCompatActivity {
    Gameboard table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playboard);

        table = new Gameboard(this);
        showTable();
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
                newTile.setOnClickListener(table);
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
}
