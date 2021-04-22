package cn.yue.base.common.widget.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Description : 自定义键盘帮助类
 * Created by yue on 2018/11/14
 */
public class KeyboardHelp {

    private int mKeyboardHeight;
    private int maxDisplayHeight;
    boolean isVisibleForLast = false;

    public void addOnSoftKeyBoardVisibleListener(final Context context, final IKeyboard iKeyboard) {
        final View decorView = ((Activity) context).getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                //获得屏幕整体的高度
                if (maxDisplayHeight < rect.bottom) {
                    maxDisplayHeight = rect.bottom;
                }
                //获得键盘高度
                int keyboardHeight = maxDisplayHeight - rect.bottom;
                boolean visible = (double) keyboardHeight != 0;
                if (visible != isVisibleForLast) {
                    mKeyboardHeight = keyboardHeight;
                    if (visible) {
                        iKeyboard.onKeyboardOpen();
                    } else {
                        iKeyboard.onKeyboardClose();
                    }
                    isVisibleForLast = visible;
                }
            }
        });
    }

    public int getKeyboardHeight() {
        return mKeyboardHeight;
    }

}
