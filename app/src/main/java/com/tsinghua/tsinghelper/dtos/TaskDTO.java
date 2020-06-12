package com.tsinghua.tsinghelper.dtos;

import com.tsinghua.tsinghelper.util.DateTimeUtil;
import com.tsinghua.tsinghelper.util.TaskInfoUtil;

import org.json.JSONObject;

import java.util.Locale;

public class TaskDTO {

    // required fields
    public int id;
    public int reviewTime;
    public int timesTotal;
    public int timesFinished;
    public double reward;
    public String type;
    public String title;
    public String startTime;
    public String deadlineStr;
    public String description;
    public String publisherId;
    public boolean isDone;
    public boolean isPaid;
    public boolean isProceeding;

    // optional fields
    public String link;
    public String site;
    public String demands;
    public String endTime;
    public String subjects;
    public int foodNum;
    public int timesPerPerson;

    public TaskDTO(JSONObject task) {
        this.id = task.optInt(TaskInfoUtil.ID, 1);
        this.reviewTime = task.optInt(TaskInfoUtil.REVIEW_TIME, 24);
        this.timesTotal = task.optInt(TaskInfoUtil.TIMES_TOTAL, 1);
        this.timesFinished = task.optInt(TaskInfoUtil.TIMES_FINISHED, 0);
        this.type = task.optString(TaskInfoUtil.TYPE, "");
        this.title = task.optString(TaskInfoUtil.TITLE, "");
        this.reward = task.optDouble(TaskInfoUtil.REWARD, 0);
        this.startTime = task.optString(TaskInfoUtil.START_TIME, "");
        this.description = task.optString(TaskInfoUtil.DESC, "");
        this.publisherId = task.optString(TaskInfoUtil.PUBLISHER_ID, "");
        this.isDone = task.optBoolean(TaskInfoUtil.IS_DONE, false);
        this.isPaid = task.optBoolean(TaskInfoUtil.IS_PAID, false);
        this.isProceeding = task.optBoolean(TaskInfoUtil.IS_PROCEEDING, true);

        this.link = optString(task, TaskInfoUtil.LINK);
        this.site = optString(task, TaskInfoUtil.SITE);
        this.demands = optString(task, TaskInfoUtil.DEMANDS);
        this.endTime = optString(task, TaskInfoUtil.END_TIME);
        this.subjects = optString(task, TaskInfoUtil.SUBJECTS);
        this.foodNum = task.optInt(TaskInfoUtil.FOOD_NUM, 1);
        this.timesPerPerson = task.optInt(TaskInfoUtil.TIMES_PER_PERSON, 1);

        int timesLeft = Math.max(timesTotal - timesFinished, 0);
        String timeLeft = DateTimeUtil.getDeadlineStr(Long.parseLong(endTime));
        this.deadlineStr = String.format(Locale.CHINA, "%d人后截止 · %s", timesLeft, timeLeft);
    }

    private String optString(JSONObject json, String key) {
        return json.isNull(key) ? "" : json.optString(key, "");
    }
}
