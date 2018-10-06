package remi.scoreboard.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.adapters.GamePlayTabAdapter;

public abstract class GameActivity extends AppCompatActivity {

    private String gameName;

    protected ArrayList<String> playersName;
    protected Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playersName = getIntent().getStringArrayListExtra(MainActivity.PLAYERS);
        gameName = getIntent().getStringExtra("gameName");

        findViews();
        setupToolbar();
        setupPager();
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.game_view_tablayout);
        viewPager = findViewById(R.id.game_viewpager);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    private void setupPager() {
        GamePlayTabAdapter adapter = new GamePlayTabAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
    }

    public ArrayList<String> getPlayersName() {
        return playersName;
    }

    public String getGameName() {
        return gameName;
    }
}
