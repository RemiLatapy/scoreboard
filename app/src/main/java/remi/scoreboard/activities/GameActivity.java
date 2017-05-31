package remi.scoreboard.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.model.Player;

public class GameActivity extends AppCompatActivity {

    protected ArrayList<String> playersName;
    protected LinearLayout cardContainerView;
    protected Toolbar toolbar;
    protected ArrayList<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playersName = getIntent().getStringArrayListExtra(MainActivity.PLAYERS);
        cardContainerView = (LinearLayout) findViewById(R.id.card_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setupToolbar();

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
