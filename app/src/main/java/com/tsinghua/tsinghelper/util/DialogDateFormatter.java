package com.tsinghua.tsinghelper.util;

import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.Date;

public class DialogDateFormatter implements DateFormatter.Formatter {

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return DateFormatter.format(date, DateFormatter.Template.TIME);
        }
        if (DateFormatter.isYesterday(date)) {
            return "昨天";
        }
        if (DateFormatter.isCurrentYear(date)) {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH);
        }
        return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
    }
}
