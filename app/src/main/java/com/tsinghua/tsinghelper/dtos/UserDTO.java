package com.tsinghua.tsinghelper.dtos;

import android.util.Log;

import com.stfalcon.chatkit.commons.models.IUser;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDTO implements IUser {

    public int id;
    public String email;
    public String grade;
    public String phone;
    public String state;
    public String avatar;
    public String wechat;
    public String realname;
    public String username;
    public String dormitory;
    public String signature;
    public String department;

    public ArrayList<UserDTO> followers = new ArrayList<>();
    public ArrayList<UserDTO> followings = new ArrayList<>();
    public ArrayList<TaskDTO> doingTasks = new ArrayList<>();
    public ArrayList<TaskDTO> failedTasks = new ArrayList<>();
    public ArrayList<TaskDTO> rewardedTasks = new ArrayList<>();
    public ArrayList<TaskDTO> moderatingTasks = new ArrayList<>();

    public UserDTO() {
    }

    public UserDTO(String id) {
        this.id = Integer.parseInt(id);
        this.avatar = HttpUtil.getUserAvatarUrlById(id);
    }

    public UserDTO(String id, String username) {
        this.username = username;
        this.id = Integer.parseInt(id);
        this.avatar = HttpUtil.getUserAvatarUrlById(id);
    }

    public UserDTO(JSONObject user) {
        // required fields
        this.id = user.optInt(UserInfoUtil.ID);
        this.phone = user.optString(UserInfoUtil.PHONE);
        this.avatar = HttpUtil.getUserAvatarUrlById(id);
        this.username = user.optString(UserInfoUtil.USERNAME);

        // optional fields
        this.email = optString(user, UserInfoUtil.EMAIL);
        this.grade = optString(user, UserInfoUtil.GRADE);
        this.state = optString(user, UserInfoUtil.STATE);
        this.wechat = optString(user, UserInfoUtil.WECHAT);
        this.realname = optString(user, UserInfoUtil.REALNAME);
        this.signature = optString(user, UserInfoUtil.SIGNATURE);
        this.dormitory = optString(user, UserInfoUtil.DORMITORY);
        this.department = optString(user, UserInfoUtil.DEPARTMENT);

        JSONArray doing = user.optJSONArray(UserInfoUtil.DOING_TASKS);
        JSONArray failed = user.optJSONArray(UserInfoUtil.FAILED_TASKS);
        JSONArray rewarded = user.optJSONArray(UserInfoUtil.REWARDED_TASKS);
        JSONArray moderating = user.optJSONArray(UserInfoUtil.MODERATING_TASKS);
        JSONArray followersUsers = user.optJSONArray(UserInfoUtil.FOLLOWERS);
        JSONArray followingsUsers = user.optJSONArray(UserInfoUtil.FOLLOWINGS);

        if (doing != null) {
            int length = doing.length();
            try {
                for (int i = 0; i < length; i++) {
                    doingTasks.add(new TaskDTO(doing.getJSONObject(i)));
                }
            } catch (JSONException e) {
                Log.e("error", e.toString());
                e.printStackTrace();
            }
        }
        if (failed != null) {
            int length = failed.length();
            try {
                for (int i = 0; i < length; i++) {
                    failedTasks.add(new TaskDTO(failed.getJSONObject(i)));
                }
            } catch (JSONException e) {
                Log.e("error", e.toString());
                e.printStackTrace();
            }
        }
        if (rewarded != null) {
            int length = rewarded.length();
            try {
                for (int i = 0; i < length; i++) {
                    rewardedTasks.add(new TaskDTO(rewarded.getJSONObject(i)));
                }
            } catch (JSONException e) {
                Log.e("error", e.toString());
                e.printStackTrace();
            }
        }
        if (moderating != null) {
            int length = moderating.length();
            try {
                for (int i = 0; i < length; i++) {
                    moderatingTasks.add(new TaskDTO(moderating.getJSONObject(i)));
                }
            } catch (JSONException e) {
                Log.e("error", e.toString());
                e.printStackTrace();
            }
        }
        if (followersUsers != null) {
            int length = followersUsers.length();
            try {
                for (int i = 0; i < length; i++) {
                    followers.add(new UserDTO(followersUsers.getJSONObject(i)));
                }
            } catch (JSONException e) {
                Log.e("error", e.toString());
                e.printStackTrace();
            }
        }
        if (followingsUsers != null) {
            int length = followingsUsers.length();
            try {
                for (int i = 0; i < length; i++) {
                    followings.add(new UserDTO(followingsUsers.getJSONObject(i)));
                }
            } catch (JSONException e) {
                Log.e("error", e.toString());
                e.printStackTrace();
            }
        }
    }

    private String optString(JSONObject json, String key) {
        return json.isNull(key) ? "" : json.optString(key, "");
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}
