package com.tsinghua.tsinghelper.ui.task;

import android.content.Context;
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
import com.tsinghua.tsinghelper.dtos.TaskDTO;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TaskFragment extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.pager_tasks)
    ViewPager mViewPager;

    private static final int NUM_TABS = 3;

    private PagerAdapter mAdapter;
    private ArrayList<TaskDTO> mTasks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState
    ) {
        View root = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        getTasks();
    }

    private void getTasks() {
        HttpUtil.get(HttpUtil.TASK_GET_OTHERS, null, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        requireActivity(), "获取任务列表失败", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 200) {
                    String resStr = response.body().string();
                    try {
                        JSONObject resJson = new JSONObject(resStr);
                        JSONArray tasks = resJson.getJSONArray("tasks");
                        mTasks.clear();
                        for (int i = 0; i < tasks.length(); i++) {
                            JSONObject task = (JSONObject) tasks.get(i);
                            mTasks.add(new TaskDTO(task));
                        }
                        displayTasks();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void displayTasks() {
        if (mViewPager.getAdapter() == null) {
            initTabs();
        } else {
            mAdapter.setTasks(0, mTasks);
            mAdapter.setTasks(1, sortTasksByTime());
            mAdapter.setTasks(2, sortTasksByReward());
        }
    }

    private void initTabs() {
        mAdapter = new PagerAdapter(getFragmentManager(), NUM_TABS, getContext());
        mViewPager.setAdapter(mAdapter);
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

    private ArrayList<TaskDTO> sortTasksByTime() {
        Comparator<TaskDTO> sorter = (task1, task2) ->
                task1.startTime.compareTo(task2.startTime);
        ArrayList<TaskDTO> newTasks = new ArrayList<>(mTasks);
        Collections.sort(newTasks, sorter);
        return newTasks;
    }

    private ArrayList<TaskDTO> sortTasksByReward() {
        Comparator<TaskDTO> sorter = (task1, task2) ->
                Double.compare(task2.reward, task1.reward);
        ArrayList<TaskDTO> newTasks = new ArrayList<>(mTasks);
        Collections.sort(newTasks, sorter);
        return newTasks;
    }

    class PagerAdapter extends FragmentPagerAdapter {
        private Context mContext;
        private final ArrayList<TaskListFragment> mFragments = new ArrayList<>();
        private final ArrayList<String> TITLES = new ArrayList<>();
        private final int numOfTabs;

        PagerAdapter(FragmentManager fm, int numTabs, Context cxt) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            mContext = cxt;
            numOfTabs = numTabs;

            TITLES.add(mContext.getResources().getString(R.string.task_tab_all));
            TITLES.add(mContext.getResources().getString(R.string.task_tab_time));
            TITLES.add(mContext.getResources().getString(R.string.task_tab_reward));

            mFragments.add(new TaskListFragment(mTasks));
            mFragments.add(new TaskListFragment(sortTasksByTime()));
            mFragments.add(new TaskListFragment(sortTasksByReward()));
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

        public void setTasks(int pos, ArrayList<TaskDTO> tasks) {
            mFragments.get(pos).setTasks(tasks);
        }

    }
}
