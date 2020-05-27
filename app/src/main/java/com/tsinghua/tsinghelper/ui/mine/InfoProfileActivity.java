package com.tsinghua.tsinghelper.ui.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.tsinghua.tsinghelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoProfileActivity extends AppCompatActivity {
    @BindView(R.id.relative_layout_items)
    RelativeLayout relative_layout_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info);
        ButterKnife.bind(this);

        initLayout();
    }

    private void initLayout() {

    }
}
