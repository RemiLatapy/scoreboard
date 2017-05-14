package remi.scoreboard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.activities.MainActivity;
import remi.scoreboard.adapters.PlayersAdapter;

public class FragmentGamePlayer extends Fragment {

    private PlayersAdapter adapter;
    private Menu menu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_players, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.players_list);
        setupRecyclerView(recyclerView);
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        adapter = new PlayersAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        if (adapter.getItemCount() > 1) {
            menu.getItem(0).setVisible(true);
        }
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((MainActivity) getActivity()).swipeRight();
                return true;
            }
        });
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public ArrayList<String> getPlayers() {
        return adapter.getPlayers();
    }

    public void savePlayers() {
        adapter.savePlayers();
    }
}
