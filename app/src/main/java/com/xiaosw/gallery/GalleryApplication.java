package com.xiaosw.gallery;

import android.app.Application;
import android.os.Handler;

/**
 * @ClassName {@link GalleryApplication}
 * @Description
 * @Date 2016-09-10 18:23.
 * @Author xiaoshiwang.
 */
public class GalleryApplication extends Application {
    public static GalleryApplication mApp;

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mHandler = new Handler();
    }

    public Handler getHandler() {
        return mHandler;
    }

    /**
     * Runs the specified action on the UI thread. If the current thread is the UI
     * thread, then the action is executed immediately. If the current thread is
     * not the UI thread, the action is posted to the event queue of the UI thread.
     *
     * @param action the action to run on the UI thread
     */
    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread().getId() != 1) {
            mHandler.post(action);
        } else {
            action.run();
        }
    }
}
