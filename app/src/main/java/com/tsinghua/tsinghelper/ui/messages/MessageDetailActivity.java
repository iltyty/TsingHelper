package com.tsinghua.tsinghelper.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.ChattingAdapter;
import com.tsinghua.tsinghelper.dtos.MessageDTO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_message)
    Toolbar mToolbar;
    @BindView(R.id.sender_name)
    TextView mSenderName;
    @BindView(R.id.send_button)
    Button sendButton;
    @BindView(R.id.input_text)
    EditText inputText;
    @BindView(R.id.message_recycler)
    RecyclerView msgRecyclerView;

    private ArrayList<MessageDTO> mMessages = new ArrayList<>();
    private ChattingAdapter mAdapter;
    Intent mIntent;

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

        mIntent = getIntent();

        initToolbar();
        initMessages();
        initLayout();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void initLayout() {
        mSenderName.setText(mIntent.getExtras().getString("SENDER_NAME"));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChattingAdapter(mMessages);
        msgRecyclerView.setAdapter(mAdapter);
        msgRecyclerView.scrollToPosition(mMessages.size() - 1);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2020/5/23 add send button clicked function
                msgRecyclerView.scrollToPosition(mMessages.size() - 1);
            }
        });
    
    }
    
    private void initMessages() {
        // TODO: 2020/5/23 get messages 
        MessageDTO msg1 = new MessageDTO(UUID.randomUUID(), UUID.randomUUID(),
                new Timestamp(System.currentTimeMillis()), "Hello world!", MessageDTO.TYPE_RECEIVED);
        mMessages.add(msg1);

        MessageDTO msg2 = new MessageDTO(UUID.randomUUID(), UUID.randomUUID(),
                new Timestamp(System.currentTimeMillis()), "Hi, I'm YSL!", MessageDTO.TYPE_SENT);
        mMessages.add(msg2);

        MessageDTO msg3 = new MessageDTO(UUID.randomUUID(), UUID.randomUUID(),
                new Timestamp(System.currentTimeMillis()), "Hi, I'm QYH!", MessageDTO.TYPE_RECEIVED);
        mMessages.add(msg3);

        MessageDTO msg4 = new MessageDTO(UUID.randomUUID(), UUID.randomUUID(),
                new Timestamp(System.currentTimeMillis()), "Test for a really LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG message!", MessageDTO.TYPE_RECEIVED);
        mMessages.add(msg4);

        for(int i = 1; i <= 5; i++) {
            MessageDTO msg5 = new MessageDTO(UUID.randomUUID(), UUID.randomUUID(),
                    new Timestamp(System.currentTimeMillis()), "Test for recycler!", MessageDTO.TYPE_SENT);
            mMessages.add(msg5);
        }
    }
}
