package cn.yue.base.common.utils.file;

import android.app.Application;

import androidx.core.content.FileProvider;

import cn.yue.base.common.utils.Utils;


/**
 * <pre>
 *     author: blankj
 *     blog  : http://blankj.com
 *     time  : 2020/03/19
 *     desc  :
 * </pre>
 */
public class UtilsFileProvider extends FileProvider {

    @Override
    public boolean onCreate() {
        //noinspection ConstantConditions
        Utils.init((Application) getContext().getApplicationContext());
        return true;
    }
}
