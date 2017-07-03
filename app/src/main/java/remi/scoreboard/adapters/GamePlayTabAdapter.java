package remi.scoreboard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import remi.scoreboard.GameFragmentsManager;
import remi.scoreboard.activities.GameActivity;

public class GamePlayTabAdapter extends FragmentPagerAdapter {

    private GameActivity ctx;

    public GamePlayTabAdapter(FragmentManager fm, GameActivity ctx) {
        super(fm);
        this.ctx = ctx;
    }

    // TODO one static class containing fragment configuration for each game
    @Override
    public Fragment getItem(int position) {
        return GameFragmentsManager.getFragmentAtPosition(ctx, position);
    }

    @Override
    public int getCount() {
        return GameFragmentsManager.getFragmentsCount(ctx);
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
