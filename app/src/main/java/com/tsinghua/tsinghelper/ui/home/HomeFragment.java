package com.tsinghua.tsinghelper.ui.home;

import android.content.Context;
import android.content.Intent;
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

import com.tsinghua.tsinghelper.MainActivity;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.TaskAdapter;
import com.tsinghua.tsinghelper.dtos.TaskDTO;
import com.tsinghua.tsinghelper.ui.task.TaskDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DividerItemDecoration mDivider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        return root;
    }

    private void initRecyclerView() {
        mAdapter = new TaskAdapter(genTasks());
        mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mDivider = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(mDivider);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        mAdapter.setOnItemClickListener(myOnClickListener);
    }

    private ArrayList<TaskDTO> genTasks() {
        ArrayList<TaskDTO> tasks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String s = String.valueOf(i + 1);
            tasks.add(new TaskDTO("任务" + s,
                    "报酬" + s, "截止日期" + s));
        }
        return tasks;
    }

    private TaskAdapter.OnItemClickListener myOnClickListener = new TaskAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            TaskDTO task = mAdapter.mTasks.get(position);
            Intent intent = new Intent(getActivity(), TaskDetail.class);
            intent.putExtra("taskTitle", task.getTitle());
            startActivity(intent);
        }
    };
}
