package com.tsinghua.tsinghelper.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tsinghua.tsinghelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.cancel)
    Button mCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Intent it = getIntent();

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(it.getStringExtra("searchType").equals("TASK")) {
                    Intent intent = new Intent(SearchActivity.this, TaskSearchActivity.class);
                    intent.putExtra("queryText", query);
                    startActivity(intent);
                } else if(it.getStringExtra("searchType").equals("MESSAGE")) {
                    Intent intent = new Intent(SearchActivity.this, MessageSearchActivity.class);
                    intent.putExtra("queryText", query);
                    startActivity(intent);
                }
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void cancel(View view) {
        finish();
    }
}
