package com.tsinghua.tsinghelper.dtos;

import com.stfalcon.chatkit.commons.models.IUser;

public class UserDTO implements IUser {

    private String id;
    private String username;
    private String avatar;

    UserDTO(String id, String username) {
        this.id = id;
        this.username = username;
        this.avatar = "http://i.imgur.com/pv1tBmT.png";
    }

    @Override
    public String getId() {
        return id;
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
