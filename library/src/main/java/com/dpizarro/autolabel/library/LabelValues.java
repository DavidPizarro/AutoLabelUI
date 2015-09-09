package com.dpizarro.autolabel.library;

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
public class LabelValues implements Parcelable {

    private int key;
    private String value;

    public LabelValues(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.key);
        dest.writeString(this.value);
    }

    private LabelValues(Parcel in) {
        this.key = in.readInt();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<LabelValues> CREATOR
            = new Parcelable.Creator<LabelValues>() {
        public LabelValues createFromParcel(Parcel source) {
            return new LabelValues(source);
        }

        public LabelValues[] newArray(int size) {
            return new LabelValues[size];
        }
    };
}
