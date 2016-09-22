package com.xiaosw.gallery.viewer;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.util.LogUtil;
import com.xiaosw.gallery.widget.adapter.BaseRecyclerAdapter;
import com.xiaosw.gallery.widget.listener.OnItemClickListener;
import com.xiaosw.gallery.widget.listener.OnItemLongClickListener;

/**
 * @ClassName : {@link BaseRecyclerView}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-22 15:15:19
 */
public abstract class BaseRecyclerView extends RecyclerView implements OnItemClickListener,
    OnItemLongClickListener {

    private static final String TAG = "BaseRecyclerView";

    int mPortNumColumns;
    int mLandNumColumns;
    LinearLayoutManager mLayoutManager;
    private ItemSize mItemSize;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (null != attrs) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SuperRecyclervView);
            try {
                mLandNumColumns = ta.getInt(R.styleable.SuperRecyclervView_landNumColumns, 0);
                mPortNumColumns = ta.getInt(R.styleable.SuperRecyclervView_portNumColumns, 0);
            } catch (Exception e) {
                LogUtil.e(TAG, "BaseRecyclerView", e);
            } finally {
                if (null != ta) {
                    ta.recycle();
                }
            }
        }
        mItemSize = new ItemSize();
        initialize(context);
    }

    void initialize(Context context) {

    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout instanceof LinearLayoutManager) {
            this.mLayoutManager = (LinearLayoutManager) layout;
        }
        handleNumColumns(getResources().getConfiguration());
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        handleNumColumns(newConfig);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            if (adapter instanceof BaseRecyclerAdapter) {
                BaseRecyclerAdapter baseRecyclerAdapter = (BaseRecyclerAdapter) adapter;
                baseRecyclerAdapter.setOnItemClickListener(this);
                baseRecyclerAdapter.setOnItemLongClickListener(this);
            }
        }
        super.setAdapter(adapter);
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
        int height = View.MeasureSpec.getSize(heightSpec);
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            updateItemSize((GridLayoutManager) layoutManager, getSpanCount(), width, height);
        }

    }

    public void handleNumColumns() {
        handleNumColumns(getResources().getConfiguration());
    }

    private void handleNumColumns(Configuration configuration) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int numColums = 1;
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                numColums = mLandNumColumns;
            } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                numColums = mPortNumColumns;
            }
            if (getSpanCount() != numColums) {
                gridLayoutManager.setSpanCount(numColums);
            }
            updateItemSize(gridLayoutManager, numColums, getWidth(), getHeight());
        }
    }

    private void updateItemSize(GridLayoutManager gridLayoutManager, double numColums, int width, int height) {
        int recyclerViewDirection = gridLayoutManager.getOrientation();
        int size = 0;
        if (recyclerViewDirection == GridLayoutManager.HORIZONTAL) {
            size = (int) (Math.ceil(height / numColums));
        } else if (recyclerViewDirection == GridLayoutManager.VERTICAL) {
            size = (int) (Math.ceil(width / numColums));
        }
        if (mItemSize.width != size
            || mItemSize.height != size) {
            mItemSize.width = size;
            mItemSize.height = size;
            Adapter adapter = getAdapter();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * {@link android.support.v7.widget.RecyclerView.LayoutManager} must is {@link GridLayoutManager}
     * @return {@link GridLayoutManager#getSpanCount()} or 1
     */
    public int getSpanCount() {
        if (getLayoutManager() instanceof GridLayoutManager) {
            return ((GridLayoutManager) getLayoutManager()).getSpanCount();
        }
        return 1;
    }

    public ItemSize getItemSize() {
        return mItemSize;
    }

    public void setPortNumColumns(int portNumColumns) {
        this.mPortNumColumns = portNumColumns;
    }

    public void setLandNumColumns(int landNumColumns) {
        this.mLandNumColumns = landNumColumns;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.mOnItemLongClickListener = itemLongClickListener;
    }

    public class ItemSize {
        public int width;
        public int height;
    }
}
