package com.tsinghua.tsinghelper.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tsinghua.tsinghelper.MainActivity;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.LoginUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText mETUsername;
    @BindView(R.id.et_password)
    EditText mETPassword;
    @BindView(R.id.et_phone)
    EditText mETPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    private boolean checkFields(String username, String password, String phone) {
        if (username.isEmpty()) {
            ToastUtil.showToast(this, "请输入用户名");
            return false;
        }
        if (password.isEmpty()) {
            ToastUtil.showToast(this, "请输入密码");
            return false;
        }
        if (phone.isEmpty()) {
            ToastUtil.showToast(this, "请输入手机号");
            return false;
        }
        return true;
    }

    public void login(View view) {
        String username = mETUsername.getText().toString();
        String password = mETPassword.getText().toString();
        String phone = mETPhoneNumber.getText().toString();

        if (!checkFields(username, password, phone)) {
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("username", username);
        params.put("password", password);

        HttpUtil.post(HttpUtil.USER_LOGIN, params, new Callback() {
            @Override
            public void onResponse(
                    @NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                JSONObject resJson;
                if (response.code() == 201) {
                    // login succeeded
                    LoginActivity.this.runOnUiThread(() ->
                            ToastUtil.showToast(LoginActivity.this, "登录成功")
                    );

                    HashMap<String, String> userInfo = new HashMap<>();
                    userInfo.put("username", username);
                    try {
                        resJson = new JSONObject(resStr);
                        userInfo.put("userId", resJson.getString("userId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LoginUtil.recordUserInfo(LoginActivity.this, userInfo);
            
                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(it);
                    finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }
        });

    }

    public void register(View view) {

    }

    public void forgetPassword(View view) {

    }
}
