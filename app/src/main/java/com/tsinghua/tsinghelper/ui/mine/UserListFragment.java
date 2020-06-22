package com.tsinghua.tsinghelper.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.UserItemAdapter;
import com.tsinghua.tsinghelper.components.DividerItemDecrator;
import com.tsinghua.tsinghelper.dtos.UserDTO;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserListFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private HashMap<String, String> queryParams;
    private UserItemAdapter mAdapter;

    public UserListFragment(HashMap<String, String> params, Context cxt) {
        queryParams = params;
        mAdapter = new UserItemAdapter(cxt, false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_list, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (queryParams != null) {
            getUsers();
        }
    }

    private void getUsers() {
        HttpUtil.get(HttpUtil.USER_RELATIONS, queryParams, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        requireActivity(), "获取关系列表失败", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONArray resJson = new JSONArray(response.body().string());
                        mAdapter.clearUsers();
                        int length = resJson.length();
                        ArrayList<UserDTO> users = new ArrayList<>();
                        for (int i = 0; i < length; i++) {
                            UserDTO user = new UserDTO(resJson.getJSONObject(i));
                            users.add(user);
                        }
                        mAdapter.setUsers(users);
                        requireActivity().runOnUiThread(() -> mAdapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        DividerItemDecrator mDivider = new DividerItemDecrator(
                requireActivity().getDrawable(R.drawable.shape_list_divider));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(mDivider);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
    }
}
