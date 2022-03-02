package cn.yue.base.common.binding.text;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import cn.yue.base.common.binding.action.Consumer1;
import cn.yue.base.common.utils.debug.LogUtils;
import cn.yue.base.common.utils.display.SizeUtils;


public class ViewAdapter {

    /**
     * textView 文字多样式设置，以string形式输入，默认“,” 分割；注意数组长度匹配
     * @param view
     * @param texts @{"aa,bb,cc"}  window上无法在xml使用中文，可指向variable中值，或者为空时，默认android:text值
     * @param textSplit texts分割符
     * @param textColors @{"#ff0000,#aaaaaa,#000000"}
     * @param textSizes @{"13,9,13"}
     * @param textStyles @{"normal,bold,italic"} or @{"0,1,2"}
     * @param lineStyles @{"null, center, under"}
     */
    @BindingAdapter(value = {"texts", "textSplit", "textColors", "textSizes", "textStyles", "lineStyles"}, requireAll = false)
    public static void setText(TextView view, final String texts, final String textSplit, final String textColors,
                               final String textSizes, final String textStyles, final String lineStyles) {
        String finalText;
        if (!TextUtils.isEmpty(texts)) {
            finalText = texts;
        } else if (!TextUtils.isEmpty(view.getText())){
            finalText = view.getText().toString();
        } else {
            return;
        }
        String split = TextUtils.isEmpty(textSplit)? "," : textSplit;
        String[] textArray = finalText.split(split);
        String[] textColorArray = null;
        if (!TextUtils.isEmpty(textColors)) {
            textColorArray = textColors.replaceAll("\\s*", "").split(",");
            if (textColorArray.length != textArray.length) {
                LogUtils.e("color array is not match text array");
                return;
            }
        }
        String[] textSizeArray = null;
        if (!TextUtils.isEmpty(textSizes)) {
            textSizeArray = textSizes.replaceAll("\\s*","").split(",");
            if (textSizeArray.length != textArray.length) {
                LogUtils.e("size array is not match text array");
                return;
            }
        }
        String[] textStyleArray = null;
        if (!TextUtils.isEmpty(textStyles)) {
            textStyleArray = textStyles.replaceAll("\\s*","").split(",");
            if (textStyleArray.length != textArray.length) {
                LogUtils.e("size array is not match text array");
                return;
            }
        }
        String[] lineStyleArray = null;
        if (!TextUtils.isEmpty(lineStyles)) {
            lineStyleArray = lineStyles.replaceAll("\\s*","").split(",");
            if (lineStyleArray.length != textArray.length) {
                LogUtils.e("size array is not match text array");
                return;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String str: textArray) {
            builder.append(str);
        }
        Spannable spannable = new SpannableString(builder.toString());
        int length = 0;
        for (int i =0; i < textArray.length; i++) {
            String str = textArray[i];
            int start = length;
            int end = length + str.length();
            if (textColorArray != null) {
                try {
                    int color = Color.parseColor(textColorArray[i]);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                    spannable.setSpan(colorSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (textSizeArray != null) {
                try {
                    int textSize = Integer.parseInt(textSizeArray[i]);
                    AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(SizeUtils.sp2dp(textSize), true);
                    spannable.setSpan(sizeSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (textStyleArray != null) {
                try {
                    String textStyle = textStyleArray[i];
                    int style;
                    switch (textStyle) {
                        case "0":
                        case "normal":
                        case "NORMAL":
                            style = Typeface.NORMAL;
                            break;
                        case "1":
                        case "bold":
                        case "BOLD":
                            style = Typeface.BOLD;
                            break;
                        case "2":
                        case "italic":
                        case "ITALIC":
                            style = Typeface.ITALIC;
                            break;
                        default:
                            return;
                    }
                    StyleSpan styleSpan = new StyleSpan(style);
                    spannable.setSpan(styleSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (lineStyleArray != null) {
                try {
                    String lineStyle = lineStyleArray[i];
                    switch (lineStyle) {
                        case "1":
                        case "center":
                            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                            spannable.setSpan(strikethroughSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            break;
                        case "2":
                        case "under":
                            UnderlineSpan underlineSpan = new UnderlineSpan();
                            spannable.setSpan(underlineSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            length = end;
        }
        view.setText(spannable);
    }


    @BindingAdapter(value = {"requestFocus"})
    public static void requestFocus(EditText editText, boolean requestFocus) {
        if (requestFocus) {
            editText.setSelection(editText.getText().length());
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
        editText.setFocusableInTouchMode(requestFocus);
    }

    @BindingAdapter(value = {"clearFocus"})
    public static void clearFocus(EditText editText, Boolean clear) {
        if (clear) {
            editText.clearFocus();
        }
    }

    /**
     * EditText输入文字改变的监听
     */
    @BindingAdapter(value = {"onTextChanged"})
    public static void addTextChangedListener(EditText editText, Consumer1<String> textChanged) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChanged.accept(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
