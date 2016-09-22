package com.xiaosw.gallery.viewer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.BaseRecyclerBean;
import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.widget.adapter.BaseRecyclerAdapter;
import com.xiaosw.gallery.widget.adapter.DateLineTabAdapter;
import com.xiaosw.gallery.widget.listener.OnItemClickListener;
import com.xiaosw.gallery.widget.listener.OnItemLongClickListener;

import java.util.HashMap;

/**
 * @ClassName : {@link DateLineRecyclerView}
 * @Description : 时间轴
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 10:10:43
 */
public class DateLineRecyclerView extends RecyclerView implements OnItemClickListener,
        OnItemLongClickListener {
    private final int mLeftDateTagColor = Color.parseColor("#1F1F1F");
    private GridLayoutManager mHLayoutManager;
    private GridLayoutManager mVLayoutManager;
    private boolean isFirstScroll = true;
    /** 记录横竖屏切换位置 */
    private int mFirstItemIndex = -1;
    private Activity mActivity;
    private int mItemWidth;
    /** 用于控制左侧圆点颜色 */
    private HashMap<Integer, DateTagInfo> mTitleLineViews;
    /** 左侧圆点 */
    private int[] colors;
    /** 标题高度 */
    private int mTitleLineHeight;
    /** 标题垂直中点坐标位置 */
    private int mTitleLineVerticalCenterOffset;
    /** 月份与天数文字高度 */
    private int mMonthAndDayTextHeight;
    /** 年份文字高度 */
    private int mYearTextHeight;
    /** 竖线的起点位置 */
    private int mLineStartY;

    /** 月与天画笔 */
    private Paint mYearPaint;
    /** 月与天画笔 */
    private Paint mMonthAndDayPaint;
    /** 绘制竖线 */
    private Paint mLinePaint;
    /** 绘制小圆 */
    private Paint mCirclePaint;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public DateLineRecyclerView(Context context) {
        this(context, null);
    }

    public DateLineRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateLineRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        init();
    }

    private void init() {
        mTitleLineViews = new HashMap<Integer, DateTagInfo>();
        mLineStartY =  getResources().getDimensionPixelOffset(R.dimen.view_height_date_line_title) / 2;
        initManager();
        initPaint();
        registerListener();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setMotionEventSplittingEnabled(false);
        }
    }

    /**
     * 初始化管理器
     */
    private void initManager() {
        mHLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.num_coloum_date_line_land));
        mHLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mVLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.num_coloum_date_line_port));
        mVLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        // 设置布局管理器
        handleNumColumns(getResources().getConfiguration());
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        colors = new int[]{
                Color.parseColor("#87D113"), // 绿
                Color.parseColor("#EEB700"), // 黄
                Color.parseColor("#94BFFF"), // 蓝
                Color.parseColor("#E95D4C")  // 红
        };
        mTitleLineViews = new HashMap<Integer, DateTagInfo>();
        mTitleLineHeight = getResources().getDimensionPixelOffset(R.dimen.view_height_date_line_title);
        mTitleLineVerticalCenterOffset = mTitleLineHeight / 2;

        // 竖线画笔
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(getResources().getColor(R.color.clr_date_line_circle));
        mLinePaint.setStrokeWidth(4);

        // 圆点画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(colors[0]);

        // 月、日画笔
        mMonthAndDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMonthAndDayPaint.setTextSize(30);
        mMonthAndDayPaint.setColor(mLeftDateTagColor);
        Rect textRect = new Rect();
        mMonthAndDayPaint.getTextBounds("08/08", 0, 5, textRect);
        mMonthAndDayTextHeight = textRect.height();

        // 年画笔
        mYearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mYearPaint.setTextSize(30);
        mYearPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mYearPaint.setColor(mLeftDateTagColor);
        mYearPaint.getTextBounds("8888", 0, 4, textRect);
        mYearTextHeight = textRect.height();
    }

    private void registerListener() {
        setRecyclerListener(new RecyclerListener() {
            @Override
            public void onViewRecycled(ViewHolder holder) {
                if (holder instanceof DateLineTabAdapter.DateLineRecyclerHolder) {
                    DateLineTabAdapter.DateLineRecyclerHolder realHolder = (DateLineTabAdapter.DateLineRecyclerHolder) holder;
                    mTitleLineViews.remove(realHolder.getItemView().getTag());
                }
            }
        });

        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (null == mActivity) {
                    return;
                }
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null == mActivity || isFirstScroll) {
                    isFirstScroll = false;
                    return;
                }
                int requestedOrientation = mActivity.getRequestedOrientation();
                if (ActivityInfo.SCREEN_ORIENTATION_LOCKED != requestedOrientation) {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                }
            }

        });
    }

    private void handleNumColumns(Configuration newConfig) {
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setLayoutManager(mHLayoutManager);
            setItemWidthByViewWidth(getWidth());
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setLayoutManager(mVLayoutManager);
            setItemWidthByViewWidth(getWidth());
        }
        Adapter adapter = getAdapter();
        if (null != adapter && adapter instanceof DateLineTabAdapter) {
            handleRow((DateLineTabAdapter) adapter);
            if (mFirstItemIndex > getSpanCount()) {
                getLayoutManager().scrollToPosition(mFirstItemIndex);
            }

        }
    }

    public void handleRow(final BaseRecyclerAdapter adapter) {

        ((GridLayoutManager) getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (null != adapter) {
                    BaseRecyclerBean mediaItem = ((BaseRecyclerAdapter<BaseRecyclerBean>) adapter).getData().get(position);
                    if (mediaItem.isTitleLine()) {
                        return getSpanCount();
                    }
                }
                return 1;
            }
        });
    }

    /**
     * 绘制圆点
     */
    public void setDrawCircle(MediaItem mediaItem, View titleLineView) {
        mTitleLineViews.put(mediaItem.getTitlePosition(),
                new DateTagInfo(titleLineView,
                        mediaItem.getYear(),
                        mediaItem.getMonthAndDay(),
                        mediaItem.isNeedDrawYear()));
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        mFirstItemIndex = getLayoutManager().findFirstVisibleItemPosition();
        super.onConfigurationChanged(newConfig);
        mTitleLineViews.clear();
        isFirstScroll = true;
        handleNumColumns(newConfig);
    }

    public int getSpanCount() {
        return ((GridLayoutManager) getLayoutManager()).getSpanCount();
    }

    private int setItemWidthByViewWidth(int width) {
        int spanCount = getSpanCount();
        mItemWidth = (width - getPaddingLeft() - 8 * (spanCount - 1)) / spanCount; // 8:horizontal space is 4dp(8px)
        return mItemWidth;
    }

    public int getItemWidth() {
        return mItemWidth;
    }

    /**
     * 绘制圆点及日期
     * @param canvas
     */
    private void drawCircleAndTitle(Canvas canvas) {
        // 绘制圆
        for(Object key : mTitleLineViews.keySet()){
            DateTagInfo dateTagInfo = mTitleLineViews.get(key);
            View attouchView = dateTagInfo.attouchView;
            if (((int) attouchView.getTag()) != -1) {
                int offsetY = 0;
                int top = attouchView.getTop();
                mCirclePaint.setColor(colors[((int) key) % colors.length]);
                canvas.drawCircle(cx, top + mTitleLineVerticalCenterOffset, radius, mCirclePaint);

                if (dateTagInfo.isDrawYear) {
                    offsetY = top + (mTitleLineHeight + mYearTextHeight) / 2;
                    float startX = (cx - mYearPaint.measureText(dateTagInfo.year)) - 50; // 50为文字距离竖线距离
                    canvas.drawText(dateTagInfo.year, startX, offsetY, mYearPaint);
                    offsetY = mYearTextHeight + 18; // 18为年月纵向距离
                }

                // draw month and day
                float startX = (cx - mMonthAndDayPaint.measureText(dateTagInfo.monthAndDay)) - 50; // 50为文字距离竖线距离
                canvas.drawText(dateTagInfo.monthAndDay,
                        startX,
                        top + (mTitleLineHeight + mMonthAndDayTextHeight) / 2 + offsetY,
                        mMonthAndDayPaint);
            } else {
                mTitleLineViews.remove(key);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.mOnItemLongClickListener = itemLongClickListener;
    }

    public void setOffscreenPageLimit(int pageLimit) {
        getRecycledViewPool().setMaxRecycledViews(0, Math.max(3, pageLimit));
    }

    public void setOffscreenPageLimit(int viewType, int pageLimit) {
        getRecycledViewPool().setMaxRecycledViews(viewType, Math.max(3, pageLimit));
    }

    @Override
    public void onItemClick(View view, int position, boolean isSelecteMode) {
        if (null != mOnItemClickListener) {
            mOnItemClickListener.onItemClick(view, position, isSelecteMode);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        if (null != mOnItemLongClickListener) {
            mOnItemLongClickListener.onItemLongClick(view, position);
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int width = View.MeasureSpec.getSize(widthSpec);
        setItemWidthByViewWidth(width);
    }

    float radius = 11;
    float cx = getPaddingLeft() - getResources().getDimension(R.dimen.padding_right_date_line); // 控制竖线距离，可调试合理距离
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getAdapter() != null && getAdapter().getItemCount() > 0) { // 有数据才绘制
            // 绘制竖线
            canvas.drawLine(cx, mLineStartY, cx, getHeight(), mLinePaint);
            drawCircleAndTitle(canvas);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            if (adapter instanceof DateLineTabAdapter) {
                handleRow((DateLineTabAdapter) adapter);
            }
            if (adapter instanceof BaseRecyclerAdapter) {
                BaseRecyclerAdapter baseRecyclerAdapter = (BaseRecyclerAdapter) adapter;
                baseRecyclerAdapter.setOnItemClickListener(this);
                baseRecyclerAdapter.setOnItemLongClickListener(this);
            }
        }
        super.setAdapter(adapter);
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) super.getLayoutManager();
    }

    class DateTagInfo {
        /** 根据该view计算圆点位置 */
        private View attouchView;
        private String year;
        private String monthAndDay;
        private boolean isDrawYear;

        public DateTagInfo(View attouchView, String year, String monthAndDay, boolean isDrawYear) {
            this.attouchView = attouchView;
            this.year = year;
            this.monthAndDay = monthAndDay;
            this.isDrawYear = isDrawYear;
        }

    }

}
