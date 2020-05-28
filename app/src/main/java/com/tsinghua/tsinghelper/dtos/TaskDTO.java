package com.tsinghua.tsinghelper.dtos;

import android.annotation.SuppressLint;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskDTO {

    // required fields
    public int id;
    public Date startTime;
    public String type;
    public String title;
    public String reward;
    public String reviewTime;
    public String description;
    public String publisherId;
    public boolean isDone;
    public boolean isPaid;
    public boolean isProceeding;

    // optional fields
    public String link;
    public String site;
    public String demands;
    public Date endTime;
    public String subjects;
    public String duration;
    public int foodNum;
    public int timesTotal;
    public int timesPerPerson;

    public TaskDTO(JSONObject task) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月dd日 HH:mm");

        String endTimeStr = optString(task, "end_time");
        String startTimeStr = optString(task, "start_time");
        if (!endTimeStr.isEmpty()) {
            this.endTime = new Date(Long.parseLong(endTimeStr));
        }
        if (!startTimeStr.isEmpty()) {
            this.startTime = new Date(Long.parseLong(startTimeStr));
        }
        this.id = task.optInt("id", 1);
        this.type = task.optString("type", "");
        this.title = task.optString("title", "");
        this.reward = task.optString("reward", "");
        this.reviewTime = task.optString("review_time", "");
        this.description = task.optString("description", "");
        this.publisherId = task.optString("publisherId", "");
        this.isDone = task.optBoolean("is_done", false);
        this.isPaid = task.optBoolean("is_paid", false);
        this.isProceeding = task.optBoolean("is_proceeding", true);

        this.link = optString(task, "link");
        this.site = optString(task, "site");
        this.demands = optString(task, "demands");
        this.subjects = optString(task, "subjects");
        this.duration = optString(task, "duration");
        this.foodNum = task.optInt("food_num", 1);
        this.timesTotal = task.optInt("times_total", 1);
        this.timesPerPerson = task.optInt("times_per_person", 1);
    }

    private String optString(JSONObject json, String key) {
        return json.isNull(key) ? "" : json.optString(key, "");
    }
}
