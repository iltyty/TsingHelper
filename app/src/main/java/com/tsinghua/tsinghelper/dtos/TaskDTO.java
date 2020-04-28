package com.tsinghua.tsinghelper.dtos;

public class TaskDTO {

    private String mTitle;
    private String mReward;
    private String mDeadline;

    public TaskDTO(String title, String reward, String deadline) {
        mTitle = title;
        mReward = reward;
        mDeadline = deadline;
    }

    public String getTitle() { return mTitle; }

    public String getReward() { return mReward; }

    public String getDeadline() { return mDeadline; }
}
