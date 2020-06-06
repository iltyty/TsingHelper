package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.TaskAdapter;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskListFragment extends Fragment {

    private HashMap<String, String> queryParams = new HashMap<>();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DividerItemDecoration mDivider;

    public TaskListFragment() {
    }

    public TaskListFragment(String taskType) {
        queryParams.put("type", taskType);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.getAllTasks(queryParams);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        return root;
    }

    private void initRecyclerView() {
        mAdapter = new TaskAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mDivider = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(mDivider);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
    }
}
