package cn.yue.base.common.widget;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.yue.base.common.utils.display.SizeUtils;

/**
 * Description : 自定义toast
 * Created by yue on 2019/3/12
 */
public class CustomToast extends Toast {

    private Context context;
    private TextView toastText;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public CustomToast(Context context) {
        super(context);
        this.context = context;
        // android 11 setView已失效
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            setGravity(Gravity.CENTER, 0, 0);
            LayoutInflater inflater = LayoutInflater.from(context);
            if (null != inflater) {
                setView(initView(context));
            }
        }
    }

    private View initView(Context context) {
        RelativeLayout layout = new RelativeLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);
        int l = SizeUtils.dp2px(15);
        int t = SizeUtils.dp2px(10);
        layout.setPadding(l, t, l, t);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(10);
        drawable.setColor(Color.parseColor("#b0222222"));
        //
        layout.setBackgroundDrawable(drawable);

        toastText = new TextView(context);
        toastText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        toastText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        toastText.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(toastText, textParams);

        return layout;
    }

    @Override
    public void setText(int resId) {
        toastText.setText(context.getText(resId));
    }

    @Override
    public void setText(CharSequence s) {
        toastText.setText(s);
    }
}
