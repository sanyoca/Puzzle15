package com.example.sanya.puzzle15;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.sanya.puzzle15.R.id.savebutton;

/**
 * Created by sanya on 2017.03.29..
 */

public class Settings extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    String theme = "Victorian";
    boolean isMusic = true, isSound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        // no lolligaggin with the screen !!!
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // read the curtrent settings (theme, sound, music)
        SharedPreferences configuration = this.getSharedPreferences("config", MODE_PRIVATE);
        theme = configuration.getString("theme", "Victorian");
        isMusic = configuration.getBoolean("music", true);
        isSound = configuration.getBoolean("sound", true);

        TextView textTheme = (TextView) findViewById(R.id.text_theme);
        TextView textMusic = (TextView) findViewById(R.id.text_music);
        TextView textSound = (TextView) findViewById(R.id.text_sound);

        Button saveButton = (Button) findViewById(R.id.savebutton);
        ImageView imageBackground = (ImageView) findViewById(R.id.image_settingsbg);

        LinearLayout layoutSettingsScreen = (LinearLayout) findViewById(R.id.settingsscreen);
        Typeface themeFontStyle;

        SwitchCompat switchMusic = (SwitchCompat) findViewById(R.id.music_onoff);
        switchMusic.setChecked(isMusic);
        SwitchCompat switchSound = (SwitchCompat) findViewById(R.id.sound_onoff);
        switchSound.setChecked(isSound);

        // set the spinner to choose between styles
        Spinner themeChooser = (Spinner)findViewById(R.id.themespinner);

        String[] items = new String[]{getString(R.string.victorian), getString(R.string.steampunk)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        themeChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        theme = "Victorian";
                        break;
                    case 1:
                        theme = "Steampunk";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        themeChooser.setAdapter(adapter);
        // set the fonts to the current theme's font
        if(theme.equals("Victorian"))   {
            themeFontStyle = Typeface.createFromAsset(getAssets(), "fonts/harrington.TTF");
            imageBackground.setImageResource(R.drawable.bg_vic);
            layoutSettingsScreen.setBackgroundResource(R.drawable.vic_background_frame);
            themeChooser.setSelection(0);
        }   else    {
            themeFontStyle = Typeface.createFromAsset(getAssets(), "fonts/SancreekRegular.ttf");
            imageBackground.setImageResource(R.drawable.bg_sp);
            layoutSettingsScreen.setBackgroundResource(R.drawable.sp_background_frame);
            themeChooser.setSelection(1);
        }
        textTheme.setTypeface(themeFontStyle);
        textMusic.setTypeface(themeFontStyle);
        textSound.setTypeface(themeFontStyle);
        saveButton.setTypeface(themeFontStyle);

        findViewById(savebutton).setOnClickListener(this);
        ((SwitchCompat) findViewById(R.id.music_onoff)).setOnCheckedChangeListener(this);
        ((SwitchCompat) findViewById(R.id.sound_onoff)).setOnCheckedChangeListener(this);

    }

    // switch between the music/sound on/off state
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId())  {
            case R.id.music_onoff:
                isMusic = !isMusic;
                break;
            case R.id.sound_onoff:
                isSound = !isSound;
                break;
            default:
                break;
        }
    }

    // save the config in case of clicking on "save" and tell it to the user by prompting a toast
    @Override
    public void onClick(View v) {
        SharedPreferences configuration = this.getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor configSave = configuration.edit();
        configSave.putString("theme", theme);
        configSave.putBoolean("music", isMusic);
        configSave.putBoolean("sound", isSound);
        configSave.apply();
        Toast.makeText(this, getString(R.string.settingssaved), Toast.LENGTH_SHORT).show();
    }
}
