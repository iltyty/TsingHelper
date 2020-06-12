package com.tsinghua.tsinghelper.ui.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.ui.mine.profile.ProfileActivity;
import com.tsinghua.tsinghelper.ui.mine.settings.SettingsActivity;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.tv_username)
    TextView mUsername;
    @BindView(R.id.avatar)
    CircleImageView mAvatar;

    @BindView(R.id.icon_settings)
    ImageView mToSettings;
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
        mToSettings.setOnClickListener(this);
        mToProfile.setOnClickListener(this);
        mToPublished.setOnClickListener(this);
        mToDoing.setOnClickListener(this);
        mToOthers.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_settings:
                Intent itSettings = new Intent(getActivity(), SettingsActivity.class);
                startActivity(itSettings);
                break;
            case R.id.relative_layout_to_profile:
                Intent itInfo = new Intent(getActivity(), ProfileActivity.class);
                startActivity(itInfo);
                break;
            case R.id.relative_layout_to_published:
                Intent itPublished = new Intent(getActivity(), PublishedTasksActivity.class);
                startActivity(itPublished);
                break;
            case R.id.relative_layout_to_doing:
                Intent itDoing = new Intent(getActivity(), DoingTasksActivity.class);
                startActivity(itDoing);
                break;
            case R.id.relative_layout_to_others:
                break;
        }
    }

    private void setUserInfo() {
        mUsername.setText(mSharedPreferences.getString("username", ""));
    }

    @Override
    public void onStart() {
        super.onStart();
        String userId = UserInfoUtil.getPref("userId", "");
        String url = HttpUtil.getUserAvatarUrlById(userId);
        Glide.with(requireContext())
                .load(url)
                .signature(new ObjectKey(
                        UserInfoUtil.getPref(UserInfoUtil.AVATAR_SIGN, "")
                ))
                .error(R.drawable.not_logged_in)
                .into(mAvatar);
    }
}
