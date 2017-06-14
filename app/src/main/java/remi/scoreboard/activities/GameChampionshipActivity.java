package remi.scoreboard.activities;

import java.util.ArrayList;

import remi.scoreboard.fragments.RankingFragment;
import remi.scoreboard.fragments.SquashPlayFragment;
import remi.scoreboard.model.MatchDay;
import remi.scoreboard.model.Player;

public class GameChampionshipActivity extends GameActivity implements SquashPlayFragment.OnDataChange {

    public RankingFragment rankingFragment;

    @Override
    public void onPlayerListChange(ArrayList<Player> playerList) {
        rankingFragment.setPlayers(playerList);
        rankingFragment.setScore();
    }

    @Override
    public void onChampionshipUpdated(ArrayList<MatchDay> championship) {

    }
}
