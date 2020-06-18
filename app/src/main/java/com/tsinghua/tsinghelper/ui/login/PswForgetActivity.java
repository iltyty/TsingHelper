package com.tsinghua.tsinghelper.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.tsinghua.tsinghelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PswForgetActivity extends AppCompatActivity {
    @BindView(R.id.psw_new_1)
    EditText mPswNew1;
    @BindView(R.id.psw_new_2)
    EditText mPswNew2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psw_forget);

        ButterKnife.bind(this);
    }

    public void pswChange(View view) {

    }
}
