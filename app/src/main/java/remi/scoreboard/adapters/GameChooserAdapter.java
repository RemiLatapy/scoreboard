package remi.scoreboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.activities.MainActivity;
import remi.scoreboard.holders.GamesHolder;
import remi.scoreboard.model.ItemGameChooser;

public class GameChooserAdapter extends RecyclerView.Adapter<GamesHolder> {

    public static final String GAME_TITLE_ID = "gameTitleId";
    public static final String GAME_CLASS = "gameClassName";

    private ArrayList<ItemGameChooser> items;
    private MainActivity activity;

    public GameChooserAdapter(MainActivity activity, ArrayList<ItemGameChooser> items) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public GamesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_game, parent, false);
        return new GamesHolder(view);
    }

    @Override
    public void onBindViewHolder(final GamesHolder holder, final int position) {
        final ItemGameChooser currentItem = items.get(position);
        holder.titleGame.setText(currentItem.getGameTitle());
        RequestOptions glideOptions = new RequestOptions();
        glideOptions.centerCrop();
        Glide.with(holder.imageGame.getContext())
                .load(currentItem.getImageResId())
                .apply(glideOptions)
                .into(holder.imageGame);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.currentGameName = currentItem.getGameTitle();
                notifyDataSetChanged();
                activity.swipeRight();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
