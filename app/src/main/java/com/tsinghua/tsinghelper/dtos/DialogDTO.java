package com.tsinghua.tsinghelper.dtos;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;
import java.util.List;

public class DialogDTO implements IDialog<MessageDTO> {

    private String id;
    private String name;
    private String avatarAddr;

    private int unreadCount;

    private List<UserDTO> sender = new ArrayList<>();
    private MessageDTO lastMsg;

    public DialogDTO(String id, String name, String avatarAddr, MessageDTO lastMsg) {
        this.id = id;
        this.name = name;
        this.avatarAddr = avatarAddr;
        this.lastMsg = lastMsg;
        this.sender.add(lastMsg.getUser());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return avatarAddr;
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
        return 0;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
