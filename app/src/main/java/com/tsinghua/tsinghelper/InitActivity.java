package com.tsinghua.tsinghelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tsinghua.tsinghelper.ui.login.LoginActivity;
import com.tsinghua.tsinghelper.util.LoginUtil;
import com.tsinghua.tsinghelper.util.UserInfoUtil;

// This activity is the main activity of the app.
// It is used to check whether the user has logged in or not.
// If logged in, the user is directed to the MainActivity.
// Otherwise, the user is forced to register(if necessary) and login.
public class InitActivity extends AppCompatActivity {

    private final String USER_INFO_FILENAME = "userinfo";

    private SharedPreferences mSharedPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        UserInfoUtil.setSharedPreferences(getSharedPreferences(USER_INFO_FILENAME, MODE_PRIVATE));

        if (LoginUtil.isLoggedIn(this)) {
            // redirect to the main page of the app
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        } else {
            // force the user to register and login
            Intent it = new Intent(this, LoginActivity.class);
            startActivity(it);
        }
        finish();
    }

}
