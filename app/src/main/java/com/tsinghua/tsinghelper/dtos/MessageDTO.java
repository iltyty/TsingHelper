package com.tsinghua.tsinghelper.dtos;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class MessageDTO {
    private UUID mSender;
    private UUID mReceiver;
    private String mContent;
    private Timestamp mTime;

    public MessageDTO(@NonNull UUID sender, @NonNull UUID receiver,
                      @NonNull Timestamp time, String content) {
        mTime = time;
        mSender = sender;
        mContent = content;
        mReceiver = receiver;
    }

    public String getContent() { return mContent; }

    public Timestamp getTime() { return mTime; }

    public String getTimeString() {
        SimpleDateFormat formatter =  new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return formatter.format(mTime);
    }

    public UUID getSender() { return mSender; }

    public UUID getReceiver() { return mReceiver; }
}
