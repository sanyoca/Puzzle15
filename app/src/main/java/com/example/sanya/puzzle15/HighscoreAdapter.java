package com.example.sanya.puzzle15;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sanya on 2017.03.19..
 */

public class HighscoreAdapter extends FragmentPagerAdapter {
    Context mContext;

    public HighscoreAdapter(Context context, FragmentManager fm)  {
        super(fm);
        mContext = context;
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
                return mContext.getString(R.string.tab_classical);
            case 1:
                return mContext.getString(R.string.tab_picture);
            case 2:
                return mContext.getString(R.string.tab_hole);
            default:
                return null;
        }
    }
}
