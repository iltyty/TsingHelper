package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.TaskDTO;
import com.tsinghua.tsinghelper.util.DateTimeUtil;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
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

public class StudyTaskActivity extends BaseTaskActivity {

    @BindView(R.id.duration)
    EditText mDuration;
    @BindView(R.id.subjects)
    EditText mSubjects;
    @BindView(R.id.times_total)
    EditText mTimesTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_study);
        ButterKnife.bind(this);

        initWidgets(this);

        int taskId = getIntent().getIntExtra("taskId", -1);
        if (taskId != -1) {
            isNewTask = false;
            mPageTitle.setText("修改任务-学习解惑");
            getTaskInfo(taskId);
        }
    }

    private void getTaskInfo(int taskId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(taskId));
        HttpUtil.get(HttpUtil.TASK_GET, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.logToConsole(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONObject resJson = new JSONObject(response.body().string());
                        JSONObject taskInfo = resJson.getJSONObject("task");
                        mTask = new TaskDTO(taskInfo);
                        StudyTaskActivity.this.runOnUiThread(() -> setTaskInfo(mTask));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void setTaskInfo(TaskDTO task) {
        super.setTaskInfo(task);
        mSubjects.setText(task.subjects);
        mTimesTotal.setText(String.valueOf(task.timesTotal));
        mDuration.setText(String.valueOf(DateTimeUtil.getDuration(task.startTime, task.endTime)));
    }

    private HashMap<String, String> checkFields() {
        HashMap<String, String> params = super.checkFields(this);
        if (params == null) {
            return null;
        }

        String duration = mDuration.getText().toString();
        String subjects = mSubjects.getText().toString();
        String timesTotal = mTimesTotal.getText().toString();

        if (subjects.isEmpty()) {
            ToastUtil.showToast(this, "涉及学科不能为空");
            return null;
        }
        if (timesTotal.isEmpty() || Integer.parseInt(timesTotal) == 0) {
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

        params.put("type", "study");
        params.put("subjects", subjects);
        params.put("times_total", timesTotal);
        params.put("end_time", Long.toString(endTimestamp));
        params.put("start_time", Long.toString(startTimestamp));
        return params;
    }

    public void createTask(View view) {
        HashMap<String, String> params = checkFields();
        if (params == null) {
            return;
        }
        super.createTask(params, this);
    }
}
