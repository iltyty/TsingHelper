package com.tsinghua.tsinghelper.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.ui.task.TaskListFragment;
import com.tsinghua.tsinghelper.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TakenTasksActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.pager_tasks)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_tasks);
        ButterKnife.bind(this);

        initToolbar();
        initTabLayout();
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    public void initTabLayout() {
        Adapter adapter = new Adapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        Intent it = getIntent();
        mTabLayout.getTabAt(it.getIntExtra("pos", 0)).select();
    }

    public static class Adapter extends FragmentPagerAdapter {

        private final int TAB_CNT = 4;
        private final String[] TYPES = {
                "doing", "moderating", "rewarded", "failed"
        };
        private final String[] TITLE = {
                "待提交", "待审核", "已领赏", "任务失败"
        };
        private final ArrayList<Fragment> mFragments = new ArrayList<>();
        private Context mContext;

        Adapter(FragmentManager fm, Context cxt) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            mContext = cxt;

            HashMap<String, String> params = new HashMap<>();
            for (int i = 0; i < TAB_CNT; i++) {
                params.put("type", TYPES[i]);
                mFragments.add(new TaskListFragment(params, HttpUtil.TASK_GET_OTHERS));
            }
        }

        @Override
        public int getCount() {
            return TAB_CNT;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
