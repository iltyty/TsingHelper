package com.tsinghua.tsinghelper.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.AccountStateAdapter;
import com.tsinghua.tsinghelper.dtos.DialogDTO;
import com.tsinghua.tsinghelper.dtos.MessageDTO;
import com.tsinghua.tsinghelper.dtos.UserDTO;
import com.tsinghua.tsinghelper.util.ChatHistoryCacheUtil;
import com.tsinghua.tsinghelper.util.DialogDateFormatter;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.MessageInfoUtil;
import com.tsinghua.tsinghelper.util.MessageStoreUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;
import com.tsinghua.tsinghelper.util.WebSocketUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MessagesFragment extends Fragment {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.dialog_list)
    DialogsList mDialogsList;

    private DialogsListAdapter<DialogDTO> mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, root);

        initSpinner();
        initAdapter();
//        readHistoryMsgs();
        getMsgsFromServer();
        initWebSocket();

        mDialogsList.setAdapter(mAdapter);

        return root;
    }

    private void setDialogs() {
        ArrayList<DialogDTO> dialogs = new ArrayList<>();
        for (String otherId : MessageStoreUtil.getMyMsgs().keySet()) {
            UserDTO other = MessageStoreUtil.getUserById(otherId);
            ArrayList<MessageDTO> msgs = MessageStoreUtil.getMsgsWithUser(otherId);
            if (msgs.size() > 0) {
                dialogs.add(new DialogDTO(otherId, other.username,
                        other.avatar, msgs.get(0)));
            }
        }
        requireActivity().runOnUiThread(() -> mAdapter.setItems(dialogs));
    }

    @Override
    public void onStart() {
        super.onStart();
        setDialogs();
    }

    private void initWebSocket() {
        WebSocketListener wsl = new WebSocketListener() {
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                try {
                    boolean existed = false;
                    JSONObject resJson = new JSONObject(text);
                    String time = resJson.getString("time");
                    String type = resJson.getString("type");
                    String content = resJson.getString("content");
                    String senderId = resJson.getString("from");
                    String senderName = resJson.getString("senderName");
                    String senderAvatar = HttpUtil.getUserAvatarUrlById(senderId);

                    UserDTO sender;
                    if (MessageStoreUtil.hasUser(senderId)) {
                        existed = true;
                        sender = MessageStoreUtil.getUserById(senderId);
                    } else {
                        sender = new UserDTO(senderId, senderName);
                        MessageStoreUtil.putNewUser(sender);
                    }
                    MessageDTO newMsg = new MessageDTO(senderId, content, time, sender, UserInfoUtil.me);
                    MessageStoreUtil.addMsg(senderId, newMsg);
                    if (existed) {
                        mAdapter.updateDialogWithMessage(senderId, newMsg);
                    } else {
                        mAdapter.addItem(new DialogDTO(senderId, senderName, senderAvatar, newMsg));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }
        };
        WebSocketUtil.setWSConnection(wsl);
    }

    private void readHistoryMsgs() {
        MessageStoreUtil.setMyMsgs(ChatHistoryCacheUtil.getAllMsgsFromHistory());
    }

    private void getMsgsFromServer() {
        HashMap<String, String> params = new HashMap<>();
        String since = UserInfoUtil.getPref(MessageInfoUtil.SINCE, "");
        params.put(MessageInfoUtil.SINCE, since);
        HttpUtil.get(HttpUtil.CHAT_MSG_GET, null, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.logToConsole(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 200) {
                    UserInfoUtil.putPref(MessageInfoUtil.SINCE,
                            String.valueOf(System.currentTimeMillis()));
                    String resStr = response.body().string();
                    try {
                        JSONObject resJson = new JSONObject(resStr);
                        UserInfoUtil.putPref(MessageInfoUtil.SINCE,
                                String.valueOf(System.currentTimeMillis()));
                        initDialogs(resJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initDialogs(JSONObject resJson) throws JSONException {
        JSONArray mySent = resJson.getJSONArray("sent");
        JSONArray myReceived = resJson.getJSONArray("received");
        parseMsgs(mySent, true);
        parseMsgs(myReceived, false);
        setDialogs();
        ChatHistoryCacheUtil.cacheAll(MessageStoreUtil.getMyMsgs());
    }

    private void parseMsgs(JSONArray array, boolean iAmSender) throws JSONException {
        int length = array.length();
        for (int i = 0; i < length; i++) {
            JSONObject other = array.getJSONObject(i);
            String otherId = other.getString("id");
            String otherName = other.getString("username");
            String fieldName = iAmSender ? "received_msgs" : "sent_msgs";
            JSONArray msgs = other.getJSONArray(fieldName);

            UserDTO otherUser;
            if (MessageStoreUtil.hasUser(otherId)) {
                otherUser = MessageStoreUtil.getUserById(otherId);
            } else {
                otherUser = new UserDTO(otherId, otherName);
                MessageStoreUtil.putNewUser(otherUser);
            }

            int msgsLen = msgs.length();
            ArrayList<MessageDTO> res = new ArrayList<>();
            for (int j = 0; j < msgsLen; j++) {
                MessageDTO msg;
                if (iAmSender) {
                    msg = new MessageDTO(msgs.getJSONObject(j), UserInfoUtil.me, otherUser);
                } else {
                    msg = new MessageDTO(msgs.getJSONObject(j), otherUser, UserInfoUtil.me);
                }
                res.add(msg);
            }
            MessageStoreUtil.addMsgs(otherId, res);
            Comparator<MessageDTO> comparator = (msg1, msg2) ->
                    msg2.getTimestamp().compareTo(msg1.getTimestamp());
            ArrayList<MessageDTO> msgsWithUser = MessageStoreUtil.getMsgsWithUser(otherId);
            Collections.sort(msgsWithUser, comparator);
        }
    }

    private void initSpinner() {
        spinner.setAdapter(new AccountStateAdapter(getContext()));
        spinner.setSelection(0, true);
    }

    private void initAdapter() {
        mAdapter = new DialogsListAdapter<>(R.layout.item_custom_dialog,
                (imageView, url, payload) ->
                        Glide.with(requireActivity()).load(url).into(imageView));
        mAdapter.setDatesFormatter(new DialogDateFormatter());
        mAdapter.setOnDialogClickListener(dialog -> {
            Intent it = new Intent(requireActivity(), MessageDetailActivity.class);
            it.putExtra("sender", String.valueOf(dialog.getId()));
            it.putExtra("username", dialog.getDialogName());
            startActivity(it);
        });
    }
}
