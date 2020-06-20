package com.tsinghua.tsinghelper.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.TaskAdapter;
import com.tsinghua.tsinghelper.components.DividerItemDecrator;
import com.tsinghua.tsinghelper.components.IconTextItem;
import com.tsinghua.tsinghelper.ui.search.SearchActivity;
import com.tsinghua.tsinghelper.util.HttpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.community)
    IconTextItem mCommunityItem;
    @BindView(R.id.meal)
    IconTextItem mMealItem;
    @BindView(R.id.study)
    IconTextItem mStudyItem;
    @BindView(R.id.questionnaire)
    IconTextItem mQuestItem;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.view)
    View mView;

    private TaskAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);

        setClickListeners();
        initRecyclerView();
        initSearchView();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.getTasks(null, HttpUtil.TASK_GET_OTHERS);
    }

    private void setClickListeners() {
        mCommunityItem.setOnClickListener(this);
        mMealItem.setOnClickListener(this);
        mStudyItem.setOnClickListener(this);
        mQuestItem.setOnClickListener(this);
    }

    private void initRecyclerView() {
        mAdapter = new TaskAdapter(getContext());
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        DividerItemDecrator divider = new DividerItemDecrator(
                requireActivity().getDrawable(R.drawable.shape_list_divider));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View v) {
        int pos = 0;
        switch (v.getId()) {
            case R.id.meal:
                pos = 1;
                break;
            case R.id.study:
                pos = 2;
                break;
            case R.id.questionnaire:
                pos = 3;
                break;
        }
        Intent it = new Intent(getActivity(), TasksTypeActivity.class);
        it.putExtra("pos", pos);
        startActivity(it);
    }

    public void initSearchView() {
        //搜索图标按钮(打开搜索框的按钮)的点击事件
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
