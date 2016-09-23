package com.xiaosw.gallery.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Toast;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.activity.fragment.MainFragment;
import com.xiaosw.gallery.util.LogUtil;
import com.xiaosw.gallery.util.MPermissionCompat;
import com.xiaosw.gallery.util.MediaCursorHelper;
import com.xiaosw.gallery.util.PTToast;
import com.xiaosw.gallery.widget.dialog.BaseDialog;
import com.xiaosw.gallery.widget.dialog.TipsDialog;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MPermissionCompat.OnRequstPermissionListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    /** 请求存储权限 */
    public static final int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1001;
    /** 预览action */
    public static final String ACTION_REVIEW = "com.android.camera.action.REVIEW";
    /** 正常启动 */
    public static final int ACTION_TYPE_DEFAULT = 0;
    /** 三方应用选择项目*/
    public static final int ACTION_TYPE_SELECT = 1;
    /** 预览 */
    public static final int ACTION_TYPE_REVIEW = 2;
    private int mActionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MPermissionCompat.requestPermissionsCompat(this,
                PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                this);

        initializeByIntent();
    }

    private void initializeByIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_PICK.equalsIgnoreCase(action)
            || Intent.ACTION_GET_CONTENT.equalsIgnoreCase(action)) {
            // select photo
            mActionType = ACTION_TYPE_SELECT;
        } else if (Intent.ACTION_VIEW.equalsIgnoreCase(action)
            || ACTION_REVIEW.equalsIgnoreCase(action)) {
            // review
            mActionType = ACTION_TYPE_REVIEW;
        } else {
            // default
            mActionType = ACTION_TYPE_DEFAULT;
        }

    }

    public int getActionType() {
        return mActionType;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 权限处理部分 begin
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onAlert(ArrayList<String> shouldShowRequestPermissions) {
        TipsDialog tipsDialog = new TipsDialog(this);
        tipsDialog.setTitle(R.string.str_tips_auth_permission_error);
        tipsDialog.setContentStr(R.string.str_tips_auth_permission_error_read_storage);
        tipsDialog.setCancelable(false);
        tipsDialog.setOnClickOperationListener(new BaseDialog.OnClickOperationListener() {
            @Override
            public void onOk(View view) {
                finish();
            }

            @Override
            public void onCancel(View view) {

            }
        });
        tipsDialog.show();
    }

    @Override
    public void onHaveAuthorized() {
        initMain();
    }

    @Override
    public void onNotNeedToApplyDynamic() {
        initMain();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMain();
                } else {
                    PTToast.makeText(this, android.R.string.selectTextMode, Toast.LENGTH_SHORT);
                    finish();
                }
                break;

            default:
                // do nothing

        }
    }

    private boolean isInitialized;
    private synchronized void initMain() {
        if (isInitialized) {
            return;
        }
        getSupportLoaderManager().initLoader(new Random().nextInt(Integer.MAX_VALUE), null, this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new MainFragment(), MainFragment.class.getSimpleName())
                .commit();
        isInitialized = true;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 权限处理部分 end
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // 数据加载 begin
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader loader = new CursorLoader(this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaCursorHelper.IMAGE_PROJECTION,
                MediaCursorHelper.FILTER_ALL_PHOTO_WHERE,
                MediaCursorHelper.FILTER_ALL_PHOTO_ARGS,
                MediaCursorHelper.IMAGE_ORDER_CLAUSE_DESC);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        LogUtil.e("-------------> onLoadFinished()");
        Cursor fodlerCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.BUCKET_ID,
                        "COUNT (".concat(MediaStore.Images.ImageColumns._ID).concat(") as totalSize ")},
                "1 == 1 ) GROUP BY ( ".concat(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME),
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN.concat(" DESC"));
        MediaCursorHelper.parseFolderCursor(this, fodlerCursor);

        MediaCursorHelper.parseImageCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    ///////////////////////////////////////////////////////////////////////////
    // 数据加载 end
    ///////////////////////////////////////////////////////////////////////////
}
