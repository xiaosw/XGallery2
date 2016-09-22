package com.xiaosw.gallery.util;

import com.xiaosw.gallery.config.AppConfig;

import java.text.SimpleDateFormat;

/**
 * @ClassName : {@link CommonUtil}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 15:15:22
 */
public class CommonUtil {
    public static SimpleDateFormat FORMAT_YEAR = new SimpleDateFormat(AppConfig.DATE_FRMAT_YEAR);
    public static SimpleDateFormat FORMAT_MONTH_DAY = new SimpleDateFormat(AppConfig.DATE_FRMAT_MONTH_DAY);
    public static SimpleDateFormat FORMAT_YEAR_MONTH_DAY = new SimpleDateFormat(AppConfig.DATE_FRMAT_YEAR_MONTH_DAY);

    public static String getYear(long date) {
        return FORMAT_YEAR.format(date);
    }

    public static String getMonthAndDay(long date) {
        return FORMAT_MONTH_DAY.format(date);
    }

    public static String getYearAndMonthAndDay(long date) {
        return FORMAT_YEAR_MONTH_DAY.format(date);
    }

}
