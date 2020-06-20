package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.util.Log;

import com.tsinghua.tsinghelper.dtos.MessageDTO;
import com.tsinghua.tsinghelper.dtos.UserDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/* Cache chat history with specified user id
 * Cache format: {userId}.json
 * Argument:
 *   userId: whom am I sending message to and receiving from
 */
public class ChatHistoryCacheUtil {

    private static final String LOG_TAG = "ChatCache";
    private static ChatHistoryCacheUtil instance = new ChatHistoryCacheUtil();
    private Context mContext;

    private ChatHistoryCacheUtil() {
    }

    public static ChatHistoryCacheUtil getInstance() {
        return instance;
    }

    public static void setContext(Context cxt) {
        getInstance().mContext = cxt;
    }

    public static HashMap<String, ArrayList<MessageDTO>> getAllMsgsFromHistory() {
        File dir = new File(String.valueOf(getInstance().mContext.getFilesDir()));
        File[] files = dir.listFiles();
        HashMap<String, ArrayList<MessageDTO>> res = new HashMap<>();
        for (File file : files) {
            String filename = file.getName();
            String[] args = filename.split("\\.");
            if (args.length == 2 && args[1].equals("json")) {
                String otherId = args[0];
                ArrayList<MessageDTO> msgs = readFile(file);
                if (msgs.isEmpty()) {
                    continue;
                }
                res.put(otherId, msgs);
            }
        }
        return res;
    }

    public static ArrayList<MessageDTO> getMsgsFromCache(String otherId) {
        String filename = String.format("%s.json", otherId);
        File file = new File(instance.mContext.getFilesDir(), filename);
        if (!file.exists()) {
            return null;
        }

        try {
            if (file.length() != 0) {
                return readFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private static ArrayList<MessageDTO> readFile(File file) {
        try {
            FileInputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            is.close();

            JSONArray msgJSONArray = new JSONArray(sb.toString());
            ArrayList<MessageDTO> res = new ArrayList<>();
            int length = msgJSONArray.length();
            UserDTO sender = null, receiver = null;
            boolean added = false;
            for (int i = 0; i < length; i++) {
                JSONObject object = msgJSONArray.getJSONObject(i);
                String senderId = object.getString("sender");
                String senderName = object.getString("senderName");
                String receiverId = object.getString("receiver");
                String receiverName = object.getString("receiverName");
                if (!added) {
                    String myID = String.valueOf(UserInfoUtil.me.id);
                    String id = senderId.equals(myID) ? receiverId : senderId;
                    String name = senderId.equals(myID) ? receiverName : senderName;
                    MessageStoreUtil.putNewUser(new UserDTO(id, name));
                    added = true;
                }
                if (sender == null) {
                    sender = new UserDTO(senderId, senderName);
                }
                if (receiver == null) {
                    receiver = new UserDTO(receiverId, receiverName);
                }
                MessageDTO msg = new MessageDTO(object, sender, receiver);
                res.add(msg);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void cache(String userId, ArrayList<MessageDTO> msgs) {
        ChatHistoryCacheUtil instance = getInstance();
        String filename = String.format("%s.json", userId);
        File file = new File(instance.mContext.getFilesDir(), filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
                e.printStackTrace();
                return;
            }
        }
        JSONArray msgJSONArray;
        try {
            msgJSONArray = instance.msgsToJSON(msgs);
            OutputStream os = new FileOutputStream(file);
            os.write(msgJSONArray.toString().getBytes());
            os.close();
        } catch (JSONException e) {
            ErrorHandlingUtil.logToConsole(e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "缓存失败");
            e.printStackTrace();
        }
    }

    public static void cacheAll(HashMap<String, ArrayList<MessageDTO>> history) {
        for (String id : history.keySet()) {
            cache(id, history.get(id));
        }
    }

    private String getCurrentUserId() {
        return String.valueOf(UserInfoUtil.me.id);
    }

    public static void clearCache() {
        File dir = new File(String.valueOf(getInstance().mContext.getFilesDir()));
        File[] files = dir.listFiles();
        for (File file : files) {
            String filename = file.getName();
            String[] args = filename.split("\\.");
            if (args.length == 2 && args[1].equals("json")) {
                file.delete();
            }
        }
    }

    private JSONArray msgsToJSON(ArrayList<MessageDTO> msgs) throws JSONException {
        JSONArray res = new JSONArray();
        for (MessageDTO msg : msgs) {
            JSONObject msgJSON = new JSONObject();
            msgJSON.put("id", msg.getId());
            msgJSON.put("time", msg.getTimestamp());
            msgJSON.put("content", msg.getContent());
            msgJSON.put("sender", String.valueOf(msg.getSender().id));
            msgJSON.put("senderName", msg.getSender().username);
            msgJSON.put("receiver", String.valueOf(msg.getReceiver().id));
            msgJSON.put("receiverName", msg.getReceiver().username);
            // TODO: other message types, e.g. image and audio
            msgJSON.put("type", "text");
            res.put(msgJSON);
        }
        return res;
    }
}
