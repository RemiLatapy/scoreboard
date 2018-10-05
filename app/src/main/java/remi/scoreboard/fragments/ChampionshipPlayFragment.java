package remi.scoreboard.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import remi.scoreboard.R;
import remi.scoreboard.activities.GameActivity;
import remi.scoreboard.activities.GameChampionshipActivity;
import remi.scoreboard.model.ChampionshipPlayer;
import remi.scoreboard.model.Match;
import remi.scoreboard.model.MatchDay;

import static remi.scoreboard.activities.MainActivity.ROTATION_NUMBER;

public class ChampionshipPlayFragment extends Fragment {

    private LinearLayout cardContainerView;

    private TextView textviewMatchdayNum;

    private TextView textviewPlayerOneName;
    private TextView textviewPlayerTwoName;

    private TextView textviewPlayerOneScore;
    private TextView textviewPlayerTwoScore;

    private ArrayList<MatchDay> championship;
    private ArrayList<ChampionshipPlayer> playerList;

    OnDataChange activityCallback;

    public static ChampionshipPlayFragment newInstance(GameChampionshipActivity ctx) {
        ChampionshipPlayFragment championshipPlayFragment = new ChampionshipPlayFragment();

        Bundle args = new Bundle();
        ArrayList<String> playerNameList = ctx.getPlayersName();
        args.putStringArrayList("playerNameList", playerNameList);
        args.putInt(ROTATION_NUMBER, ctx.getIntent().getIntExtra(ROTATION_NUMBER, 1));
        championshipPlayFragment.setArguments(args);
        return championshipPlayFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            createPlayers();
            if (playerList.size() == 0) {
                return;
            }

            generateChampionship();
        } else {
            championship = savedInstanceState.getParcelableArrayList("championship");
            playerList = savedInstanceState.getParcelableArrayList("playerList");
        }
    }

    private void updateActivityData() {
        activityCallback.onPlayerListChange(playerList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gameview_cards, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardContainerView = view.findViewById(R.id.card_container);
        addAllMatchdayView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("championship", championship);
        outState.putParcelableArrayList("playerList", playerList);
    }

    private void createPlayers() {
        ArrayList<String> playerNameList = getArguments().getStringArrayList("playerNameList");
        assert playerNameList != null;
        playerList = new ArrayList<>();
        for (int i = 0; i < playerNameList.size(); i++) {
            playerList.add(new ChampionshipPlayer(playerNameList.get(i), (i + 1)));
        }
        if (playerList.size() % 2 != 0) {
            playerList.add(new ChampionshipPlayer("Bye", playerList.size() + 1));
        }
    }

    private void generateChampionship() {
        championship = new ArrayList<>();

        int numDays = (playerList.size() - 1); // Days needed to complete championship

        List<ChampionshipPlayer> players = new ArrayList<>(playerList); // Copy list
        players.remove(0);  // Remove first player, to fix it

        int playersSize = players.size();

        for (int r = 0; r < getArguments().getInt(ROTATION_NUMBER, 1); r++) {
            for (int day = 0; day < numDays; day++) {
                MatchDay matchDay = new MatchDay();

                int playerIdx = day % playersSize;

                matchDay.add(new Match(players.get(playerIdx), playerList.get(0)));

                for (int idx = 1; idx < playerList.size() / 2; idx++) {
                    int firstPlayer = (day + idx) % playersSize;
                    int secondPlayer = (day + playersSize - idx) % playersSize;

                    matchDay.add(new Match(players.get(firstPlayer), players.get(secondPlayer)));
                }

                championship.add(matchDay);
            }
        }
    }

    private void addAllMatchdayView() {
        View view;
        for (int i = 0; i < championship.size(); i++) {
            for (int j = 0; j < championship.get(i).size(); j++) {
                view = getActivity().getLayoutInflater().inflate(R.layout.item_card_matchday, cardContainerView, false);
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
        if (match.isFinished()) {
            textviewPlayerOneScore.setText(String.valueOf(match.getScorePlayerOne()));
            textviewPlayerTwoScore.setText(String.valueOf(match.getScorePlayerTwo()));
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
                View dialogMatchScore = getActivity().getLayoutInflater().inflate(R.layout.dialog_match_score, (ViewGroup) v.getRootView(), false);
                ((TextView) dialogMatchScore.findViewById(R.id.player1_name)).setText(currentMatch.getPlayerOne().getName());
                ((TextView) dialogMatchScore.findViewById(R.id.player2_name)).setText(currentMatch.getPlayerTwo().getName());
                (new AlertDialog.Builder(getActivity()))
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

                ((ChampionshipPlayer) currentMatch.getPlayerOne()).addGoalAverage(currentMatch.getScorePlayerOne() - currentMatch.getScorePlayerTwo());
                ((ChampionshipPlayer) currentMatch.getPlayerTwo()).addGoalAverage(currentMatch.getScorePlayerTwo() - currentMatch.getScorePlayerOne());

                if (currentMatch.getScorePlayerOne() > currentMatch.getScorePlayerTwo()) {
                    ((ChampionshipPlayer) currentMatch.getPlayerOne()).addVictories();
                    ((ChampionshipPlayer) currentMatch.getPlayerTwo()).addDefeats();
                } else {
                    ((ChampionshipPlayer) currentMatch.getPlayerTwo()).addVictories();
                    ((ChampionshipPlayer) currentMatch.getPlayerOne()).addDefeats();
                }

                ((ChampionshipPlayer) currentMatch.getPlayerOne()).addGamesPlayed();
                ((ChampionshipPlayer) currentMatch.getPlayerTwo()).addGamesPlayed();

                updateActivityData();
                updateViews(currentMatch, currentMatchday);
            }
        };
    }

    public interface OnDataChange {
        public void onPlayerListChange(ArrayList<ChampionshipPlayer> playerList);

        public void onChampionshipUpdated(ArrayList<MatchDay> championship);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityCallback = (OnDataChange) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
