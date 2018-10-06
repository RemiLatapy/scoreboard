package remi.scoreboard.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import remi.scoreboard.R;
import remi.scoreboard.activities.GameActivity;
import remi.scoreboard.model.Player;
import remi.scoreboard.model.SimpleScorePlayer;

public class SimpleScorePlayFragment extends Fragment {

    protected LinearLayout cardContainerView;
    protected TextView playerPoints;
    protected ArrayList<SimpleScorePlayer> playerList;
    protected SortMode sortMode = SortMode.num;
    private TextView playerName;
    private TextView playerNum;

    public static SimpleScorePlayFragment newInstance(GameActivity activity) {
        SimpleScorePlayFragment simpleScorePlayFragment = new SimpleScorePlayFragment();

        Bundle args = new Bundle();
        ArrayList<String> playerNameList = activity.getPlayersName();
        args.putStringArrayList("playerNameList", playerNameList);
        simpleScorePlayFragment.setArguments(args);

        return simpleScorePlayFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            createPlayers();
        } else {
            playerList = savedInstanceState.getParcelableArrayList("playerList");
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_sort).setVisible(true);
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
        addAllPlayersView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("playerList", playerList);
    }

    protected void createPlayers() {
        ArrayList<String> playerNameList = getArguments().getStringArrayList("playerNameList");
        assert playerNameList != null;
        playerList = new ArrayList<>();
        for (int i = 0; i < playerNameList.size(); i++) {
            playerList.add(new SimpleScorePlayer(playerNameList.get(i), (i + 1)));
        }
    }

    protected void addAllPlayersView() {
        View view;

        ArrayList<SimpleScorePlayer> sortedPlayerList = playerList;
        if (sortMode == SortMode.rank)
            Collections.sort(sortedPlayerList);
        else if (sortMode == SortMode.num)
            Collections.sort(sortedPlayerList, Player.PlayerNumComparator);

        for (SimpleScorePlayer player : sortedPlayerList) {
            view = getActivity().getLayoutInflater().inflate(getCardRes(), cardContainerView, false);
            findViews(view);
            fillTextViews(player);
            buildAlertDialog(view, player);
            cardContainerView.addView(view);
        }
    }

    protected void refreshAllPlayersView() {
        cardContainerView.removeAllViews();
        addAllPlayersView();
    }

    protected void buildAlertDialog(View view, final Player currentPlayer) {
        view.setOnClickListener(v -> {
            AlertDialog scoreDialog = new AlertDialog.Builder(getActivity())
                    .setCancelable(true)
                    .setTitle(currentPlayer.getName())
                    .setView(getActivity().getLayoutInflater().inflate(getPlayerScoreDialogRes(), (ViewGroup) v.getRootView(), false))
                    .setPositiveButton("Valider", null)
                    .setNegativeButton("Annuler", (d, which) -> d.dismiss())
                    .create();

            // Define positive action here to be able to not dismiss dialog
            // https://stackoverflow.com/a/7636468/9994620
            scoreDialog.setOnShowListener(dialog -> {
                Button button = scoreDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(view1 -> {
                    if (checkScoreValues(scoreDialog)) {
                        updatePlayerModel(currentPlayer, scoreDialog);
                        if (sortMode == SortMode.rank)
                            refreshAllPlayersView(); // ensure rank order
                        else if (sortMode == SortMode.num)
                            updateViews(v, currentPlayer); // only update concerned player's view
                        scoreDialog.dismiss();
                    }
                });
            });

            scoreDialog.show();
        });
    }

    protected void updatePlayerModel(Player currentPlayer, AlertDialog scoreDialog) {
        String pointsText = ((EditText) scoreDialog.findViewById(R.id.points)).getText().toString();

        if (!pointsText.isEmpty()) {
            ((SimpleScorePlayer) currentPlayer).addPoints(Integer.parseInt(pointsText));
        }
    }

    protected boolean checkScoreValues(AlertDialog scoreDialog) {
        return true;
    }

    @NonNull
    protected DialogInterface.OnClickListener getValidateListener(final Player currentPlayer, final View playerView) {
        return (dialog, which) -> {


        };
    }

    protected void updateViews(View playerView, Player player) {
        findViews(playerView);
        playerPoints.setText(String.format("%d points", ((SimpleScorePlayer) player).getScore()));
    }

    protected void fillTextViews(Player player) {
        playerNum.setText(String.format("Joueur %d", player.getNum()));
        playerName.setText(player.getName());
        playerPoints.setText(String.format("%d points", ((SimpleScorePlayer) player).getScore()));
    }

    protected void findViews(View view) {
        playerNum = view.findViewById(R.id.player_num);
        playerName = view.findViewById(R.id.player_name);
        playerPoints = view.findViewById(R.id.player_points);
    }

    protected int getCardRes() {
        return R.layout.item_card_player_simple_score;
    }

    protected int getPlayerScoreDialogRes() {
        return R.layout.dialog_player_simple_score_score;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            sortMode = sortMode == SortMode.num ? SortMode.rank : SortMode.num;
            refreshAllPlayersView();
            Toast.makeText(getContext(),
                    String.format("Sort by %s", sortMode == SortMode.rank ? "score" : "player"),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected enum SortMode {
        num, rank
    }
}
