package com.tsinghua.tsinghelper.ui.mine.profile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.components.PreferenceItem;
import com.tsinghua.tsinghelper.engines.GlideEngine;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
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

    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.preference_username)
    PreferenceItem mUsername;
    @BindView(R.id.preference_signature)
    PreferenceItem mSignature;
    @BindView(R.id.preference_phone)
    PreferenceItem mRealname;
    @BindView(R.id.preference_department)
    PreferenceItem mDepartment;
    @BindView(R.id.preference_grade)
    PreferenceItem mGrade;
    @BindView(R.id.preference_dormitory)
    PreferenceItem mDormitory;
    @BindView(R.id.preference_wechat)
    PreferenceItem mWechat;
    @BindView(R.id.preference_email)
    PreferenceItem mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        ButterKnife.bind(this);

        setClickListeners();
    }

    private void setClickListeners() {
        mUsername.setOnClickListener(this);
        mSignature.setOnClickListener(this);
        mRealname.setOnClickListener(this);
        mDepartment.setOnClickListener(this);
        mGrade.setOnClickListener(this);
        mDormitory.setOnClickListener(this);
        mWechat.setOnClickListener(this);
        mEmail.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
        getImages();
    }

    private void getImages() {
        ArrayList<String> urls = new ArrayList<>();
        String userId = String.valueOf(UserInfoUtil.me.id);
        urls.add(HttpUtil.getUserAvatarUrlById(userId));
        urls.add(HttpUtil.getUserBgUrlById(userId));

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
    }

    public void initViews() {
        mUsername.setValue(UserInfoUtil.me.username);
        mRealname.setValue(UserInfoUtil.me.realname);
        mDepartment.setValue(UserInfoUtil.me.department);
        mGrade.setValue(UserInfoUtil.me.grade);
        mDormitory.setValue(UserInfoUtil.me.dormitory);
        mWechat.setValue(UserInfoUtil.me.wechat);
        mEmail.setValue(UserInfoUtil.me.email);
        mSignature.setValue(UserInfoUtil.me.signature.isEmpty() ?
                "未填写" : UserInfoUtil.me.signature);
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
                    ErrorHandlingUtil.handleNetworkError(
                            ProfileSettingsActivity.this,
                            "服务器开了小差，休息一会儿吧", e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response)
                        throws IOException {
                    if (response.code() == 201) {
                        if (requestCode == AVATAR_REQUEST_CODE) {
                            UserInfoUtil.putPref(UserInfoUtil.AVATAR_SIGN,
                                    String.valueOf(System.currentTimeMillis()));
                            ProfileSettingsActivity.this.runOnUiThread(
                                    () -> mAvatar.setImageURI(Uri.parse(path)));
                        } else if (requestCode == BG_REQUEST_CODE) {
                            UserInfoUtil.putPref(UserInfoUtil.BG_SIGN,
                                    String.valueOf(System.currentTimeMillis()));
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
                it.putExtra("fieldName", UserInfoUtil.USERNAME);
                it.putExtra("fieldMaxLen", UserInfoUtil.USERNAME_MAX_LEN);
                break;
            case R.id.preference_signature:
                it.putExtra("fieldTitle", "个性签名");
                it.putExtra("fieldName", UserInfoUtil.SIGNATURE);
                it.putExtra("fieldMaxLen", UserInfoUtil.SIGNATURE_MAX_LEN);
                break;
            case R.id.preference_realname:
                it.putExtra("fieldTitle", "真实姓名");
                it.putExtra("fieldName", UserInfoUtil.REALNAME);
                it.putExtra("fieldMaxLen", UserInfoUtil.REALNAME_LEN);
                break;
            case R.id.preference_department:
                it.putExtra("fieldTitle", "所在院系");
                it.putExtra("fieldName", UserInfoUtil.DEPARTMENT);
                it.putExtra("fieldMaxLen", UserInfoUtil.DEPARTMENT_MAX_LEN);
                break;
            case R.id.preference_grade:
                it.putExtra("fieldTitle", "年级");
                it.putExtra("fieldName", UserInfoUtil.GRADE);
                it.putExtra("fieldMaxLen", UserInfoUtil.GRADE_MAX_LEN);
                break;
            case R.id.preference_dormitory:
                it.putExtra("fieldTitle", "宿舍地址");
                it.putExtra("fieldName", UserInfoUtil.DORMITORY);
                it.putExtra("fieldMaxLen", UserInfoUtil.DORMITORY_MAX_LEN);
                break;
            case R.id.preference_wechat:
                it.putExtra("fieldTitle", "微信");
                it.putExtra("fieldName", UserInfoUtil.WECHAT);
                it.putExtra("fieldMaxLen", UserInfoUtil.WECHAT_MAX_LEN);
                break;
            case R.id.preference_email:
                it.putExtra("fieldTitle", "邮箱地址");
                it.putExtra("fieldName", UserInfoUtil.EMAIL);
                it.putExtra("fieldMaxLen", UserInfoUtil.EMAIL_MAX_LEN);
                break;
        }
        startActivity(it);
    }

    public void back(View view) {
        onBackPressed();
    }
}
