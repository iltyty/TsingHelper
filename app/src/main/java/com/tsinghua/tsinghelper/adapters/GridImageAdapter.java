package com.tsinghua.tsinghelper.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.entity.LocalMedia;
import com.tsinghua.tsinghelper.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder> {

    private static final int TYPE_CAMERA = 1;
    private static final int TYPE_PICTURE = 2;

    private Context mContext;
    private int selectMax = 4;
    private List<LocalMedia> mList;
    private LayoutInflater mInflater;
    private onItemClickListener mItemClickListener;
    private onAddPicClickListener mPicClickListener;

    public GridImageAdapter(Context cxt, onAddPicClickListener listener, List<LocalMedia> list) {
        mList = list;
        mContext = cxt;
        mPicClickListener = listener;
        mInflater = LayoutInflater.from(cxt);
    }

    public void setSelectMax(int max) {
        selectMax = max;
    }

    public void setData(List<LocalMedia> list) {
        mList = list;
    }

    public void setItemClickListener(onItemClickListener listener) {
        mItemClickListener = listener;
    }

    private boolean isShowingImage(int pos) {
        return pos == mList.size();
    }

    @Override
    public int getItemCount() {
        int size = mList.size();
        return size < selectMax ? size + 1 : size;
    }

    @Override
    public int getItemViewType(int pos) {
        return isShowingImage(pos) ? TYPE_CAMERA : TYPE_PICTURE;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new ViewHolder(mInflater.inflate(R.layout.component_image, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        if (getItemViewType(pos) == TYPE_CAMERA) {
            viewHolder.mImage.setImageResource(R.drawable.add_img);
            viewHolder.mImage.setOnClickListener(v ->
                    mPicClickListener.onAddPicClick((Activity) mContext));
            viewHolder.mLayout.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mLayout.setVisibility(View.VISIBLE);
            viewHolder.mLayout.setOnClickListener(v -> {
                int idx = viewHolder.getAdapterPosition();
                if (idx != RecyclerView.NO_POSITION) {
                    mList.remove(idx);
                    notifyItemRemoved(idx);
                    notifyItemRangeChanged(idx, mList.size());
                }
            });

            LocalMedia media = mList.get(pos);
            String path;
            if (media.isCut() && !media.isCompressed()) {
                // was cut
                path = media.getCutPath();
            } else if (media.isCompressed()) {
                // was compressed
                path = media.getCompressPath();
            } else {
                // original image
                path = media.getPath();
            }
            if (media.isCompressed()) {
                Log.i("compress image result:", new File(media.getCompressPath()).length() / 1024 + "k");
                Log.i("压缩地址::", media.getCompressPath());
            }
            Log.i("原图地址：", media.getPath());

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.color.new_task_image_bg_color)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(viewHolder.itemView.getContext())
                    .load(path)
                    .apply(options)
                    .into(viewHolder.mImage);

            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(v -> {
                    int pos1 = viewHolder.getAdapterPosition();
                    mItemClickListener.onItemClick(pos1, v);
                });
            }
        }
    }

    public interface onItemClickListener {
        void onItemClick(int pos, View v);
    }

    public interface onAddPicClickListener {
        void onAddPicClick(Activity activity);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        LinearLayout mLayout;

        ViewHolder(View view) {
            super(view);
            mImage = view.findViewById(R.id.image);
            mLayout = view.findViewById(R.id.linear_layout);
        }
    }
}
