package com.dpizarro.libraries.autolabelui;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.AutoLabelUISettings;
import com.dpizarro.autolabel.library.Label;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SimpleFragment extends Fragment {

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
                        .withBackgroundResource(R.color.default_background_label)
                        .withIconCross(R.drawable.cross)
                        .withMaxLabels(6)
                        .withShowCross(true)
                        .withLabelsClickables(true)
                        .withTextColor(android.R.color.white)
                        .withTextSize(R.dimen.label_title_size)
                        .withLabelPadding(30)
                        .build();

        mAutoLabel.setSettings(autoLabelUISettings);
    }

    private void setListeners() {
        mAutoLabel.setOnLabelsCompletedListener(new AutoLabelUI.OnLabelsCompletedListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onLabelsCompleted() {
                Snackbar.make(getView(), "Completed!", Snackbar.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnRemoveLabelListener(new AutoLabelUI.OnRemoveLabelListener() {
            @Override
            public void onRemoveLabel(Label removedLabel, int position) {
                Snackbar.make(removedLabel, "Label with text \" " + removedLabel.getTag() + "\" has been removed.", Snackbar.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnLabelsEmptyListener(new AutoLabelUI.OnLabelsEmptyListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onLabelsEmpty() {
                Snackbar.make(getView(), "EMPTY!", Snackbar.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(Label labelClicked) {
                Snackbar.make(labelClicked, labelClicked.getText(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void findViews(View view) {
        mAutoLabel = (AutoLabelUI) view.findViewById(R.id.label_view);
        etLabelAdd = (EditText) view.findViewById(R.id.etLabel);
        etLabelRemove = (EditText) view.findViewById(R.id.etLabelRemove);

        Button btAddLabel = (Button) view.findViewById(R.id.btAddLabel);
        btAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToAdd = etLabelAdd.getText().toString();
                if (!textToAdd.isEmpty()) {
                    boolean success = mAutoLabel.addLabel(textToAdd);
                    if (success) {
                        Snackbar.make(v, "Label added!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(v, "ERROR! Label not added", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button btRemoveLabel = (Button) view.findViewById(R.id.btRemoveLabel);
        btRemoveLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToRemove = etLabelRemove.getText().toString();
                if (!textToRemove.isEmpty()) {
                    boolean success = mAutoLabel.removeLabel(textToRemove);

                    if (success) {
                        Snackbar.make(v, "Label removed!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(v, "ERROR! Label not removed", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
