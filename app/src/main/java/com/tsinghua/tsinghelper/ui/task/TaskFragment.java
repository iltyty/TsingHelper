package com.tsinghua.tsinghelper.ui.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.ui.search.SearchActivity;
import com.tsinghua.tsinghelper.util.HttpUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskFragment extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.pager_tasks)
    ViewPager mViewPager;

    private static final int NUM_TABS = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState
    ) {
        View root = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, root);
        initTabs();
        initSearchView();
        return root;
    }

    private void initTabs() {
        PagerAdapter mPagerAdapter = new PagerAdapter(getFragmentManager(), NUM_TABS, getContext());
        mViewPager.setAdapter(mPagerAdapter);
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

    public static class PagerAdapter extends FragmentPagerAdapter {
        private Context mContext;
        private static final ArrayList<Fragment> mFragments = new ArrayList<>();
        private static final ArrayList<String> TITLES = new ArrayList<>();
        private final int numOfTabs;

        PagerAdapter(FragmentManager fm, int numTabs, Context cxt) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            mContext = cxt;
            numOfTabs = numTabs;

            TITLES.add(mContext.getResources().getString(R.string.task_tab_all));
            TITLES.add(mContext.getResources().getString(R.string.task_tab_time));
            TITLES.add(mContext.getResources().getString(R.string.task_tab_reward));

            for (int i = 0; i < numOfTabs; i++) {
                mFragments.add(new TaskListFragment(HttpUtil.TASK_GET_OTHERS));
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES.get(position);
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    public void initSearchView() {
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), SearchActivity.class);
                it.putExtra("searchType", "TASK");
                startActivityForResult(it, 1);
            }
        });
    }
}
