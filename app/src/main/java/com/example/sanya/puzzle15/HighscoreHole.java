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
public class HighscoreHole extends Fragment {

    public HighscoreHole() {
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

        String[] prefFiles = {"holehighscore30", "holehighscore50", "holehighscore100"};
        String[] bestMovesString = {"best1move", "best2move", "best3move", "best4move", "best5move"};
        int[] topMovesLayouts = {R.id.top5moves30layout, R.id.top5moves50layout, R.id.top5moves100layout};
        int[] bestMoves = {0, 0, 0, 0, 0, 0};
        TextView insertThis;

        for(int i=0; i<=2; i++) {
            SharedPreferences highscoreSaves = getActivity().getSharedPreferences(prefFiles[i], MODE_PRIVATE);
            for(int j=0; j<=4; j++) {
                bestMoves[j] = highscoreSaves.getInt(bestMovesString[j], 1000000);
                LinearLayout insertMovesHere = (LinearLayout) rootView.findViewById(topMovesLayouts[i]);
                insertThis = new TextView(getActivity());
                insertThis.setText(String.valueOf(bestMoves[j]));
                insertMovesHere.addView(insertThis);
            }
        }
        return rootView;
    }
}
