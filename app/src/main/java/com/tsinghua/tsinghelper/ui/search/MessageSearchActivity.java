package com.tsinghua.tsinghelper.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.dtos.DialogDTO;
import com.tsinghua.tsinghelper.ui.messages.MessageDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageSearchActivity extends AppCompatActivity {
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.dialog_list)
    DialogsList mDialogsList;

    private DialogsListAdapter<DialogDTO> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_message);
        ButterKnife.bind(this);

        initSearchView();
        initAdapter();
        initDialogs();

        mDialogsList.setAdapter(mAdapter);
    }

    private void initAdapter() {
        mAdapter = new DialogsListAdapter<>(R.layout.item_custom_dialog,
                (imageView, url, payload) ->
                        Glide.with(this).load(url).into(imageView));
        mAdapter.setOnDialogClickListener(dialog -> {
            Intent it = new Intent(this, MessageDetailActivity.class);
            startActivity(it);
        });
    }

    private void initDialogs() {
        for (int i = 0; i < 10; i++) {
            String str = String.valueOf(i);
//            mAdapter.addItem(new DialogDTO(str, "对话框" + str));
        }

        // TODO: 2020/6/20 get message search result from backend
    }

    public void initSearchView() {
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MessageSearchActivity.this, SearchActivity.class);
                it.putExtra("searchType", "MESSAGE");
                startActivityForResult(it, 1);
            }
        });
    }


}
