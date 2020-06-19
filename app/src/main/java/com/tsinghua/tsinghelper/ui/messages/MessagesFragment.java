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
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.MessageInfoUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

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

public class MessagesFragment extends Fragment {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.dialog_list)
    DialogsList mDialogsList;

    private DialogsListAdapter<DialogDTO> mAdapter;
    private HashMap<String, ArrayList<MessageDTO>> myMsgs = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, root);

        initSpinner();
        initAdapter();
        getMessages();

        mDialogsList.setAdapter(mAdapter);

        return root;
    }

    private void getMessages() {
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
                    UserInfoUtil.putPref(MessageInfoUtil.SINCE, String.valueOf(System.currentTimeMillis()));
                    String resStr = response.body().string();
                    try {
                        JSONArray resJson = new JSONArray(resStr);
                        initDialogs(resJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initDialogs(JSONArray resJson) throws JSONException {
        int length = resJson.length();
        for (int i = 0; i < length; i++) {
            JSONObject dialogJson = resJson.getJSONObject(i);
            String senderId = dialogJson.getString("id");
            String senderAvatar = HttpUtil.getUserAvatarUrlById(senderId);
            String senderName = dialogJson.getString("username");
            UserDTO sender = new UserDTO(senderId, senderName);
            JSONArray sentMsgs = dialogJson.getJSONArray("sent_msgs");

            int msgsLen = sentMsgs.length();
            ArrayList<MessageDTO> msgs = new ArrayList<>();
            for (int j = 0; j < msgsLen; j++) {
                MessageDTO msg = new MessageDTO(sentMsgs.getJSONObject(j), senderId, senderName);
                msgs.add(msg);
            }
            Comparator<MessageDTO> comparator = new Comparator<MessageDTO>() {
                @Override
                public int compare(MessageDTO msg1, MessageDTO msg2) {
                    return msg1.getTimestamp().compareTo(msg2.getTimestamp());
                }
            };
            Collections.sort(msgs, comparator);
            myMsgs.put(senderId, msgs);
            MessageDTO lastMsg = msgs.get(msgs.size() - 1);
            mAdapter.addItem(new DialogDTO(senderId, senderName, senderAvatar, lastMsg));
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
        mAdapter.setOnDialogClickListener(dialog -> {
            Intent it = new Intent(requireActivity(), MessageDetailActivity.class);
            startActivity(it);
        });
    }

//    private void initDialogs() {
//        for (int i = 0; i < 10; i++) {
//            String str = String.valueOf(i);
//            mAdapter.addItem(new DialogDTO(str, "对话框" + str));
//        }
//    }
}
