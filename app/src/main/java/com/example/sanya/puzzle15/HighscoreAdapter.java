package com.example.sanya.puzzle15;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sanya on 2017.03.19..
 */

public class HighscoreAdapter extends FragmentPagerAdapter {

    public HighscoreAdapter(FragmentManager fm)  {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)   {
            case 0:
                return new HighscoreClassic();
            case 1:
                return new HighscorePicture();
            case 2:
                return new HighscoreHole();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)   {
            case 0:
                return "Classical";
            case 1:
                return "Picture";
            case 2:
                return "Hole";
            default:
                return null;
        }
    }
}
