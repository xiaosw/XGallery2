package com.xiaosw.gallery.config;

import java.util.HashMap;

/**
 * @ClassName : {@link AppConfig}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 12:12:02
 */
public class AppConfig {

    /** 标记是否为调试模式 */
    public static final boolean DEBUG = true;

    /** 年月日,时分秒格式 */
    public static final String DATE_FRMAT_YEAR_MONTH_DAY_HOUR_MINUES_SENCONDS = "yyyy/MM/dd HH:mm:ss";
    /** 年月日格式 */
    public static final String DATE_FRMAT_YEAR_MONTH_DAY = "yyyy/MM/dd";
    /** 月日格式 */
    public static final String DATE_FRMAT_MONTH_DAY = "MM/dd";
    /** 年格式 */
    public static final String DATE_FRMAT_YEAR = "yyyy";

    /** 按钮仿重复点击间隔时间 */
    public static final int DURATION_CLICK_GAP = 320;

    /** 主目录（用于区分其它） */
    public static final HashMap<String, Integer> MAIN_MEDIA_FOLDER = new HashMap<String, Integer>();
    static {
        MAIN_MEDIA_FOLDER.put("Camera", 0);
        MAIN_MEDIA_FOLDER.put("Video", 1);
        MAIN_MEDIA_FOLDER.put("Screenshots", 2);
        MAIN_MEDIA_FOLDER.put("Download", 3);
        MAIN_MEDIA_FOLDER.put("download", 4);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Glide相关
    ///////////////////////////////////////////////////////////////////////////
    /** 加载本地文件前缀 */
    public static final String GLIDE_NATIVE_PREFIX = "file://";
}
