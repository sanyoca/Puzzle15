package com.example.sanya.puzzle15;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.sanya.puzzle15.R.id.button_highscores;
import static com.example.sanya.puzzle15.R.id.button_quitgame;
import static com.example.sanya.puzzle15.R.id.button_rules;
import static com.example.sanya.puzzle15.R.id.button_startholes;
import static com.example.sanya.puzzle15.R.id.button_startpicture;
import static com.example.sanya.puzzle15.R.id.button_startwalls;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // no lolligaggin with the screen !!!
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // import/set a font for the title
        Typeface fontHarrington = Typeface.createFromAsset(getAssets(), "fonts/harrington.TTF");
        TextView gameTitle = (TextView) findViewById(R.id.textview_gametitle);
        gameTitle.setTypeface(fontHarrington);
        // and for the buttons - also set the onclicklistener
        int[] buttons = {R.id.button_startclassical, R.id.button_startpicture, R.id.button_startholes, R.id.button_startwalls, R.id.button_highscores, R.id.button_rules, R.id.button_quitgame};
        Button fontChangeButton;
        for(int i = 0; i<=6; i++)   {
            fontChangeButton = (Button) findViewById(buttons[i]);
            fontChangeButton.setOnClickListener(this);
            fontChangeButton.setTypeface(fontHarrington);
        }
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
            case button_startpicture: {
                intentStart = new Intent(MainActivity.this, Picturepuzzle.class);
                startActivity(intentStart);
                break;
            }
            // starts the hole version game
            case button_startholes: {
                intentStart = new Intent(MainActivity.this, Holespuzzle.class);
                startActivity(intentStart);
                break;
            }
            // starts the walls version game
            case button_startwalls: {
                intentStart = new Intent(MainActivity.this, Wallspuzzle.class);
                startActivity(intentStart);
                break;
            }

            // shows the high scores
            case button_highscores: {
                intentStart = new Intent(MainActivity.this, Highscores.class);
                startActivity(intentStart);
                break;
            }

            // displays the rules and history
            case button_rules: {
                intentStart = new Intent(MainActivity.this, Showrules.class);
                startActivity(intentStart);
                break;
            }

            // quits the game
            case button_quitgame:  {
                this.finishAffinity();
            }
            default: {
                break;
            }
        }
    }
}
