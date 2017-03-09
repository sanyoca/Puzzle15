package com.example.sanya.puzzle15;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Holespuzzle extends AppCompatActivity {
    Gameboard table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int[][] holes = {{0, 0, 0, 0}, {0, -1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};

        super.onCreate(savedInstanceState);
        setContentView(R.layout.playboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        table = new Gameboard(holes);
    }

    // not implemented yet - used for test purposes only
}
