package remi.scoreboard.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.model.Player;

public class PhaseDixPlay extends AppCompatActivity {

    TextView playerNum;
    TextView playerName;
    TextView playerPhase;
    TextView playerPoints;
    private LinearLayout playerContainerView;
    private ArrayList<String> playersName;
    private Toolbar toolbar;
    private ArrayList<Player> playerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase_dix);

        playerContainerView = (LinearLayout) findViewById(R.id.card_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        playersName = getIntent().getStringArrayListExtra(MainActivity.PLAYERS);

        createPlayers();
        setupToolbar();
        addAllPlayersView();
    }

    private void createPlayers() {
        playerList = new ArrayList<>();
        for (int i = 0; i < playersName.size(); i++) {
            playerList.add(new Player(playersName.get(i), (i + 1)));
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void addAllPlayersView() {
        View view;
        for (int i = 0; i < playersName.size(); i++) {
            view = getLayoutInflater().inflate(R.layout.item_card_player, playerContainerView, false);
            findViews(view);
            fillTextViews(i);
            final Player currentPlayer = playerList.get(i);
            buildAlertDialog(view, currentPlayer);
            playerContainerView.addView(view);
        }
    }

    private void buildAlertDialog(View view, final Player currentPlayer) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new AlertDialog.Builder(PhaseDixPlay.this))
                        .setCancelable(true)
                        .setTitle(currentPlayer.getName())
                        .setView(getLayoutInflater().inflate(R.layout.dialog_player_score, (ViewGroup) v.getRootView(), false))
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
                    currentPlayer.addPoints(Integer.parseInt(pointsText));
                }
                if (((CheckBox) ((AlertDialog) dialog).findViewById(R.id.phase)).isChecked()) {
                    currentPlayer.validPhase();
                }
                updateViews(currentPlayer);
            }
        };
    }

    private void updateViews(Player player) {
        findViews(playerContainerView.getChildAt(player.getNum() - 1));
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
