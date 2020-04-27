package com.tsinghua.tsinghelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.next.easynavigation.view.EasyNavigationBar;
import com.tsinghua.tsinghelper.ui.home.HomeFragment;
import com.tsinghua.tsinghelper.ui.messages.MessagesFragment;
import com.tsinghua.tsinghelper.ui.mine.MineFragment;
import com.tsinghua.tsinghelper.ui.task.TaskFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // text of bottom tab bar item
    private String[] mTabText;
    // icon when not selected
    private int[] mNormalIcon = { R.drawable.ic_home_24dp, R.drawable.ic_task_24dp,
            R.drawable.ic_add_24dp, R.drawable.ic_message_24dp, R.drawable.ic_mine_24dp };
    // icon when selected
    private int[] mSelectIcon = { R.drawable.ic_home_active_24dp, R.drawable.ic_task_active_24dp,
            R.drawable.ic_add_24dp, R.drawable.ic_message_active_24dp,
            R.drawable.ic_mine_active_24dp };
    // bottom navigation bar
    EasyNavigationBar mNavigationBar;
    // all fragments
    List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationBar = findViewById(R.id.navigation_bar);

        mTabText = new String[]{ getString(R.string.title_home), getString(R.string.title_task),
                "", getString(R.string.title_messages), getString(R.string.title_mine)};

        mFragments.add(new HomeFragment());
        mFragments.add(new TaskFragment());
        mFragments.add(new MessagesFragment());
        mFragments.add(new MineFragment());

        mNavigationBar.titleItems(mTabText)
                .normalIconItems(mNormalIcon)
                .selectIconItems(mSelectIcon)
                .fragmentList(mFragments)
                .mode(EasyNavigationBar.MODE_ADD)
                .fragmentManager(getSupportFragmentManager())
                .build();
    }
}
