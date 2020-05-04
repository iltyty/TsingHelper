package com.tsinghua.tsinghelper.ui.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.AccountStateAdapter;
import com.tsinghua.tsinghelper.adapters.MessageAdapter;
import com.tsinghua.tsinghelper.dtos.MessageDTO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesFragment extends Fragment {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DividerItemDecoration mDivider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, root);

        initSpinner();
        initRecyclerView();

        return root;
    }

    private void initSpinner() {
        spinner.setAdapter(new AccountStateAdapter(getContext()));
        spinner.setSelection(0, true);
    }


    private void initRecyclerView() {
        mAdapter = new MessageAdapter(getActivity(), getMessages());
        mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mDivider = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(mDivider);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    private ArrayList<MessageDTO> getMessages() {
        ArrayList<MessageDTO> messages = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String s = String.valueOf(i + 1);
            messages.add(new MessageDTO(UUID.randomUUID(), UUID.randomUUID(),
                    new Timestamp(System.currentTimeMillis()), "消息内容" + s));
        }
        return messages;
    }
}
