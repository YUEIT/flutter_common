package cn.yue.base.common.utils.app;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import cn.yue.base.common.utils.Utils;

/**
 * Description : 分辨率转换
 * Created by yue on 2019/3/11
 */

public class DisplayUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        if (null == Utils.getContext()) return (int) dpValue;
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        if (null == Utils.getContext()) return (int) pxValue;
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px（正常字体下，1sp=1dp）
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        if (null == Utils.getContext()) return (int) spValue;
        final float fontScale = Utils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * sp转dp
     * @param spValue
     * @return
     */
    public static int sp2dp(float spValue) {
        int sp2Px = sp2px(spValue);
        return px2dip(sp2Px);
    }


    public static int getQToPx(int rid){
        return (int) Utils.getContext().getResources().getDimension(rid);
    }

    public static Drawable getDrawable(int drawable){
        return ContextCompat.getDrawable(Utils.getContext(), drawable);
    }
}
