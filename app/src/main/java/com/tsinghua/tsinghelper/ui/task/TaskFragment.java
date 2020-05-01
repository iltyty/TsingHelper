package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tsinghua.tsinghelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskFragment extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.pager_tasks)
    ViewPager mViewPager;

    private static final int NUM_TABS = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, root);
        initTab();
        return root;
    }

    private void initTab() {
        PagerAdpter mPagerAdapter = new PagerAdpter(getFragmentManager(), NUM_TABS);
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText("所有任务"));
        mTabLayout.addTab(mTabLayout.newTab().setText("时间排序"));
        mTabLayout.addTab(mTabLayout.newTab().setText("报酬排序"));

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public static class PagerAdpter extends FragmentPagerAdapter {
        int numOfTabs;

        PagerAdpter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs = numOfTabs;
        }

        @Override
        public int getCount() {
            return this.numOfTabs;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new TaskFragment1();
            } else if(position == 1) {
                return new TaskFragment2();
            } else {
                return new TaskFragment3();
            }
        }
    }
}
