package com.tsinghua.tsinghelper.dtos;

import androidx.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import java.util.Date;

public class MessageDTO implements IMessage,
        MessageContentType.Image, MessageContentType {

    private static int count = 0;

    private String id;
    private String content;
    private String timestamp;
    private UserDTO sender;

    public MessageDTO(String content) {
        this.id = UserInfoUtil.getPref("userId", "1");
        this.content = content;
        this.timestamp = String.valueOf(System.currentTimeMillis());
        this.sender = new UserDTO(id, "用户" + id);
    }

    public MessageDTO(String id, String timestamp, String content) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;

        id = String.valueOf(Integer.parseInt(id) % 3 + 1);
        this.sender = new UserDTO(id, "用户" + id);
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
