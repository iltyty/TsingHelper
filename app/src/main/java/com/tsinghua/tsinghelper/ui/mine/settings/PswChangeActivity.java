package com.tsinghua.tsinghelper.ui.mine.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PswChangeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.psw_old)
    EditText mPswOld;
    @BindView(R.id.psw_new_1)
    EditText mPswNew1;
    @BindView(R.id.psw_new_2)
    EditText mPswNew2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psw_change);

        ButterKnife.bind(this);
        initToolbar();
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private HashMap<String, String> checkField() {
        String pwdPattern = "^\\w{5,16}$";
        String newPwd1 = mPswNew1.getText().toString();
        String newPwd2 = mPswNew2.getText().toString();

        if (!newPwd1.matches(pwdPattern) || !newPwd2.matches(pwdPattern)) {
            ToastUtil.showToast(this, "密码必须为5到16位");
            return null;
        }
        if (!newPwd1.equals(newPwd2)) {
            ToastUtil.showToast(this, "两次输入的密码不一致");
            return null;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("newPwd", newPwd1);
        params.put("oldPwd", mPswOld.getText().toString());
        return params;
    }

    public void pswReset(View view) {
        HashMap<String, String> params = checkField();
        if (params == null) {
            return;
        }

        HttpUtil.post(HttpUtil.USER_MODIFY_PWD, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        PswChangeActivity.this, "网络错误，请稍后重试", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                if (response.code() == 201) {
                    ToastUtil.showToastOnUIThread(
                            PswChangeActivity.this, "密码修改成功");
                    finish();
                } else if (response.code() == 400) {
                    ToastUtil.showToastOnUIThread(
                            PswChangeActivity.this, "原密码错误");
                }
            }
        });
    }
}
