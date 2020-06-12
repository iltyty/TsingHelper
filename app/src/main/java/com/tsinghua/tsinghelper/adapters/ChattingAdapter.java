package com.tsinghua.tsinghelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.MessageDTO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ViewHolder> {

    private ArrayList<MessageDTO> mMessages;

    public ChattingAdapter(ArrayList<MessageDTO> messages) {
        mMessages = messages;
    }

    @NonNull
    @Override
    public ChattingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingAdapter.ViewHolder holder, int position) {
        MessageDTO msg = mMessages.get(position);
        if(msg.getType() == MessageDTO.TYPE_RECEIVED) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        } else if(msg.getType() == MessageDTO.TYPE_SENT) {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.left_layout)
        LinearLayout leftLayout;
        @BindView(R.id.right_layout)
        LinearLayout rightLayout;
        @BindView(R.id.message_left)
        TextView leftMsg;
        @BindView(R.id.message_right)
        TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
