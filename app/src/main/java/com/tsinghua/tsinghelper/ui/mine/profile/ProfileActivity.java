package com.tsinghua.tsinghelper.ui.mine.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

        for (int i = 0; i < urls.size(); i++) {
            getImage(urls.get(i), i);
        }
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
                            ProfileActivity.this.runOnUiThread(
                                    () -> mAvatar.setImageBitmap(bm));
                        } else if (code == 1) {
                            // code for getting background image
                            ProfileActivity.this.runOnUiThread(new Runnable() {
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

    public void editBtnClicked(View view) {
        Intent it = new Intent(this, ProfileSettingsActivity.class);
        startActivityForResult(it, 1);
    }
}
