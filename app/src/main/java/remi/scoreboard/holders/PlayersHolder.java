package remi.scoreboard.holders;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import remi.scoreboard.R;
import remi.scoreboard.adapters.PlayersAdapter;

public class PlayersHolder extends RecyclerView.ViewHolder {

    public final EditText playerName;
    public final ImageButton removeBtn;
    final private PlayersAdapter playersAdapter;

    public PlayersHolder(View itemView, final PlayersAdapter playersAdapter) {
        super(itemView);

        playerName = (EditText) itemView.findViewById(R.id.editText_playerName);
        removeBtn = (ImageButton) itemView.findViewById(R.id.remove_player_btn);
        this.playersAdapter = playersAdapter;

        setupFocusChangeListener();
        setupAutoAddListener();
        setupRemoveBtnListener(itemView);
    }

    private void setupFocusChangeListener() {
        playerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && playerName.length() != 0) {
                    playersAdapter.setPlayerName(getAdapterPosition(), playerName.getText().toString());
                } else if (!hasFocus && !isLast()) {
                    final int playerToRemove = getAdapterPosition();
                    (new Handler()).post(new Runnable() {
                        @Override
                        public void run() {
                            playersAdapter.remove(playerToRemove);
                        }
                    });
                } else if (!hasFocus && isLast()) {
                    final int playerNameToWipe = getAdapterPosition();
                    (new Handler()).post(new Runnable() {
                        @Override
                        public void run() {
                            playersAdapter.setPlayerName(playerNameToWipe, "");
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
                            playersAdapter.add();
                        }
                    });
                    if (isFirst()) {
                        playersAdapter.getFragment().getMenu().getItem(0).setVisible(true);
                    }
                } else if (s.length() == 0 && isSecondToLast()) {
                    removeBtn.setVisibility(View.INVISIBLE);
                    (new Handler()).post(new Runnable() {
                        @Override
                        public void run() {
                            playersAdapter.remove(playersAdapter.getItemCount() - 1);
                        }
                    });
                    playersAdapter.getFragment().getMenu().getItem(0).setVisible(false);
                }
            }
        });
    }

    private boolean isFirst() {
        return getAdapterPosition() == 0;
    }

    private boolean isLast() {
        return playersAdapter.getItemCount() - 1 == getAdapterPosition();
    }

    private boolean isSecondToLast() {
        return playersAdapter.getItemCount() - 2 == getAdapterPosition();
    }

    private void setupRemoveBtnListener(final View itemView) {
        itemView.findViewById(R.id.remove_player_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirst() && isSecondToLast()) {
                    playersAdapter.getFragment().getMenu().getItem(0).setVisible(false);
                }
                playersAdapter.remove(getAdapterPosition());
            }
        });
    }

    public void savePlayers() {
        playersAdapter.setPlayerName(getAdapterPosition(), playerName.getText().toString());
    }
}
