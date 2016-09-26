package com.xiaosw.gallery.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.controll.VideoControll;
import com.xiaosw.gallery.util.ScreenUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @ClassName {@link MainActivity}
 * @Description 视频播放
 *
 * @Data 2016-09-23 08:08
 * @Auth xiaosw0802@163.com
 */
public class MovieActivity extends BaseActivity {

    private static final String TAG = "MovieActivity";

    /** 播放URI */
    private static final String KEY_PLAY_URI = "PLAY_URI";

    /** 播放URI */
    private static final String KEY_PLAY_PATH = "PLAY_PATH";

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

    @Bind(R.id.view_navigation_span)
    View view_navigation_span;

    @Bind(R.id.view_header_container)
    View view_header_container;

    @Bind(R.id.iv_back)
    ImageView iv_back;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenUtil.setFullScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        initView();
        mVideoControll = new VideoControll(this, mSurfaceView);
        mUri = getIntent().getData();
    }

    private void initView() {
        ViewGroup.LayoutParams params = view_navigation_span.getLayoutParams();
        params.height = getIntent().getIntExtra(MainActivity.KEY_NAVIGATION_HEIGHT, 0);
        view_header_container.setBackgroundResource(R.mipmap.bg_top_bar);
        iv_back.setImageResource(R.mipmap.ic_back_white);

        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mVideoControll.setVideoURI(mUri);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoControll.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoControll.onPause();
    }

    @Override
    protected void onDestroy() {
        ScreenUtil.cancelFullScreen(this);
        super.onDestroy();
        mVideoControll.onStop();
    }

    @OnClick({R.id.iv_back, R.id.iv_play, R.id.iv_center_play})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                mVideoControll.toggle();
                if (mVideoControll.isPlaying()) {
                    iv_center_play.setVisibility(View.GONE);
                } else {
                    iv_center_play.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.iv_center_play:
                mVideoControll.onStart();
                view.setVisibility(View.GONE);
                break;

            case R.id.iv_back:
                finish();
                break;

            default:
                // do nothing

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
