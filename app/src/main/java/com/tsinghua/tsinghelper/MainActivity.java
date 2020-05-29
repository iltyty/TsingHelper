package com.tsinghua.tsinghelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.jaeger.library.StatusBarUtil;
import com.next.easynavigation.view.EasyNavigationBar;
import com.tsinghua.tsinghelper.ui.home.HomeFragment;
import com.tsinghua.tsinghelper.ui.messages.MessagesFragment;
import com.tsinghua.tsinghelper.ui.mine.MineFragment;
import com.tsinghua.tsinghelper.ui.task.NewTaskTypeActivity;
import com.tsinghua.tsinghelper.ui.task.TaskFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // text of bottom tab bar item
    private String[] mTabText;
    // icon when not selected
    private int[] mNormalIcon = {R.drawable.ic_home_24dp, R.drawable.ic_task_24dp,
            R.drawable.ic_add_24dp, R.drawable.ic_message_24dp, R.drawable.ic_mine_24dp};
    // icon when selected
    private int[] mSelectIcon = {R.drawable.ic_home_active_24dp, R.drawable.ic_task_active_24dp,
            R.drawable.ic_add_24dp, R.drawable.ic_message_active_24dp,
            R.drawable.ic_mine_active_24dp};
    // bottom navigation bar
    @BindView(R.id.navigation_bar)
    EasyNavigationBar mNavigationBar;
    // all fragments
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarUpperAPI21(true);
//        StatusBarUtil.setTransparent(this);
//        StatusBarUtil.setColor(MainActivity.this, 16777215);

        ButterKnife.bind(this);

        mTabText = new String[]{getString(R.string.title_home), getString(R.string.title_task),
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
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        if (position == 2) {
                            Intent it = new Intent(MainActivity.this, NewTaskTypeActivity.class);
                            startActivityForResult(it, 1);
                            return true;
                        } else if (position == 4) {
//                            StatusBarUtil.setColor(MainActivity.this, 16768570);
                            setStatusBarUpperAPI21(false);
                        } else if (position <= 4) {
//                            StatusBarUtil.setColor(MainActivity.this, 16777215);
                            setStatusBarUpperAPI21(true);
                        }
                        return false;
                    }
                })
                .build();
    }

    private void setStatusBarUpperAPI21(boolean colorPrimary){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(!colorPrimary) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        } else {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
    }
}
