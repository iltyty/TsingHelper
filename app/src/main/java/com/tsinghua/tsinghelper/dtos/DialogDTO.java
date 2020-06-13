package com.tsinghua.tsinghelper.dtos;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.tsinghua.tsinghelper.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class DialogDTO implements IDialog<MessageDTO> {
    private String id;
    private String name;

    private int unreadCount;

    private List<UserDTO> sender = new ArrayList<>();
    private MessageDTO lastMsg;


    public DialogDTO(String id, String name) {
        this.id = id;
        this.name = name;
        this.sender.add(new UserDTO(id, "用户" + id));
        lastMsg = new MessageDTO(id, String.valueOf(System.currentTimeMillis()), "测试消息内容");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return HttpUtil.getUserAvatarUrlById(1);
    }

    @Override
    public String getDialogName() {
        return name;
    }

    @Override
    public List<UserDTO> getUsers() {
        return sender;
    }

    @Override
    public MessageDTO getLastMessage() {
        return lastMsg;
    }

    @Override
    public void setLastMessage(MessageDTO message) {
        lastMsg = message;
    }

    @Override
    public int getUnreadCount() {
        return 2;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
