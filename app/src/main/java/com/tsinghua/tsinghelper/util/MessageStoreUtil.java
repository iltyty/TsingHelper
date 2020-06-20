package com.tsinghua.tsinghelper.util;

import com.tsinghua.tsinghelper.dtos.MessageDTO;
import com.tsinghua.tsinghelper.dtos.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageStoreUtil {

    private static MessageStoreUtil instance = new MessageStoreUtil();
    private ArrayList<UserDTO> users = new ArrayList<>();
    private HashMap<String, ArrayList<MessageDTO>> myMsgs = new HashMap<>();

    public static void putNewUser(UserDTO user) {
        getInstance().users.add(user);
        getInstance().myMsgs.put(String.valueOf(user.id), new ArrayList<>());
    }

    public static MessageStoreUtil getInstance() {
        return instance;
    }

    public static HashMap<String, ArrayList<MessageDTO>> getMyMsgs() {
        return getInstance().myMsgs;
    }

    public static void setMyMsgs(HashMap<String, ArrayList<MessageDTO>> msgs) {
        instance.myMsgs = msgs;
    }

    public static ArrayList<MessageDTO> getMsgsWithUser(String userId) {
        return getInstance().myMsgs.get(userId);
    }

    public static boolean hasUser(UserDTO user) {
        return hasUser(user.id);
    }

    public static boolean hasUser(String id) {
        return hasUser(Integer.parseInt(id));
    }

    public static boolean hasUser(int id) {
        MessageStoreUtil instance = getInstance();
        for (UserDTO u : instance.users) {
            if (u.id == id) {
                return true;
            }
        }
        return false;
    }

    public static UserDTO getUserById(String id) {
        return getUserById(Integer.parseInt(id));
    }

    public static UserDTO getUserById(int id) {
        if (!hasUser(id)) {
            return null;
        }
        ArrayList<UserDTO> users = getInstance().users;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id == id) {
                return users.get(i);
            }
        }
        return null;
    }

    public static void addMsg(String otherId, MessageDTO msg) {
        MessageStoreUtil instance = getInstance();
        ArrayList<MessageDTO> myMsgs = instance.myMsgs.get(otherId);
        if (myMsgs == null) {
            instance.myMsgs.put(otherId, new ArrayList<>());
        }
        instance.myMsgs.get(otherId).add(msg);
    }

    public ArrayList<UserDTO> getUsers() {
        return users;
    }

    public static void putMsgs(String otherId, ArrayList<MessageDTO> msgs) {
        MessageStoreUtil instance = getInstance();
        if (!hasUser(otherId)) {
            instance.users.add(new UserDTO(otherId));
        }
        getInstance().myMsgs.put(otherId, msgs);
    }

}
