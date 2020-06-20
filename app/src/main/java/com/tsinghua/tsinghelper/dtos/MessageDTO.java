package com.tsinghua.tsinghelper.dtos;

import androidx.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;
import com.tsinghua.tsinghelper.util.MessageInfoUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.json.JSONObject;

import java.util.Date;

public class MessageDTO implements IMessage,
        MessageContentType.Image, MessageContentType {

    private static int count = 0;

    private String id;
    private String content;
    private String timestamp;
    private UserDTO sender;

    public MessageDTO(JSONObject message) {
        this.id = message.optString(MessageInfoUtil.ID, "");
        this.content = message.optString(MessageInfoUtil.CONTENT, "");
        this.timestamp = message.optString(MessageInfoUtil.TIME, "");
    }

    public MessageDTO(String content) {
        this.id = UserInfoUtil.getPref("userId", "1");
        this.content = content;
        this.timestamp = String.valueOf(System.currentTimeMillis());
        this.sender = new UserDTO(id, "用户" + id);
    }

    public MessageDTO(JSONObject message, UserDTO user) {
        this(message);
        this.sender = user;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return content;
    }

    @Override
    public UserDTO getUser() {
        return sender;
    }

    @Override
    public Date getCreatedAt() {
        return new Date(Long.parseLong(timestamp));
    }

    @Nullable
    @Override
    public String getImageUrl() {
        return null;
    }
}
