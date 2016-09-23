package com.xiaosw.gallery.widget.helper;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @ClassName  : {@link ViewRecyler}
 * @Description: 视图复用器
 * <p> 用来将闲置的View记录起来，方便下次直接使用。 该类主要是用在 {@link PagerAdapter} 中
 * <pre>
 *  public Object instantiateItem(ViewGroup container, int position) {
 *      View convertView = mViewPool.pop();
 *      if (convertView == null) {
 *          convertView = inflater.inflate(R.layout.item_autoskip_ad, container, false);
 *      }
 *      ...
 *      container.addView(convertView);
 *      return convertView;
 *  }
 *  
 * public void destroyItem(ViewGroup container, int position, Object object) {
 *      if (object instanceof View) {
 *          container.removeView((View) object);
 *          mViewPool.add((View) object);
 *      }
 * }
 * </pre>
 *
 * @date 2015-10-28下午4:20:32
 * @Author xiaoshiwang <xiaoshiwang@rytong.com>
 */
public class ViewRecyler<T extends View> {

    private ArrayList<WeakReference<T>> recyler;

    public ViewRecyler() {
        recyler = new ArrayList<WeakReference<T>>();
    }

    public ViewRecyler(int capacity) {
        recyler = new ArrayList<WeakReference<T>>(capacity);
    }

    /**
     * 将视图添加到复用器中，以便下次使用
     * @param view
     */
    public void add(T view) {
        recyler.add(new WeakReference<T>(view));
    }

    /**
     * 将复用器中的闲置View取出
     * @return
     */
    public T pop() {
        return recyler.isEmpty() ? null : recyler.remove(0).get();
    }

}