package com.tsinghua.tsinghelper.ui.mine.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.engines.GlideEngine;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileSettingsActivity extends AppCompatActivity {

    @BindView(R.id.avatar)
    CircleImageView mAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        ButterKnife.bind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            String userId = UserInfoUtil
                    .getUserInfoSharedPreferences()
                    .getString("userId", "");
            String url = String.format("%s%s/avatar", HttpUtil.USER_PREFIX, userId);
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
                        ProfileSettingsActivity.this.runOnUiThread(
                                () -> mAvatar.setImageBitmap(bm));
                    }
                }
            });
        } catch (IOException ignored) {
        }
    }

    public void showGallery(View view) {
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
                .forResult(PictureConfig.CHOOSE_REQUEST);
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
        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            images = PictureSelector.obtainMultipleResult(data);
            if (images.isEmpty()) {
                return;
            }
            String path = getImagePath(images.get(0));
            HttpUtil.uploadImage(HttpUtil.AVATAR_UPLOAD, path, new Callback() {
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
                        ProfileSettingsActivity.this.runOnUiThread(
                                () -> mAvatar.setImageURI(Uri.parse(path)));
                    } else {
                        ToastUtil.showToastOnUIThread(ProfileSettingsActivity.this,
                                "上传头像失败，请稍后重试");
                    }
                }
            });
        }
    }
}
