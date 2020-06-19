package com.tsinghua.tsinghelper.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.UserItemAdapter;
import com.tsinghua.tsinghelper.components.DividerItemDecrator;
import com.tsinghua.tsinghelper.dtos.TaskDTO;
import com.tsinghua.tsinghelper.dtos.UserDTO;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.TaskInfoUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TaskReviewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_task)
    Toolbar mToolbar;
    @BindView(R.id.task_title)
    TextView mTitle;
    @BindView(R.id.doing_list)
    RecyclerView mDoingList;
    @BindView(R.id.failed_list)
    RecyclerView mFailedList;
    @BindView(R.id.rewarded_list)
    RecyclerView mRewardedList;
    @BindView(R.id.moderating_list)
    RecyclerView mModeratingList;
    @BindView(R.id.task_view_count)
    TextView mViewCount;

    private TaskDTO mTask;

    private UserItemAdapter mDoingAdapter;
    private UserItemAdapter mFailedAdapter;
    private UserItemAdapter mRewardedAdapter;
    private UserItemAdapter mModeratingAdapter;

    private ArrayList<UserDTO> doingUsers = new ArrayList<>();
    private ArrayList<UserDTO> failedUsers = new ArrayList<>();
    private ArrayList<UserDTO> rewardedUsers = new ArrayList<>();
    private ArrayList<UserDTO> moderatingUsers = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_review);
        setStatusBarUpperAPI21();

        ButterKnife.bind(this);
        initToolbar();
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void initRecyclerViews() {
        RecyclerView.LayoutManager lmDoing = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        RecyclerView.LayoutManager lmFailed = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        RecyclerView.LayoutManager lmRewarded = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        RecyclerView.LayoutManager lmModerating = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        DividerItemDecrator divider = new DividerItemDecrator(
                getDrawable(R.drawable.shape_list_divider));

        mDoingAdapter = new UserItemAdapter(this, doingUsers, false);
        mFailedAdapter = new UserItemAdapter(this, failedUsers, false);
        mRewardedAdapter = new UserItemAdapter(this, rewardedUsers, false);
        mModeratingAdapter = new UserItemAdapter(this, moderatingUsers, true);

        mDoingList.setLayoutManager(lmDoing);
        mFailedList.setLayoutManager(lmFailed);
        mRewardedList.setLayoutManager(lmRewarded);
        mModeratingList.setLayoutManager(lmModerating);
        mDoingList.setAdapter(mDoingAdapter);
        mFailedList.setAdapter(mFailedAdapter);
        mRewardedList.setAdapter(mRewardedAdapter);
        mModeratingList.setAdapter(mModeratingAdapter);
        mDoingList.addItemDecoration(divider);
        mFailedList.addItemDecoration(divider);
        mRewardedList.addItemDecoration(divider);
        mModeratingList.addItemDecoration(divider);
        mDoingList.setNestedScrollingEnabled(false);
        mFailedList.setNestedScrollingEnabled(false);
        mRewardedList.setNestedScrollingEnabled(false);
        mModeratingList.setNestedScrollingEnabled(false);
    }

    private void initViews() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(getIntent().getIntExtra("id", -1)));
        HttpUtil.get(HttpUtil.TASK_GET, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONObject resJson = new JSONObject(response.body().string());
                        JSONObject taskInfo = resJson.getJSONObject("task");
                        mTask = new TaskDTO(taskInfo);
                        doingUsers = mTask.doingUsers;
                        failedUsers = mTask.failedUsers;
                        rewardedUsers = mTask.rewardedUsers;
                        moderatingUsers = mTask.moderatingUsers;

                        TaskReviewActivity.this.runOnUiThread(() -> {
                            mTitle.setText(mTask.title);
                            mViewCount.setText(String.valueOf(mTask.viewCount));
                            initRecyclerViews();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setStatusBarUpperAPI21() {
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

    public void moderateTask(UserDTO user, boolean passed) {
        HashMap<String, String> params = new HashMap<>();
        params.put("passed", String.valueOf(passed));
        params.put("takerId", String.valueOf(user.id));
        params.put("taskId", String.valueOf(getIntent().getIntExtra("id", -1)));
        HttpUtil.post(HttpUtil.TASK_MODERATE, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 201) {
                    ToastUtil.showToastOnUIThread(TaskReviewActivity.this, "审核成功");
                    doingUsers.remove(user);
                    if (passed) {
                        rewardedUsers.add(user);
                        TaskReviewActivity.this.runOnUiThread(() -> {
                            mDoingAdapter.notifyDataSetChanged();
                            mRewardedAdapter.notifyDataSetChanged();
                        });
                    } else {
                        failedUsers.add(user);
                        TaskReviewActivity.this.runOnUiThread(() -> {
                            mDoingAdapter.notifyDataSetChanged();
                            mModeratingAdapter.notifyDataSetChanged();
                        });
                    }
                }
            }
        });
    }

    public void toTaskInfoPage(View view) {
        String type = mTask.type;
        Intent it;
        switch (type) {
            case TaskInfoUtil.TYPE_COMMUNITY:
                it = new Intent(TaskReviewActivity.this, CommunityTaskActivity.class);
                break;
            case TaskInfoUtil.TYPE_MEAL:
                it = new Intent(TaskReviewActivity.this, MealTaskActivity.class);
                break;
            case TaskInfoUtil.TYPE_STUDY:
                it = new Intent(TaskReviewActivity.this, StudyTaskActivity.class);
                break;
            case TaskInfoUtil.TYPE_QUESTIONNAIRE:
                it = new Intent(TaskReviewActivity.this, QuestionnaireTaskActivity.class);
                break;
            default:
                return;
        }
        it.putExtra("taskId", mTask.id);
        startActivity(it);
    }

}
