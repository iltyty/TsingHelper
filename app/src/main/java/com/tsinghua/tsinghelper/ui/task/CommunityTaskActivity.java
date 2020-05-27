package com.tsinghua.tsinghelper.ui.task;

import android.os.Bundle;
import android.view.View;

import com.tsinghua.tsinghelper.R;

import java.util.HashMap;

public class CommunityTaskActivity extends BaseTaskActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_community);

        initWidgets(this);
    }

    private HashMap<String, String> checkFields() {
        HashMap<String, String> params = super.checkFields(this);

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
