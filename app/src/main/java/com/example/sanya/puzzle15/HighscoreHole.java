package com.example.sanya.puzzle15;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HighscoreHole extends Fragment {

    public HighscoreHole() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_highscore, container, false);

        String[] prefFiles = {"holehighscore30", "holehighscore50", "holehighscore100"};
        String[] bestMovesString = {"best1move", "best2move", "best3move", "best4move", "best5move"};
        String[] bestMovesTimesString = {"best1movetime", "best2movetime", "best3movetime", "best4movetime", "best5movetime"};
        String[] bestTimesString = {"best1time", "best2time", "best3time", "best4time", "best5time"};
        int[] topMovesLayouts = {R.id.top5moves30layout, R.id.top5moves50layout, R.id.top5moves100layout};
        int[] topTimesLayouts = {R.id.top5times30layout, R.id.top5times50layout, R.id.top5times100layout};
        int[] bestMoves = {0, 0, 0, 0, 0, 0};
        String[] bestMovesTimes = {"", "", "", "", "", ""};
        String[] bestTimes = {"", "", "", "", "", ""};
        TextView insertThisMoves, insertThisTimes;

        SharedPreferences configuration = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        String stringTheme = configuration.getString("theme", "Victorian");
        Typeface themeFontStyle;
        if (stringTheme.equals("Victorian")) {
            rootView.findViewById(R.id.highscore_scroll).setBackgroundResource(R.drawable.bg_vic);
            themeFontStyle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/harrington.TTF");
        } else  {
            rootView.findViewById(R.id.highscore_scroll).setBackgroundResource(R.drawable.bg_sp);
            themeFontStyle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SancreekRegular.ttf");
        }

        final int[] intTilesResources = {R.id.titletop5moves, R.id.textView_top5moves_30steps, R.id.textView_top5moves_50steps, R.id.textView_top5moves_100steps, R.id.titletop5times, R.id.textView_top5times_30steps, R.id.textView_top5times_50steps, R.id.textView_top5times_100steps};

        TextView tv;
        for(int i=0; i<=7; i++) {
            tv = (TextView) rootView.findViewById(intTilesResources[i]);
            tv.setTypeface(themeFontStyle);
        }

        for(int i=0; i<=2; i++) {
            SharedPreferences highscoreSaves = getActivity().getSharedPreferences(prefFiles[i], MODE_PRIVATE);
            for(int j=0; j<=4; j++) {
                bestMoves[j] = highscoreSaves.getInt(bestMovesString[j], 0);
                if(bestMoves[j] == 1000000) bestMoves[j] = 0;
                bestMovesTimes[j] = highscoreSaves.getString(bestMovesTimesString[j], "00:00");
                LinearLayout insertMovesHere = (LinearLayout) rootView.findViewById(topMovesLayouts[i]);

                insertThisMoves = new TextView(getActivity());
                if(bestMovesTimes[j].equals("")) bestMovesTimes[j]="00:00";
                insertThisMoves.setText(String.valueOf(bestMoves[j]) + " // " + bestMovesTimes[j]);
                insertThisMoves.setGravity(Gravity.CENTER_HORIZONTAL);
                insertMovesHere.addView(insertThisMoves);

                bestTimes[j] = highscoreSaves.getString(bestTimesString[j], "00:00 // 0");
                if(bestTimes[j].equals("1000")) {
                    bestTimes[j] = "00:00 // 0";
                }
                LinearLayout insertTimesHere = (LinearLayout) rootView.findViewById(topTimesLayouts[i]);
                insertThisTimes = new TextView(getActivity());
                insertThisTimes.setText(bestTimes[j]);
                insertThisTimes.setGravity(Gravity.CENTER_HORIZONTAL);
                insertTimesHere.addView(insertThisTimes);
            }
        }
        return rootView;
    }
}
