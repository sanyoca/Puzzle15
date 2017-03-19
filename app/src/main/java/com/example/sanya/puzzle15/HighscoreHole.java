package com.example.sanya.puzzle15;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        return rootView;
    }
}
