package remi.scoreboard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import remi.scoreboard.fragments.SquashPlayFragment;

public class GamePlayTabAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;

    private ArrayList<String> playerNameList;

    public GamePlayTabAdapter(FragmentManager fm, ArrayList<String> playerNameList) {
        super(fm);
        this.playerNameList = playerNameList;
    }

    // TODO one static class containing fragment configuration for each game
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SquashPlayFragment.newInstance(playerNameList);
            case 1:
                return SquashPlayFragment.newInstance(playerNameList);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // TODO use the static class to get titles
                return "Score";
            case 1:
                return "Ranking";
            default:
                return null;
        }
    }
}
