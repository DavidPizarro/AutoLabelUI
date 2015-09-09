package com.dpizarro.autolabel.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

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
public class AutoViewGroup extends ViewGroup {

    private final List<List<View>> mLines       = new ArrayList<>();
    private final List<Integer> mLineHeights    = new ArrayList<>();
    private final List<Integer> mLineMargins    = new ArrayList<>();
    private List<View> lineViews                = new ArrayList<>();

    public AutoViewGroup(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * This method will place each label next to another. If there is not enough
     * space for the next label, it will be added in a new new line.
     *
     * See {@link AutoLabelUI#onLayout(boolean, int, int, int, int)}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = getPaddingTop() + getPaddingBottom();

        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);
            boolean lastChild = i == childCount - 1;

            if (child.getVisibility() == View.GONE) {

                if (lastChild) {
                    width = Math.max(width, lineWidth);
                    height += lineHeight;
                }

                continue;
            }

            measureChildWithMargins(child, widthMeasureSpec, lineWidth, heightMeasureSpec, height);

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidthMode = MeasureSpec.AT_MOST;
            int childWidthSize = sizeWidth;

            int childHeightMode = MeasureSpec.AT_MOST;
            int childHeightSize = sizeHeight;

            if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthMode = MeasureSpec.EXACTLY;
                childWidthSize -= lp.leftMargin + lp.rightMargin;
            } else if (lp.width >= 0) {
                childWidthMode = MeasureSpec.EXACTLY;
                childWidthSize = lp.width;
            }

            if (lp.height >= 0) {
                childHeightMode = MeasureSpec.EXACTLY;
                childHeightSize = lp.height;
            } else if (modeHeight == MeasureSpec.UNSPECIFIED) {
                childHeightMode = MeasureSpec.UNSPECIFIED;
                childHeightSize = 0;
            }

            child.measure(
                    MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                    MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode)
            );

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            if (lineWidth + childWidth > sizeWidth) {

                width = Math.max(width, lineWidth);
                lineWidth = childWidth;

                height += lineHeight;
                lineHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            }

            if (lastChild) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }

        }

        width += getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(
                (modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width,
                (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    /**
     * This method will place each label next to another. If there is not enough
     * space for the next label, it will be added in a new new line.
     *
     * See {@link AutoLabelUI#onMeasure(int, int)}
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mLines.clear();
        mLineHeights.clear();
        mLineMargins.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;
        lineViews.clear();

        float horizontalGravityFactor = 0;

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;

            if (lineWidth + childWidth > width) {
                mLineHeights.add(lineHeight);
                mLines.add(lineViews);
                mLineMargins.add((int) ((width - lineWidth) * horizontalGravityFactor)
                        + getPaddingLeft());

                lineHeight = 0;
                lineWidth = 0;
                lineViews = new ArrayList<>();
            }

            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(child);
        }

        mLineHeights.add(lineHeight);
        mLines.add(lineViews);
        mLineMargins.add((int) ((width - lineWidth) * horizontalGravityFactor) + getPaddingLeft());

        int verticalGravityMargin = 0;

        int numLines = mLines.size();

        int left;
        int top = getPaddingTop();

        for (int i = 0; i < numLines; i++) {

            lineHeight = mLineHeights.get(i);
            lineViews = mLines.get(i);
            left = mLineMargins.get(i);

            int children = lineViews.size();

            for (int j = 0; j < children; j++) {

                View child = lineViews.get(j);

                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                // if height is match_parent we need to remeasure child to line height
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    int childWidthMode = MeasureSpec.AT_MOST;
                    int childWidthSize = lineWidth;

                    if (lp.width == LayoutParams.MATCH_PARENT) {
                        childWidthMode = MeasureSpec.EXACTLY;
                    } else if (lp.width >= 0) {
                        childWidthMode = MeasureSpec.EXACTLY;
                        childWidthSize = lp.width;
                    }

                    child.measure(
                            MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                            MeasureSpec.makeMeasureSpec(lineHeight - lp.topMargin - lp.bottomMargin,
                                    MeasureSpec.EXACTLY)
                    );
                }

                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                int gravityMargin = 0;

                if (Gravity.isVertical(lp.gravity)) {
                    switch (lp.gravity) {
                        case Gravity.TOP:
                        default:
                            break;
                        case Gravity.CENTER_VERTICAL:
                        case Gravity.CENTER:
                            gravityMargin =
                                    (lineHeight - childHeight - lp.topMargin - lp.bottomMargin) / 2;
                            break;
                        case Gravity.BOTTOM:
                            gravityMargin = lineHeight - childHeight - lp.topMargin
                                    - lp.bottomMargin;
                            break;
                    }
                }

                child.layout(left + lp.leftMargin,
                        top + lp.topMargin + gravityMargin + verticalGravityMargin,
                        left + childWidth + lp.leftMargin,
                        top + childHeight + lp.topMargin + gravityMargin + verticalGravityMargin);

                left += childWidth + lp.leftMargin + lp.rightMargin;

            }

            top += lineHeight;
        }

    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public final int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

    }
}
