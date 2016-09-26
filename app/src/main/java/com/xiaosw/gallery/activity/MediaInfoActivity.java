package com.xiaosw.gallery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.util.CommonUtil;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName {@link MediaInfoActivity}
 * @Description 内容信息
 *
 * @Data 2016-09-23 08:08
 * @Auth xiaosw0802@163.com
 */
public class MediaInfoActivity extends Activity {

    public static final String KEY_MEDIA_ITEM = "KEY_MEDIA_ITEM";

    @Bind(R.id.tv_path)
    TextView tv_path;

    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.tv_size)
    TextView tv_size;

    @Bind(R.id.tv_memory_size)
    TextView tv_memory_size;

    @Bind(R.id.tv_dir)
    TextView tv_dir;

    @Bind(R.id.tv_mime_type)
    TextView tv_mime_type;

    @Bind(R.id.tv_date_taken)
    TextView tv_date_taken;

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_info);
        ButterKnife.bind(this);
        tv_title.setText(R.string.str_media_detail);
        iv_back.setVisibility(View.GONE);
        MediaItem mediaItem = (MediaItem) getIntent().getSerializableExtra(KEY_MEDIA_ITEM);
        tv_path.setText(mediaItem.getData());
        tv_name.setText(Html.fromHtml(String.format(getString(R.string.str_media_name), mediaItem.getDisplayName())));
        tv_size.setText(Html.fromHtml(String.format(getString(R.string.str_media_size), mediaItem.getWidth() + " * " + mediaItem.getHeight())));
        tv_memory_size.setText(Html.fromHtml(String.format(getString(R.string.str_media_memory_size), formatMemory(mediaItem.getSize()))));
        tv_dir.setText(Html.fromHtml(String.format(getString(R.string.str_media_buckete_display_name), mediaItem.getBucketDisplayName())));
        tv_mime_type.setText(Html.fromHtml(String.format(getString(R.string.str_media_mime_type), mediaItem.getMimeType())));
        tv_date_taken.setText(Html.fromHtml(String.format(getString(R.string.str_media_date_taken), CommonUtil.getAllDate(mediaItem.getDateTaken()))));
    }

    /**
     *
     * @param memory byte
     * @return
     */
    private String formatMemory(int memory) {
        DecimalFormat df = new java.text.DecimalFormat("#.00");
        double temp = memory / 1024; // kb
        if (temp < 1024) {
            return df.format(temp) + "KB";
        } else if (temp < (1024 * 1024)) {
            return df.format(temp / 1024) + "MB";
        }
        return memory + "Byte";
    }

}
