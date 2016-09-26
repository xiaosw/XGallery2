package com.xiaosw.gallery.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.xiaosw.gallery.GalleryApplication;
import com.xiaosw.gallery.R;
import com.xiaosw.gallery.controll.BaseMediaControll;
import com.xiaosw.gallery.controll.VideoControll;
import com.xiaosw.gallery.util.ScreenUtil;
import com.xiaosw.gallery.viewer.VideoSurfaceView;

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
public class MovieActivity extends BaseActivity implements BaseMediaControll.OnProgressInfoUpdateListener,
    MediaPlayer.OnCompletionListener, VideoSurfaceView.VideoSurfaceViewGestureListener {

    private static final String TAG = "MovieActivity";

    /** 播放URI */
    private static final String KEY_PLAY_URI = "PLAY_URI";

    /** 播放URI */
    private static final String KEY_PLAY_PATH = "PLAY_PATH";
    /** 无操作时，5s隐藏操作选项 */
    public static final int HIDE_BAR_DURATION = 5000;

    private VideoControll mVideoControll;

    /** 绘制视频 */
    @Bind(R.id.surface_view)
    VideoSurfaceView mSurfaceView;

    /** 屏幕中间 播放、暂停 */
    @Bind(R.id.iv_center_play)
    ImageView iv_center_play;

    /** 左下角播放 */
    @Bind(R.id.iv_play)
    ImageView iv_play;


    /** 当前播放进度 */
    @Bind(R.id.seek_bar_movie_progress)
    SeekBar seek_bar_movie_progress;

    @Bind(R.id.view_navigation_span)
    View view_navigation_span;

    /** 头部控制模块容器 */
    @Bind(R.id.view_header_container)
    View view_header_container;
    /** 底部控制模块容器 */
    @Bind(R.id.view_controll)
    View view_bottom_controll;

    @Bind(R.id.iv_back)
    ImageView iv_back;

    @Bind(R.id.tv_total_time)
    TextView tv_total_time;

    @Bind(R.id.tv_current_time)
    TextView tv_current_time;

    private Uri mUri;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenUtil.setFullScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        mHandler = GalleryApplication.mApp.getHandler();
        ButterKnife.bind(this);
        mUri = getIntent().getData();
        initView();
        mVideoControll = new VideoControll(this, mUri, mSurfaceView);
        mVideoControll.setOnProgressInfoUpdateListener(this);
        mVideoControll.setOnCompletionListener(this);
        mHandler.postDelayed(mHideBarTask, HIDE_BAR_DURATION);
    }

    private void initView() {
        ViewGroup.LayoutParams params = view_navigation_span.getLayoutParams();
        params.height = getIntent().getIntExtra(MainActivity.KEY_NAVIGATION_HEIGHT, 0);
        view_header_container.setBackgroundResource(R.mipmap.bg_top_bar);
        iv_back.setImageResource(R.mipmap.ic_back_white);
        mSurfaceView.setVideoSurfaceViewGestureListener(this);
        seek_bar_movie_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            boolean isTouch;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isTouch) {
                    mVideoControll.seekTo(progress);
                    tv_current_time.setText(mVideoControll.stringForTime(progress));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;
                mHandler.removeCallbacks(mHideBarTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouch = false;
                mHandler.postDelayed(mHideBarTask, HIDE_BAR_DURATION);
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        seek_bar_movie_progress.setMax(mediaPlayer.getDuration());
        tv_total_time.setText(mVideoControll.stringForTime(mediaPlayer.getDuration()));
    }

    @Override
    public void onProgressUpdate(int total, int current) {
        seek_bar_movie_progress.setProgress(current);
        if (current < total) {
            current = (int) Math.ceil(current / 1000d) * 1000;
        }
        tv_current_time.setText(mVideoControll.stringForTime(current));
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    @Override
    protected void onPause() {
        mVideoControll.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ScreenUtil.cancelFullScreen(this);
        mVideoControll.onStop();
        super.onDestroy();
    }

    @Override
    public boolean onDown() {
        mHandler.removeCallbacks(mHideBarTask);
        return true;
    }

    @Override
    public boolean onSurfaceViewClick(MotionEvent e) {
        toggleAnim();
        return true;
    }

    @Override
    public boolean onSurfaceViewScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onUp() {
        mHandler.postDelayed(mHideBarTask, HIDE_BAR_DURATION);
    }

    @OnClick({R.id.iv_back, R.id.iv_play, R.id.iv_center_play})
    void doClick(View view) {
        mHandler.removeCallbacks(mHideBarTask);
        switch (view.getId()) {
            case R.id.iv_play:
            case R.id.iv_center_play:
                mVideoControll.toggle();
                if (mVideoControll.isPlaying()) {
                    iv_play.setImageResource(R.mipmap.quantum_ic_pause_circle_fill_white_24);
                    iv_center_play.setVisibility(View.GONE);
                } else {
                    iv_play.setImageResource(R.mipmap.quantum_ic_play_circle_fill_white_24);
                    iv_center_play.setVisibility(View.VISIBLE);
                }
                mVideoControll.setResumeNeededPlay(mVideoControll.isPlaying());
                break;

            case R.id.iv_back:
                finish();
                break;

            default:
                // do nothing

        }
        if (mVideoControll.isPlaying()) {
            mHandler.postDelayed(mHideBarTask, HIDE_BAR_DURATION);
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 动效处理
    ///////////////////////////////////////////////////////////////////////////
    private volatile boolean isShowing = true;
    private ValueAnimator mValueAnimator;
    private float lastTo;

    public void toggleAnim() {
        if (isShowing) {
            hide();
        } else {
            show();
        }
    }

    private void show() {
        isShowing = true;
        startAnimator(0f, 1f, new DecelerateInterpolator(), 300);
    }

    private void hide() {
        isShowing = false;
        startAnimator(1f, 0f, new AccelerateInterpolator(), 300);
    }

    private void startAnimator(float from, final float to, Interpolator interpolator, long duration) {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.setFloatValues(lastTo);
            mValueAnimator.end();
            view_bottom_controll.clearAnimation();
            view_header_container.clearAnimation();
        }
        lastTo = to;
        mValueAnimator = ValueAnimator.ofFloat(from, to);
        mValueAnimator.setDuration(duration);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                ViewHelper.setTranslationY(view_header_container, (int) (view_header_container.getHeight() * (value - 1.0f)));
                ViewHelper.setTranslationY(view_bottom_controll, (int) (view_bottom_controll.getHeight() * (1.0f - value)));
            }
        });
        mValueAnimator.start();
    }

    private Runnable mHideBarTask = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
}
