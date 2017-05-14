package remi.scoreboard.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import remi.scoreboard.fragments.FragmentGameChooser;
import remi.scoreboard.fragments.FragmentGamePlayer;
import remi.scoreboard.fragments.FragmentGameRules;

public class SetupGamePagerAdapter extends FragmentPagerAdapter {

    final private int NUM_FRAGS = 3;
    private Context ctx;
    private FragmentGamePlayer fragmentGamePlayer;

    public SetupGamePagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentGameChooser.instantiate(ctx, FragmentGameChooser.class.getName());
            case 1:
                fragmentGamePlayer = (FragmentGamePlayer) FragmentGamePlayer.instantiate(ctx, FragmentGamePlayer.class.getName());
                return fragmentGamePlayer;
            case 2:
                return FragmentGameRules.instantiate(ctx, FragmentGameRules.class.getName());
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_FRAGS;
    }

    public ArrayList<String> getPlayers() {
        if (fragmentGamePlayer == null) {
            Log.d("SetupGamePagerAdapter", "fragmentGamePlayer == null");
        }
        return fragmentGamePlayer.getPlayers();
    }

    public void savePlayers() {
        fragmentGamePlayer.savePlayers();
    }
}
