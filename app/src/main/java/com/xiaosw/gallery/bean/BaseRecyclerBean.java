package com.xiaosw.gallery.bean;

/**
 * @ClassName : {@link BaseRecyclerBean}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 21:21:59
 */
public class BaseRecyclerBean {

    /** 标记是否为标题行 */
    boolean isTitleLine;

    public boolean isTitleLine() {
        return isTitleLine;
    }

    public void setTitleLine(boolean titleLine) {
        isTitleLine = titleLine;
    }
}
