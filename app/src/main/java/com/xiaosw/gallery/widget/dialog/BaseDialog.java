package com.xiaosw.gallery.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.controll.AlertWindowControll;
import com.xiaosw.gallery.util.ScreenUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @ClassName : {@link BaseDialog}
 * @Description : 弹框基类
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-23 10:10:52
 */
public class BaseDialog extends Dialog {

    @Bind(R.id.tv_dialog_title)
    TextView tv_dialog_title; // 标题

    @Bind(R.id.tv_tips_content)
    TextView tv_tips_content; // 提示内容

    @Bind(R.id.bt_ok)
    Button bt_ok; // 确定

    @Bind(R.id.bt_cencel)
    Button bt_cencel; // 取消

    View mContentView;
    private OnClickOperationListener mOnClickOperationListener;

    public BaseDialog(Context context) {
        this(context, R.style.alert_dialog);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        mContentView = View.inflate(getContext(), R.layout.dialog_tips, null);
        setContentView(mContentView);
        ButterKnife.bind(this, mContentView);
        initialize();
    }

    void initialize() {
    }

    public void setOnClickOperationListener(OnClickOperationListener onClickOperationListener) {
        this.mOnClickOperationListener = onClickOperationListener;
    }

    public void setTitle(CharSequence title) {
        tv_dialog_title.setText(title);
    }

    public void setTitle(int resId) {
        tv_dialog_title.setText(resId);
    }

    public void setContentStr(CharSequence title) {
        tv_tips_content.setText(title);
    }

    public void setContentStr(int resId) {
        tv_tips_content.setText(resId);
    }

    public void setOkStr(CharSequence title) {
        bt_ok.setText(title);
    }

    public void setOkStr(int resId) {
        bt_ok.setText(resId);
    }

    public void setCancelStr(CharSequence title) {
        bt_cencel.setText(title);
    }

    public void setCancelStr(int resId) {
        bt_cencel.setText(resId);
    }

    public TextView getTitleView() {
        return tv_dialog_title;
    }

    public TextView getContentView() {
        return tv_tips_content;
    }

    public Button getOkView() {
        return bt_ok;
    }

    public Button getCancelView() {
        return bt_cencel;
    }

    WindowManager.LayoutParams initWindowParams() {
        return initWindowParams((ScreenUtil.getScreenWH(getContext())[0] * 3 / 4), -1);
    }

    WindowManager.LayoutParams initWindowParams(int width, int height) {
        // 设置dialog样式
        Window window = getWindow();
        android.view.WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.gravity = Gravity.CENTER;
        if (width > 0) {
            windowParams.width = width;
        }
        if (height > 0) {
            windowParams.height = height;
        }
        return windowParams;
    }

    @OnClick({R.id.bt_ok, R.id.bt_cencel})
    final void onClick(View view) {
        dismiss();
        if (null == mOnClickOperationListener) {
            return;
        }
        switch (view.getId()) {
            case R.id.bt_ok:
                mOnClickOperationListener.onOk(view);
            break;

            case R.id.bt_cencel:
                mOnClickOperationListener.onCancel(view);
            break;

            default:
                // do nothing
        }
    }

    /**
     * 按钮点击回掉
     */
    public interface OnClickOperationListener {
        /**
         * 确定
         * @param view
         */
        public void onOk(View view);

        /**
         * 取消
         * @param view
         */
        public void onCancel(View view);
    }

    @Override
    public void show() {
        super.show();
        AlertWindowControll.INSTANCE.show(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        AlertWindowControll.INSTANCE.dissmiss(this);
    }
}