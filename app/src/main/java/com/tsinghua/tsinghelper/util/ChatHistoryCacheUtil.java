package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.util.Log;

import com.tsinghua.tsinghelper.dtos.MessageDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

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
            OutputStream os = new FileOutputStream(file, true);
            os.write(msgJSONArray.toString().getBytes());
            os.close();
        } catch (JSONException e) {
            ErrorHandlingUtil.logToConsole(e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "缓存失败");
            e.printStackTrace();
        }
    }

    private String getCurrentUserId() {
        return String.valueOf(UserInfoUtil.me.id);
    }

    private JSONArray msgsToJSON(ArrayList<MessageDTO> msgs) throws JSONException {
        JSONArray res = new JSONArray();
        for (MessageDTO msg : msgs) {
            JSONObject msgJSON = new JSONObject();
            msgJSON.put("id", msg.getId());
            msgJSON.put("time", msg.getTimestamp());
            msgJSON.put("content", msg.getContent());
            msgJSON.put("sender", msg.getSender());
            msgJSON.put("receiver", msg.getReceiver());
            // TODO: other message types, e.g. image and audio
            msgJSON.put("type", "text");
            res.put(msgJSON);
        }
        return res;
    }
}
