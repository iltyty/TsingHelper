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

public class MessageItem extends RelativeLayout {
    @BindView(R.id.message_item_icon)
    ImageView mSenderAvatar;
    @BindView(R.id.message_item_content)
    TextView mContent;
    @BindView(R.id.message_item_time)
    TextView mTime;

    private View mView;

    public MessageItem(Context context) { this(context, null); }

    public MessageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MessageItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.component_message_item,
                this, true);
        ButterKnife.bind(mView);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.message_item_attr);
        mSenderAvatar.setImageResource(a.getResourceId(
                R.styleable.message_item_attr_message_icon_src,  R.drawable.ic_home_item_36dp));
        mContent.setText(a.getString(R.styleable.message_item_attr_message_content_text));
        mTime.setText(a.getString(R.styleable.message_item_attr_message_time_text));
    }
}
