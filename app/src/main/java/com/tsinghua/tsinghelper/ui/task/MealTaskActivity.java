package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;

import com.tsinghua.tsinghelper.R;

public class MealTaskActivity extends BaseTaskActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_meal);

        initWidgets(this);
    }
}
