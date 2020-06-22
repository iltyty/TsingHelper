package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.TaskDTO;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MealTaskActivity extends BaseTaskActivity {

    @BindView(R.id.site)
    EditText mSite;
    @BindView(R.id.food_num)
    EditText mFoodNum;
    @BindView(R.id.end_time)
    TimePicker mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_meal);

        initWidgets();

        int taskId = getIntent().getIntExtra("taskId", -1);
        if (taskId != -1) {
            isNewTask = false;
            mPageTitle.setText("修改任务-代餐跑腿");
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
                        MealTaskActivity.this.runOnUiThread(() -> setTaskInfo(mTask));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initWidgets() {
        super.initWidgets(this);
        mEndTime.setIs24HourView(true);
    }

    private HashMap<String, String> checkFields() {
        HashMap<String, String> params = super.checkFields(this);
        if (params == null) {
            return null;
        }

        String site = mSite.getText().toString();
        String foodNum = mFoodNum.getText().toString();
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"));
        cal.set(Calendar.HOUR_OF_DAY, mEndTime.getCurrentHour());
        cal.set(Calendar.MINUTE, mEndTime.getCurrentMinute());

        if (site.isEmpty()) {
            ToastUtil.showToast(this, "代餐地点不能为空");
            return null;
        }
        if (foodNum.isEmpty() || Integer.parseInt(foodNum) == 0) {
            ToastUtil.showToast(this, "代餐份数必须大于0");
            return null;
        }

        params.put("site", site);
        params.put("type", "meal");
        params.put("food_num", foodNum);
        params.put("times_total", "1");
        params.put("end_time", Long.toString(cal.getTimeInMillis()));
        params.put("start_time", Long.toString(new Date().getTime()));
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
        mSite.setText(task.site);
        mFoodNum.setText(String.valueOf(task.foodNum));
    }
}
