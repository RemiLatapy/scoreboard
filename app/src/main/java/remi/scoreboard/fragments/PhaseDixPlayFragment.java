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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.activities.GameActivity;
import remi.scoreboard.model.PhaseDixPlayer;
import remi.scoreboard.model.Player;

public class PhaseDixPlayFragment extends Fragment {

    TextView playerNum;
    TextView playerName;
    TextView playerPhase;
    TextView playerPoints;

    ArrayList<Player> playerList;
    protected LinearLayout cardContainerView;

    public static PhaseDixPlayFragment newInstance(GameActivity activity)
    {
        PhaseDixPlayFragment phaseDixPlayFragment = new PhaseDixPlayFragment();

        Bundle args = new Bundle();
        ArrayList<String> playerNameList = activity.getPlayersName();
        args.putStringArrayList("playerNameList", playerNameList);
        phaseDixPlayFragment.setArguments(args);

        return phaseDixPlayFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            createPlayers();
        }
        else
        {
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

    private void createPlayers() {
        ArrayList<String> playerNameList = getArguments().getStringArrayList("playerNameList");
        assert playerNameList != null;
        playerList = new ArrayList<>();
        for (int i = 0; i < playerNameList.size(); i++) {
            playerList.add(new PhaseDixPlayer(playerNameList.get(i), (i + 1)));
        }
    }

    private void addAllPlayersView() {
        View view;
        for (int i = 0; i < playerList.size(); i++) {
            view = getActivity().getLayoutInflater().inflate(R.layout.item_card_player_phase10, cardContainerView, false);
            findViews(view);
            fillTextViews(i);
            buildAlertDialog(view, playerList.get(i));
            cardContainerView.addView(view);
        }
    }

    private void buildAlertDialog(View view, final Player currentPlayer) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new AlertDialog.Builder(getActivity()))
                        .setCancelable(true)
                        .setTitle(currentPlayer.getName())
                        .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_player_phase10_score, (ViewGroup) v.getRootView(), false))
                        .setPositiveButton("Valider", getValidateListener(currentPlayer))
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
    private DialogInterface.OnClickListener getValidateListener(final Player currentPlayer) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pointsText = ((EditText) ((AlertDialog) dialog).findViewById(R.id.points)).getText().toString();
                if (!pointsText.isEmpty()) {
                    ((PhaseDixPlayer) currentPlayer).addPoints(Integer.parseInt(pointsText));
                }
                if (((CheckBox) ((AlertDialog) dialog).findViewById(R.id.phase)).isChecked()) {
                    ((PhaseDixPlayer) currentPlayer).validPhase();
                }
                updateViews(((PhaseDixPlayer) currentPlayer));
            }
        };
    }

    private void updateViews(PhaseDixPlayer player) {
        findViews(cardContainerView.getChildAt(player.getNum() - 1));
        playerPhase.setText("Phase " + player.getPhase());
        playerPoints.setText(player.getPoints() + " points");
    }

    private void fillTextViews(int i) {
        playerNum.setText("Joueur " + (i + 1));
        playerName.setText(playerList.get(i).getName());
        playerPhase.setText("Phase 1");
        playerPoints.setText("0 points");
    }

    private void findViews(View view) {
        playerNum = view.findViewById(R.id.player_num);
        playerName = view.findViewById(R.id.player_name);
        playerPhase = view.findViewById(R.id.player_phase);
        playerPoints = view.findViewById(R.id.player_points);
    }
}
