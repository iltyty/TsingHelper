package com.tsinghua.tsinghelper.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsinghua.tsinghelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountStateItem extends RelativeLayout {
    @BindView(R.id.state_icon)
    ImageView itemIcon;
    @BindView(R.id.state_text)
    TextView itemText;

    private View mView;

    public AccountStateItem(Context context) { this(context, null); }

    public AccountStateItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AccountStateItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.component_state_item,
                this, true);
        ButterKnife.bind(mView);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AccountStateItem);
        itemIcon.setImageResource(a.getResourceId(R.styleable.AccountStateItem_state_icon_src,
                R.drawable.ic_green_dot_8dp));
        itemText.setText(a.getString(R.styleable.AccountStateItem_state_text));
    }
}
