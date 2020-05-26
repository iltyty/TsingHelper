package com.tsinghua.tsinghelper.ui.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tsinghua.tsinghelper.R;
import com.tsinghua.tsinghelper.adapters.GridImageAdapter;
import com.tsinghua.tsinghelper.engines.GlideEngine;
import com.tsinghua.tsinghelper.managers.ImageGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Registered")
public class BaseTaskActivity extends AppCompatActivity {
    protected int maxSelectNum = 4;
    protected Toolbar mToolbar;
    protected ScrollView mScrollView;
    protected RecyclerView mRecyclerView;
    protected GridImageAdapter mAdapter;
    protected List<LocalMedia> mSelectList = new ArrayList<>();

    protected GridImageAdapter.onAddPicClickListener onAddPicClickListener = this::showGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setFocusChangeListeners() {
        mScrollView.getViewTreeObserver().addOnGlobalFocusChangeListener(
                (oldFocus, newFocus) -> {
                    mScrollView.smoothScrollTo(0, newFocus.getBottom());
                    mScrollView.post(newFocus::requestFocus);
                });
    }

    protected void initWidgets(Activity activity) {
        // find widgets
        mToolbar = findViewById(R.id.toolbar);
        mScrollView = findViewById(R.id.scroll_view);
        mRecyclerView = findViewById(R.id.recycler_view);

        setFocusChangeListeners();

        // initiate toolbar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        ImageGridLayoutManager manager = new ImageGridLayoutManager(
                this, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new GridImageAdapter(activity, onAddPicClickListener, mSelectList);
        mAdapter.setData(mSelectList);
        mAdapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new GridImageAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                if (mSelectList.size() > 0) {
                    LocalMedia media = mSelectList.get(pos);
                    String pictureType = media.getMimeType();
                    int mediaType = PictureMimeType.getMimeType(pictureType);
                    if (mediaType == 1) { // image preview
                        PictureSelector.create(activity).externalPicturePreview(
                                pos, mSelectList, AnimationType.DEFAULT_ANIMATION);
                    }
                }
            }
        });
    }

    protected void showGallery(Activity activity) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(maxSelectNum)
                .minSelectNum(0)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .isPreviewImage(true)
                .isZoomAnim(true)
                .isEnableCrop(true)
                .isCamera(false)
                .isCompress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            images = PictureSelector.obtainMultipleResult(data);
            mSelectList.addAll(images);
            mAdapter.setData(mSelectList);
            mAdapter.notifyDataSetChanged();
        }
    }
}
