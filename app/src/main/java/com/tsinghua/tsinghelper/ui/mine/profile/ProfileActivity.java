package com.tsinghua.tsinghelper.ui.mine.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.tv_username)
    TextView mUsername;
    @BindView(R.id.tv_signature)
    TextView mSignature;
    @BindView(R.id.button_info_modify)
    Button mBtnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }

    private void initViews() {
        SharedPreferences sp = UserInfoUtil.getUserInfoSharedPreferences();
        mUsername.setText(sp.getString("username", ""));
        mSignature.setText(sp.getString("signature", ""));
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
        String userId = UserInfoUtil
                .getUserInfoSharedPreferences()
                .getString("userId", "");
        ArrayList<String> urls = new ArrayList<>();
        urls.add(String.format("%s%s/avatar", HttpUtil.USER_PREFIX, userId));
        urls.add(String.format("%s%s/background", HttpUtil.USER_PREFIX, userId));

        try {
            Glide.with(this).load(urls.get(0)).into(mAvatar);
            Glide.with(this).load(urls.get(1)).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource,
                                            @Nullable Transition<? super Drawable> transition) {
                    mRelativeLayout.setBackground(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    public void editBtnClicked(View view) {
        Intent it = new Intent(this, ProfileSettingsActivity.class);
        startActivityForResult(it, 1);
    }
}
