package com.tsinghua.tsinghelper.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.TaskAdapter;
import com.tsinghua.tsinghelper.components.DividerItemDecrator;
import com.tsinghua.tsinghelper.util.HttpUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskSearchActivity extends AppCompatActivity {
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.task_result)
    RecyclerView mRecyclerView;

    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DividerItemDecrator mDivider;
    private HashMap<String, String> queryParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_task);
        ButterKnife.bind(this);

        initSearchView();
        initRecyclerView();
    }

    public void initSearchView() {
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TaskSearchActivity.this, SearchActivity.class);
                it.putExtra("searchType", "TASK");
                startActivityForResult(it, 1);
            }
        });
    }

    public void initRecyclerView() {
        // TODO: 2020/6/20 get search result from backend
        queryParams = new HashMap<>();
        queryParams.put("searchType", "TASK");


        mAdapter = new TaskAdapter(mRecyclerView.getContext());
        mLayoutManager = new LinearLayoutManager(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        mDivider = new DividerItemDecrator(
                this.getDrawable(R.drawable.shape_list_divider));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(mDivider);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        mAdapter.getTasks(queryParams, HttpUtil.TASK_GET_OTHERS);
    }

    public void cancel(View view) {
        finish();
    }
}
