package com.tsinghua.tsinghelper.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月dd日 HH:mm");

    public static long calculateEndTimestamp(long startTimeStamp, int amount, TemporalUnit unit) {
        long interval = amount * 1000;
        switch (unit) {
            case SECOND:
                break;
            case MINUTE:
                interval *= 60;
                break;
            case HOUR:
                interval *= 3600;
                break;
            case DAY:
                interval *= 86400;
                break;
            case MONTH:
                interval *= 259200;
                break;
        }
        return startTimeStamp + interval;
    }

    public static String getDeadlineStr(long endTimestamp) {
        long delta = endTimestamp - new Date().getTime();
        if (delta <= 0) {
            return "已截止";
        }

        long days = delta / 86400000;
        long hours = (delta % 86400000) / 3600000;
        long minutes = (delta % 86400000 % 3600000) / 60000;

        if (days > 0) {
            return days + "天后截止";
        }
        if (hours > 0) {
            return hours + "小时后截止";
        }
        return minutes + "分钟后截止";
    }

    public static int getDuration(String startTime, String endTime) {
        long start = Long.parseLong(startTime);
        long end = Long.parseLong(endTime);
        long delta = end - start;
        return (int) (delta / (1000 * 3600 * 24));
    }

    public enum TemporalUnit {
        SECOND, MINUTE, HOUR, DAY, MONTH
    }
}
