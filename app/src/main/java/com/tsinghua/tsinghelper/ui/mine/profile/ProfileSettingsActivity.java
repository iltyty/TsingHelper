package com.tsinghua.tsinghelper.ui.mine.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.components.PreferenceItem;
import com.tsinghua.tsinghelper.engines.GlideEngine;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private int AVATAR_REQUEST_CODE = 1;
    private int BG_REQUEST_CODE = 2;
    private int FIELD_MODIFY_CODE = 3;

    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.preference_username)
    PreferenceItem mUsername;
    @BindView(R.id.preference_signature)
    PreferenceItem mSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        ButterKnife.bind(this);

        setClickListeners();
    }

    private void initViews() {
        SharedPreferences sp = UserInfoUtil.getUserInfoSharedPreferences();
        mUsername.setValue(sp.getString("username", ""));
        mSignature.setValue(sp.getString("signature", "未填写"));
    }

    private void setClickListeners() {
        mUsername.setOnClickListener(this);
        mSignature.setOnClickListener(this);
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

//        for (int i = 0; i < urls.size(); i++) {
//            getImage(urls.get(i), i);
//        }

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
    }

    private void getImage(String url, int code) {
        try {
            HttpUtil.downloadImage(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response)
                        throws IOException {
                    if (response.code() == 200) {
                        byte[] bytes = response.body().bytes();
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        if (code == 0) {
                            // code for getting avatar
                            ProfileSettingsActivity.this.runOnUiThread(
                                    () -> mAvatar.setImageBitmap(bm));
                        } else if (code == 1) {
                            // code for getting background image
                            ProfileSettingsActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable db = new BitmapDrawable(getResources(), bm);
                                    mRelativeLayout.setBackground(db);
                                }
                            });
                        }
                    }
                }
            });
        } catch (IOException ignored) {
        }
    }

    public void showGallery(View view) {
        int code = view.getId() == R.id.avatar ? AVATAR_REQUEST_CODE : BG_REQUEST_CODE;
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .minSelectNum(0)
                .imageSpanCount(4)
                .isPreviewImage(true)
                .isZoomAnim(true)
                .isEnableCrop(true)
                .isCamera(false)
                .isCompress(true)
                .cropImageWideHigh(500, 500)
                .freeStyleCropEnabled(true)
                .withAspectRatio(1, 1)
                .forResult(code);
    }

    private String getImagePath(LocalMedia media) {
        if (media.isCut() && !media.isCompressed()) {
            // was cut
            return media.getCutPath();
        } else if (media.isCompressed()) {
            // was compressed
            return media.getCompressPath();
        }
        // original image
        return media.getPath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK && requestCode == FIELD_MODIFY_CODE) {
            String fieldName = data.getStringExtra("fieldName");
            String fieldValue = data.getStringExtra(fieldName);
            UserInfoUtil.putPref(fieldName, fieldValue);
            initViews();
        }
        if (resultCode == RESULT_OK &&
                (requestCode == AVATAR_REQUEST_CODE || requestCode == BG_REQUEST_CODE)) {
            images = PictureSelector.obtainMultipleResult(data);
            if (images.isEmpty()) {
                return;
            }
            String path = getImagePath(images.get(0));
            String url = requestCode == AVATAR_REQUEST_CODE ?
                    HttpUtil.AVATAR_UPLOAD : HttpUtil.BACKGROUND_UPLOAD;
            HttpUtil.uploadImage(url, path, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                    ToastUtil.showToastOnUIThread(ProfileSettingsActivity.this,
                            "服务器开了小差，休息一会儿吧");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response)
                        throws IOException {
                    if (response.code() == 201) {
                        if (requestCode == AVATAR_REQUEST_CODE) {
                            ProfileSettingsActivity.this.runOnUiThread(
                                    () -> mAvatar.setImageURI(Uri.parse(path)));
                        } else if (requestCode == BG_REQUEST_CODE) {
                            ProfileSettingsActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable db = Drawable.createFromPath(path);
                                    mRelativeLayout.setBackground(db);
                                }
                            });
                        }
                    } else {
                        ToastUtil.showToastOnUIThread(ProfileSettingsActivity.this,
                                "上传失败，请稍后重试");
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, FieldModifyActivity.class);
        switch (v.getId()) {
            case R.id.preference_username:
                it.putExtra("fieldTitle", "昵称");
                it.putExtra("fieldName", "username");
                it.putExtra("fieldMaxLen", UserInfoUtil.USERNAME_MAX_LEN);
                break;
            case R.id.preference_signature:
                it.putExtra("fieldTitle", "个性签名");
                it.putExtra("fieldName", "signature");
                it.putExtra("fieldMaxLen", UserInfoUtil.SIGNATURE_MAX_LEN);
                break;
        }
        startActivityForResult(it, FIELD_MODIFY_CODE);
    }
}
