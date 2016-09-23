package com.xiaosw.gallery.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @ClassName : {@link TipsDialog}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-23 11:11:04
 */
public class TipsDialog extends BaseDialog {

    public TipsDialog(Context context) {
        super(context);
    }

    public TipsDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    void initialize() {
        getCancelView().setVisibility(View.GONE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getOkView().getLayoutParams();
        params.leftMargin = 200;
        params.rightMargin = 200;
    }
}
