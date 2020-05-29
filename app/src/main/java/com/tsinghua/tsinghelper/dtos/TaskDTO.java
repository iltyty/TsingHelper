package com.tsinghua.tsinghelper.dtos;

import com.tsinghua.tsinghelper.util.DateTimeUtil;

import org.json.JSONObject;

public class TaskDTO {

    // required fields
    public int id;
    public double reward;
    public String type;
    public String title;
    public String startTime;
    public String reviewTime;
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
    public String duration;
    public int foodNum;
    public int timesTotal;
    public int timesPerPerson;

    public TaskDTO(JSONObject task) {
        this.id = task.optInt("id", 1);
        this.type = task.optString("type", "");
        this.title = task.optString("title", "");
        this.reward = task.optDouble("reward", 0);
        this.startTime = task.optString("start_time", "");
        this.reviewTime = task.optString("review_time", "");
        this.description = task.optString("description", "");
        this.publisherId = task.optString("publisherId", "");
        this.isDone = task.optBoolean("is_done", false);
        this.isPaid = task.optBoolean("is_paid", false);
        this.isProceeding = task.optBoolean("is_proceeding", true);

        this.link = optString(task, "link");
        this.site = optString(task, "site");
        this.demands = optString(task, "demands");
        this.endTime = optString(task, "end_time");
        this.subjects = optString(task, "subjects");
        this.duration = optString(task, "duration");
        this.foodNum = task.optInt("food_num", 1);
        this.timesTotal = task.optInt("times_total", 1);
        this.timesPerPerson = task.optInt("times_per_person", 1);

        this.deadlineStr = DateTimeUtil.getDeadlineStr(Long.parseLong(endTime));
    }

    private String optString(JSONObject json, String key) {
        return json.isNull(key) ? "" : json.optString(key, "");
    }
}
