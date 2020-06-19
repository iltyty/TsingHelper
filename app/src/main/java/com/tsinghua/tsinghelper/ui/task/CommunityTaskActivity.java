package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.TaskDTO;
import com.tsinghua.tsinghelper.util.DateTimeUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommunityTaskActivity extends BaseTaskActivity {

    @BindView(R.id.duration)
    EditText mDuration;
    @BindView(R.id.times_total)
    EditText mTimesTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_community);
        ButterKnife.bind(this);

        initWidgets(this);

        int taskId = getIntent().getIntExtra("taskId", -1);
        if (taskId != -1) {
            isNewTask = false;
            mPageTitle.setText("修改任务-社区互助");
            getTaskInfo(taskId);
        }
    }

    private void getTaskInfo(int taskId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(taskId));
        HttpUtil.get(HttpUtil.TASK_GET, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONObject resJson = new JSONObject(response.body().string());
                        JSONObject taskInfo = resJson.getJSONObject("task");
                        mTask = new TaskDTO(taskInfo);
                        CommunityTaskActivity.this.runOnUiThread(() -> setTaskInfo(mTask));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private HashMap<String, String> checkFields() {
        HashMap<String, String> params = super.checkFields(this);
        if (params == null) {
            return null;
        }

        if (mTimesTotal == null) {
            ToastUtil.showToast(this, "空!");
            return null;
        }
        String duration = mDuration.getText().toString();
        String timesTotal = mTimesTotal.getText().toString();

        if (timesTotal.isEmpty() || Integer.parseInt(timesTotal) < 1) {
            ToastUtil.showToast(this, "预计完成次数必须大于0");
            return null;
        }
        if (duration.isEmpty() || Double.parseDouble(duration) == 0) {
            ToastUtil.showToast(this, "持续时间必须大于0");
            return null;
        }

        int amount = Integer.parseInt(duration);
        long startTimestamp = new Date().getTime();
        long endTimestamp = DateTimeUtil.calculateEndTimestamp(
                startTimestamp, amount, DateTimeUtil.TemporalUnit.DAY);

        params.put("type", "community");
        params.put("times_total", timesTotal);
        params.put("start_time", Long.toString(startTimestamp));
        params.put("end_time", Long.toString(endTimestamp));
        return params;
    }

    public void createTask(View view) {
        HashMap<String, String> params = checkFields();
        if (params == null) {
            return;
        }
        super.createTask(params, this);
    }

    @Override
    protected void setTaskInfo(TaskDTO task) {
        super.setTaskInfo(task);
        mTimesTotal.setText(String.valueOf(task.timesTotal));
        mDuration.setText(String.valueOf(DateTimeUtil.getDuration(task.startTime, task.endTime)));
    }
}
