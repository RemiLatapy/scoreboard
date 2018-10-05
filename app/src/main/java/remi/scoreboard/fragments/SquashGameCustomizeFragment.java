package remi.scoreboard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import remi.scoreboard.R;

import static remi.scoreboard.activities.GameChampionshipActivity.ROTATION_NUMBER;

public class SquashGameCustomizeFragment extends GameCustomizeFragment {

    private AppCompatSpinner spinner;
    private NumberPicker numberPicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.widgets_squash_customize, (LinearLayout)view, true);
        return view;
    }

    @Override
    protected void findViews(View view)
    {
        spinner = view.findViewById(R.id.rules_chooser);
        numberPicker = view.findViewById(R.id.rotation_number);
    }

    @Override
    protected void setupCustomizeView() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.rules_array_squash, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
    }

    // TODO resolve cast warning
    @Override
    public String getRule() {
        return ((ArrayAdapter<CharSequence>) spinner.getAdapter()).getItem(spinner.getSelectedItemPosition()).toString();
    }

    @Override
    public Intent putCustomExtras(Intent intent) {
        return intent.putExtra(ROTATION_NUMBER, numberPicker.getValue());
    }
}
