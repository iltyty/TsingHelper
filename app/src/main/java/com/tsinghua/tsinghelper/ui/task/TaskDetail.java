package com.tsinghua.tsinghelper.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.tsinghua.tsinghelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDetail extends AppCompatActivity {
    @BindView(R.id.toolbar_task)
    Toolbar mToolbar;
    @BindView(R.id.task_scrollView)
    ScrollView mScrollView;
    @BindView(R.id.time_check)
    TextView mTimeCheck;
    @BindView(R.id.times_per_person)
    TextView mTimesPerPerson;
    @BindView(R.id.text_task_title)
    TextView mTaskTitle;
    @BindView(R.id.text_task_reward)
    TextView mTaskReward;
    @BindView(R.id.text_task_deadline)
    TextView mTaskDeadline;
    @BindView(R.id.description)
    TextView mTaskDescription;
    @BindView(R.id.requirements)
    TextView mTaskRequirement;


    Intent mIntent;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        setStatusBarUpperAPI21();
        ButterKnife.bind(this);

        mIntent = getIntent();

        initToolbar();
        initLayout();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void initLayout() {
        // TODO: 2020/6/11 get task detail from backend
        mScrollView.setFillViewport(true);
    }

    private void setStatusBarUpperAPI21(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.darkblue));
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
    }
}
