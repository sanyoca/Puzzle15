package com.example.sanya.puzzle15;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by sanya on 2017.03.19..
 */

public class Highscores extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscores);

        ViewPager vP = (ViewPager) findViewById(R.id.viewpager);
        HighscoreAdapter highscoreAdapter = new HighscoreAdapter(this, getSupportFragmentManager());
        vP.setAdapter(highscoreAdapter);

        TabLayout tL = (TabLayout) findViewById(R.id.tabs);
        tL.setupWithViewPager(vP);

        // read the configuration and apply
        SharedPreferences configuration = this.getSharedPreferences("config", MODE_PRIVATE);
        String stringTheme = configuration.getString("theme", "Victorian");
        Typeface themeFontStyle;
        if (stringTheme.equals("Victorian")) { // victorian style
            themeFontStyle = Typeface.createFromAsset(getAssets(), "fonts/harrington.TTF");
            ((ImageView) findViewById(R.id.highscore_bg)).setImageResource(R.drawable.bg_vic);
        } else { // steampunk style
            themeFontStyle = Typeface.createFromAsset(getAssets(), "fonts/SancreekRegular.ttf");
            ((ImageView) findViewById(R.id.highscore_bg)).setImageResource(R.drawable.bg_sp);
        }
    }
}
