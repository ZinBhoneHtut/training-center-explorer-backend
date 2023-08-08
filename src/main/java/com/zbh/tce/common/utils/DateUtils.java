package com.zbh.tce.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private DateUtils() {
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }
}
