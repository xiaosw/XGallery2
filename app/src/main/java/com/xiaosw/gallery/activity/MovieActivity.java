package com.xiaosw.gallery.activity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.controll.VideoControll;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName {@link MainActivity}
 * @Description 视频播放
 *
 * @Data 2016-09-23 08:08
 * @Auth xiaosw0802@163.com
 */
public class MovieActivity extends BaseActivity {

    private VideoControll mVideoControll;

    /** 绘制视频 */
    @Bind(R.id.surface_view)
    SurfaceView mSurfaceView;

    /** 屏幕中间 播放、暂停 */
    @Bind(R.id.iv_center_play)
    ImageView iv_center_play;

    /** 左下角播放 */
    @Bind(R.id.iv_play)
    ImageView iv_play;

    /** 底部控制模块容器 */
    @Bind(R.id.view_controll)
    View view_controll;

    /** 当前播放进度 */
    @Bind(R.id.seek_bar_movie_progress)
    SeekBar seek_bar_movie_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        mVideoControll = new VideoControll(this);
    }

    
}
