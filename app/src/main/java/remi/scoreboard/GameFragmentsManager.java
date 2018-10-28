//package remi.scoreboard;
//
//import android.content.Context;
//import android.support.v4.app.Fragment;
//
//import remi.scoreboard.jetpack.activities.GameActivity;
//import remi.scoreboard.jetpack.activities.GameChampionshipActivity;
//import remi.scoreboard.jetpack.activities.GameSimpleScoreActivity;
//import remi.scoreboard.fragments.PhaseDixPlayFragment;
//import remi.scoreboard.fragments.RankingFragment;
//import remi.scoreboard.fragments.GameFragment;
//import remi.scoreboard.fragments.ChampionshipPlayFragment;
//
//public final class GameFragmentsManager {
//
//    private GameFragmentsManager() {
//    }
//
//    public static Fragment getFragmentAtPosition(GameActivity gameActivity, int pos) {
//        if (gameActivity instanceof GameChampionshipActivity) {
//            if (pos == 0)
//                return ChampionshipPlayFragment.newInstance((GameChampionshipActivity)gameActivity);
//            else if (pos == 1)
//                return RankingFragment.newInstance();
//        } else if (gameActivity instanceof GameSimpleScoreActivity) {
//            if (pos == 0) {
//                if (gameActivity.getGameName().equals(gameActivity.getString(R.string.game_name_phase_dix)))
//                    return PhaseDixPlayFragment.newInstance(gameActivity);
//                else
//                    return GameFragment.newInstance(gameActivity);
//            }
//        }
//
//        throw new Error("No fragment for game " + gameActivity.getClass().toString() + " at position " + String.valueOf(pos));
//    }
//
//    public static int getFragmentsCount(Context ctx) {
//        if (ctx instanceof GameChampionshipActivity) {
//            return 2;
//        } else if (ctx instanceof GameSimpleScoreActivity) {
//            return 1;
//        }
//
//        throw new Error("No activity: " + ctx.getClass().toString());
//    }
//}
