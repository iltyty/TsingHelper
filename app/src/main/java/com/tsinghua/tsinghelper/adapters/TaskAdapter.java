package com.tsinghua.tsinghelper.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.TaskDTO;
import com.tsinghua.tsinghelper.ui.task.TaskDetailActivity;
import com.tsinghua.tsinghelper.ui.task.TaskReviewActivity;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
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

public class TaskAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<TaskDTO> mTasks;
    private static final int EMPTY_VIEW_TYPE = -1;

    public TaskAdapter(Context cxt) {
        mContext = cxt;
        mTasks = new ArrayList<>();
    }

    public void setTasks(ArrayList<TaskDTO> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int pos) {
        if (mTasks == null || mTasks.size() <= 0) {
            return EMPTY_VIEW_TYPE;
        }
        return super.getItemViewType(pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == EMPTY_VIEW_TYPE) {
            view = inflater.inflate(R.layout.component_empty_task_list, parent, false);
            return new EmptyViewHolder(view);
        }
        view = inflater.inflate(R.layout.component_task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mTasks.size() > 0 ? mTasks.size() : 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).setTaskData(mTasks.get(position));
        }
    }

    public void setTasks(JSONObject resJson) throws JSONException {
        JSONArray tasks = resJson.getJSONArray("tasks");
        mTasks.clear();
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = (JSONObject) tasks.get(i);
            mTasks.add(new TaskDTO(task));
        }
        ((Activity) mContext).runOnUiThread(this::notifyDataSetChanged);
    }

    public void getTasks(HashMap<String, String> params, String url) {
        HttpUtil.get(url, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        (Activity) mContext,
                        "获取任务列表失败，请稍后重试", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 200) {
                    String resStr = response.body().string();
                    try {
                        setTasks(new JSONObject(resStr));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(@NotNull View view) {
            super(view);
        }
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
            mTaskAvatar.setImageResource(R.drawable.not_logged_in);

            String url = HttpUtil.getUserAvatarUrlById(task.publisherId);
            // do not cache
            Glide.with(mContext)
                    .load(url)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.not_logged_in)
                    .into(mTaskAvatar);
        }

        @Override
        public void onClick(View v) {
            TaskDTO task = mTasks.get(getAdapterPosition());
            String id = String.valueOf(UserInfoUtil.me.id);
            if (id.equals(String.valueOf(task.publisherId))) {
                Intent it = new Intent(mContext, TaskReviewActivity.class);
                it.putExtra("id", task.id);
                mContext.startActivity(it);
            } else {
                Intent it = new Intent(mContext, TaskDetailActivity.class);
                it.putExtra("id", task.id);
                it.putExtra("publisherId", task.publisherId);
                mContext.startActivity(it);
            }
        }
    }
}
