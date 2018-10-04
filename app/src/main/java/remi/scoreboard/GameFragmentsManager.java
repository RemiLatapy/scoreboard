package remi.scoreboard;

import android.content.Context;
import android.support.v4.app.Fragment;

import remi.scoreboard.activities.GameActivity;
import remi.scoreboard.activities.GameChampionshipActivity;
import remi.scoreboard.activities.GameSimpleScoreActivity;
import remi.scoreboard.fragments.RankingFragment;
import remi.scoreboard.fragments.SimpleScorePlayFragment;
import remi.scoreboard.fragments.ChampionshipPlayFragment;

public final class GameFragmentsManager {

    private GameFragmentsManager() { }

    public static Fragment getFragmentAtPosition(GameActivity ctx, int pos)
    {
        if(ctx instanceof GameChampionshipActivity)
        {
            if(pos == 0)
                return ChampionshipPlayFragment.newInstance(ctx);
            else if(pos == 1)
                return RankingFragment.newInstance();
        }
        else if(ctx instanceof GameSimpleScoreActivity)
        {
            if (pos == 0)
                return SimpleScorePlayFragment.newInstance(ctx);
        }

        throw new Error("No fragment for game " + ctx.getClass().toString() + " at position " + String.valueOf(pos));
    }

    public static int getFragmentsCount(Context ctx)
    {
        if(ctx instanceof GameChampionshipActivity)
        {
            return 2;
        }
        else if(ctx instanceof GameSimpleScoreActivity)
        {
            return 1;
        }

        throw new Error("No activity: " + ctx.getClass().toString());
    }
}
