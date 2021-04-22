package cn.yue.base.common.widget;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yue.base.common.R;

/**
 * Description :
 * Created by yue on 2019/3/8
 */
public class TopBar extends RelativeLayout {

    private LinearLayout leftLL;
    private LinearLayout rightLL;
    private TextView leftTV;
    private ImageView leftIV;
    private TextView centerTV;
    private TextView rightTV;
    private ImageView rightIV;
    private View divider;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.layout_top_bar, this);
        leftLL = findViewById(R.id.leftLL);
        rightLL = findViewById(R.id.rightLL);
        leftTV = findViewById(R.id.leftTV);
        leftIV = findViewById(R.id.leftIV);
        centerTV = findViewById(R.id.centerTV);
        rightTV = findViewById(R.id.rightTV);
        rightIV = findViewById(R.id.rightIV);
        divider = findViewById(R.id.divider);
        defaultStyle();
    }

    private void defaultStyle() {
        setBackgroundColor(Color.WHITE);
        leftTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        leftTV.setTextColor(Color.parseColor("#000000"));
        centerTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        centerTV.setTextColor(Color.parseColor("#000000"));
        rightTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        rightTV.setTextColor(Color.parseColor("#000000"));
        leftIV.setVisibility(GONE);
        leftTV.setVisibility(GONE);
        rightTV.setVisibility(GONE);
        rightIV.setVisibility(GONE);
        divider.setVisibility(GONE);
    }

    public TopBar setBgColor(@ColorInt int color) {
        setBackgroundColor(color);
        return this;
    }

    public TopBar setBarVisibility(int visible) {
        setVisibility(visible);
        return this;
    }

    public TopBar setLeftTextStr(String s) {
        if (leftTV != null) {
            leftTV.setVisibility(VISIBLE);
            leftTV.setText(s);
        }
        return this;
    }

    public TopBar setLeftImage(@DrawableRes int resId) {
        if (leftIV != null) {
            leftIV.setVisibility(VISIBLE);
            leftIV.setImageResource(resId);
        }
        return this;
    }

    public TopBar setLeftClickListener(OnClickListener onClickListener) {
        if (leftLL != null) {
            leftLL.setOnClickListener(onClickListener);
        }
        return this;
    }

    public TopBar setCenterTextStr(String s) {
        if (centerTV != null) {
            centerTV.setVisibility(VISIBLE);
            centerTV.setText(s);
        }
        return this;
    }

    public TopBar setCenterClickListener(OnClickListener onClickListener) {
        if (centerTV != null) {
            centerTV.setOnClickListener(onClickListener);
        }
        return this;
    }

    public TopBar setRightTextStr(String s) {
        if (rightTV != null) {
            rightTV.setVisibility(VISIBLE);
            rightTV.setText(s);
        }
        return this;
    }

    public TopBar setRightImage(@DrawableRes int resId) {
        if (rightIV != null) {
            rightIV.setVisibility(VISIBLE);
            rightIV.setImageResource(resId);
        }
        return this;
    }

    public TopBar setRightClickListener(OnClickListener onClickListener) {
        if (rightLL != null) {
            rightLL.setOnClickListener(onClickListener);
        }
        return this;
    }

    public TopBar setLeftTextSize(float sp) {
        if (leftTV != null) {
            leftTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        }
        return this;
    }

    public TopBar setLeftTextColor(@ColorInt int color) {
        if (leftTV != null) {
            leftTV.setTextColor(color);
        }
        return this;
    }

    public TopBar setCenterTextSize(float sp) {
        if (centerTV != null) {
            centerTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        }
        return this;
    }

    public TopBar setCenterTextColor(@ColorInt int color) {
        if (centerTV != null) {
            centerTV.setTextColor(color);
        }
        return this;
    }

    public TopBar setRightTextSize(float sp) {
        if (rightTV != null) {
            rightTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        }
        return this;
    }

    public TopBar setRightTextColor(@ColorInt int color) {
        if (rightTV != null) {
            rightTV.setTextColor(color);
        }
        return this;
    }

    public TopBar setDividerVisible(boolean visible) {
        if (divider != null) {
            divider.setVisibility(visible ? VISIBLE : GONE);
        }
        return this;
    }
}
