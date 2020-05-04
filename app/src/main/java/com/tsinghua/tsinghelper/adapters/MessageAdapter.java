package com.tsinghua.tsinghelper.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.MessageDTO;
import com.tsinghua.tsinghelper.ui.messages.MessageDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<MessageDTO> mMessages;
    private Context mContext;

    public MessageAdapter(Context context, ArrayList<MessageDTO> messages) {
        mContext = context;
        mMessages = messages;
    }

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.message_item_icon)
        ImageView mMessageAvatar;
        @BindView(R.id.message_item_content)
        TextView mMessageContent;
        @BindView(R.id.message_item_time)
        TextView mMessageTime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void setMessagesData(MessageDTO message) {
            mMessageContent.setText(message.getContent());
            mMessageTime.setText(message.getTimeString());

            // TODO: set message sender's avatar
            mMessageAvatar.setImageResource(R.drawable.ic_community_item_32dp);
        }

        @Override
        public void onClick(View v) {
//            MessageDTO message = mMessages.get(getAdapterPosition());
            Intent intent = new Intent(mContext, MessageDetail.class);

            intent.putExtra("SENDER_NAME", "消息内容");
            // TODO: send more information

            mContext.startActivity(intent);
        }
    }

}
