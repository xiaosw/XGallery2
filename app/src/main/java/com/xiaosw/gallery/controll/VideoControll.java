package com.xiaosw.gallery.controll;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @ClassName : {@link VideoControll}
 * @Description: 视频播放控制器
 *
 * @Date 2016-09-24 13:16
 * @Author xiaosw <xiaosw0802@163.com>
 */
public class VideoControll extends BaseMediaControll {

    private static final String TAG = "VideoControll";

    public VideoControll(Context context, final Uri uri, SurfaceView surfaceView) {
        super(context);
        mSurfaceView = surfaceView;
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                setMediaURI(uri);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }
}
