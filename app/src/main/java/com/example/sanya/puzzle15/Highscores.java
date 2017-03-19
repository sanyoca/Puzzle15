package com.example.sanya.puzzle15;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sanya on 2017.03.19..
 */

public class Highscores extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscores);

        ViewPager vP = (ViewPager) findViewById(R.id.viewpager);
        HighscoreAdapter highscoreAdapter = new HighscoreAdapter(getSupportFragmentManager());
        vP.setAdapter(highscoreAdapter);

        TabLayout tL = (TabLayout) findViewById(R.id.tabs);
        tL.setupWithViewPager(vP);
    }
}
