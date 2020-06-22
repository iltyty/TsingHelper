package com.tsinghua.tsinghelper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tsinghua.tsinghelper.dtos.UserDTO;
import com.tsinghua.tsinghelper.ui.login.LoginActivity;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

// This activity is the main activity of the app.
// It is used to check whether the user has logged in or not.
// If logged in, the user is directed to the MainActivity.
// Otherwise, the user is forced to register(if necessary) and login.
public class InitActivity extends AppCompatActivity {

    private final String USER_INFO_FILENAME = "userinfo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        UserInfoUtil.setSharedPreferences(getSharedPreferences(USER_INFO_FILENAME, MODE_PRIVATE));

        if (UserInfoUtil.isLoggedIn()) {
            // redirect to the main page of the app
            genMe();
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        } else {
            // force the user to register and login
            Intent it = new Intent(this, LoginActivity.class);
            startActivity(it);
        }
        finish();
    }

    private void genMe() {
        UserInfoUtil.me = new UserDTO();
        UserInfoUtil.me.email = UserInfoUtil.getPref(UserInfoUtil.EMAIL, "");
        UserInfoUtil.me.grade = UserInfoUtil.getPref(UserInfoUtil.GRADE, "");
        UserInfoUtil.me.phone = UserInfoUtil.getPref(UserInfoUtil.PHONE, "");
        UserInfoUtil.me.state = UserInfoUtil.getPref(UserInfoUtil.STATE, "online");
        UserInfoUtil.me.wechat = UserInfoUtil.getPref(UserInfoUtil.WECHAT, "");
        UserInfoUtil.me.realname = UserInfoUtil.getPref(UserInfoUtil.REALNAME, "");
        UserInfoUtil.me.username = UserInfoUtil.getPref(UserInfoUtil.USERNAME, "");
        UserInfoUtil.me.dormitory = UserInfoUtil.getPref(UserInfoUtil.DORMITORY, "");
        UserInfoUtil.me.signature = UserInfoUtil.getPref(UserInfoUtil.SIGNATURE, "");
        UserInfoUtil.me.department = UserInfoUtil.getPref(UserInfoUtil.DEPARTMENT, "");
        UserInfoUtil.me.id = Integer.parseInt(UserInfoUtil.getPref(UserInfoUtil.ID, "0"));
    }

}
