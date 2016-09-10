package com.xiaosw.gallery.viewer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.GridView;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.util.LogUtil;
import com.xiaosw.gallery.util.ScreenUtil;

/**
 * @ClassName {@link SupportGridView}
 * @Description
 * @Date 2016-09-10 16:03.
 * @Author xiaoshiwang.
 */
public class SupportGridView extends GridView {

    private int mRealNumColumns;

    public SupportGridView(Context context) {
        this(context, null);
    }

    private int mRealScreenWidth;

    public SupportGridView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.gridViewStyle);
    }

    public SupportGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(21)
    public SupportGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        onConfigurationChanged(getResources().getConfiguration());
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Resources res = getResources();
        int land = res.getInteger(R.integer.num_coloum_album_land);
        int port = res.getInteger(R.integer.num_coloum_album_port);
        if (mRealNumColumns > 0 && (mRealNumColumns != land && mRealNumColumns != port)) { // 自定义的列数
            return;
        }
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRealNumColumns = land;
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRealNumColumns = port;
        }
        mRealScreenWidth = ScreenUtil.getScreenWH(getContext())[0];
        setNumColumns(mRealNumColumns);
    }

    public int getRealNumColumns() {
        return mRealNumColumns;
    }

    public int getRealScreenWidth() {
        return mRealScreenWidth;
    }

}
