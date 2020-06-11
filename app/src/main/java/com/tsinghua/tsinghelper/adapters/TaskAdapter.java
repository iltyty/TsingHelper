package com.tsinghua.tsinghelper.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.TaskDTO;
import com.tsinghua.tsinghelper.ui.task.TaskDetail;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<TaskDTO> mTasks;

    public TaskAdapter(Context cxt) {
        mContext = cxt;
        mTasks = new ArrayList<>();
    }

    public void setTasks(ArrayList<TaskDTO> tasks) {
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_task_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mTasks == null ? 0 : mTasks.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setTaskData(mTasks.get(position));
    }

    public void getAllTasks(HashMap<String, String> params) {
        HttpUtil.get(HttpUtil.TASK_GET_ALL, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ToastUtil.showToastOnUIThread((Activity) mContext,
                        "获取任务列表失败，请稍后重试");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                String resStr = response.body().string();
                try {
                    JSONObject resJson = new JSONObject(resStr);
                    JSONArray tasks = resJson.getJSONArray("tasks");
                    mTasks.clear();
                    for (int i = 0; i < tasks.length(); i++) {
                        JSONObject task = (JSONObject) tasks.get(i);
                        mTasks.add(new TaskDTO(task));
                    }
                    ((Activity) mContext).runOnUiThread(() -> notifyDataSetChanged());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.task_item_avatar)
        ImageView mTaskAvatar;
        @BindView(R.id.task_item_title)
        TextView mTaskTitle;
        @BindView(R.id.task_item_reward)
        TextView mTaskReward;
        @BindView(R.id.task_item_deadline)
        TextView mTaskDeadline;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        void setTaskData(TaskDTO task) {
            char rmb = 165;
            mTaskTitle.setText(task.title);
            mTaskDeadline.setText(task.deadlineStr);
            mTaskReward.setText(String.format(Locale.CHINA,
                    "%s%.2f元", String.valueOf(rmb), task.reward));

            // TODO: set task publisher's avatar
            mTaskAvatar.setImageResource(R.drawable.ic_community_item_32dp);

            String url = String.format("%s%s/avatar", HttpUtil.USER_PREFIX, task.publisherId);
            try {
                Glide.with(mContext)
                        .load(url)
                        .signature(new ObjectKey(
                                UserInfoUtil.getPref(UserInfoUtil.AVATAR_SIGN, "")
                        ))
                        .into(mTaskAvatar);
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
        }

        @Override
        public void onClick(View v) {
            TaskDTO task = mTasks.get(getAdapterPosition());
            Intent it = new Intent(mContext, TaskDetail.class);

            it.putExtra("TASK_TITLE", task.title);
            // TODO: send more information

            mContext.startActivity(it);
        }
    }
}
