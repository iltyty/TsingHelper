package com.tsinghua.tsinghelper.adapters;

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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<UserDTO> mUsers;
    private boolean showModerationBtn;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_user_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setUserData(mUsers.get(position));
        if (showModerationBtn) {
            holder.setBtnVisibility(View.VISIBLE);
            holder.setImageVisibility(View.INVISIBLE);
        } else {
            holder.setBtnVisibility(View.INVISIBLE);
            holder.setImageVisibility(View.VISIBLE);
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
        @BindView(R.id.btn_moderation)
        Button mBtnModeration;

        private int viewType;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.viewType = viewType;
            view.setOnClickListener(this);
        }

        void setUserData(UserDTO user) {
            mUsername.setText(user.username);
            mId.setText(String.format("ID: %s", String.valueOf(user.id)));

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
            Intent it = new Intent(mContext, ProfileActivity.class);
            it.putExtra("userId", String.valueOf(user.id));
            mContext.startActivity(it);
        }

        public void setImageVisibility(int visibility) {
            mImage.setVisibility(visibility);
        }

        public void setBtnVisibility(int visibility) {
            mBtnModeration.setVisibility(visibility);
        }
    }
}
