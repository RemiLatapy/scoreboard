package remi.scoreboard.fragments;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import remi.scoreboard.R;

public class SquashGameCustomizeFragment extends GameCustomizeFragment {

    private AppCompatSpinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        assert view != null;
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root_fragment_customize);
        inflater.inflate(R.layout.widgets_squash_customize, rootView, true);
        return view;
    }

    @Override
    protected void findViews(View view)
    {
        spinner = (AppCompatSpinner) view.findViewById(R.id.rules_chooser);
    }

    @Override
    protected void setupCustomizeView() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.rules_array_squash, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public String getRule() {
        return ((ArrayAdapter<CharSequence>) spinner.getAdapter()).getItem(spinner.getSelectedItemPosition()).toString();
    }
}
