package com.tsinghua.tsinghelper.ui.search;

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

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.cancel)
    Button mCancel;
    @BindView(R.id.task_result)
    RecyclerView mRecyclerView;

    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initRecyclerView();

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTasks(query);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchTasks(String query) {
        HashMap<String, String> params = new HashMap<>();
        params.put("q", query);
        mAdapter.getTasks(params, HttpUtil.TASK_SEARCH);
    }

    public void initRecyclerView() {
        mAdapter = new TaskAdapter(mRecyclerView.getContext());
        RecyclerView.LayoutManager lm = new LinearLayoutManager(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        DividerItemDecrator divider = new DividerItemDecrator(
                this.getDrawable(R.drawable.shape_list_divider));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    public void cancel(View view) {
        finish();
    }
}
