package com.tsinghua.tsinghelper.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tsinghua.tsinghelper.MainActivity;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

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

public class PswForgetActivity extends AppCompatActivity {
    @BindView(R.id.pswForget_phone)
    EditText mPhone;
    @BindView(R.id.pswForget_newPsw)
    EditText mNewPsw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psw_forget);

        ButterKnife.bind(this);
    }

    private HashMap<String, String> checkFields() {
        // 11位手机号码
        String phonePattern = "^\\d{11}$";
        // 5到16位字母和数字的组合
        String pwdPattern = "^\\w{5,16}$";
        String password = mNewPsw.getText().toString();
        String phone = mPhone.getText().toString();

        if (!password.matches(pwdPattern)) {
            ToastUtil.showToast(this, "密码必须为5到16位");
            return null;
        }
        if (!phone.matches(phonePattern)) {
            ToastUtil.showToast(this, "请输入11位手机号");
            return null;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);
        return params;
    }

    public void pswChange(View view) {
        HashMap<String, String> params = checkFields();
        if (params == null) {
            return;
        }

        HttpUtil.post(HttpUtil.USER_FORGET_PWD, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        PswForgetActivity.this, "网络错误，请稍后重试", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                switch (response.code()) {
                    case 201:
                        String resStr = response.body().string();
                        ToastUtil.showToastOnUIThread(PswForgetActivity.this, "登录成功");
                        // remember login status
                        try {
                            JSONObject json = new JSONObject(resStr);
                            String token = json.getString("token");
                            params.put("auth", token);
                        } catch (JSONException ignored) {
                        }
                        params.remove("password");
                        saveUserInfo(resStr, params);
                        Intent it = new Intent(PswForgetActivity.this, MainActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                        break;
                    case 404:
                        ToastUtil.showToastOnUIThread(
                                PswForgetActivity.this, "用户名不存在");
                        break;
                    case 400:
                        ToastUtil.showToastOnUIThread(
                                PswForgetActivity.this, "请求参数不合法");
                        break;
                }
            }
        });
    }

    private void saveUserInfo(String resStr, HashMap<String, String> params) {
        JSONObject resJson;
        try {
            resJson = new JSONObject(resStr);
            params.put("userId", resJson.getString("userId"));
            params.put("username", resJson.getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UserInfoUtil.putPrefs(params);
    }

    public void back(View view) {
        onBackPressed();
    }
}
