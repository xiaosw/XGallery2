package com.xiaosw.gallery;

import android.app.Application;

/**
 * @ClassName {@link GalleryApplication}
 * @Description
 * @Date 2016-09-10 18:23.
 * @Author xiaoshiwang.
 */
public class GalleryApplication extends Application {
    public static GalleryApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }
}
