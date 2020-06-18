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
import com.tsinghua.tsinghelper.util.HttpUtil;
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
    @BindView(R.id.times_per_person)
    TextView mTimesPerPerson;
    @BindView(R.id.text_task_title)
    TextView mTaskTitle;
    @BindView(R.id.text_task_reward)
    TextView mTaskReward;
    @BindView(R.id.text_task_deadline)
    TextView mTaskDeadline;
    @BindView(R.id.description)
    TextView mTaskDescription;
    @BindView(R.id.requirements)
    TextView mTaskRequirement;
    @BindView(R.id.number_finished)
    TextView mTimesFinished;
    @BindView(R.id.publisher)
    UserItem mPublisher;
    @BindView(R.id.task_take)
    Button mTaskTake;

    private int taskId;
    private int publisherId;
    private boolean isDoing;
    private boolean isFailed;
    private boolean isRewarded;

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
                Log.e("error", e.toString());
                e.printStackTrace();
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

        HttpUtil.get(HttpUtil.TASK_GET, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                ResponseBody resBody = response.body();
                assert resBody != null;
                String resStr = resBody.string();
                TaskDetailActivity.this.runOnUiThread(() -> setTaskInfo(resStr));
            }
        });
    }

    private void setTaskInfo(String resStr) {
        try {
            JSONObject resJson = new JSONObject(resStr);
            JSONObject taskInfo = resJson.getJSONObject("task");
            TaskDTO taskDTO = new TaskDTO(taskInfo);
            String userId = UserInfoUtil.getPref("userId", "-1");

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
            if (isDoing) {
                setTakeButtonAsTaken();
            }

            mTaskTitle.setText(taskDTO.title);
            mTaskDeadline.setText(taskDTO.deadlineStr);
            mTaskDescription.setText(taskDTO.description);
            mTaskReward.setText(String.valueOf(taskDTO.reward));
            mTimeCheck.setText(String.format(Locale.CHINA, "奖励%d小时内审核", taskDTO.reviewTime));
            mTimesPerPerson.setText(String.format(Locale.CHINA, "每人可做%d次", taskDTO.timesPerPerson));
            mTimesFinished.setText(String.format(Locale.CHINA, "已有%d人完成", taskDTO.timesFinished));
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
                Log.e("error", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 201) {
                    TaskDetailActivity.this.runOnUiThread(() -> setTakeButtonAsTaken());
                }
            }
        });
    }

    private void setTakeButtonAsTaken() {
        mTaskTake.setBackgroundColor(getColor(R.color.green));
        mTaskTake.setText("提交任务");
    }

    private void setTakeButtonAsNotTaken() {
        mTaskTake.setBackgroundColor(getColor(R.color.colorPrimary));
        mTaskTake.setText("接受任务");
    }
}
