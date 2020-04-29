package com.tsinghua.tsinghelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsinghua.tsinghelper.R;

import java.util.ArrayList;

public class AccountStateAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Integer> mStateIcons = new ArrayList<>();
    private final String[] mStateText = { "在线", "忙碌", "隐身" };

    public AccountStateAdapter(Context context) {
        mContext = context;
        // online state
        mStateIcons.add(R.drawable.ic_green_dot_8dp);
        // busy state
        mStateIcons.add(R.drawable.ic_yellow_dot_8dp);
        // invisible state
        mStateIcons.add(R.drawable.ic_red_dot_8dp);
    }

    @Override
    public int getCount() {
        return mStateIcons == null ? 0 : mStateIcons.size();
    }

    @Override
    public Object getItem(int position) {
        return mStateIcons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.component_state_item, null);
        }
        ImageView stateIcon = convertView.findViewById(R.id.state_icon);
        stateIcon.setImageResource(mStateIcons.get(position));

        TextView stateText = convertView.findViewById(R.id.state_text);
        stateText.setText(mStateText[position]);

        return convertView;
    }
}
