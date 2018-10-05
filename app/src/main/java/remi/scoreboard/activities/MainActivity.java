package remi.scoreboard.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.adapters.SetupGamePagerAdapter;
import remi.scoreboard.fragments.PhaseDixPlayFragment;
import remi.scoreboard.view.NonSwipeableViewPager;

public class MainActivity extends AppCompatActivity {

    public static final String PLAYERS = "remi.scoreboard.activities.players";
    public static final String GAME_RULES = "remi.scoreboard.activities.gamerules";

    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private NonSwipeableViewPager pager;
    private boolean toolBarNavigationListenerIsRegistered = false;

    public String currentGameName;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentGameName = "";

        findViewsByIds();
        setupPager();
        setupToolbar();
        setupDrawer();
        setupFab();
    }


    private ArrayList<String> getPlayers() {
        if (pager.getAdapter() instanceof SetupGamePagerAdapter) {
            return ((SetupGamePagerAdapter) pager.getAdapter()).getPlayers();
        }
        return null;
    }

    private String getRule() {
        if (pager.getAdapter() instanceof SetupGamePagerAdapter) {
            return ((SetupGamePagerAdapter) pager.getAdapter()).getRule();
        }
        return "";
    }

    private void setupPager() {
        final SetupGamePagerAdapter adapter = new SetupGamePagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new NonSwipeableViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        adapter.notifyChangeInPosition(1);
                        enableBackbutton(false);
                        hideSoftKeyboard();
                        if (getWindow().getCurrentFocus() != null) {
                            getWindow().getCurrentFocus().clearFocus();
                        }
                        fab.hide();
                        break;
                    case 1:
                        enableBackbutton(true);
                        fab.hide();
                        break;
                    case 2:
                        enableBackbutton(true);
                        if (getWindow().getCurrentFocus() != null) {
                            getWindow().getCurrentFocus().clearFocus();
                        }
                        hideSoftKeyboard();
                        fab.show();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void findViewsByIds() {
        pager = (NonSwipeableViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void enableBackbutton(boolean enable) {
        if(enable) {
            drawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(!toolBarNavigationListenerIsRegistered) {
                drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                toolBarNavigationListenerIsRegistered = true;
            }

        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(null);
            toolBarNavigationListenerIsRegistered = false;
        }
    }

    private void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pager.getCurrentItem()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent = new Intent();
                        if (currentGameName.equals(getString(R.string.game_name_phase_dix))) {
                            intent.setClass(MainActivity.this, GamePhaseDixActivity.class);
                        } else if (currentGameName.equals(getString(R.string.game_name_squash))) {
                            intent.setClass(MainActivity.this, GameChampionshipActivity.class);
                        } else if (currentGameName.equals(getString(R.string.game_name_scopa)) ||
                                currentGameName.equals(getString(R.string.game_name_uno))) {
                            intent.setClass(MainActivity.this, GameSimpleScoreActivity.class);
                        } else {
                            throw new Error("Unknow game : " + currentGameName);
                        }

                        intent.putExtra(PLAYERS, getPlayers());
                        intent.putExtra(GAME_RULES, getRule());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar,R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!swipeLeft()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public void swipeRight() {
        int currentItem = pager.getCurrentItem();
        if (currentItem + 1 < pager.getAdapter().getCount()) {
            pager.setCurrentItem(currentItem + 1, true);
        }
    }

    public boolean swipeLeft() {
        int currentItem = pager.getCurrentItem();
        if (currentItem - 1 >= 0) {
            pager.setCurrentItem(currentItem - 1, true);
            return true;
        }
        return false;
    }
}
