package remi.scoreboard.activities;

import java.util.ArrayList;

import remi.scoreboard.fragments.RankingFragment;
import remi.scoreboard.fragments.ChampionshipPlayFragment;
import remi.scoreboard.model.ChampionshipPlayer;
import remi.scoreboard.model.MatchDay;

public class GameChampionshipActivity extends GameActivity implements ChampionshipPlayFragment.OnDataChange {

    public static final String ROTATION_NUMBER = "remi.scoreboard.activities.rotationnumber";

    public RankingFragment rankingFragment;

    @Override
    public void onPlayerListChange(ArrayList<ChampionshipPlayer> playerList) {
        rankingFragment.setPlayers(playerList);
        rankingFragment.setScore();
    }

    @Override
    public void onChampionshipUpdated(ArrayList<MatchDay> championship) {

    }
}
