package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;

public class QuestionnaireTaskActivity extends BaseTaskActivity {

    @BindView(R.id.duration)
    EditText mDuration;
    @BindView(R.id.times_total)
    EditText mTimesTotal;
    @BindView(R.id.requirements)
    EditText mRequirements;
    @BindView(R.id.link)
    EditText mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_questionnaire);

        initWidgets(this);
    }

    private HashMap<String, String> checkFields() {
        HashMap<String, String> params = super.checkFields(this);
        if (params == null) {
            return null;
        }

        String link = mLink.getText().toString();
        String duration = mDuration.getText().toString();
        String timesTotal = mTimesTotal.getText().toString();

        if (timesTotal.isEmpty() || Integer.parseInt(timesTotal) == 0) {
            ToastUtil.showToast(this, "预计完成次数必须为大于零的整数");
            return null;
        }
        if (duration.isEmpty() || Double.parseDouble(duration) == 0) {
            ToastUtil.showToast(this, "持续时间必须大于零");
            return null;
        }
        if (link.isEmpty()) {
            ToastUtil.showToast(this, "问卷链接不能为空");
            return null;
        }

        params.put("link", link);
        params.put("duration", duration);
        params.put("times_total", timesTotal);
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
