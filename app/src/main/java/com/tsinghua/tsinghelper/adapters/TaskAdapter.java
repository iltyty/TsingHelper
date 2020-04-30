package com.tsinghua.tsinghelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.TaskDTO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<TaskDTO> mTasks;

    public TaskAdapter(ArrayList<TaskDTO> tasks) { this.mTasks = tasks; }

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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.task_item_avatar)
        ImageView mTaskAvatar;
        @BindView(R.id.task_item_title)
        TextView mTaskTitle;
        @BindView(R.id.task_item_reward)
        TextView mTaskReward;
        @BindView(R.id.task_item_deadline)
        TextView mTaskDeadline;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setTaskData(TaskDTO task) {
            mTaskTitle.setText(task.getTitle());
            mTaskReward.setText(task.getReward());
            mTaskDeadline.setText(task.getDeadline());

            // TODO: set task publisher's avatar
            mTaskAvatar.setImageResource(R.drawable.ic_community_item_32dp);
        }
    }
}
