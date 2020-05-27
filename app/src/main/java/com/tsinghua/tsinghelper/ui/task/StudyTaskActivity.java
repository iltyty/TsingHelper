package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudyTaskActivity extends BaseTaskActivity {

    @BindView(R.id.duration)
    EditText mDuration;
    @BindView(R.id.subjects)
    EditText mSubjects;
    @BindView(R.id.times_per_person)
    EditText mTimesPerPerson;
    @BindView(R.id.times_total)
    EditText mTimesTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_study);
        ButterKnife.bind(this);

        initWidgets(this);
    }

    private HashMap<String, String> checkFields() {
        HashMap<String, String> params = super.checkFields(this);
        if (params == null) {
            return null;
        }

        String duration = mDuration.getText().toString();
        String subjects = mSubjects.getText().toString();
        String timesTotal = mTimesTotal.getText().toString();
        String timesPerPerson = mTimesPerPerson.getText().toString();

        if (subjects.isEmpty()) {
            ToastUtil.showToast(this, "涉及学科不能为空");
            return null;
        }
        if (timesPerPerson.isEmpty() || Integer.parseInt(timesPerPerson) == 0) {
            ToastUtil.showToast(this, "可完成次数必须为大于零的整数");
            return null;
        }
        if (timesTotal.isEmpty() || Integer.parseInt(timesTotal) == 0) {
            ToastUtil.showToast(this, "预计完成次数必须为大于零的整数");
            return null;
        }
        if (duration.isEmpty() || Double.parseDouble(duration) == 0) {
            ToastUtil.showToast(this, "持续时间必须大于零");
            return null;
        }

        params.put("subjects", subjects);
        params.put("duration", duration);
        params.put("times_total", timesTotal);
        params.put("time_per_person", timesPerPerson);
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
