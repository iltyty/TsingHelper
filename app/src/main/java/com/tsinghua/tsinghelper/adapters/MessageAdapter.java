package com.tsinghua.tsinghelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.MessageDTO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<MessageDTO> mMessages;

    public MessageAdapter(ArrayList<MessageDTO> messages) { mMessages = messages; }

    public void setMessages(ArrayList<MessageDTO> messages) {
        mMessages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_message_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        holder.setMessagesData(mMessages.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_item_icon)
        ImageView mMessageAvatar;
        @BindView(R.id.message_item_content)
        TextView mMessageContent;
        @BindView(R.id.message_item_time)
        TextView mMessageTime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setMessagesData(MessageDTO message) {
            mMessageContent.setText(message.getContent());
            mMessageTime.setText(message.getTimeString());

            // TODO: set message sender's avatar
            mMessageAvatar.setImageResource(R.drawable.ic_community_item_32dp);
        }
    }

}
