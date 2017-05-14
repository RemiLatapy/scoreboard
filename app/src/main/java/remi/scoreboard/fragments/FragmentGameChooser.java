package remi.scoreboard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.activities.MainActivity;
import remi.scoreboard.adapters.AdapterGameChooser;
import remi.scoreboard.model.ItemGameChooser;

public class FragmentGameChooser extends Fragment {

    public FragmentGameChooser() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_games, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecycler(view);
    }

    private void setupRecycler(View view) {
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycler);
        LinearLayoutManager llm = new LinearLayoutManager(rv.getContext());
        AdapterGameChooser agc = new AdapterGameChooser((MainActivity) getActivity(), mockGameList());

        rv.setLayoutManager(llm);
        rv.setAdapter(agc);
    }

    private ArrayList<ItemGameChooser> mockGameList() {
        ArrayList<ItemGameChooser> data = new ArrayList<>();
        data.add(new ItemGameChooser(R.drawable.thumbail_phase_10, getString(R.string.game_name_phase_dix), FragmentGamePlayer.class));
        data.add(new ItemGameChooser(R.drawable.thumbail_uno, getString(R.string.game_name_uno), FragmentGamePlayer.class));
        data.add(new ItemGameChooser(R.drawable.thumbail_tarot, getString(R.string.game_name_tarot), FragmentGamePlayer.class));
        return data;
    }


}
