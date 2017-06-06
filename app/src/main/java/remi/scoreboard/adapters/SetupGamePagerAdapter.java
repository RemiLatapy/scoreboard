package remi.scoreboard.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.activities.MainActivity;
import remi.scoreboard.fragments.GameChooserFragment;
import remi.scoreboard.fragments.GameCustomizeFragment;
import remi.scoreboard.fragments.GamePlayerFragment;
import remi.scoreboard.fragments.SquashGameCustomizeFragment;

public class SetupGamePagerAdapter extends FragmentPagerAdapter {

    private final int NUM_FRAGS = 3;
    private long baseId = 0;
    private Context ctx;
    private GamePlayerFragment gamePlayerFragment;
    private GameChooserFragment gameChooserFragment;
    private GameCustomizeFragment gameCustomizeFragment;

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
                String frag = "";
                if(((MainActivity) ctx).currentGameName.equals(ctx.getString(R.string.game_name_squash)))
                {
                     frag = SquashGameCustomizeFragment.class.getName();
                }
                else if(((MainActivity) ctx).currentGameName.equals(ctx.getString(R.string.game_name_phase_dix)))
                {
                    frag = GameCustomizeFragment.class.getName();
                }
                if(!frag.isEmpty()) {
                    gameCustomizeFragment = (GameCustomizeFragment) GameCustomizeFragment.instantiate(ctx, frag);
                }
                Log.d("FRAG", frag);
                return gameCustomizeFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_FRAGS;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public long getItemId(int position) {
        return baseId + position;
    }

    public ArrayList<String> getPlayers() {
        if (gamePlayerFragment == null) {
            Log.d("SetupGamePagerAdapter", "gamePlayerFragment == null");
        }
        return gamePlayerFragment.getPlayers();
    }

    public String getRule() {
        if (gameCustomizeFragment == null) {
            Log.d("SetupGamePagerAdapter", "gameCustomizeFragment == null");
        }
        return gameCustomizeFragment.getRule();
    }

    public void savePlayers() {
        gamePlayerFragment.savePlayers();
    }

    /**
     * Notify that the position of a fragment has been changed.
     * Create a new ID for each position to force recreation of the fragment
     * @param n number of items which have been changed
     */
    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }
}
