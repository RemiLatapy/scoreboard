package remi.scoreboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.fragments.GamePlayerFragment;
import remi.scoreboard.holders.PlayersHolder;
import remi.scoreboard.model.ItemPlayerAdd;

public class PlayersAddAdapter extends RecyclerView.Adapter<PlayersHolder> {

    private final GamePlayerFragment fragment;
    private ArrayList<ItemPlayerAdd> items;
    private PlayersHolder playerHolder;

    public PlayersAddAdapter(GamePlayerFragment gamePlayerFragment) {
        items = new ArrayList<>();
        fragment = gamePlayerFragment;
        add();
    }

    @Override
    public PlayersHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item, parent, false);
        playerHolder = new PlayersHolder(itemView, this);
        return playerHolder;
    }

    @Override
    public void onBindViewHolder(PlayersHolder holder, int position) {
        ItemPlayerAdd item = items.get(position);
        holder.playerName.setText(item.getName());
        if (position == getItemCount() - 1) {
            holder.removeBtn.setVisibility(View.INVISIBLE);
        } else {
            holder.removeBtn.setVisibility(View.VISIBLE);
        }
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add() {
        add(new ItemPlayerAdd(""));
    }

    private void add(ItemPlayerAdd player) {
        items.add(player);
        notifyItemInserted(items.size() - 1);
    }

    public void remove(int playerPosition) {
        if (playerPosition != -1) {
            items.remove(playerPosition);
            notifyItemRemoved(playerPosition);
        }
    }

    public void setPlayerName(int playerPosition, String name) {
        if (playerPosition != -1) {
            items.get(playerPosition).setName(name);
        }
    }

    public GamePlayerFragment getFragment() {
        return fragment;
    }

    public ArrayList<String> getPlayers() {
        ArrayList<String> playerNameArray = new ArrayList<>();
        for (int i = 0; i < items.size() - 1; i++) {
            playerNameArray.add(items.get(i).getName());
        }
        return playerNameArray;
    }

    public void savePlayers() {
        playerHolder.savePlayers();
    }
}
