package cn.yue.base.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.yue.base.common.R;
import cn.yue.base.common.databinding.LayoutHintDialogBinding;

/**
 * Description : 提示框
 * Created by yue on 2019/3/15
 */
public class HintDialog extends Dialog{

    private LayoutHintDialogBinding binding;

    public HintDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.FadeAnimation);
        View content = View.inflate(context, R.layout.layout_hint_dialog, null);
        binding = DataBindingUtil.bind(content);
        setContentView(content);
        setCanceledOnTouchOutside(true);
    }

    public static final class Builder {
        private Context context;
        private String titleStr;
        private int titleColor;
        private boolean isShowTitle = true;
        private String contentStr;
        private int contentColor;
        private View contentView;
        private String leftClickStr;
        private int leftColor;
        private String rightClickStr;
        private int rightColor;
        private boolean isSingleClick;
        private OnLeftClickListener onLeftClickListener;
        private OnRightClickListener onRightClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTitleStr(String titleStr) {
            this.titleStr = titleStr;
            return this;
        }

        public Builder setShowTitle(boolean showTitle) {
            isShowTitle = showTitle;
            return this;
        }

        public Builder setContentStr(String contentStr) {
            this.contentStr = contentStr;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setLeftClickStr(String leftClickStr) {
            this.leftClickStr = leftClickStr;
            return this;
        }

        public Builder setRightClickStr(String rightClickStr) {
            this.rightClickStr = rightClickStr;
            return this;
        }

        public Builder setSingleClick(boolean singleClick) {
            isSingleClick = singleClick;
            return this;
        }

        public Builder setOnLeftClickListener(OnLeftClickListener onLeftClickListener) {
            this.onLeftClickListener = onLeftClickListener;
            return this;
        }

        public Builder setOnRightClickListener(OnRightClickListener onRightClickListener) {
            this.onRightClickListener = onRightClickListener;
            return this;
        }

        public Builder setTitleColor(@ColorInt int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setContentColor(@ColorInt int contentColor) {
            this.contentColor = contentColor;
            return this;
        }

        public Builder setLeftColor(@ColorInt int leftColor) {
            this.leftColor = leftColor;
            return this;
        }

        public Builder setRightColor(@ColorInt int rightColor) {
            this.rightColor = rightColor;
            return this;
        }

        private HintDialog hintDialog;
        public HintDialog build() {
            if (context == null) {
                throw new NullPointerException("context is null");
            }
            if (hintDialog == null) {
                hintDialog = new HintDialog(context);
            }
            if (isShowTitle) {
                hintDialog.binding.titleTV.setText(titleStr);
                hintDialog.binding.titleTV.setVisibility(View.VISIBLE);
            } else {
                hintDialog.binding.titleTV.setVisibility(View.GONE);
            }
            if (titleColor > 0) {
                hintDialog.binding.titleTV.setTextColor(titleColor);
            }
            if (contentView == null) {
                if (!TextUtils.isEmpty(contentStr)) {
                    TextView contentTV = new TextView(context);
                    contentTV.setTextColor(Color.parseColor("#9b9b9b"));
                    contentTV.setGravity(Gravity.CENTER);
                    contentTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
                    contentTV.setText(contentStr);
                    if (contentColor > 0) {
                        contentTV.setTextColor(contentColor);
                    }
                    hintDialog.binding.contentLL.addView(contentTV);
                }
            } else {
                hintDialog.binding.contentLL.addView(contentView);
            }
            hintDialog.binding.leftClickTV.setText(leftClickStr);
            if (leftColor > 0) {
                hintDialog.binding.leftClickTV.setTextColor(leftColor);
            }
            hintDialog.binding.rightClickTV.setText(rightClickStr);
            if (rightColor > 0) {
                hintDialog.binding.rightClickTV.setTextColor(rightColor);
            }
            hintDialog.binding.leftClickTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onLeftClickListener != null) {
                        onLeftClickListener.onLeftClick();
                    }
                    hintDialog.dismiss();
                }
            });
            hintDialog.binding.rightClickTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRightClickListener != null) {
                        onRightClickListener.onRightClick();
                    }
                    hintDialog.dismiss();
                }
            });
            if (isSingleClick) {
                if (!TextUtils.isEmpty(leftClickStr)) {
                    hintDialog.binding.leftClickTV.setVisibility(View.VISIBLE);
                    hintDialog.binding.rightClickTV.setVisibility(View.GONE);
                } else if (!TextUtils.isEmpty(rightClickStr)){
                    hintDialog.binding.leftClickTV.setVisibility(View.GONE);
                    hintDialog.binding.rightClickTV.setVisibility(View.VISIBLE);
                } else {
                    hintDialog.binding.rightClickTV.setVisibility(View.VISIBLE);
                    hintDialog.binding.leftClickTV.setVisibility(View.GONE);
                }
                hintDialog.binding.divider.setVisibility(View.GONE);
            }
            return hintDialog;
        }
    }

    public interface OnLeftClickListener{
        void onLeftClick();
    }

    public interface OnRightClickListener{
        void onRightClick();
    }

}
