package com.tsinghua.tsinghelper.ui.messages;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.MessageDTO;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.message_list)
    MessagesList messagesList;

    MessagesListAdapter<MessageDTO> mAdapter;

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

        genMessages();
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
        messagesList.setAdapter(mAdapter);

    }

    private void genMessages() {
        String ts = String.valueOf(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            mAdapter.addToStart(new MessageDTO(String.valueOf(i), ts, String.valueOf(i)), true);
        }
    }
}
