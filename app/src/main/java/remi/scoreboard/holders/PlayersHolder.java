package remi.scoreboard.holders;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;
import remi.scoreboard.R;
import remi.scoreboard.adapters.PlayersAddAdapter;

public class PlayersHolder extends RecyclerView.ViewHolder {

    public final EditText playerName;
    final private PlayersAddAdapter playersAddAdapter;

    public PlayersHolder(View itemView, final PlayersAddAdapter playersAddAdapter) {
        super(itemView);

        playerName = itemView.findViewById(R.id.player_name);
        this.playersAddAdapter = playersAddAdapter;

        setupFocusChangeListener();
    }

    private void setupFocusChangeListener() {
        playerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && playerName.length() != 0) {
                    playersAddAdapter.setPlayerName(getAdapterPosition(), playerName.getText().toString());
                } else if (!hasFocus && !isLast()) {
                    final int playerToRemove = getAdapterPosition();
                    (new Handler()).post(new Runnable() {
                        @Override
                        public void run() {
                            playersAddAdapter.remove(playerToRemove);
                        }
                    });
                } else if (!hasFocus && isLast()) {
                    final int playerNameToWipe = getAdapterPosition();
                    (new Handler()).post(new Runnable() {
                        @Override
                        public void run() {
                            playersAddAdapter.setPlayerName(playerNameToWipe, "");
                        }
                    });
                }
            }
        });
    }

    private boolean isLast() {
        return playersAddAdapter.getItemCount() - 1 == getAdapterPosition();
    }


    public void savePlayers() {
        playersAddAdapter.setPlayerName(getAdapterPosition(), playerName.getText().toString());
    }
}
