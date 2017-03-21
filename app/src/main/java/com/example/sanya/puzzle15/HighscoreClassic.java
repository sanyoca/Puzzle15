package com.example.sanya.puzzle15;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HighscoreClassic extends Fragment {

    public HighscoreClassic() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_highscore, container, false);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/harrington.TTF");
        TextView top5MovesView = (TextView) rootView.findViewById(R.id.titletop5moves);
        top5MovesView.setTypeface(font);

        TextView top5TimeView = (TextView) rootView.findViewById(R.id.titletop5times);
        top5TimeView.setTypeface(font);

        String[] prefFiles = {"classichighscore30", "classichighscore50", "classichighscore100"};
        String[] bestMovesString = {"best1move", "best2move", "best3move", "best4move", "best5move"};
        String[] bestTimesString = {"best1time", "best2time", "best3time", "best4time", "best5time"};
        int[] topMovesLayouts = {R.id.top5moves30layout, R.id.top5moves50layout, R.id.top5moves100layout};
        int[] topTimesLayouts = {R.id.top5times30layout, R.id.top5times50layout, R.id.top5times100layout};
        String[] bestMoves = {"", "", "", "", "", ""};
        String[] bestTimes = {"", "", "", "", "", ""};
        TextView insertThisMoves, insertThisTimes;

        for(int i=0; i<=2; i++) {
            SharedPreferences highscoreSaves = getActivity().getSharedPreferences(prefFiles[i], MODE_PRIVATE);
            for(int j=0; j<=4; j++) {
                bestMoves[j] = highscoreSaves.getString(bestMovesString[j], "0 // 0");

                LinearLayout insertMovesHere = (LinearLayout) rootView.findViewById(topMovesLayouts[i]);

                insertThisMoves = new TextView(getActivity());
                insertThisMoves.setText(bestMoves[j]);
                insertMovesHere.addView(insertThisMoves);

                bestTimes[j] = highscoreSaves.getString(bestTimesString[j], "0 // 0");

                LinearLayout insertTimesHere = (LinearLayout) rootView.findViewById(topTimesLayouts[i]);
                insertThisTimes = new TextView(getActivity());
                insertThisTimes.setText(bestTimes[j]);
                insertTimesHere.addView(insertThisTimes);
            }
        }
        return rootView;
    }
}
