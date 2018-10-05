package remi.scoreboard.fragments;

import android.app.AlertDialog;
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

import remi.scoreboard.R;
import remi.scoreboard.activities.GameActivity;
import remi.scoreboard.model.Player;
import remi.scoreboard.model.SimpleScorePlayer;


public class SimpleScorePlayFragment extends Fragment {

    protected LinearLayout cardContainerView;

    protected TextView playerPoints;
    protected ArrayList<SimpleScorePlayer> playerList;

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
        if (savedInstanceState == null) {
            createPlayers();
        } else {
            playerList = savedInstanceState.getParcelableArrayList("playerList");
        }
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
        for (int i = 0; i < playerList.size(); i++) {
            view = getActivity().getLayoutInflater().inflate(getCardRes(), cardContainerView, false);
            findViews(view);
            fillTextViews(playerList.get(i));
            buildAlertDialog(view, playerList.get(i));
            cardContainerView.addView(view);
        }
    }

    protected void buildAlertDialog(View view, final Player currentPlayer) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new AlertDialog.Builder(getActivity()))
                        .setCancelable(true)
                        .setTitle(currentPlayer.getName())
                        .setView(getActivity().getLayoutInflater().inflate(getPlayerScoreDialogRes(), (ViewGroup) v.getRootView(), false))
                        .setPositiveButton("Valider", getValidateListener(currentPlayer))
                        .setNegativeButton("Annuler", getCancelListener())
                        .show();
            }
        });
    }

    @NonNull
    protected DialogInterface.OnClickListener getCancelListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
    }

    @NonNull
    protected DialogInterface.OnClickListener getValidateListener(final Player currentPlayer) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pointsText = ((EditText) ((AlertDialog) dialog).findViewById(R.id.points)).getText().toString();
                if (!pointsText.isEmpty()) {
                    ((SimpleScorePlayer) currentPlayer).addPoints(Integer.parseInt(pointsText));
                }
                updateViews(((SimpleScorePlayer) currentPlayer));
            }
        };
    }

    protected void updateViews(Player player) {
        findViews(cardContainerView.getChildAt(player.getNum() - 1));
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
}
