package com.xiaosw.gallery.activity.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * @ClassName : {@link BaseFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 11:11:51
 */
public class BaseFragment extends Fragment {

    FragmentActivity mActivity;
    View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }
}
