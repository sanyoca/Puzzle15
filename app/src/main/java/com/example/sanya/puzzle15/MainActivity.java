package com.example.sanya.puzzle15;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface fontHarrington = Typeface.createFromAsset(getAssets(), "fonts/harrington.TTF");
        TextView gameTitle = (TextView) findViewById(R.id.textview_gametitle);
        gameTitle.setTypeface(fontHarrington);

        findViewById(R.id.button_startclassical).setOnClickListener(this);
        findViewById(R.id.button_startpicture).setOnClickListener(this);
        findViewById(R.id.button_rules).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int intWhatWasClicked = v.getId();
        Intent intentStart;
        switch(intWhatWasClicked)   {
            case R.id.button_startclassical: {
                intentStart  = new Intent(MainActivity.this, Classicalpuzzle.class);
                startActivity(intentStart);
                break;
            }
            case R.id.button_startpicture:  {
                intentStart = new Intent(MainActivity.this, Picturepuzzle.class);
                startActivity(intentStart);
                break;
            }
            case R.id.button_rules: {
                intentStart = new Intent(MainActivity.this, Showrules.class);
                startActivity(intentStart);
                break;
            }
            default:    {
                break;
            }
        }
    }
}
