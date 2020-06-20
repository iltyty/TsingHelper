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
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.MessageDTO;
import com.tsinghua.tsinghelper.dtos.UserDTO;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.MessageDateFormatter;
import com.tsinghua.tsinghelper.util.MessageStoreUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

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

public class MessageDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.message_list)
    MessagesList messagesList;
    @BindView(R.id.username)
    TextView mUsername;

    MessagesListAdapter<MessageDTO> mAdapter;

    private String senderId;

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

        initToolbar();
        initAdapter();
        setStatusBarUpperAPI21();
        getMessages();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void initAdapter() {
        String id = UserInfoUtil.getPref("userId", "");
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
        senderId = getIntent().getStringExtra("sender");
        mUsername.setText(getIntent().getStringExtra("username"));

        params.put("receiver", senderId);
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
        String userId = UserInfoUtil.getPref("userId", "-1");
        String username = UserInfoUtil.getPref("username", "");
        UserDTO sender = new UserDTO(userId, username);
        ArrayList<MessageDTO> msgs = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            MessageDTO msg = new MessageDTO(sentMsgs.getJSONObject(i), sender);
            msgs.add(msg);
        }
        msgs.addAll(MessageStoreUtil.getReceivedMsgsFromUser(senderId));
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
