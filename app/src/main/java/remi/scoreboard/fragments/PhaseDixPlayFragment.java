package remi.scoreboard.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.activities.GameActivity;
import remi.scoreboard.model.PhaseDixPlayer;
import remi.scoreboard.model.Player;

public class PhaseDixPlayFragment extends SimpleScorePlayFragment {

    TextView playerPhase;

    public static PhaseDixPlayFragment newInstance(GameActivity activity) {
        PhaseDixPlayFragment phaseDixPlayFragment = new PhaseDixPlayFragment();

        Bundle args = new Bundle();
        ArrayList<String> playerNameList = activity.getPlayersName();
        args.putStringArrayList("playerNameList", playerNameList);
        phaseDixPlayFragment.setArguments(args);

        return phaseDixPlayFragment;
    }

    @Override
    protected void createPlayers() {
        ArrayList<String> playerNameList = getArguments().getStringArrayList("playerNameList");
        assert playerNameList != null;
        playerList = new ArrayList<>();
        for (int i = 0; i < playerNameList.size(); i++) {
            playerList.add(new PhaseDixPlayer(playerNameList.get(i), (i + 1)));
        }
    }

    @NonNull
    @Override
    protected DialogInterface.OnClickListener getValidateListener(final Player currentPlayer, final View playerView) {
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
                if (sortMode == SortMode.rank)
                    refreshAllPlayersView(); // ensure rank order
                else if (sortMode == SortMode.num)
                    updateViews(playerView, currentPlayer); // only update concerned player's view
            }
        };
    }

    @Override
    protected void updateViews(View playerView, Player player) {
        super.updateViews(playerView, player);
        playerPhase.setText(String.format("Phase %d", ((PhaseDixPlayer)player).getPhase()));
    }

    @Override
    protected void fillTextViews(Player player) {
        super.fillTextViews(player);
        playerPhase.setText(String.format("Phase %d", ((PhaseDixPlayer)player).getPhase()));
    }

    @Override
    protected void findViews(View view) {
        super.findViews(view);
        playerPhase = view.findViewById(R.id.player_phase);
    }

    @Override
    protected int getCardRes() {
        return R.layout.item_card_player_phase10;
    }

    @Override
    protected int getPlayerScoreDialogRes() {
        return R.layout.dialog_player_phase10_score;
    }
}
