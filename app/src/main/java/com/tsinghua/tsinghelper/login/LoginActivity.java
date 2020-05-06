package com.tsinghua.tsinghelper.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText mETUsername;
    @BindView(R.id.et_password)
    EditText mETPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    public void login(View view) {
        if (mETUsername.getText().toString().isEmpty()) {
            ToastUtil.showToast(this, "请输入用户名");
            return;
        }
        if (mETPassword.getText().toString().isEmpty()) {
            ToastUtil.showToast(this, "请输入密码");
            return;
        }
    }

    public void register(View view) {

    }

    public void forgetPassword(View view) {

    }
}
