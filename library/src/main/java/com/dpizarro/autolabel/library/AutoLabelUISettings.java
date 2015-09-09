package com.dpizarro.autolabel.library;

import com.dpizarro.autolabeluilibrary.R;

import android.os.Parcel;
import android.os.Parcelable;

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
public class AutoLabelUISettings implements Parcelable {

    public static final boolean DEFAULT_SHOW_CROSS          = true;
    public static final boolean DEFAULT_LABELS_CLICKABLES   = false;
    public static final int DEFAULT_MAX_LABELS              = -1;
    public static final int DEFAULT_ICON_CROSS              = R.drawable.cross;

    private int mMaxLabels;
    private boolean mShowCross;
    private int mIconCross;
    private int mTextColor;
    private int mTextSize;
    private int mBackgroundColor;
    private boolean mLabelsClickables;

    private AutoLabelUISettings(Builder builder) {
        setMaxLabels(builder.bMaxLabels);
        setShowCross(builder.bShowCross);
        setIconCross(builder.bIconCross);
        setTextColor(builder.bTextColor);
        setTextSize(builder.bTextSize);
        setBackgroundColor(builder.bBackgroundColor);
        setLabelsClickables(builder.bLabelsClickables);
    }

    public int getMaxLabels() {
        return mMaxLabels;
    }

    private void setMaxLabels(int maxLabels) {
        mMaxLabels = maxLabels;
    }

    public boolean isShowCross() {
        return mShowCross;
    }

    private void setShowCross(boolean showCross) {
        mShowCross = showCross;
    }

    public int getIconCross() {
        return mIconCross;
    }

    private void setIconCross(int iconCross) {
        mIconCross = iconCross;
    }

    public int getTextColor() {
        return mTextColor;
    }

    private void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    private void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    private void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public boolean isLabelsClickables() {
        return mLabelsClickables;
    }

    private void setLabelsClickables(boolean labelsClickables) {
        mLabelsClickables = labelsClickables;
    }


    public static final class Builder {

        private int bMaxLabels              = DEFAULT_MAX_LABELS;
        private boolean bShowCross          = DEFAULT_SHOW_CROSS;
        private int bIconCross              = DEFAULT_ICON_CROSS;
        private int bTextColor              = android.R.color.white;
        private int bTextSize               = R.dimen.label_title_size;
        private int bBackgroundColor        = R.color.default_background_label;
        private boolean bLabelsClickables   = DEFAULT_LABELS_CLICKABLES;

        public Builder() {
        }

        public Builder(AutoLabelUISettings copy) {
            this.bMaxLabels = copy.mMaxLabels;
            this.bShowCross = copy.mShowCross;
            this.bIconCross = copy.mIconCross;
            this.bTextColor = copy.mTextColor;
            this.bTextSize = copy.mTextSize;
            this.bBackgroundColor = copy.mBackgroundColor;
            this.bLabelsClickables = copy.mLabelsClickables;
        }

        public Builder withMaxLabels(int bMaxLabels) {
            this.bMaxLabels = bMaxLabels;
            return this;
        }

        public Builder withShowCross(boolean showCross) {
            this.bShowCross = showCross;
            return this;
        }

        public Builder withIconCross(int iconCross) {
            this.bIconCross = iconCross;
            return this;
        }

        public Builder withTextColor(int textColor) {
            this.bTextColor = textColor;
            return this;
        }

        public Builder withTextSize(int textSize) {
            this.bTextSize = textSize;
            return this;
        }

        public Builder withBackgroundColor(int backgroundColor) {
            this.bBackgroundColor = backgroundColor;
            return this;
        }

        public Builder withLabelsClickables(boolean labelsClickables) {
            this.bLabelsClickables = labelsClickables;
            return this;
        }

        public AutoLabelUISettings build() {
            return new AutoLabelUISettings(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMaxLabels);
        dest.writeByte(mShowCross ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mIconCross);
        dest.writeInt(this.mTextColor);
        dest.writeInt(this.mTextSize);
        dest.writeInt(this.mBackgroundColor);
        dest.writeByte(mLabelsClickables ? (byte) 1 : (byte) 0);
    }

    private AutoLabelUISettings(Parcel in) {
        this.mMaxLabels = in.readInt();
        this.mShowCross = in.readByte() != 0;
        this.mIconCross = in.readInt();
        this.mTextColor = in.readInt();
        this.mTextSize = in.readInt();
        this.mBackgroundColor = in.readInt();
        this.mLabelsClickables = in.readByte() != 0;
    }

    public static final Creator<AutoLabelUISettings> CREATOR
            = new Creator<AutoLabelUISettings>() {
        public AutoLabelUISettings createFromParcel(Parcel source) {
            return new AutoLabelUISettings(source);
        }

        public AutoLabelUISettings[] newArray(int size) {
            return new AutoLabelUISettings[size];
        }
    };
}
