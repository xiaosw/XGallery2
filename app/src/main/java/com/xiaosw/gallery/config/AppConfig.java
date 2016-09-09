package com.xiaosw.gallery.config;

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

	/** 年月日格式 */
	public static final String DATE_FRMAT_YEAR_MONTH_DAY = "yyyy/MM/dd";
	/** 月日格式 */
	public static final String DATE_FRMAT_MONTH_DAY = "MM/dd";
	/** 年格式 */
	public static final String DATE_FRMAT_YEAR = "yyyy";

	/** 按钮仿重复点击间隔时间 */
	public static final int DURATION_CLICK_GAP = 320;


	///////////////////////////////////////////////////////////////////////////
	// Glide相关
	///////////////////////////////////////////////////////////////////////////
	/** 加载本地文件前缀 */
	public static final String GLIDE_NATIVE_PREFIX = "file://";
}
