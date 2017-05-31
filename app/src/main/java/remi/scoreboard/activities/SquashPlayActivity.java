package remi.scoreboard.activities;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import remi.scoreboard.R;
import remi.scoreboard.model.Player;

/**
 * Created by remil on 31/05/2017.
 */

public class SquashPlayActivity extends GameActivity {

    // Typedef
    private class MatchDay extends ArrayList<Pair<Player, Player>> {}
    private class Championship extends ArrayList<MatchDay> {}

    private TextView textviewMatchdayNum;
    private TextView textviewPlayerOneName;
    private TextView textviewPlayerTwoName;

    private Championship championship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createPlayers();
        generateChampionship();
        addAllMatchdayView();
    }

    private void createPlayers() {
        playerList = new ArrayList<>();
        for (int i = 0; i < playersName.size(); i++) {
            playerList.add(new Player(playersName.get(i), (i + 1)));
        }
        if(playerList.size()%2 != 0)
        {
            playerList.add(new Player("Bye", playerList.size()+1));
        }
    }

    private void generateChampionship() {
        championship = new Championship();

        int numDays = (playerList.size() - 1); // Days needed to complete championship

        List<Player> players = new ArrayList<>(playerList); // Copy list
        players.remove(0);  // Remove first player, to fix it

        int playersSize = players.size();

        for (int day = 0; day < numDays; day++)
        {
            MatchDay matchDay = new MatchDay();

            int playerIdx = day % playersSize;

            matchDay.add(new Pair<>(players.get(playerIdx), playerList.get(0)));

            for (int idx = 1; idx < playerList.size() / 2; idx++)
            {
                int firstPlayer = (day + idx) % playersSize;
                int secondPlayer = (day  + playersSize - idx) % playersSize;

                matchDay.add(new Pair<>(players.get(firstPlayer), players.get(secondPlayer)));
            }

            championship.add(matchDay);
        }
    }

    private void addAllMatchdayView() {
        View view;
        for (int i = 0; i < championship.size(); i++) {
            for (int j = 0; j < championship.get(i).size(); j++) {
                view = getLayoutInflater().inflate(R.layout.item_card_matchday, cardContainerView, false);
                findViews(view);
                fillTextViews(i, j);
                cardContainerView.addView(view);
            }
        }
    }

    private void fillTextViews(int matchdayNum, int matchNum) {
        textviewMatchdayNum.setText("Matchday " + (matchdayNum + 1));
        textviewPlayerOneName.setText(championship.get(matchdayNum).get(matchNum).first.getName());
        textviewPlayerTwoName.setText(championship.get(matchdayNum).get(matchNum).second.getName());
    }

    private void findViews(View view) {
        textviewMatchdayNum = (TextView) view.findViewById(R.id.matchday_num);
        textviewPlayerOneName = (TextView) view.findViewById(R.id.player1_name);
        textviewPlayerTwoName = (TextView) view.findViewById(R.id.player2_name);
    }
}
