package com.tsinghua.tsinghelper.ui.mine.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.UserDTO;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.tv_username)
    TextView mUsername;
    @BindView(R.id.tv_signature)
    TextView mSignature;
    @Nullable
    @BindView(R.id.button_info_modify)
    Button mBtnEdit;

    private boolean isMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        ButterKnife.bind(this);
    }

    private void setContentView() {
        Intent it = getIntent();
        String userId = it.getStringExtra("userId");
        assert userId != null;
        isMe = userId.equals(UserInfoUtil.getPref("userId", ""));

        if (isMe) {
            setContentView(R.layout.activity_profile_me);
        } else {
            setContentView(R.layout.activity_profile_others);
        }
    }

    private void initViews() {
        if (isMe) {
            SharedPreferences sp = UserInfoUtil.getUserInfoSharedPreferences();
            mUsername.setText(sp.getString("username", ""));
            mSignature.setText(sp.getString("signature", ""));
        } else {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
        String userId;
        if (isMe) {
            userId = UserInfoUtil
                    .getUserInfoSharedPreferences()
                    .getString("userId", "");
        } else {
            userId = getIntent().getStringExtra("userId");
        }

        getImages(userId);
        getUserInfo(userId);
    }

    private void getImages(String userId) {
        ArrayList<String> urls = new ArrayList<>();
        urls.add(HttpUtil.getUserAvatarUrlById(userId));
        urls.add(HttpUtil.getUserBgUrlById(userId));
        if (isMe) {
            Glide.with(this)
                    .load(urls.get(0))
                    .signature(new ObjectKey(
                            UserInfoUtil.getPref(UserInfoUtil.AVATAR_SIGN, "")
                    ))
                    .error(R.drawable.not_logged_in)
                    .into(mAvatar);
            Glide.with(this)
                    .load(urls.get(1))
                    .signature(new ObjectKey(
                            UserInfoUtil.getPref(UserInfoUtil.BG_SIGN, "")
                    ))
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource,
                                                    @Nullable Transition<? super Drawable> transition) {
                            mRelativeLayout.setBackground(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        } else {
            Glide.with(this)
                    .load(urls.get(0))
                    .signature(new ObjectKey(
                            UserInfoUtil.getPref(UserInfoUtil.AVATAR_SIGN, "")
                    ))
                    .error(R.drawable.not_logged_in)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(mAvatar);
            Glide.with(this)
                    .load(urls.get(1))
                    .signature(new ObjectKey(
                            UserInfoUtil.getPref(UserInfoUtil.BG_SIGN, "")
                    ))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource,
                                                    @Nullable Transition<? super Drawable> transition) {
                            mRelativeLayout.setBackground(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }

    public void editBtnClicked(View view) {
        Intent it = new Intent(this, ProfileSettingsActivity.class);
        startActivityForResult(it, 1);
    }

    private void getUserInfo(String userId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "");
        params.put("signature", "");
        String url = HttpUtil.getUserProfileUrlById(userId);
        HttpUtil.get(url, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.logToConsole(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONObject resJson = new JSONObject(response.body().string());
                        UserDTO user = new UserDTO(resJson);
                        if (isMe) {
                            UserInfoUtil.putPref("signature", user.signature);
                            ProfileActivity.this.runOnUiThread(() ->
                                    mSignature.setText(user.signature));
                        } else {
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mUsername.setText(user.username);
                                    mSignature.setText(user.signature);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        Log.e("error", e.toString());
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void follow(View view) {

    }

    public void sendMessage(View view) {

    }

    public void back(View view) {
        onBackPressed();
    }
}
