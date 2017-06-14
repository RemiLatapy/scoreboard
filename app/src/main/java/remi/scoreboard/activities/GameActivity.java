package remi.scoreboard.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.adapters.GamePlayTabAdapter;
import remi.scoreboard.fragments.GamePlayerFragment;
import remi.scoreboard.model.Player;

import static remi.scoreboard.activities.MainActivity.ROTATION_NUMBER;

public class GameActivity extends AppCompatActivity {

    public int rotationsNumber = 1;
    protected ArrayList<String> playersName;
    protected Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playersName = getIntent().getStringArrayListExtra(MainActivity.PLAYERS);
        rotationsNumber = getIntent().getIntExtra(ROTATION_NUMBER, 1);

        findViews();
        setupToolbar();
        setupPager();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.game_view_tablayout);
        viewPager = (ViewPager) findViewById(R.id.game_viewpager);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupPager() {
        GamePlayTabAdapter adapter = new GamePlayTabAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
    }

    public ArrayList<String> getPlayersName() {
        return playersName;
    }
}
