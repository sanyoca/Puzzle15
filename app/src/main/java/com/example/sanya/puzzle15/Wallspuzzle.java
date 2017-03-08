package com.example.sanya.puzzle15;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Wallspuzzle extends AppCompatActivity {
    Gameboard table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playboard);

        table = new Gameboard(this);
    }
}
