package remi.scoreboard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import remi.scoreboard.R;
import remi.scoreboard.activities.MainActivity;

public class GameRulesFragment extends Fragment {

    private AppCompatSpinner spinner;

    public GameRulesFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rules, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        spinner = (AppCompatSpinner) view.findViewById(R.id.rules_chooser);
        setupRuleChooser(spinner);
    }

    private void setupRuleChooser(AppCompatSpinner spinner) {
        String currentGameName = ((MainActivity) getActivity()).currentGameName;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.rules_array_default, android.R.layout.simple_spinner_item);

        if (currentGameName == getString(R.string.game_name_phase_dix)) {
            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.rules_array_phase10, android.R.layout.simple_spinner_item);
        } else if (currentGameName == getString(R.string.game_name_squash)) {
            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.rules_array_squash, android.R.layout.simple_spinner_item);

        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public String getRule() {
        return ((ArrayAdapter<CharSequence>) spinner.getAdapter()).getItem(spinner.getSelectedItemPosition()).toString();
    }
}
