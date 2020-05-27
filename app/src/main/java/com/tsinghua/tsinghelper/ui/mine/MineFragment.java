package com.tsinghua.tsinghelper.ui.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineFragment extends Fragment implements View.OnClickListener {

    private static final int TO_PROFILE_CODE = 1;
    private static final int TO_PUBLISHED_CODE = 2;
    private static final int TO_DOING_CODE = 3;
    private static final int TO_OTHERS_CODE = 4;

    @BindView(R.id.tv_username)
    TextView mUsername;

    @BindView(R.id.relative_layout_to_profile)
    RelativeLayout mToProfile;
    @BindView(R.id.relative_layout_to_published)
    RelativeLayout mToPublished;
    @BindView(R.id.relative_layout_to_doing)
    RelativeLayout mToDoing;
    @BindView(R.id.relative_layout_to_others)
    RelativeLayout mToOthers;
    private SharedPreferences mSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, root);

        mSharedPreferences = UserInfoUtil.getUserInfoSharedPreferences();

        setUserInfo();
        setClickListeners();

        return root;
    }

    private void setClickListeners() {
        mToProfile.setOnClickListener(this);
        mToPublished.setOnClickListener(this);
        mToDoing.setOnClickListener(this);
        mToOthers.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_layout_to_profile:
                break;
            case R.id.relative_layout_to_published:
                Intent itPublished = new Intent(getActivity(), PublishedTasksActivity.class);
                startActivityForResult(itPublished, TO_PUBLISHED_CODE);
                break;
            case R.id.relative_layout_to_doing:
                Intent itDoing = new Intent(getActivity(), DoingTasksActivity.class);
                startActivityForResult(itDoing, TO_DOING_CODE);
                break;
            case R.id.relative_layout_to_others:
                break;
        }
    }

    private void setUserInfo() {
        mUsername.setText(mSharedPreferences.getString("username", ""));
    }
}
