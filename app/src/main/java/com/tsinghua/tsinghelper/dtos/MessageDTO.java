package com.tsinghua.tsinghelper.dtos;

import androidx.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;
import com.tsinghua.tsinghelper.util.MessageInfoUtil;

import org.json.JSONObject;

import java.util.Date;

public class MessageDTO implements IMessage,
        MessageContentType.Image, MessageContentType {

    private String id;
    private String content;
    private String timestamp;
    private UserDTO sender;
    private UserDTO receiver;

    public MessageDTO(JSONObject message) {
        this.id = message.optString(MessageInfoUtil.ID, "");
        this.content = message.optString(MessageInfoUtil.CONTENT, "");
        this.timestamp = message.optString(MessageInfoUtil.TIME, "");
    }

    public MessageDTO(String id, String content, String timestamp,
                      UserDTO sender, UserDTO receiver) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
    }

    public MessageDTO(JSONObject message, UserDTO sender, UserDTO receiver) {
        this(message);
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public UserDTO getSender() {
        return sender;
    }

    public UserDTO getReceiver() {
        return receiver;
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
