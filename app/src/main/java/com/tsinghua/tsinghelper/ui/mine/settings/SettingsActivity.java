package com.tsinghua.tsinghelper.ui.mine.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.components.PreferenceItem;
import com.tsinghua.tsinghelper.ui.login.LoginActivity;
import com.tsinghua.tsinghelper.util.GlideCacheUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image_cache)
    PreferenceItem mImageCache;
    @BindView(R.id.file_cache)
    PreferenceItem mFileCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initToolbar();
    }

    @Override
    public void onStart() {
        super.onStart();
        initPrefs();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initPrefs() {
        mImageCache.setValue(GlideCacheUtil.getCacheSize());
    }

    public void clearImageCache(View view) {
        GlideCacheUtil.clearImageAllCache();
        mImageCache.setValue(GlideCacheUtil.getCacheSize());
    }

    public void logout(View view) {
        UserInfoUtil.clearUserInfo();
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
        finish();
    }

    public void passwordChange(View view) {
        Intent it = new Intent(SettingsActivity.this, PswChangeActivity.class);
        startActivityForResult(it, 1);
    }
}
