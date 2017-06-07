package remi.scoreboard.holders;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import remi.scoreboard.R;
import remi.scoreboard.adapters.PlayersAddAdapter;

public class PlayersHolder extends RecyclerView.ViewHolder {

    public final EditText playerName;
    public final ImageButton removeBtn;
    final private PlayersAddAdapter playersAddAdapter;

    public PlayersHolder(View itemView, final PlayersAddAdapter playersAddAdapter) {
        super(itemView);

        playerName = (EditText) itemView.findViewById(R.id.editText_playerName);
        removeBtn = (ImageButton) itemView.findViewById(R.id.remove_player_btn);
        this.playersAddAdapter = playersAddAdapter;

        setupFocusChangeListener();
        setupAutoAddListener();
        setupRemoveBtnListener(itemView);
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

    private void setupAutoAddListener() {
        playerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && isLast()) {
                    removeBtn.setVisibility(View.VISIBLE);
                    (new Handler()).post(new Runnable() {
                        @Override
                        public void run() {
                            playersAddAdapter.add();
                        }
                    });
                    if (isFirst()) {
                        playersAddAdapter.getFragment().getMenu().getItem(0).setVisible(true);
                    }
                } else if (s.length() == 0 && isSecondToLast()) {
                    removeBtn.setVisibility(View.INVISIBLE);
                    (new Handler()).post(new Runnable() {
                        @Override
                        public void run() {
                            playersAddAdapter.remove(playersAddAdapter.getItemCount() - 1);
                        }
                    });
                    playersAddAdapter.getFragment().getMenu().getItem(0).setVisible(false);
                }
            }
        });
    }

    private boolean isFirst() {
        return getAdapterPosition() == 0;
    }

    private boolean isLast() {
        return playersAddAdapter.getItemCount() - 1 == getAdapterPosition();
    }

    private boolean isSecondToLast() {
        return playersAddAdapter.getItemCount() - 2 == getAdapterPosition();
    }

    private void setupRemoveBtnListener(final View itemView) {
        itemView.findViewById(R.id.remove_player_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirst() && isSecondToLast()) {
                    playersAddAdapter.getFragment().getMenu().getItem(0).setVisible(false);
                }
                playersAddAdapter.remove(getAdapterPosition());
            }
        });
    }

    public void savePlayers() {
        playersAddAdapter.setPlayerName(getAdapterPosition(), playerName.getText().toString());
    }
}
