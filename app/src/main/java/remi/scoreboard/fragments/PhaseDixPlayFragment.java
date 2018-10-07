package remi.scoreboard.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void updatePlayerModel(Player currentPlayer, AlertDialog scoreDialog) {
        super.updatePlayerModel(currentPlayer, scoreDialog); // parent updates score
        if (((CheckBox) scoreDialog.findViewById(R.id.chk_phase)).isChecked()) {
            ((PhaseDixPlayer) currentPlayer).validPhase();
        }
    }

    @Override
    protected void fixPlayerModel(Player currentPlayer, AlertDialog scoreDialog) {
        super.fixPlayerModel(currentPlayer, scoreDialog);
        String phaseText = ((EditText) scoreDialog.findViewById(R.id.phases)).getText().toString();
        if (!phaseText.isEmpty()) {
            ((PhaseDixPlayer) currentPlayer).setPhase(Integer.parseInt(phaseText));
        }
    }

    @Override
    protected boolean checkScoreValues(AlertDialog dialog) {
        String error;
        int points = 0;

        String pointsText = ((EditText) dialog.findViewById(R.id.points)).getText().toString();
        if (!pointsText.isEmpty())
            points = Integer.parseInt(pointsText);

        boolean phase = ((CheckBox) dialog.findViewById(R.id.chk_phase)).isChecked();
        if (!phase && points < 50)
            error = "Too few points";
        else if (points % 5 != 0)
            error = "Score must be a multiple of 5";
        else {
            return true;
        }

        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void updateViews(View playerView, Player player) {
        super.updateViews(playerView, player);
        playerPhase.setText(String.format("Phase %d", ((PhaseDixPlayer) player).getPhase()));
    }

    @Override
    protected void fillTextViews(Player player) {
        super.fillTextViews(player);
        playerPhase.setText(String.format("Phase %d", ((PhaseDixPlayer) player).getPhase()));
    }

    @Override
    protected void findViews(View view) {
        super.findViews(view);
        playerPhase = view.findViewById(R.id.player_phase);
    }

    @Override
    protected void fillFixScoreDialogViews(Player player, AlertDialog dialog) {
        super.fillFixScoreDialogViews(player, dialog);
        ((EditText) dialog.findViewById(R.id.phases))
                .setText(String.valueOf(((PhaseDixPlayer) player).getPhase()));
    }

    @Override
    protected int getCardRes() {
        return R.layout.item_card_player_phase10;
    }

    @Override
    protected int getPlayerScoreDialogRes() {
        return R.layout.dialog_player_phase10_score;
    }

    @Override
    protected int getPlayerScoreFixingDialogRes() {
        return R.layout.dialog_player_phase10_score_fixing;
    }
}
