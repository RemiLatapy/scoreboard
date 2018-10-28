package remi.scoreboard.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import remi.scoreboard.R;
import remi.scoreboard.holders.PlayersHolder;
import remi.scoreboard.jetpack.fragment.UserListFragment;
import remi.scoreboard.model.ItemPlayerAdd;

import java.util.ArrayList;

public class PlayersAddAdapter extends RecyclerView.Adapter<PlayersHolder> {

    private final UserListFragment fragment;
    private ArrayList<ItemPlayerAdd> items;
    private PlayersHolder playerHolder;

    public PlayersAddAdapter(UserListFragment userListFragment) {
        items = new ArrayList<>();
        fragment = userListFragment;
        add();
    }

    @Override
    public PlayersHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_user, parent, false);
        playerHolder = new PlayersHolder(itemView, this);
        return playerHolder;
    }

    @Override
    public void onBindViewHolder(PlayersHolder holder, int position) {
        ItemPlayerAdd item = items.get(position);
        holder.playerName.setText(item.getName());
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

    public UserListFragment getFragment() {
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
