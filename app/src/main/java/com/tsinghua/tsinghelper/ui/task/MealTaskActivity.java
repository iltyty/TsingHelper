package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.ToastUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import butterknife.BindView;

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

        initWidgets(this);
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
        params.put("foodNum", foodNum);
        params.put("endTime", String.valueOf(cal.getTimeInMillis()));

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
