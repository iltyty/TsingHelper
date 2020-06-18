package com.tsinghua.tsinghelper.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsinghua.tsinghelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserItem extends RelativeLayout {

    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.id)
    TextView mId;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.btn_moderation)
    Button mBtn;

    private View mView;

    public UserItem(Context context) {
        this(context, null);
    }

    public UserItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public UserItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.component_user_item,
                this, true);
        ButterKnife.bind(mView);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UserItem);
        mAvatar.setImageResource(a.getResourceId(R.styleable.UserItem_user_avatar_src,
                R.drawable.not_logged_in));
        mUsername.setText(a.getString(R.styleable.UserItem_user_username));
        mId.setText(a.getString(R.styleable.UserItem_user_id));
        mImage.setImageResource(a.getResourceId(R.styleable.UserItem_android_src,
                R.drawable.ic_right_arrow_8dp));
    }

    public void setAvatar(Drawable db) {
        mAvatar.setImageDrawable(db);
    }

    public void setUsername(String username) {
        mUsername.setText(username);
    }

    public void setId(String id) {
        mId.setText("ID: " + id);
    }

    public void setId(int id) {
        setId(String.valueOf(id));
    }

    public void setImageVisibility(int visibility) {
        mImage.setVisibility(visibility);
    }

    public void setBtnVisibility(int visibility) {
        mBtn.setVisibility(visibility);
    }
}

