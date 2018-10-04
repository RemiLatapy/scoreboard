package remi.scoreboard.fragments;

import android.content.Context;
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
import remi.scoreboard.model.ChampionshipPlayer;

public class RankingFragment extends Fragment{

    private ArrayMap<ChampionshipPlayer, View> playerRowsMap;
    private LinearLayout rowContainerView;

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
        rowContainerView = view.findViewById(R.id.row_container);
    }

    public void setPlayers(ArrayList<ChampionshipPlayer> playerList) {
        if(playerRowsMap != null)
            return;

        playerRowsMap = new ArrayMap<>();

        for (ChampionshipPlayer player : playerList) {
            View row = getActivity().getLayoutInflater().inflate(R.layout.item_row_ranking, rowContainerView, false);
            playerRowsMap.put(player, row);
            rowContainerView.addView(row);
        }

        for (ArrayMap.Entry<ChampionshipPlayer, View> entry : playerRowsMap.entrySet())
        {
            ((TextView)entry.getValue().findViewById(R.id.rank_player_name)).setText(entry.getKey().getName());
        }
    }

    public void setScore()
    {
        assert playerRowsMap != null;
        for (ArrayMap.Entry<ChampionshipPlayer, View> entry : playerRowsMap.entrySet())
        {
            ((TextView)entry.getValue().findViewById(R.id.rank_win)).setText(String.valueOf(entry.getKey().getVictories()));
            ((TextView)entry.getValue().findViewById(R.id.rank_lost)).setText(String.valueOf(entry.getKey().getDefeats()));
            ((TextView)entry.getValue().findViewById(R.id.rank_points)).setText(String.valueOf(entry.getKey().getGoalAverage()));
            ((TextView)entry.getValue().findViewById(R.id.rank_played_game)).setText(String.valueOf(entry.getKey().getGamesPlayed()));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((GameChampionshipActivity)context).rankingFragment = this;
    }
}
