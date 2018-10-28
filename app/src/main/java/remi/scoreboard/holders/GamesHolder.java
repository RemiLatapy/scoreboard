package remi.scoreboard.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import remi.scoreboard.R;

public class GamesHolder extends RecyclerView.ViewHolder {

    public final ImageView imageGame;
    public final TextView titleGame;


    public GamesHolder(View view) {
        super(view);
        imageGame = view.findViewById(R.id.game_image);
        titleGame = view.findViewById(R.id.game_title);
    }
}
