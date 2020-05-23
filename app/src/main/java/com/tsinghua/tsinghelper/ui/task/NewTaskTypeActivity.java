package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.components.IconTextItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewTaskTypeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.community)
    IconTextItem mCommunityItem;
    @BindView(R.id.meal)
    IconTextItem mMealItem;
    @BindView(R.id.study)
    IconTextItem mStudyItem;
    @BindView(R.id.questionnaire)
    IconTextItem mQuestItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_type);
        ButterKnife.bind(this);

        initToolbar();
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    public void setClickListeners() {
        mCommunityItem.setOnClickListener(this);
        mMealItem.setOnClickListener(this);
        mStudyItem.setOnClickListener(this);
        mQuestItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community:
            case R.id.meal:
            case R.id.study:
            case R.id.questionnaire:
                System.out.println("clicked");
                break;
        }
    }
}
