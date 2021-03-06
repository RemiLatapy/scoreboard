package remi.scoreboard.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
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

public class MainActivity extends AppCompatActivity {

    public static final String PLAYERS = "remi.scoreboard.activities.players";
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private ViewPager pager;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsByIds();

        setupPager();
        setupToolbar();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pager.getCurrentItem()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent = new Intent(MainActivity.this, PhaseDixPlay.class);
                        intent.putExtra(PLAYERS, getPlayers());
                        startActivity(intent);
                        break;
                }
            }
        });

        setupDrawer();
    }

    private ArrayList<String> getPlayers() {
        if (pager.getAdapter() instanceof SetupGamePagerAdapter) {
            return ((SetupGamePagerAdapter) pager.getAdapter()).getPlayers();
        }
        return null;
    }

    private void setupPager() {
        SetupGamePagerAdapter adapter = new SetupGamePagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        hideSoftKeyboard();
                        if (getWindow().getCurrentFocus() != null) {
                            getWindow().getCurrentFocus().clearFocus();
                        }
                    case 1:
                        fab.hide();
                        break;
                    case 2:
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
        pager = (ViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
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
