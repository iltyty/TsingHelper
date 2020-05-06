package com.tsinghua.tsinghelper.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tsinghua.tsinghelper.R;

import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText mETUsername;
    @BindView(R.id.et_password)
    EditText mETPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void login(View view) {

    }

    public void forgetPassword(View view) {

    }
}
