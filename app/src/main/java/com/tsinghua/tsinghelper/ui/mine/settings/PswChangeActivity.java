package com.tsinghua.tsinghelper.ui.mine.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.tsinghua.tsinghelper.R;

public class PswChangeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.psw_old)
    EditText mPswOld;
    @BindView(R.id.psw_new_1)
    EditText mPswNew1;
    @BindView(R.id.psw_new_2)
    EditText mPswNew2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psw_change);

        ButterKnife.bind(this);
        initToolbar();
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void pswReset(View view) {
    }
}
