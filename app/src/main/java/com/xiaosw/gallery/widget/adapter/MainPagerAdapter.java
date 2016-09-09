package com.xiaosw.gallery.widget.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xiaosw.gallery.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : {@link MainPagerAdapter}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 11:11:43
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
	public final String TAG = "MainPagerAdapter";
	private ArrayList<Fragment> mPages;

	public MainPagerAdapter(FragmentManager fm, ArrayList<Fragment> pages) {
		super(fm);
		this.mPages = pages;
		if (mPages == null) {
			mPages = new ArrayList<>();
		}
	}

	public void addPage(Fragment page) {
		mPages.add(page);
		notifyDataSetChanged();
	}

	public void reload(List<Fragment> pages) throws NullPointerException {
		mPages.clear();
		if (null == pages) {
			throw new NullPointerException("pages must not null!!!");
		}
		mPages.addAll(pages);
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {
		return mPages.get(position);
	}

	@Override
	public int getCount() {
		return mPages.size();
	}
}
