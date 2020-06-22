package com.tsinghua.tsinghelper.ui.mine.profile;

import android.content.Intent;
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
import com.tsinghua.tsinghelper.ui.messages.MessageDetailActivity;
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
    @Nullable
    @BindView(R.id.btn_follow)
    Button mBtnFollow;
    @Nullable
    @BindView(R.id.btn_send_msg)
    Button mBtnSendMsg;
    @BindView(R.id.online_state)
    ImageView mOnlineState;
    @BindView(R.id.total_followed)
    TextView mTotalFollowers;
    @BindView(R.id.total_follow)
    TextView mTotalFollowings;

    private String uid;
    private String uname;
    private boolean isMe;
    private boolean hasFollowed;
    private int followersNum;
    private int followingsNum;

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
        isMe = userId.equals(String.valueOf(UserInfoUtil.me.id));

        if (isMe) {
            setContentView(R.layout.activity_profile_me);
        } else {
            setContentView(R.layout.activity_profile_others);
        }
    }

    private void initViews() {
        if (isMe) {
            mUsername.setText(UserInfoUtil.me.username);
            mSignature.setText(UserInfoUtil.me.signature);
            getMyFollowStates();
            switch (UserInfoUtil.me.state) {
                case "busy":
                    mOnlineState.setImageResource(R.drawable.ic_yellow_dot_8dp);
                    break;
                case "offline":
                    mOnlineState.setImageResource(R.drawable.ic_red_dot_8dp);
                    break;
                default:
                    mOnlineState.setImageResource(R.drawable.ic_green_dot_8dp);
                    break;
            }
        }
    }

    private void getMyFollowStates() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(UserInfoUtil.me.id));
        HttpUtil.get(HttpUtil.USER_FOLLOW_STATE, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.logToConsole(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 201) {
                    try {
                        JSONObject resJson = new JSONObject(response.body().string());
                        int followersNum = resJson.optInt("followersNum", 0);
                        int followingsNum = resJson.optInt("followingsNum", 0);
                        ProfileActivity.this.runOnUiThread(() -> {
                            mTotalFollowers.setText(String.valueOf(followersNum));
                            mTotalFollowings.setText(String.valueOf(followingsNum));
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
        String userId;
        if (isMe) {
            userId = String.valueOf(UserInfoUtil.me.id);
            getImages(userId);
        } else {
            userId = getIntent().getStringExtra("userId");
            uid = userId;
            getImages(userId);
            getUserInfo(userId);
        }
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
        params.put(UserInfoUtil.STATE, "");
        params.put(UserInfoUtil.USERNAME, "");
        params.put(UserInfoUtil.SIGNATURE, "");
        params.put(UserInfoUtil.PHONE, "");
        params.put(UserInfoUtil.REALNAME, "");
        params.put(UserInfoUtil.DEPARTMENT, "");
        params.put(UserInfoUtil.GRADE, "");
        params.put(UserInfoUtil.DORMITORY, "");
        params.put(UserInfoUtil.WECHAT, "");
        params.put(UserInfoUtil.EMAIL, "");
        params.put(UserInfoUtil.FOLLOWERS, "");
        params.put(UserInfoUtil.FOLLOWINGS, "");

        String url = HttpUtil.getUserProfileUrlById(userId);
        HttpUtil.get(url, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.logToConsole(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONObject resJson = new JSONObject(response.body().string());
                        UserDTO other = new UserDTO(resJson);
                        uname = other.username;
                        followersNum = other.followers.size();
                        followingsNum = other.followings.size();
                        for (UserDTO user : other.followers) {
                            if (user.id == UserInfoUtil.me.id) {
                                hasFollowed = true;
                                break;
                            }
                        }
                        ProfileActivity.this.runOnUiThread(() -> {
                            mUsername.setText(uname);
                            mSignature.setText(other.signature);
                            if (hasFollowed) {
                                setBtnFollowAsFollowed();
                            }
                            mTotalFollowers.setText(String.valueOf(followersNum));
                            mTotalFollowings.setText(String.valueOf(followingsNum));
                            switch (other.state) {
                                case "online":
                                    mOnlineState.setImageResource(R.drawable.ic_green_dot_8dp);
                                    break;
                                case "busy":
                                    mOnlineState.setImageResource(R.drawable.ic_yellow_dot_8dp);
                                    break;
                                case "offline":
                                    mOnlineState.setImageResource(R.drawable.ic_red_dot_8dp);
                                    break;
                            }
                        });
                    } catch (JSONException e) {
                        Log.e("error", e.toString());
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setBtnFollowAsFollowed() {
        assert mBtnFollow != null;
        mBtnFollow.setText("已关注");
        mBtnFollow.setTextColor(getColor(R.color.white));
        mBtnFollow.setBackground(getDrawable(R.drawable.btn_bg_transparent));
    }

    private void setBtnFollowAsUnfollowed() {
        assert mBtnFollow != null;
        mBtnFollow.setText("关注");
        mBtnFollow.setTextColor(getColor(R.color.black));
        mBtnFollow.setBackground(getDrawable(R.drawable.btn_follow));
    }

    public void btnFollowClicked(View view) {
        if (hasFollowed) {
            unfollow();
        } else {
            follow();
        }
    }

    private void follow() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", uid);
        HttpUtil.post(HttpUtil.USER_FOLLOW, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        ProfileActivity.this, "网络错误", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 201) {
                    hasFollowed = true;
                    followersNum++;
                    ProfileActivity.this.runOnUiThread(() -> {
                        mTotalFollowers.setText(String.valueOf(followersNum));
                        setBtnFollowAsFollowed();
                    });
                }
            }
        });
    }

    private void unfollow() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", uid);
        HttpUtil.post(HttpUtil.USER_UNFOLLOW, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        ProfileActivity.this, "网络错误", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 201) {
                    hasFollowed = false;
                    followersNum--;
                    ProfileActivity.this.runOnUiThread(() -> {
                        mTotalFollowers.setText(String.valueOf(followersNum));
                        setBtnFollowAsUnfollowed();
                    });
                }
            }
        });
    }

    public void sendMessage(View view) {
        Intent it = new Intent(this, MessageDetailActivity.class);
        it.putExtra("sender", uid);
        it.putExtra("username", uname);
        startActivity(it);
    }

    public void back(View view) {
        onBackPressed();
    }
}
