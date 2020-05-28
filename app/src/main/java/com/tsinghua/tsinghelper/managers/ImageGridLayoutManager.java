package com.tsinghua.tsinghelper.managers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageGridLayoutManager extends GridLayoutManager {

    final RecyclerView.State mState = new RecyclerView.State();
    private int[] mMeasureDimension = new int[2];

    public ImageGridLayoutManager(Context cxt, int spanCount) {
        super(cxt, spanCount);
    }

    public ImageGridLayoutManager(Context cxt, int spanCount,
                                  int orientation, boolean reverseLayout) {
        super(cxt, spanCount, orientation, reverseLayout);
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int pos,
                                   int widthSpec, int heightSpec, int[] measureDimension) {
        int count = mState.getItemCount();
        if (pos < count) {
            try {
                View view = recycler.getViewForPosition(0);
                RecyclerView.LayoutParams params =
                        (RecyclerView.LayoutParams) view.getLayoutParams();
                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                        getPaddingLeft() + getPaddingRight(), params.height);
                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                        getPaddingTop() + getPaddingBottom(), params.height);
                view.measure(childWidthSpec, childHeightSpec);
                measureDimension[0] = view.getMeasuredWidth() +
                        params.leftMargin + params.rightMargin;
                measureDimension[1] = view.getMeasuredHeight() +
                        params.bottomMargin + params.topMargin;
                recycler.recycleView(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);

        int width = 0;
        int height = 0;
        int span = getSpanCount();
        int count = getItemCount();

        for (int i = 0; i < count; i++) {
            measureScrapChild(recycler, i,
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    mMeasureDimension);

            if (getOrientation() == HORIZONTAL) {
                if (i % span == 0) {
                    width += mMeasureDimension[0];
                }
                if (i == 0) {
                    height = mMeasureDimension[1];
                }
            } else {
                if (i % span == 0) {
                    height += mMeasureDimension[1];
                }
                if (i == 0) {
                    width = mMeasureDimension[0];
                }
            }
        }

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        }
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        }

        setMeasuredDimension(width, height);
    }
}
