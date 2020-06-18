package com.tsinghua.tsinghelper.dtos;

import com.tsinghua.tsinghelper.util.DateTimeUtil;
import com.tsinghua.tsinghelper.util.TaskInfoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class TaskDTO {

    // required fields
    public int id;
    public int viewCount;
    public int reviewTime;
    public int timesTotal;
    public int publisherId;
    public int timesFinished;
    public double reward;
    public String type;
    public String title;
    public String startTime;
    public String deadlineStr;
    public String description;
    public ArrayList<UserDTO> doingUsers = new ArrayList<>();
    public ArrayList<UserDTO> failedUsers = new ArrayList<>();
    public ArrayList<UserDTO> rewardedUsers = new ArrayList<>();
    public ArrayList<UserDTO> moderatingUsers = new ArrayList<>();

    // optional fields
    public String link;
    public String site;
    public String demands;
    public String endTime;
    public String subjects;
    public int foodNum;
    public int timesPerPerson;

    public TaskDTO(JSONObject task) {
        this.id = task.optInt(TaskInfoUtil.ID, 0);
        this.viewCount = task.optInt(TaskInfoUtil.VIEW_COUNT, 0);
        this.publisherId = task.optInt(TaskInfoUtil.PUBLISHER_ID, 0);
        this.reviewTime = task.optInt(TaskInfoUtil.REVIEW_TIME, 24);
        this.timesTotal = task.optInt(TaskInfoUtil.TIMES_TOTAL, 1);
        this.timesFinished = task.optInt(TaskInfoUtil.TIMES_FINISHED, 0);
        this.type = task.optString(TaskInfoUtil.TYPE, "");
        this.title = task.optString(TaskInfoUtil.TITLE, "");
        this.reward = task.optDouble(TaskInfoUtil.REWARD, 0);
        this.startTime = task.optString(TaskInfoUtil.START_TIME, "");
        this.description = task.optString(TaskInfoUtil.DESC, "");

        JSONArray doing = task.optJSONArray(TaskInfoUtil.DOING_USERS);
        JSONArray failed = task.optJSONArray(TaskInfoUtil.FAILED_USERS);
        JSONArray rewarded = task.optJSONArray(TaskInfoUtil.REWARDED_USERS);
        JSONArray moderating = task.optJSONArray(TaskInfoUtil.MODERATING_USERS);
        if (doing != null) {
            int length = doing.length();
            try {
                for (int i = 0; i < length; i++) {
                    doingUsers.add(new UserDTO(doing.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (failed != null) {
            int length = failed.length();
            try {
                for (int i = 0; i < length; i++) {
                    failedUsers.add(new UserDTO(failed.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (rewarded != null) {
            int length = rewarded.length();
            try {
                for (int i = 0; i < length; i++) {
                    rewardedUsers.add(new UserDTO(rewarded.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (moderating != null) {
            int length = moderating.length();
            try {
                for (int i = 0; i < length; i++) {
                    moderatingUsers.add(new UserDTO(moderating.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        this.link = optString(task, TaskInfoUtil.LINK);
        this.site = optString(task, TaskInfoUtil.SITE);
        this.demands = optString(task, TaskInfoUtil.DEMANDS);
        this.endTime = optString(task, TaskInfoUtil.END_TIME);
        this.subjects = optString(task, TaskInfoUtil.SUBJECTS);
        this.foodNum = task.optInt(TaskInfoUtil.FOOD_NUM, 1);
        this.timesPerPerson = task.optInt(TaskInfoUtil.TIMES_PER_PERSON, 1);

        int timesLeft = Math.max(timesTotal - timesFinished, 0);
        String timeLeft = DateTimeUtil.getDeadlineStr(Long.parseLong(endTime));
        if (timesLeft == 0 || timeLeft.equals("已截止")) {
            this.deadlineStr = "已截止";
        } else {
            this.deadlineStr = String.format(Locale.CHINA, "%d人后截止 · %s", timesLeft, timeLeft);
        }
    }

    private String optString(JSONObject json, String key) {
        return json.isNull(key) ? "" : json.optString(key, "");
    }
}
