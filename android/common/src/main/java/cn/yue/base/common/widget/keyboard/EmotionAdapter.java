package cn.yue.base.common.widget.keyboard;

import android.content.Context;

import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.image.ImageLoader;
import cn.yue.base.common.widget.emoji.Emoji;
import cn.yue.base.common.widget.emoji.EmojiImageView;
import cn.yue.base.common.widget.keyboard.mode.EmotionUtils;
import cn.yue.base.common.widget.keyboard.mode.IEmotion;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;

/**
 * Description : 表情adapter
 * Created by yue on 2018/11/15
 */
public class EmotionAdapter<T extends IEmotion> extends CommonAdapter<T>{

    public EmotionAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public int getLayoutIdByType(int viewType) {
        return R.layout.item_emotion;
    }

    @Override
    public void bindData(CommonViewHolder<T> holder, int position, T t) {
        EmojiImageView emotionItemIV = holder.getView(R.id.emotionItemIV);
        if (t.getImageResId() > 0) {
            if (t instanceof Emoji) {
                emotionItemIV.setEmoji(new String(Character.toChars(t.getImageResId())));
            } else {
                emotionItemIV.setImageResource(t.getImageResId());
            }
        } else {
            ImageLoader.getLoader().loadImage(emotionItemIV, t.getImageUrl());
        }
        setOnItemClickListener(new CommonViewHolder.OnItemClickListener<T>() {
            @Override
            public void onItemClick(int position, T t) {
                if (EmotionUtils.getOnEmotionClickListener() != null) {
                    for (OnEmotionClickListener emotionClickListener : EmotionUtils.getOnEmotionClickListener()) {
                        emotionClickListener.onClick(t);
                    }
                }
            }
        });
    }
}
