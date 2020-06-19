package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.TaskAdapter;
import com.tsinghua.tsinghelper.components.DividerItemDecrator;
import com.tsinghua.tsinghelper.dtos.TaskDTO;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskListFragment extends Fragment {

    private HashMap<String, String> queryParams;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DividerItemDecrator mDivider;

    private String url;

    public TaskListFragment(String url) {
        this.url = url;
    }

    public TaskListFragment(HashMap<String, String> params, String url) {
        this.url = url;
        queryParams = params;
    }

    public TaskListFragment(ArrayList<TaskDTO> tasks) {
        mAdapter.setTasks(tasks);
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

    @Override
    public void onStart() {
        super.onStart();
        if (queryParams != null) {
            mAdapter.getTasks(queryParams, url);
        }
    }

    private void initRecyclerView() {
        mAdapter = new TaskAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mDivider = new DividerItemDecrator(
                requireActivity().getDrawable(R.drawable.shape_list_divider));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(mDivider);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
    }
}
