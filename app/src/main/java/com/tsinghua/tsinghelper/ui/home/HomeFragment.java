package com.tsinghua.tsinghelper.ui.home;

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

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.TaskAdapter;
import com.tsinghua.tsinghelper.components.IconTextItem;
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

    private TaskAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);

        setClickListeners();
        initRecyclerView();

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
        DividerItemDecoration divider = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
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
}
