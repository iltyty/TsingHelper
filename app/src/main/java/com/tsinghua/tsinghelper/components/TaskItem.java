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

public class TaskItem extends RelativeLayout {

    @BindView(R.id.task_item_avatar)
    ImageView mAvatar;
    @BindView(R.id.task_item_title)
    TextView mTitle;
    @BindView(R.id.task_item_reward)
    TextView mReward;
    @BindView(R.id.task_item_deadline)
    TextView mDeadline;

    private View mView;

    public TaskItem(Context context) {
        this(context, null);
    }

    public TaskItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TaskItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.component_task_item,
                this, true);
        ButterKnife.bind(mView);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TaskItem);
        mAvatar.setImageResource(a.getResourceId(R.styleable.TaskItem_task_avatar_src,
                R.drawable.ic_community_item_32dp));
        mTitle.setText(a.getString(R.styleable.TaskItem_task_title));
        mReward.setText(a.getString(R.styleable.TaskItem_task_reward));
        mDeadline.setText(a.getString(R.styleable.TaskItem_task_deadline));
    }
}

