package com.tsinghua.tsinghelper.ui.mine.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.util.ErrorHandlingUtil;
import com.tsinghua.tsinghelper.util.HttpUtil;
import com.tsinghua.tsinghelper.util.ToastUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FieldModifyActivity extends AppCompatActivity implements TextWatcher {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.page_title)
    TextView mPageTitle;
    @BindView(R.id.finish)
    TextView mFinish;
    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.len_hint)
    TextView mLenHint;

    private int fieldMaxLen;
    private String fieldName;
    private String fieldTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_modify);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent it = getIntent();
        fieldName = it.getStringExtra("fieldName");
        fieldTitle = "修改" + it.getStringExtra("fieldTitle");
        fieldMaxLen = it.getIntExtra("fieldMaxLen", 20);

        mPageTitle.setText(fieldTitle);
        mEditText.addTextChangedListener(this);
        mEditText.setText(getUserInfo());
        mLenHint.setText(String.format(Locale.CHINA, "%d/%d",
                mEditText.getText().toString().length(), fieldMaxLen));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Editable et = mEditText.getText();
        int len = et.length();
        if (len > fieldMaxLen) {
            int endIndex = Selection.getSelectionEnd(et);
            String oldStr = et.toString();
            String newStr = oldStr.substring(0, fieldMaxLen);
            mEditText.setText(newStr);
            et = mEditText.getText();

            int newLen = et.length();
            if (endIndex > newLen) {
                endIndex = et.length();
            }
            Selection.setSelection(et, endIndex);
            len = fieldMaxLen;
        }
        mLenHint.setText(String.format(Locale.CHINA, "%d/%d", len, fieldMaxLen));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void finish(View view) {
        String value = mEditText.getText().toString();
        HashMap<String, String> params = new HashMap<>();
        params.put(fieldName, value);

        HttpUtil.post(HttpUtil.USER_MODIFY, params, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorHandlingUtil.handleNetworkError(
                        FieldModifyActivity.this, "网络错误，请稍后重试", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 201) {
                    ToastUtil.showToastOnUIThread(FieldModifyActivity.this, "修改成功");
                    updateMyInfo(value);
                }
            }
        });

        Intent it = new Intent();
        it.putExtra("fieldName", fieldName);
        it.putExtra(fieldName, value);
        setResult(RESULT_OK, it);
        finish();
    }

    private String getUserInfo() {
        switch (fieldName) {
            case UserInfoUtil.USERNAME:
                return UserInfoUtil.me.username;
            case UserInfoUtil.SIGNATURE:
                return UserInfoUtil.me.signature;
            case UserInfoUtil.REALNAME:
                return UserInfoUtil.me.realname;
            case UserInfoUtil.DEPARTMENT:
                return UserInfoUtil.me.department;
            case UserInfoUtil.GRADE:
                return UserInfoUtil.me.grade;
            case UserInfoUtil.DORMITORY:
                return UserInfoUtil.me.dormitory;
            case UserInfoUtil.WECHAT:
                return UserInfoUtil.me.wechat;
            case UserInfoUtil.EMAIL:
                return UserInfoUtil.me.email;
            default:
                return "";
        }
    }

    private void updateMyInfo(String value) {
        switch (fieldName) {
            case UserInfoUtil.USERNAME:
                UserInfoUtil.me.username = value;
                break;
            case UserInfoUtil.SIGNATURE:
                UserInfoUtil.me.signature = value;
                break;
            case UserInfoUtil.REALNAME:
                UserInfoUtil.me.realname = value;
                break;
            case UserInfoUtil.DEPARTMENT:
                UserInfoUtil.me.department = value;
                break;
            case UserInfoUtil.GRADE:
                UserInfoUtil.me.grade = value;
                break;
            case UserInfoUtil.DORMITORY:
                UserInfoUtil.me.dormitory = value;
                break;
            case UserInfoUtil.WECHAT:
                UserInfoUtil.me.wechat = value;
                break;
            case UserInfoUtil.EMAIL:
                UserInfoUtil.me.email = value;
                break;
        }
    }
}
