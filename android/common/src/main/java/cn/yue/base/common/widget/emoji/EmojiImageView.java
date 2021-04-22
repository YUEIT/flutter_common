package cn.yue.base.common.widget.emoji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Description : 用于显示emoji的ImageView
 * Created by yue on 2018/12/12
 */
public class EmojiImageView extends AppCompatImageView {

    private final static int MIN_WIDTH = 200;
    private final static int MIN_HEIGHT = 200;
    private float textSize = 60;
    private TextPaint mTextPaint;
    public EmojiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMinimumHeight(MIN_HEIGHT);
        setMinimumWidth(MIN_WIDTH);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoColorEmoji.ttf"));
    }

    private CharSequence emoji;
    public void setEmoji(CharSequence emoji) {
        this.emoji = emoji;
        invalidate();
    }

    public void setEmojiSize (float px) {
        textSize = px;
        mTextPaint.setTextSize(px);
        invalidate();
    }

    public void setTypeFace(Typeface typeFace) {
        mTextPaint.setTypeface(typeFace);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (emoji != null) {
            Rect targetRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
            int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
            canvas.drawText(emoji.toString(), targetRect.centerX(), baseline, mTextPaint);
        } else {
            super.onDraw(canvas);
        }
    }
}
