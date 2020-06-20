package com.tsinghua.tsinghelper.util;

import com.tsinghua.tsinghelper.dtos.MessageDTO;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageStoreUtil {

    private static MessageStoreUtil instance = new MessageStoreUtil();
    private HashMap<String, ArrayList<MessageDTO>> sentMsgs = new HashMap<>();
    private HashMap<String, ArrayList<MessageDTO>> receivedMsgs = new HashMap<>();

    public static MessageStoreUtil getInstance() {
        return instance;
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

    public static void putSentMsgs(String receiverId, ArrayList<MessageDTO> msgs) {
        getInstance().sentMsgs.put(receiverId, msgs);
    }

    public static void putReceivedMsgs(String senderId, ArrayList<MessageDTO> msgs) {
        getInstance().receivedMsgs.put(senderId, msgs);
    }

}
