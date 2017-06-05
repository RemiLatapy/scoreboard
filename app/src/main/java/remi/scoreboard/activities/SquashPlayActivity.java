package remi.scoreboard.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import remi.scoreboard.R;
import remi.scoreboard.model.Match;
import remi.scoreboard.model.Player;

public class SquashPlayActivity extends GameActivity {

    // Typedef
    private class MatchDay extends ArrayList<Match> {}
    private class Championship extends ArrayList<MatchDay> {}

    private TextView textviewMatchdayNum;

    private TextView textviewPlayerOneName;
    private TextView textviewPlayerTwoName;

    private TextView textviewPlayerOneScore;
    private TextView textviewPlayerTwoScore;

    private Championship championship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createPlayers();
        if(playerList.size() == 0)
        {
            return;
        }
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

            matchDay.add(new Match(players.get(playerIdx), playerList.get(0)));

            for (int idx = 1; idx < playerList.size() / 2; idx++)
            {
                int firstPlayer = (day + idx) % playersSize;
                int secondPlayer = (day  + playersSize - idx) % playersSize;

                matchDay.add(new Match(players.get(firstPlayer), players.get(secondPlayer)));
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
                buildAlertDialog(view, championship.get(i).get(j), championship.get(i));
                cardContainerView.addView(view);
            }
        }
    }

    private void updateViews(Match currentMatch, MatchDay currentMatchday) {
        findViews(cardContainerView.getChildAt(championship.indexOf(currentMatchday) * currentMatchday.size() + currentMatchday.indexOf(currentMatch)));
        textviewPlayerOneScore.setText(String.valueOf(currentMatch.getScorePlayerOne()));
        textviewPlayerTwoScore.setText(String.valueOf(currentMatch.getScorePlayerTwo()));
    }

    private void fillTextViews(int matchdayNum, int matchNum) {
        textviewMatchdayNum.setText("Matchday " + String.valueOf(matchdayNum + 1));
        Match match = championship.get(matchdayNum).get(matchNum);
        textviewPlayerOneName.setText(match.getPlayerOne().getName());
        textviewPlayerTwoName.setText(match.getPlayerTwo().getName());
        if(match.isFinished())
        {
            textviewPlayerOneScore.setText(match.getScorePlayerOne());
            textviewPlayerTwoScore.setText(match.getScorePlayerTwo());
        }
    }

    private void findViews(View view) {
        textviewMatchdayNum = (TextView) view.findViewById(R.id.matchday_num);
        textviewPlayerOneName = (TextView) view.findViewById(R.id.player1_name);
        textviewPlayerTwoName = (TextView) view.findViewById(R.id.player2_name);
        textviewPlayerOneScore = (TextView) view.findViewById(R.id.player1_score);
        textviewPlayerTwoScore = (TextView) view.findViewById(R.id.player2_score);
    }

    private void buildAlertDialog(View view, final Match currentMatch, final MatchDay currentMatchday) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogMatchScore = getLayoutInflater().inflate(R.layout.dialog_match_score, (ViewGroup) v.getRootView(), false);
                ((TextView)dialogMatchScore.findViewById(R.id.player1_name)).setText(currentMatch.getPlayerOne().getName());
                ((TextView)dialogMatchScore.findViewById(R.id.player2_name)).setText(currentMatch.getPlayerTwo().getName());
                (new AlertDialog.Builder(SquashPlayActivity.this))
                        .setCancelable(true)
                        .setTitle("Matchday " + String.valueOf(championship.indexOf(currentMatchday) + 1))
                        .setView(dialogMatchScore)
                        .setPositiveButton("Valider", getValidateListener(currentMatch, currentMatchday))
                        .setNegativeButton("Annuler", getCancelListener())
                        .show();
            }
        });
    }

    @NonNull
    private DialogInterface.OnClickListener getCancelListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener getValidateListener(final Match currentMatch, final MatchDay currentMatchday) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String scorePlayerOne = ((EditText) ((AlertDialog) dialog).findViewById(R.id.player1_score)).getText().toString();
                String scorePlayerTwo = ((EditText) ((AlertDialog) dialog).findViewById(R.id.player2_score)).getText().toString();

                if (!scorePlayerOne.isEmpty() && !scorePlayerTwo.isEmpty()) {
                    currentMatch.setScorePlayerOne(Integer.parseInt(scorePlayerOne));
                    currentMatch.setScorePlayerTwo(Integer.parseInt(scorePlayerTwo));
                }

                updateViews(currentMatch, currentMatchday);
            }
        };
    }
}
