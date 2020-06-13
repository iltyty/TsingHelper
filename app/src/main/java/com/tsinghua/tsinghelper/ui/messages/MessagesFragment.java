package com.tsinghua.tsinghelper.ui.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.AccountStateAdapter;
import com.tsinghua.tsinghelper.dtos.DialogDTO;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesFragment extends Fragment {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.dialog_list)
    DialogsList mDialogsList;

    private DialogsListAdapter<DialogDTO> mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, root);

        initSpinner();

        mAdapter = new DialogsListAdapter<>(R.layout.item_custom_dialog,
                (imageView, url, payload) ->
                        Glide.with(requireActivity()).load(url).into(imageView));

        initDialogs();

        mDialogsList.setAdapter(mAdapter);

        return root;
    }

    private void initSpinner() {
        spinner.setAdapter(new AccountStateAdapter(getContext()));
        spinner.setSelection(0, true);
    }

    private void initDialogs() {
        for (int i = 0; i < 10; i++) {
            String str = String.valueOf(i);
            mAdapter.addItem(new DialogDTO(str, "对话框" + str));
        }
    }
}
