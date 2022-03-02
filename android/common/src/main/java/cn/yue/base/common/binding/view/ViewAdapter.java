package cn.yue.base.common.binding.view;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;

import androidx.databinding.BindingAdapter;

import cn.yue.base.common.binding.action.Consumer;
import cn.yue.base.common.binding.action.Consumer2;
import cn.yue.base.common.binding.action.Function;
import cn.yue.base.common.utils.display.SizeUtils;

public class ViewAdapter {

    @BindingAdapter(value = {"onClickListener"})
    public static void setOnClickListener(View view, Consumer onClickListener) {
        if (onClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.accept();
                }
            });
        } else {
            view.setOnClickListener(null);
        }
    }

    @BindingAdapter(value = {"onLongClickListener"})
    public static void setOnLongClickListener(View view, Function<Boolean> onLongClickListener) {
        if (onLongClickListener != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onLongClickListener.apply();
                }
            });
        } else {
            view.setOnLongClickListener(null);
        }
    }

    @BindingAdapter(value = {"requestFocus"})
    public static void requestFocus(View view, boolean requestFocus) {
        if (requestFocus) {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        } else {
            view.clearFocus();
        }
    }

    @BindingAdapter("onFocusChangeCommand")
    public static void setOnFocusChangeListener(View view, Consumer2<View, Boolean> focusChangeListener) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focusChangeListener.accept(v, hasFocus);
            }
        });
    }

    @BindingAdapter(value = {"selected"})
    public static void setSelected(View view, boolean selected) {
        view.setClickable(true);
        view.setFocusable(true);
        view.setSelected(selected);
    }

    /**
     * 设置背景 shape
     */
    @BindingAdapter(value = {"color", "startColor", "endColor",
            "radius", "topLeftRadius", "topRightRadius", "bottomLeftRadius", "bottomRightRadius",
            "strokeWidth", "strokeColor"}, requireAll = false)
    public static void setBackground(View view, String color, String startColor, String endColor,
                                     float radius, float topLeftRadius, float topRightRadius,
                                     float bottomLeftRadius, float bottomRightRadius, float strokeWidth,
                                     String strokeColor) {
        try {
            GradientDrawable drawable = new GradientDrawable();
            if (!TextUtils.isEmpty(startColor) && !TextUtils.isEmpty(endColor)) {
                drawable.setColors(new int[]{Color.parseColor(startColor), Color.parseColor(endColor)});
            } else if (!TextUtils.isEmpty(color)) {
                drawable.setColor(Color.parseColor(color));
            }
            if (topLeftRadius != 0 || topRightRadius != 0 || bottomLeftRadius != 0 || bottomRightRadius != 0) {
                drawable.setCornerRadii(new float[]{
                        SizeUtils.dp2px(topLeftRadius), SizeUtils.dp2px(topLeftRadius),
                        SizeUtils.dp2px(topRightRadius), SizeUtils.dp2px(topRightRadius),
                        SizeUtils.dp2px(bottomRightRadius), SizeUtils.dp2px(bottomRightRadius),
                        SizeUtils.dp2px(bottomLeftRadius), SizeUtils.dp2px(bottomLeftRadius)
                });
            }
            if (radius > 0) {
                drawable.setCornerRadius(SizeUtils.dp2px(radius));
            }
            if (strokeWidth > 0) {
                drawable.setStroke(SizeUtils.dp2px(strokeWidth), Color.parseColor(strokeColor));
            }
            view.setBackground(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
