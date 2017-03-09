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

        // import/set a font for the title
        Typeface fontHarrington = Typeface.createFromAsset(getAssets(), "fonts/harrington.TTF");
        TextView gameTitle = (TextView) findViewById(R.id.textview_gametitle);
        gameTitle.setTypeface(fontHarrington);

        // set up the onClicklistener for the game buttons
        findViewById(R.id.button_startclassical).setOnClickListener(this);
        findViewById(R.id.button_startpicture).setOnClickListener(this);
        findViewById(R.id.button_rules).setOnClickListener(this);
    }

    /**
     * handles the clicks on the game buttons
     *
     * @param v the view, that was clicked on
     */
    @Override
    public void onClick(View v) {
        int intWhatWasClicked = v.getId();
        Intent intentStart;
        switch (intWhatWasClicked) {
            // starts the classical game
            case R.id.button_startclassical: {
                intentStart = new Intent(MainActivity.this, Classicalpuzzle.class);
                startActivity(intentStart);
                break;
            }
            // starts the picture solve game
            case R.id.button_startpicture: {
                intentStart = new Intent(MainActivity.this, Picturepuzzle.class);
                startActivity(intentStart);
                break;
            }
            // displays the rules and history
            case R.id.button_rules: {
                intentStart = new Intent(MainActivity.this, Showrules.class);
                startActivity(intentStart);
                break;
            }
            default: {
                break;
            }
        }
    }
}
