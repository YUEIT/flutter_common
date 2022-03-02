package cn.yue.base.common.widget;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yue.base.common.R;
import cn.yue.base.common.databinding.LayoutTopBarBinding;
import cn.yue.base.common.utils.app.BarUtils;
import cn.yue.base.common.utils.display.SizeUtils;

/**
 * Description :
 * Created by yue on 2019/3/8
 */
public class TopBar extends RelativeLayout {

    LayoutTopBarBinding binding;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_top_bar, this, true);
        View statusBarSpace = findViewById(R.id.statusBarSpace);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)statusBarSpace.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layoutParams.height = Math.max(BarUtils.getStatusBarHeight(), SizeUtils.dp2px(30f));
        }
        defaultStyle();
    }

    private void defaultStyle() {
        setBackgroundColor(Color.WHITE);
        binding.leftTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        binding.leftTV.setTextColor(Color.parseColor("#000000"));
        binding.centerTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        binding.centerTV.setTextColor(Color.parseColor("#000000"));
        binding.rightTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        binding.rightTV.setTextColor(Color.parseColor("#000000"));
        binding.leftIV.setVisibility(GONE);
        binding.leftTV.setVisibility(GONE);
        binding.rightTV.setVisibility(GONE);
        binding.rightIV.setVisibility(GONE);
        binding.divider.setVisibility(GONE);
        binding.centerIV.setVisibility(View.GONE);
    }

    public TopBar setBgColor(@ColorInt int color) {
        setBackgroundColor(color);
        return this;
    }

    public TopBar setBarVisibility(int visible) {
        setVisibility(visible);
        return this;
    }

    public TopBar setContentVisibility(int visibility) {
        binding.barContentRL.setVisibility(visibility);
        return this;
    }

    public TopBar setLeftTextStr(String s) {
        binding.leftTV.setVisibility(VISIBLE);
        binding.leftTV.setText(s);
        return this;
    }

    public TopBar setLeftImage(@DrawableRes int resId) {
        binding.leftIV.setVisibility(VISIBLE);
        binding.leftIV.setImageResource(resId);
        return this;
    }

    public TopBar setLeftClickListener(OnClickListener onClickListener) {
        binding.leftLL.setOnClickListener(onClickListener);
        return this;
    }

    public TopBar setCenterTextStr(String s) {
        binding.centerTV.setVisibility(VISIBLE);
        binding.centerTV.setText(s);
        return this;
    }

    public TopBar setCenterClickListener(OnClickListener onClickListener) {
        binding.centerTV.setOnClickListener(onClickListener);
        return this;
    }

    public TopBar setRightTextStr(String s) {
        binding.rightTV.setVisibility(VISIBLE);
        binding.rightTV.setText(s);
        return this;
    }

    public TopBar setRightImage(@DrawableRes int resId) {
        binding.rightIV.setVisibility(VISIBLE);
        binding.rightIV.setImageResource(resId);
        return this;
    }

    public TopBar setRightClickListener(OnClickListener onClickListener) {
        binding.rightLL.setOnClickListener(onClickListener);
        return this;
    }

    public TopBar setLeftTextSize(float sp) {
        binding.leftTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    public TopBar setLeftTextColor(@ColorInt int color) {
        binding.leftTV.setTextColor(color);
        return this;
    }

    public TopBar setCenterTextSize(float sp) {
        binding.centerTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    public TopBar setCenterTextColor(@ColorInt int color) {
        binding.centerTV.setTextColor(color);
        return this;
    }

    public TopBar setCenterImage(int resId) {
        if (resId == 0) {
            binding.centerIV.setVisibility(View.GONE);
        } else {
            binding.centerIV.setVisibility(View.VISIBLE);
            binding.centerIV.setImageResource(resId);
        }
        return this;
    }

    public TopBar setRightTextSize(float sp) {
        binding.rightTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    public TopBar setRightTextColor(@ColorInt int color) {
        binding.rightTV.setTextColor(color);
        return this;
    }

    public TopBar setDividerVisible(boolean visible) {
        binding.divider.setVisibility(visible ? VISIBLE : GONE);
        return this;
    }
}
