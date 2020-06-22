package com.tsinghua.tsinghelper.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.UserDTO;
import com.tsinghua.tsinghelper.ui.mine.profile.ProfileActivity;
import com.tsinghua.tsinghelper.ui.task.TaskReviewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserItemAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<UserDTO> mUsers;
    private boolean showModerationBtn;
    private static final int SHOW_MODERATION_BTN_VIEW_TYPE = -1;

    public UserItemAdapter(Context cxt, boolean show) {
        mContext = cxt;
        showModerationBtn = show;
        mUsers = new ArrayList<>();
    }

    public UserItemAdapter(Context cxt, ArrayList<UserDTO> users, boolean show) {
        mUsers = users;
        mContext = cxt;
        showModerationBtn = show;
    }

    public void setUsers(ArrayList<UserDTO> users) {
        mUsers = users;
    }

    public void clearUsers() {
        mUsers.clear();
    }

    @Override
    public int getItemViewType(int pos) {
        if (mUsers == null || mUsers.size() <= 0) {
            return SHOW_MODERATION_BTN_VIEW_TYPE;
        }
        return super.getItemViewType(pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == SHOW_MODERATION_BTN_VIEW_TYPE) {
            view = inflater.inflate(R.layout.component_empty_recycler_view, parent, false);
            return new EmptyViewHolder(view);
        }
        view = inflater.inflate(R.layout.component_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mUsers.size() > 0 ? mUsers.size() : 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder newHolder = (ViewHolder) holder;
            newHolder.setUserData(mUsers.get(position));
            if (showModerationBtn) {
                newHolder.setImageVisibility(View.INVISIBLE);
                newHolder.setBtnPassedVisibility(View.VISIBLE);
                newHolder.setBtnFailedVisibility(View.VISIBLE);
            } else {
                newHolder.setImageVisibility(View.VISIBLE);
                newHolder.setBtnPassedVisibility(View.INVISIBLE);
                newHolder.setBtnFailedVisibility(View.INVISIBLE);
            }
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(@NonNull View view) {
            super(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.avatar)
        CircleImageView mAvatar;
        @BindView(R.id.username)
        TextView mUsername;
        @BindView(R.id.id)
        TextView mId;
        @BindView(R.id.image)
        ImageView mImage;
        @BindView(R.id.btn_passed)
        Button mBtnPassed;
        @BindView(R.id.btn_failed)
        Button mBtnFailed;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            mBtnPassed.setOnClickListener(this);
            mBtnFailed.setOnClickListener(this);
        }

        void setUserData(UserDTO user) {
            mUsername.setText(user.username);
            mId.setText(String.format("ID: %s", String.valueOf(user.id)));
            mImage.setImageResource(R.drawable.ic_right_arrow_8dp);

            Glide.with(mContext)
                    .load(user.avatar)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.not_logged_in)
                    .into(mAvatar);
        }

        @Override
        public void onClick(View v) {
            UserDTO user = mUsers.get(getAdapterPosition());
            if (v.getId() == R.id.btn_passed) {
                Activity activity = (Activity) mContext;
                if (activity instanceof TaskReviewActivity) {
                    ((TaskReviewActivity) activity).moderateTask(user, true);
                }
                return;
            } else if (v.getId() == R.id.btn_failed) {
                Activity activity = (Activity) mContext;
                if (activity instanceof TaskReviewActivity) {
                    ((TaskReviewActivity) activity).moderateTask(user, false);
                }
                return;
            }
            Intent it = new Intent(mContext, ProfileActivity.class);
            it.putExtra("userId", String.valueOf(user.id));
            mContext.startActivity(it);
        }

        public void setImageVisibility(int visibility) {
            mImage.setVisibility(visibility);
        }

        public void setBtnPassedVisibility(int visibility) {
            mBtnPassed.setVisibility(visibility);
        }

        public void setBtnFailedVisibility(int visibility) {
            mBtnFailed.setVisibility(visibility);
        }
    }
}
