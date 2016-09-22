package com.xiaosw.gallery.activity.fragment;

import android.view.View;
import android.widget.TextView;

import com.xiaosw.gallery.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @ClassName : {@link ContainerHeaderFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-22 20:20:32
 */
public abstract class ContainerHeaderFragment<T> extends MediaDataObserverFragment<T> {

    public static final String KEY_TITLE = "title";

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_function)
    TextView tv_function; // 右侧功能

    @Bind(R.id.view_header_container)
    View view_header_container; // 头

    void setTitle(String title) {
        tv_title.setText(title);
    }

    void setTitle(CharSequence title) {
        tv_title.setText(title);
    }

    void setTitle(int resId) {
        tv_title.setText(resId);
    }

    void setFunctionDescription(String description) {
        tv_function.setText(description);
    }

    void setFunctionDescription(int resId) {
        tv_function.setText(resId);
    }

    @OnClick({R.id.iv_back, R.id.tv_function})
    void doOperation(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;

            case R.id.tv_function:
                doFunctionOperation();
                break;
        }
    }

    void doFunctionOperation() {

    }
}
