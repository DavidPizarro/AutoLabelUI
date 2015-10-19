package com.dpizarro.autolabel.library;

import com.dpizarro.autolabeluilibrary.R;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (C) 2015 David Pizarro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class AutoLabelUI extends AutoViewGroup implements Label.OnClickCrossListener, Label.OnLabelClickListener {

    private static final String LOG_TAG = AutoLabelUI.class.getSimpleName();
    private static final int EMPTY      = 0;
    private int labelsCounter           = EMPTY;

    private final Context mContext;
    private int mTextSize;
    private int mTextColor;
    private int mBackgroundResource;
    private int mIconCross;
    private AutoLabelUISettings mAutoLabelUISettings;
    private int mMaxLabels = AutoLabelUISettings.DEFAULT_MAX_LABELS;
    private boolean mShowCross = AutoLabelUISettings.DEFAULT_SHOW_CROSS;
    private boolean mLabelsClickables = AutoLabelUISettings.DEFAULT_LABELS_CLICKABLES;
    private int mLabelPadding;

    private OnRemoveLabelListener listenerOnRemoveLabel;
    private OnLabelsCompletedListener listenerOnLabelsCompleted;
    private OnLabelsEmptyListener listenerOnLabelsEmpty;
    private OnLabelClickListener listenerOnLabelClick;

    /**
     * Default constructor
     */
    public AutoLabelUI(Context context) {
        this(context, null);
    }

    /**
     * Default constructor
     */
    public AutoLabelUI(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Default constructor
     */
    public AutoLabelUI(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        if (!isInEditMode()) {
            getAttributes(attrs);
        }
    }

    /**
     * Retrieve styles attributes
     */
    private void getAttributes(AttributeSet attrs) {
        TypedArray typedArray = mContext
                .obtainStyledAttributes(attrs, R.styleable.LabelsView, 0, 0);

        if (typedArray != null) {
            try {
                mTextSize = typedArray.getDimensionPixelSize(R.styleable.LabelsView_text_size,
                        getResources().getDimensionPixelSize(R.dimen.label_title_size));
                mTextColor = typedArray.getColor(R.styleable.LabelsView_text_color,
                        getResources().getColor(android.R.color.white));
                mBackgroundResource = typedArray.getResourceId(R.styleable.LabelsView_label_background_res,
                        R.color.default_background_label);
                mMaxLabels = typedArray.getInteger(R.styleable.LabelsView_max_labels,
                        AutoLabelUISettings.DEFAULT_MAX_LABELS);
                mShowCross = typedArray.getBoolean(R.styleable.LabelsView_show_cross,
                        AutoLabelUISettings.DEFAULT_SHOW_CROSS);
                mIconCross = typedArray.getResourceId(R.styleable.LabelsView_icon_cross,
                        AutoLabelUISettings.DEFAULT_ICON_CROSS);
                mLabelsClickables = typedArray.getBoolean(R.styleable.LabelsView_label_clickable,
                        AutoLabelUISettings.DEFAULT_LABELS_CLICKABLES);
                mLabelPadding = typedArray.getDimensionPixelSize(R.styleable.LabelsView_label_padding,
                        getResources().getDimensionPixelSize(R.dimen.padding_label_view));

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error while creating the view AutoLabelUI: ", e);
            } finally {
                typedArray.recycle();
            }
        }

    }

    /**
     * Method to add a Label if is possible.
     *
     * @param textLabel is the text of the label added.
     * @param position  is the position of the label using a list.
     */
    public boolean addLabel(String textLabel, int position) {
        if (!checkLabelsCompleted()) {
            Label label = new Label(getContext(), mTextSize, mIconCross, mShowCross,
                    mTextColor, mBackgroundResource, mLabelsClickables, mLabelPadding);
            label.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            label.setText(textLabel);
            label.setTag(position);
            label.setOnClickCrossListener(this);
            label.setOnLabelClickListener(this);

            addView(label);
            increaseLabelsCounter();

            setLayoutTransition(new LayoutTransition());
            requestLayout();

            return true;
        }

        if (listenerOnLabelsCompleted != null) {
            listenerOnLabelsCompleted.onLabelsCompleted();
        }
        return false;
    }

    /**
     * Method to add a Label if is possible.
     *
     * @param textLabel is the text of the label added.
     */
    public boolean addLabel(String textLabel) {
        if (!checkLabelsCompleted()) {
            Label label = new Label(getContext(), mTextSize, mIconCross, mShowCross,
                    mTextColor, mBackgroundResource, mLabelsClickables, mLabelPadding);
            label.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            label.setText(textLabel);
            label.setTag(textLabel);
            label.setOnClickCrossListener(this);
            label.setOnLabelClickListener(this);

            addView(label);
            increaseLabelsCounter();

            setLayoutTransition(new LayoutTransition());
            requestLayout();

            return true;
        }

        if (listenerOnLabelsCompleted != null) {
            listenerOnLabelsCompleted.onLabelsCompleted();
        }
        return false;
    }

    private boolean checkLabelsCompleted() {
        return !(mMaxLabels == -1 || getMaxLabels() > getLabelsCounter());
    }

    public int getLabelsCounter() {
        return labelsCounter;
    }

    private void increaseLabelsCounter() {
        this.labelsCounter++;
    }

    private void decreaseLabelsCounter() {
        this.labelsCounter--;
    }

    private void resetLabelsCounter(){
        this.labelsCounter = EMPTY;
    }

    /**
     * Method called when the cross icon is clicked.
     *
     * @param view the {@link Label} object.
     */
    @Override
    public void onClickCross(View view) {
        removeView(view);
        decreaseLabelsCounter();

        if (listenerOnRemoveLabel != null) {
            if( view.getTag() instanceof Integer){
                listenerOnRemoveLabel.onRemoveLabel(view, (Integer) view.getTag());
            }
            else{
                listenerOnRemoveLabel.onRemoveLabel(view, -1);
            }
        }
        if (getLabelsCounter() == EMPTY) {
            if (listenerOnLabelsEmpty != null) {
                listenerOnLabelsEmpty.onLabelsEmpty();
            }
        }
        requestLayout();
    }

    /**
     * Method called when the {@link Label} object is clicked.
     *
     * @param v the {@link Label} object.
     */
    @Override
    public void onClickLabel(View v) {
        if (listenerOnLabelClick != null) {
            listenerOnLabelClick.onClickLabel(v);
        }
    }

    /**
     * Method to remove a label using a list.
     *
     * @param position of the item to remove.
     */
    public boolean removeLabel(int position) {
        Label view = (Label) findViewWithTag(position);
        if (view != null) {
            removeView(view);
            decreaseLabelsCounter();
            if (getLabelsCounter() == EMPTY) {
                if (listenerOnLabelsEmpty != null) {
                    listenerOnLabelsEmpty.onLabelsEmpty();
                }
            }
            requestLayout();
            return true;
        }
        return false;
    }

    /**
     * Method to remove a label using a list.
     *
     * @param labelToRemove the text of the {@link Label} to remove.
     */
    public boolean removeLabel(String labelToRemove) {
        Label view = (Label) findViewWithTag(labelToRemove);
        if (view != null) {
            removeView(view);
            decreaseLabelsCounter();
            if (getLabelsCounter() == EMPTY) {
                if (listenerOnLabelsEmpty != null) {
                    listenerOnLabelsEmpty.onLabelsEmpty();
                }
            }
            requestLayout();
            return true;
        }
        return false;
    }

    public void clear(){
        removeAllViews();

        labelsCounter = EMPTY;
        if (listenerOnLabelsEmpty != null) {
            listenerOnLabelsEmpty.onLabelsEmpty();
        }

        requestLayout();
    }

    public Label getLabel(int position){
        return (Label) getChildAt(position);
    }

    public List<Label> getLabels(){
        ArrayList<Label> labels = new ArrayList<>();
        for (int i = 0;i < getChildCount();i++){
            labels.add(getLabel(i));
        }
        return labels;
    }

    public int getMaxLabels() {
        return mMaxLabels;
    }

    public void setMaxLabels(int maxLabels) {
        this.mMaxLabels = maxLabels;
    }

    public boolean isShowCross() {
        return mShowCross;
    }

    public void setShowCross(boolean showCross) {
        this.mShowCross = showCross;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public boolean isLabelsClickables() {
        return mLabelsClickables;
    }

    public void setLabelsClickables(boolean labelsClickables) {
        this.mLabelsClickables = labelsClickables;
    }

    public void setLabelPadding(int padding){
        int newPadding;
        try{
            newPadding = (int) getResources().getDimension(padding);
        }catch (Resources.NotFoundException e){
            newPadding = padding;
        }
        this.mLabelPadding = newPadding;
    }

    public void setTextSize(int textSize) {
        int newSize;
        try {
            newSize = (int) getResources().getDimension(textSize);
        } catch (Resources.NotFoundException e) {
            newSize = textSize;
        }
        this.mTextSize = newSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        int newColor;
        try {
            newColor = getResources().getColor(textColor);
        } catch (Resources.NotFoundException e) {
            newColor = textColor;
        }
        this.mTextColor = newColor;
    }

    public int getBackgroundResource() {
        return mBackgroundResource;
    }

    public void setBackgroundResource(int backgroundResource){
        this.mBackgroundResource = backgroundResource;
    }

    public int getIconCross() {
        return mIconCross;
    }

    public void setIconCross(int iconCross) {
        this.mIconCross = iconCross;
    }

    /**
     * Set a callback listener when a label is removed
     *
     * @param listener Callback instance.
     */
    public void setOnRemoveLabelListener(OnRemoveLabelListener listener) {
        this.listenerOnRemoveLabel = listener;
    }

    /**
     * Set a callback listener when there are the maximum number of labels.
     *
     * @param listener Callback instance.
     */
    public void setOnLabelsCompletedListener(OnLabelsCompletedListener listener) {
        this.listenerOnLabelsCompleted = listener;
    }

    /**
     * Set a callback listener when there are not labels.
     *
     * @param listener Callback instance.
     */
    public void setOnLabelsEmptyListener(OnLabelsEmptyListener listener) {
        this.listenerOnLabelsEmpty = listener;
    }

    /**
     * Set a callback listener when the {@link Label} is clicked.
     *
     * @param listener Callback instance.
     */
    public void setOnLabelClickListener(OnLabelClickListener listener) {
        this.listenerOnLabelClick = listener;
    }

    /**
     * This method sets the desired functionalities of the labels to make easy.
     *
     * @param autoLabelUISettings Object with all functionalities to make easy.
     */
    public void setSettings(AutoLabelUISettings autoLabelUISettings) {
        mAutoLabelUISettings = autoLabelUISettings;
        setMaxLabels(autoLabelUISettings.getMaxLabels());
        setShowCross(autoLabelUISettings.isShowCross());
        setBackgroundColor(autoLabelUISettings.getBackgroundResource());
        setTextColor(autoLabelUISettings.getTextColor());
        setTextSize(autoLabelUISettings.getTextSize());
        setIconCross(autoLabelUISettings.getIconCross());
        setLabelsClickables(autoLabelUISettings.isLabelsClickables());
        setLabelPadding(autoLabelUISettings.getLabelPadding());
    }

    /**
     * Save the state of the labels when orientation screen changed.
     */
    @Override
    public Parcelable onSaveInstanceState() {

        //save everything
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putParcelable("stateSettings", mAutoLabelUISettings);
        bundle.putSerializable("labelsAdded", (Serializable) getAllLabelsAdded());
        return bundle;
    }

    private List<LabelValues> getAllLabelsAdded() {
        List<LabelValues> listLabelValues = new ArrayList<>();
        int childcount = getChildCount();
        for (int i = 0; i < childcount; i++) {
            Label label = (Label) getChildAt(i);

            if(label.getTag() instanceof Integer){
                listLabelValues.add(new LabelValues((int) label.getTag(), label.getText()));
            }
            else{
                listLabelValues.add(new LabelValues(-1, label.getText()));
            }
        }

        return listLabelValues;
    }

    /**
     * Retrieve the state of the labels when orientation screen changed.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            //load everything
            AutoLabelUISettings autoLabelUISettings = bundle.getParcelable("stateSettings");
            if (autoLabelUISettings != null) {
                setSettings(autoLabelUISettings);
            }

            resetLabelsCounter();

            List<LabelValues> labelsAdded = (List<LabelValues>) bundle
                    .getSerializable("labelsAdded");

            for(int i=0; i<labelsAdded.size();i++){
                LabelValues labelValues = labelsAdded.get(i);

                if(labelValues.getKey() == -1){
                    addLabel(labelValues.getValue());
                }
                else{
                    addLabel(labelValues.getValue(), labelValues.getKey());
                }
            }

            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Interface for a callback when a label is removed.
     * Container Activity/Fragment must implement this interface
     */
    public interface OnRemoveLabelListener {

        /**
         * Callback when a label is removed.
         *
         * @param view has been removed.
         * @param position of the item to remove.
         */
        void onRemoveLabel(View view, int position);
    }

    /**
     * Interface for a callback listener when there are the maximum number of labels.
     * Container Activity/Fragment must implement this interface
     */
    public interface OnLabelsCompletedListener {

        /**
         * Callback when there are the maximum number of labels.
         */
        void onLabelsCompleted();
    }

    /**
     * Interface for a callback listener when there are not labels.
     * Container Activity/Fragment must implement this interface
     */
    public interface OnLabelsEmptyListener {

        /**
         * Call when there are not labels.
         */
        void onLabelsEmpty();
    }

    /**
     * Interface for a callback listener when the {@link Label} is clicked.
     * Container Activity/Fragment must implement this interface.
     */
    public interface OnLabelClickListener {

        /**
         * Call when the {@link Label} is clicked.
         */
        void onClickLabel(View v);
    }
}