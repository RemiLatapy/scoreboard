package remi.scoreboard.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import remi.scoreboard.R;

public class HolderGame extends RecyclerView.ViewHolder {

    public final ImageView imageGame;
    public final TextView titleGame;


    public HolderGame(View view) {
        super(view);
        imageGame = (ImageView) view.findViewById(R.id.game_image);
        titleGame = (TextView) view.findViewById(R.id.game_title);
    }
}
