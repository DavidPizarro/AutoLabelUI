package com.dpizarro.libraries.autolabelui;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.AutoLabelUISettings;
import com.dpizarro.autolabel.library.Label;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by dpizarro
 */
public class SimpleFragment extends Fragment {

    private static final String LOG_TAG = SimpleFragment.class.getSimpleName();

    private AutoLabelUI mAutoLabel;
    private EditText etLabelAdd;
    private EditText etLabelRemove;

    public static SimpleFragment newInstance() {
        return new SimpleFragment();
    }

    public SimpleFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_fragment, container, false);
        findViews(view);
        setListeners();
        setAutoLabelUISettings();
        return view;
    }

    private void setAutoLabelUISettings() {
        AutoLabelUISettings autoLabelUISettings =
                new AutoLabelUISettings.Builder()
                        .withBackgroundColor(R.color.default_background_label)
                        .withIconCross(R.drawable.cross)
                        .withMaxLabels(6)
                        .withShowCross(true)
                        .withLabelsClickables(true)
                        .withTextColor(android.R.color.white)
                        .withTextSize(R.dimen.label_title_size)
                        .build();

        mAutoLabel.setSettings(autoLabelUISettings);
    }

    private void setListeners() {
        mAutoLabel.setOnLabelsCompletedListener(new AutoLabelUI.OnLabelsCompletedListener() {
            @Override
            public void onLabelsCompleted() {
                Toast.makeText(getActivity(), "Completed!", Toast.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnRemoveLabelListener(new AutoLabelUI.OnRemoveLabelListener() {
            @Override
            public void onRemoveLabel(View view, int position) {
                Log.d(LOG_TAG, "Label with text \" " + view.getTag() + "\" has been removed." );
            }
        });

        mAutoLabel.setOnLabelsEmptyListener(new AutoLabelUI.OnLabelsEmptyListener() {
            @Override
            public void onLabelsEmpty() {
                Toast.makeText(getActivity(), "EMPTY!", Toast.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(View v) {
                Toast.makeText(getActivity(), ((Label) v).getText(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void findViews(View view) {
        mAutoLabel = (AutoLabelUI) view.findViewById(R.id.label_view);
        Button btAddLabel = (Button) view.findViewById(R.id.btAddLabel);
        btAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToAdd = etLabelAdd.getText().toString();
                if(!textToAdd.isEmpty()){
                    boolean success = mAutoLabel.addLabel(textToAdd);
                    if(success)
                        Toast.makeText(getActivity(), "Label added", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "ERROR! Label not added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btRemoveLabel = (Button) view.findViewById(R.id.btRemoveLabel);
        btRemoveLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToRemove = etLabelRemove.getText().toString();
                if(!textToRemove.isEmpty()) {
                    boolean success = mAutoLabel.removeLabel(textToRemove);

                    if(success)
                        Toast.makeText(getActivity(), "Label removed", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "ERROR! Label not removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etLabelAdd = (EditText) view.findViewById(R.id.etLabel);
        etLabelRemove = (EditText) view.findViewById(R.id.etLabelRemove);
    }

}
