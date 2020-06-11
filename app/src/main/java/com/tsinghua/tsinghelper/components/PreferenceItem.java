package com.tsinghua.tsinghelper.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsinghua.tsinghelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreferenceItem extends RelativeLayout {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.value)
    TextView mValue;

    private View mView;

    public PreferenceItem(Context context) {
        this(context, null);
    }

    public PreferenceItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PreferenceItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.component_preference_item,
                this, true);
        ButterKnife.bind(mView);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PreferenceItem);
        mTitle.setText(a.getString(R.styleable.PreferenceItem_preference_title));
        mValue.setText(a.getString(R.styleable.PreferenceItem_preference_value));
    }

    public void setValue(String value) {
        mValue.setText(value);
    }
}
