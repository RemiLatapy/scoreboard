package remi.scoreboard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import remi.scoreboard.R;
import remi.scoreboard.activities.GameChampionshipActivity;
import remi.scoreboard.model.Player;

public class RankingFragment extends Fragment{

    private ArrayMap<Player, View> playerRowsMap;

    public static RankingFragment newInstance()
    {
        return new RankingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout rowContainerView = (LinearLayout) view.findViewById(R.id.row_container);

        playerRowsMap = new ArrayMap<>();

        ArrayList<Player> playerList = ((GameChampionshipActivity)getActivity()).playerList;

        for (Player player : playerList) {
            View row = getActivity().getLayoutInflater().inflate(R.layout.item_row_ranking, rowContainerView, false);
            playerRowsMap.put(player, row);
            rowContainerView.addView(row);
        }

        for (ArrayMap.Entry<Player, View> entry : playerRowsMap.entrySet())
        {
            ((TextView)entry.getValue().findViewById(R.id.rank_player_name)).setText(entry.getKey().getName());
            ((TextView)entry.getValue().findViewById(R.id.rank_lost)).setText("1");
            ((TextView)entry.getValue().findViewById(R.id.rank_played_game)).setText("2");
            ((TextView)entry.getValue().findViewById(R.id.rank_points)).setText("3");
            ((TextView)entry.getValue().findViewById(R.id.rank_position)).setText("4");
            ((TextView)entry.getValue().findViewById(R.id.rank_win)).setText("5");
        }
    }
}
