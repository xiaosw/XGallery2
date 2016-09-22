package com.xiaosw.gallery.activity.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.xiaosw.gallery.R;

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

    void switchFragment(Fragment fragment) {
        mActivity.getSupportFragmentManager()
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right)
            .replace(R.id.content, fragment, fragment.getClass().getCanonicalName())
            .addToBackStack(null)
            .commit();
    }
}
