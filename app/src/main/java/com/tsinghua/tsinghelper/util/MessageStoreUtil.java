package com.tsinghua.tsinghelper.util;

import com.tsinghua.tsinghelper.dtos.MessageDTO;
import com.tsinghua.tsinghelper.dtos.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageStoreUtil {

    private static MessageStoreUtil instance = new MessageStoreUtil();
    private ArrayList<UserDTO> users = new ArrayList<>();
    private HashMap<String, ArrayList<MessageDTO>> sentMsgs = new HashMap<>();
    private HashMap<String, ArrayList<MessageDTO>> receivedMsgs = new HashMap<>();

    public static MessageStoreUtil getInstance() {
        return instance;
    }

    public static void putNewUser(UserDTO user) {
        getInstance().users.add(user);
        getInstance().sentMsgs.put(String.valueOf(user.id), new ArrayList<>());
        getInstance().receivedMsgs.put(String.valueOf(user.id), new ArrayList<>());
    }

    public static HashMap<String, ArrayList<MessageDTO>> getMySentMsgs() {
        return getInstance().sentMsgs;
    }

    public static HashMap<String, ArrayList<MessageDTO>> getMyReceivedMsgs() {
        return getInstance().receivedMsgs;
    }

    public static ArrayList<MessageDTO> getSentMsgsFromUser(String userId) {
        return getInstance().sentMsgs.get(userId);
    }

    public static ArrayList<MessageDTO> getReceivedMsgsFromUser(String userId) {
        return getInstance().receivedMsgs.get(userId);
    }

    public static void putNewUserIfNotExist(UserDTO user) {
        MessageStoreUtil instance = getInstance();
        if (!instance.users.contains(user)) {
            instance.users.add(user);
            instance.sentMsgs.put(String.valueOf(user.id), new ArrayList<>());
            instance.receivedMsgs.put(String.valueOf(user.id), new ArrayList<>());
        }
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

    public static void addSentMsg(String receiverId, MessageDTO msg) {
        MessageStoreUtil instance = getInstance();
        ArrayList<MessageDTO> sentMsgs = instance.sentMsgs.get(receiverId);
        if (sentMsgs == null) {
            instance.sentMsgs.put(receiverId, new ArrayList<>());
        }
        instance.sentMsgs.get(receiverId).add(msg);
    }

    public static void addReceivedMsg(String senderId, MessageDTO msg) {
        ArrayList<MessageDTO> msgs = getInstance().receivedMsgs.get(senderId);
        if (msgs != null) {
            msgs.add(msg);
        }
    }

    public static void putSentMsgs(String receiverId, ArrayList<MessageDTO> msgs) {
        getInstance().sentMsgs.put(receiverId, msgs);
    }

    public ArrayList<UserDTO> getUsers() {
        return users;
    }

    public static void putReceivedMsgs(String senderId, ArrayList<MessageDTO> msgs) {
        getInstance().receivedMsgs.put(senderId, msgs);
    }

}
