package com.tsinghua.tsinghelper.ui.task;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.components.UserItem;
import com.tsinghua.tsinghelper.dtos.TaskDTO;
import com.tsinghua.tsinghelper.dtos.UserDTO;
import com.tsinghua.tsinghelper.ui.mine.profile.ProfileActivity;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TaskDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_task)
    Toolbar mToolbar;
    @BindView(R.id.task_scrollView)
    ScrollView mScrollView;
    @BindView(R.id.time_check)
    TextView mTimeCheck;
    @BindView(R.id.task_type)
    TextView mTaskType;
    @BindView(R.id.text_task_title)
    TextView mTaskTitle;
    @BindView(R.id.text_task_reward)
    TextView mTaskReward;
    @BindView(R.id.text_task_deadline)
    TextView mTaskDeadline;
    @BindView(R.id.description)
    TextView mTaskDescription;
    @BindView(R.id.number_finished)
    TextView mTimesFinished;
    @BindView(R.id.publisher)
    UserItem mPublisher;
    @BindView(R.id.task_take)
    Button mTaskTake;
    @BindView(R.id.view_demands)
    TextView mViewDemands;
    @BindView(R.id.demands)
    TextView mDemands;
    @BindView(R.id.view_link)
    TextView mViewLink;
    @BindView(R.id.link)
    TextView mLink;
    @BindView(R.id.view_subjects)
    TextView mViewSubjects;
    @BindView(R.id.subjects)
    TextView mSubjects;
    @BindView(R.id.view_site)
    TextView mViewSite;
    @BindView(R.id.site)
    TextView mSite;
    @BindView(R.id.view_food_num)
    TextView mViewFoodNum;
    @BindView(R.id.food_num)
    TextView mFoodNum;

    private int taskId;
    private int publisherId;
    private boolean isDoing;
    private boolean isFailed;
    private boolean isRewarded;
    private boolean isUnderModeration;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        setStatusBarUpperAPI21();
        ButterKnife.bind(this);

        initToolbar();
        initViews();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void initViews() {
        Intent it = getIntent();
        taskId = it.getIntExtra("id", -1);
        publisherId = it.getIntExtra("publisherId", -1);
        if (taskId == -1 || publisherId == -1) {
            return;
        }

        getPublisherInfo();
        getTaskInfo();
    }

    private void getPublisherInfo() {
        mPublisher.setId(publisherId);
        String avatarUrl = HttpUtil.getUserAvatarUrlById(publisherId);
        String profileUrl = HttpUtil.getUserProfileUrlById(publisherId);
        Glide.with(this)
                .load(avatarUrl)
                .error(R.drawable.not_logged_in)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                                                @Nullable Transition<? super Drawable> transition) {
                        TaskDetailActivity.this.runOnUiThread(() -> mPublisher.setAvatar(resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        HttpUtil.get(profileUrl, null, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.logToConsole(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                try {
                    JSONObject resJson = new JSONObject(response.body().string());
                    UserDTO user = new UserDTO(resJson);
                    TaskDetailActivity.this.runOnUiThread(() ->
                            mPublisher.setUsername(user.username));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTaskInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(taskId));
        params.put("browsing", "");

        HttpUtil.get(HttpUtil.TASK_GET, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        TaskDetailActivity.this, "网络错误，请稍后重试", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 200) {
                    ResponseBody resBody = response.body();
                    assert resBody != null;
                    String resStr = resBody.string();
                    TaskDetailActivity.this.runOnUiThread(() -> setTaskInfo(resStr));
                }
            }
        });
    }

    private void setTaskInfo(String resStr) {
        try {
            JSONObject resJson = new JSONObject(resStr);
            JSONObject taskInfo = resJson.getJSONObject("task");
            TaskDTO taskDTO = new TaskDTO(taskInfo);
            String userId = String.valueOf(UserInfoUtil.me.id);

            for (UserDTO user : taskDTO.doingUsers) {
                if (userId.equals(String.valueOf(user.id))) {
                    isDoing = true;
                    break;
                }
            }
            for (UserDTO user : taskDTO.failedUsers) {
                if (userId.equals(String.valueOf(user.id))) {
                    isFailed = true;
                    break;
                }
            }
            for (UserDTO user : taskDTO.rewardedUsers) {
                if (userId.equals(String.valueOf(user.id))) {
                    isRewarded = true;
                    break;
                }
            }
            for (UserDTO user : taskDTO.moderatingUsers) {
                if (userId.equals(String.valueOf(user.id))) {
                    isUnderModeration = true;
                    break;
                }
            }
            if (isDoing) {
                setTakeButtonAsTaken();
            } else if (isFailed || isRewarded) {
                setTakeButtonAsDone();
            } else if (isUnderModeration) {
                setTakeButtonAsUnderModeration();
            }

            mTaskTitle.setText(taskDTO.title);
            mTaskDeadline.setText(taskDTO.deadlineStr);
            mTaskDescription.setText(taskDTO.description);
            mTaskReward.setText(String.valueOf(taskDTO.reward));
            mTimeCheck.setText(String.format(Locale.CHINA, "奖励%d小时内审核", taskDTO.reviewTime));
            mTimesFinished.setText(String.format(Locale.CHINA, "已有%d人完成", taskDTO.timesFinished));
            switch (taskDTO.type) {
                case "community":
                    mTaskType.setText("社区互助");
                    break;
                case "meal":
                    mTaskType.setText("代餐跑腿");
                    mViewSite.setVisibility(View.VISIBLE);
                    mSite.setText(taskDTO.site);
                    mViewFoodNum.setVisibility(View.VISIBLE);
                    mFoodNum.setText(String.valueOf(taskDTO.foodNum));
                    break;
                case "study":
                    mTaskType.setText("学习解惑");
                    mViewSubjects.setVisibility(View.VISIBLE);
                    mSubjects.setText(taskDTO.subjects);
                    break;
                case "questionnaire":
                    mTaskType.setText("个人问卷");
                    mViewDemands.setVisibility(View.VISIBLE);
                    mDemands.setText(taskDTO.demands);
                    mViewLink.setVisibility(View.VISIBLE);
                    mLink.setText(taskDTO.link);
                    break;
            }
        } catch (JSONException e) {
            Log.e("error", e.toString());
            e.printStackTrace();
        }
    }

    private void setStatusBarUpperAPI21(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.task_detail_bg_color));
        ViewGroup mContentView = findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
    }

    public void toProfileActivity(View view) {
        Intent it = new Intent(TaskDetailActivity.this, ProfileActivity.class);
        it.putExtra("userId", String.valueOf(publisherId));
        startActivity(it);
    }

    public void takeTask(View view) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(taskId));
        HttpUtil.post(HttpUtil.TASK_TAKE, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        TaskDetailActivity.this, "网络错误，请稍后重试", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 201) {
                    isDoing = true;
                    isFailed = false;
                    isRewarded = false;
                    isUnderModeration = false;
                    TaskDetailActivity.this.runOnUiThread(() -> {
                        ToastUtil.showToast(TaskDetailActivity.this, "任务接取成功");
                        setTakeButtonAsTaken();
                    });
                }
            }
        });
    }

    public void btnTakeClicked(View view) {
        if (isFailed || isRewarded || isUnderModeration) {
            return;
        }
        if (isDoing) {
            submitTask(view);
            return;
        }
        takeTask(view);
    }

    public void submitTask(View view) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(taskId));
        HttpUtil.post(HttpUtil.TASK_SUBMIT, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        TaskDetailActivity.this, "提交失败，请稍后重试", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 201) {
                    isDoing = false;
                    isFailed = false;
                    isRewarded = false;
                    isUnderModeration = true;
                    TaskDetailActivity.this.runOnUiThread(() -> {
                        ToastUtil.showToast(TaskDetailActivity.this, "提交成功");
                        setTakeButtonAsUnderModeration();
                    });
                }
            }
        });
    }

    private void setTakeButtonAsTaken() {
        mTaskTake.setBackgroundColor(getColor(R.color.green));
        mTaskTake.setText("提交任务");
    }

    private void setTakeButtonAsUnderModeration() {
        mTaskTake.setBackgroundColor(getColor(R.color.light_gray));
        mTaskTake.setText("等待审核");
    }

    private void setTakeButtonAsDone() {
        mTaskTake.setBackgroundColor(getColor(R.color.light_gray));
        mTaskTake.setText("已完成");
    }
}
