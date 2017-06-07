package remi.scoreboard.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.model.PhaseDixPlayer;
import remi.scoreboard.model.Player;

// TODO fragmentization like Squash
public class PhaseDixPlayActivity extends GameActivity {

    TextView playerNum;
    TextView playerName;
    TextView playerPhase;
    TextView playerPoints;

    ArrayList<Player> playerList;
    protected LinearLayout cardContainerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createPlayers();
        addAllPlayersView();
    }

    private void createPlayers() {
        playerList = new ArrayList<>();
        for (int i = 0; i < playersName.size(); i++) {
            playerList.add(new PhaseDixPlayer(playersName.get(i), (i + 1)));
        }
    }

    private void addAllPlayersView() {
        View view;
        for (int i = 0; i < playersName.size(); i++) {
            view = getLayoutInflater().inflate(R.layout.item_card_player, cardContainerView, false);
            findViews(view);
            fillTextViews(i);
            final Player currentPlayer = playerList.get(i);
            buildAlertDialog(view, currentPlayer);
            cardContainerView.addView(view);
        }
    }

    private void buildAlertDialog(View view, final Player currentPlayer) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new AlertDialog.Builder(PhaseDixPlayActivity.this))
                        .setCancelable(true)
                        .setTitle(currentPlayer.getName())
                        .setView(getLayoutInflater().inflate(R.layout.dialog_player_phase10_score, (ViewGroup) v.getRootView(), false))
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
        playerName.setText(playersName.get(i));
        playerPhase.setText("Phase 1");
        playerPoints.setText("0 points");
    }

    private void findViews(View view) {
        playerNum = (TextView) view.findViewById(R.id.player_num);
        playerName = (TextView) view.findViewById(R.id.player_name);
        playerPhase = (TextView) view.findViewById(R.id.player_phase);
        playerPoints = (TextView) view.findViewById(R.id.player_points);
    }
}
