package remi.scoreboard.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import remi.scoreboard.fragments.GameChooserFragment;
import remi.scoreboard.fragments.GamePlayerFragment;
import remi.scoreboard.fragments.GameRulesFragment;

public class SetupGamePagerAdapter extends FragmentPagerAdapter {

    final int NUM_FRAGS = 3;
    private Context ctx;
    private GamePlayerFragment gamePlayerFragment;
    private GameChooserFragment gameChooserFragment;
    private GameRulesFragment gameRulesFragment;

    public SetupGamePagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                gameChooserFragment = (GameChooserFragment) GameChooserFragment.instantiate(ctx, GameChooserFragment.class.getName());
                return gameChooserFragment;
            case 1:
                gamePlayerFragment = (GamePlayerFragment) GamePlayerFragment.instantiate(ctx, GamePlayerFragment.class.getName());
                return gamePlayerFragment;
            case 2:
                gameRulesFragment = (GameRulesFragment) GameRulesFragment.instantiate(ctx, GameRulesFragment.class.getName());
                ;
                return gameRulesFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_FRAGS;
    }

    public ArrayList<String> getPlayers() {
        if (gamePlayerFragment == null) {
            Log.d("SetupGamePagerAdapter", "gamePlayerFragment == null");
        }
        return gamePlayerFragment.getPlayers();
    }

    public String getRule() {
        if (gameRulesFragment == null) {
            Log.d("SetupGamePagerAdapter", "gameRulesFragment == null");
        }
        return gameRulesFragment.getRule();
    }

    public void savePlayers() {
        gamePlayerFragment.savePlayers();
    }
}
