package com.tsinghua.tsinghelper.util;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

import java.io.File;
import java.math.BigDecimal;

public class GlideCacheUtil {
    private static GlideCacheUtil instance = new GlideCacheUtil();
    private Context mContext;

    private GlideCacheUtil() {
    }

    public static GlideCacheUtil getInstance() {
        return instance;
    }

    public static void setContext(Context cxt) {
        getInstance().mContext = cxt;
    }

    // clear image disk cache
    public static void clearImageDiskCache() {
        Context cxt = getInstance().mContext;
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                // currently in main UI thread
                new Thread(() -> Glide.get(cxt).clearDiskCache()).start();
            } else {
                Glide.get(cxt).clearDiskCache();
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
            e.printStackTrace();
        }
    }

    // clear image memory cache
    public static void clearImageMemoryCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                // can only executed in the main UI thread
                Glide.get(getInstance().mContext).clearMemory();
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
            e.printStackTrace();
        }
    }

    // clear all image cache
    public static void clearImageAllCache() {
        clearImageDiskCache();
        clearImageMemoryCache();
        String catchDir = getInstance().mContext.getExternalCacheDir() +
                ExternalPreferredCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
    }

    // get total cached image size
    public static String getCacheSize() {
        GlideCacheUtil instance = getInstance();
        try {
            return getFormattedSize(instance.getFolderSize(new File(
                    instance.mContext.getCacheDir() + "/" +
                            InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        return "";
    }

    public static String getFormattedSize(double size) {
        double kiloBytes = size / 1024;
        if (kiloBytes < 1) {
            return size + "B";
        }

        double megaBytes = kiloBytes / 1024;
        if (megaBytes < 1) {
            BigDecimal res = new BigDecimal(Double.toString(kiloBytes));
            return res.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaBytes = megaBytes / 1024;
        if (gigaBytes < 1) {
            BigDecimal res = new BigDecimal(Double.toString(megaBytes));
            return res.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaBytes / 1024;
        if (teraBytes < 1) {
            BigDecimal res = new BigDecimal(Double.toString(gigaBytes));
            return res.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }

        BigDecimal res = new BigDecimal(teraBytes);
        return res.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    private long getFolderSize(File file) {
        long size = 0;
        try {
            File[] files = file.listFiles();
            assert files != null;
            for (File f : files) {
                if (f.isDirectory()) {
                    size += getFolderSize(f);
                } else {
                    size += f.length();
                }
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
            e.printStackTrace();
        }
        return size;
    }

    private void deleteFolderFiles(String filePath, boolean deleteThisPath) {
        if (filePath.isEmpty()) {
            return;
        }

        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    deleteFolderFiles(f.getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
            e.printStackTrace();
        }
    }
}
