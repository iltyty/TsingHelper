package com.tsinghua.tsinghelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AccountStateAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener {

    private Context mContext;
    private ArrayList<Integer> mStateIcons = new ArrayList<>();
    private final String[] mStateText = {"在线", "忙碌", "离线"};

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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> params = new HashMap<>();
        final String state;
        switch (position) {
            case 0:
                state = "online";
                break;
            case 1:
                state = "busy";
                break;
            default:
                state = "offline";
                break;
        }
        params.put("state", state);
        HttpUtil.post(HttpUtil.USER_ONLINE_STATE, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.logToConsole(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 201) {
                    UserInfoUtil.me.state = state;
                    UserInfoUtil.putPref(UserInfoUtil.STATE, state);
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
