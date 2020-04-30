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

public class IconTextItem extends RelativeLayout {
    @BindView(R.id.item_icon)
    ImageView itemIcon;
    @BindView(R.id.item_text)
    TextView itemText;

    private View mView;

    public IconTextItem(Context context) { this(context, null); }

    public IconTextItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IconTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.component_icon_text_item,
                this, true);
        ButterKnife.bind(mView);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconTextItem);
        itemIcon.setImageResource(a.getResourceId(R.styleable.IconTextItem_icon_src,
                R.drawable.ic_community_item_32dp));
        itemText.setText(a.getString(R.styleable.IconTextItem_text));
    }
}
