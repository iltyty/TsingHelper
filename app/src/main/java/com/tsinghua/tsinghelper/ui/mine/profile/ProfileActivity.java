package com.tsinghua.tsinghelper.ui.mine.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.relative_layout_items)
    RelativeLayout relative_layout_items;
    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.button_info_modify)
    Button mBtnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
                        ProfileActivity.this.runOnUiThread(
                                () -> mAvatar.setImageBitmap(bm));
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
