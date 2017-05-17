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

    final int NUM_FRAGS = 3;
    private Context ctx;
    private FragmentGamePlayer fragmentGamePlayer;
    private FragmentGameChooser fragmentGameChooser;
    private FragmentGameRules fragmentGameRules;

    public SetupGamePagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                fragmentGameChooser = (FragmentGameChooser) FragmentGameChooser.instantiate(ctx, FragmentGameChooser.class.getName());
                return fragmentGameChooser;
            case 1:
                fragmentGamePlayer = (FragmentGamePlayer) FragmentGamePlayer.instantiate(ctx, FragmentGamePlayer.class.getName());
                return fragmentGamePlayer;
            case 2:
                fragmentGameRules = (FragmentGameRules) FragmentGameRules.instantiate(ctx, FragmentGameRules.class.getName());
                ;
                return fragmentGameRules;
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

    public String getRule() {
        if (fragmentGameRules == null) {
            Log.d("SetupGamePagerAdapter", "fragmentGameRules == null");
        }
        return fragmentGameRules.getRule();
    }

    public void savePlayers() {
        fragmentGamePlayer.savePlayers();
    }
}
