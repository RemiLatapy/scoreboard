package remi.scoreboard.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import remi.scoreboard.R;
import remi.scoreboard.model.Player;
import remi.scoreboard.model.SimpleScorePlayer;

public class GameFragment extends Fragment {

    protected LinearLayout cardContainerView;
    protected ArrayList<SimpleScorePlayer> playerList;
    protected SortMode sortMode = SortMode.num;

    // TODO remove these var
    protected TextView playerPoints;
    private TextView playerName;
    private TextView playerNum;

//    public static GameFragment newInstance(GameActivity activity) {
//        GameFragment gameFragment = new GameFragment();
//
//        Bundle args = new Bundle();
//        ArrayList<String> playerNameList = activity.getPlayersName();
//        args.putStringArrayList("playerNameList", playerNameList);
//        gameFragment.setArguments(args);
//
//        return gameFragment;
//    }

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
        return inflater.inflate(R.layout.fragment_game_play, container, false);
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
                    .setTitle("Add score to " + currentPlayer.getName())
                    .setView(getActivity().getLayoutInflater().inflate(getPlayerScoreDialogRes(), (ViewGroup) v.getRootView(), false))
                    .setPositiveButton("Valider", null)
                    .setNegativeButton("Annuler", null)
                    .create();

            // Define positive action here to be able to not dismiss dialog
            // https://stackoverflow.com/a/7636468/9994620
            scoreDialog.setOnShowListener(dialog -> {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(view1 -> {
                    if (checkScoreValues(scoreDialog)) {
                        updatePlayerModel(currentPlayer, (AlertDialog) dialog);
                        if (sortMode == SortMode.rank)
                            refreshAllPlayersView(); // ensure rank order
                        else if (sortMode == SortMode.num)
                            updateViews(v, currentPlayer); // only update concerned player's view
                        dialog.dismiss();
                    }
                });
            });

            scoreDialog.show();
        });

        view.setOnLongClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setCancelable(true)
                    .setTitle("Fix score of " + currentPlayer.getName())
                    .setView(getActivity().getLayoutInflater().inflate(getPlayerScoreFixingDialogRes(), (ViewGroup) v.getRootView(), false))
                    .setPositiveButton("Valider", (d, w) -> {
                        fixPlayerModel(currentPlayer, (AlertDialog) d);
                        if (sortMode == SortMode.rank)
                            refreshAllPlayersView(); // ensure rank order
                        else if (sortMode == SortMode.num)
                            updateViews(v, currentPlayer); // only update concerned player's view
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
            fillFixScoreDialogViews(currentPlayer, dialog);
            return true;
        });
    }

    protected void updatePlayerModel(Player currentPlayer, AlertDialog scoreDialog) {
        String pointsText = ((EditText) scoreDialog.findViewById(R.id.points)).getText().toString();
        if (!pointsText.isEmpty()) {
            ((SimpleScorePlayer) currentPlayer).addPoints(Integer.parseInt(pointsText));
        }
    }

    protected void fixPlayerModel(Player currentPlayer, AlertDialog scoreDialog) {
        String pointsText = ((EditText) scoreDialog.findViewById(R.id.points)).getText().toString();
        if (!pointsText.isEmpty()) {
            ((SimpleScorePlayer) currentPlayer).setPoints(Integer.parseInt(pointsText));
        }
    }

    protected boolean checkScoreValues(AlertDialog scoreDialog) {
        return true;
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

    protected void fillFixScoreDialogViews(Player player, AlertDialog dialog) {
        ((EditText) dialog.findViewById(R.id.points))
                .setText(String.valueOf(((SimpleScorePlayer) player).getScore()));
    }

    protected int getCardRes() {
        return R.layout.item_card_player;
    }

    protected int getPlayerScoreDialogRes() {
        return R.layout.dialog_player_simple_score_score;
    }

    protected int getPlayerScoreFixingDialogRes() {
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
