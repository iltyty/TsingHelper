package com.tsinghua.tsinghelper.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.components.IconTextItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewTaskTypeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.community)
    IconTextItem mCommunityItem;
    @BindView(R.id.meal)
    IconTextItem mMealItem;
    @BindView(R.id.study)
    IconTextItem mStudyItem;
    @BindView(R.id.questionnaire)
    IconTextItem mQuestItem;

    private static final int TO_COMMUNITY_CODE = 0;
    private static final int TO_MEAL_CODE = 1;
    private static final int TO_QUESTIONNAIRE_CODE = 2;
    private static final int TO_STUDY_CODE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_type);
        ButterKnife.bind(this);

        setClickListeners();

        initToolbar();
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    public void setClickListeners() {
        mCommunityItem.setOnClickListener(this);
        mMealItem.setOnClickListener(this);
        mStudyItem.setOnClickListener(this);
        mQuestItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community:
                Intent toCommunity = new Intent(NewTaskTypeActivity.this, CommunityTaskActivity.class);
                startActivityForResult(toCommunity, TO_COMMUNITY_CODE);
                finish();
                break;
            case R.id.meal:
                Intent toMeal = new Intent(NewTaskTypeActivity.this, MealTaskActivity.class);
                startActivityForResult(toMeal, TO_MEAL_CODE);
                finish();
                break;
            case R.id.study:
                Intent toStudy = new Intent(NewTaskTypeActivity.this, StudyTaskActivity.class);
                startActivityForResult(toStudy, TO_STUDY_CODE);
                finish();
                break;
            case R.id.questionnaire:
                Intent toQuestionnaire = new Intent(NewTaskTypeActivity.this, QuestionnaireTaskActivity.class);
                startActivityForResult(toQuestionnaire, TO_QUESTIONNAIRE_CODE);
                finish();
                break;
        }
    }
}
