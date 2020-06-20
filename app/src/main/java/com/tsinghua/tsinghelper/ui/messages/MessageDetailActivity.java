package com.tsinghua.tsinghelper.ui.messages;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.MessageDTO;
import com.tsinghua.tsinghelper.dtos.UserDTO;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.MessageDateFormatter;
import com.tsinghua.tsinghelper.util.MessageStoreUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;
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
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MessageDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.message_list)
    MessagesList messagesList;
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.input)
    MessageInput mInput;

    MessagesListAdapter<MessageDTO> mAdapter;

    private UserDTO receiver;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);

        initViews();
        getMessages();
    }

    private void initViews() {
        initToolbar();
        initAdapter();
        setInputListener();
        setStatusBarUpperAPI21();
    }

    private void setInputListener() {
        mInput.setInputListener(input -> sendMessage(input.toString()));
    }

    private boolean sendMessage(String content) {
        JSONObject msgInfo = new JSONObject();
        String timestamp = String.valueOf(new Date().getTime());
        try {
            msgInfo.put("time", timestamp);
            msgInfo.put("content", content);
            msgInfo.put("type", "text");
            msgInfo.put("to", receiver.id);
            if (WebSocketUtil.getWebSocket().send(msgInfo.toString())) {
                ToastUtil.showToastOnUIThread(MessageDetailActivity.this, "发送成功");
            }

            MessageDTO msg;
            if (MessageStoreUtil.hasUser(receiver.id)) {
                msg = new MessageDTO(String.valueOf(UserInfoUtil.me.id), content, timestamp,
                        UserInfoUtil.me, MessageStoreUtil.getUserById(receiver.id));
            } else {
                MessageStoreUtil.putNewUser(receiver);
                msg = new MessageDTO(String.valueOf(UserInfoUtil.me.id), content,
                        timestamp, UserInfoUtil.me, receiver);
            }
            MessageStoreUtil.addSentMsg(String.valueOf(receiver.id), msg);
            MessageDetailActivity.this.runOnUiThread(() -> mAdapter.addToStart(msg, true));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void initAdapter() {
        String id = String.valueOf(UserInfoUtil.me.id);
        ImageLoader imageLoader = (imageView, url, payload) ->
                Glide.with(MessageDetailActivity.this)
                        .load(url).into(imageView);
        MessageHolders mh = new MessageHolders();
        mh.setIncomingTextLayout(R.layout.item_custom_incoming_msg);
        mh.setOutcomingTextLayout(R.layout.item_custom_outcoming_msg);
        mAdapter = new MessagesListAdapter<>(id, mh, imageLoader);
        mAdapter.setDateHeadersFormatter(new MessageDateFormatter());
        messagesList.setAdapter(mAdapter);

    }

    private void getMessages() {
        HashMap<String, String> params = new HashMap<>();
        String otherId = getIntent().getStringExtra("sender");
        String otherName = getIntent().getStringExtra("username");
        receiver = new UserDTO(otherId, otherName);
        mUsername.setText(otherName);

        params.put("receiver", otherId);
        HttpUtil.get(HttpUtil.CHAT_MSG_SENT, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        MessageDetailActivity.this, "网络错误", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONArray resJson = new JSONArray(response.body().string());
                        initMessages(resJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initMessages(JSONArray sentMsgs) throws JSONException {
        int length = sentMsgs.length();
        ArrayList<MessageDTO> msgs = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            MessageDTO msg = new MessageDTO(sentMsgs.getJSONObject(i), UserInfoUtil.me, receiver);
            msgs.add(msg);
        }

        // cache chat history
//        ChatHistoryCacheUtil.cache(String.valueOf(receiver.id), msgs);

        ArrayList<MessageDTO> receivedMsgs = MessageStoreUtil
                .getReceivedMsgsFromUser(String.valueOf(receiver.id));
        if (receivedMsgs != null) {
            msgs.addAll(receivedMsgs);
        }
        Comparator<MessageDTO> comparator = (msg1, msg2) ->
                msg1.getTimestamp().compareTo(msg2.getTimestamp());
        Collections.sort(msgs, comparator);
        MessageDetailActivity.this.runOnUiThread(() -> mAdapter.addToEnd(msgs, true));
    }

    private void setStatusBarUpperAPI21() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transparent));
        ViewGroup mContentView = findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
    }
}
