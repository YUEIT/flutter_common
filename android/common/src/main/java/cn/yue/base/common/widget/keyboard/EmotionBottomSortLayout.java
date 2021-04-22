package cn.yue.base.common.widget.keyboard;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.image.ImageLoader;
import cn.yue.base.common.widget.keyboard.mode.IEmotionSort;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;

/**
 * Description : 键盘底下的表情分类layout
 * Created by yue on 2018/11/16
 */
public class EmotionBottomSortLayout extends LinearLayout {

    public EmotionBottomSortLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private RecyclerView sortRV;
    private CommonAdapter commonAdapter;
    private List<IEmotionSort> list = new ArrayList<>();
    private int currentIndex = 0;

    private void initView(Context context) {
        inflate(context, R.layout.layout_emotion_bottom_sort, this);
        sortRV = findViewById(R.id.emotionSortRV);
        sortRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        sortRV.setAdapter(commonAdapter = new CommonAdapter<IEmotionSort>(context, list) {
            @Override
            public int getLayoutIdByType(int viewType) {
                return R.layout.item_emotion_bottom_sort;
            }

            @Override
            public void bindData(CommonViewHolder<IEmotionSort> holder, int position, IEmotionSort iEmotionSort) {
                if (position == currentIndex) {
                    holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
                } else {
                    holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                ImageView imageView = holder.getView(R.id.emotionImageIV);
                if (iEmotionSort.getImageResId() > 0) {
                    imageView.setImageResource(iEmotionSort.getImageResId());
                } else {
                    ImageLoader.getLoader().loadImage(imageView, iEmotionSort.getIconUrl());
                }
                setOnItemClickListener(new CommonViewHolder.OnItemClickListener<IEmotionSort>() {
                    @Override
                    public void onItemClick(int position, IEmotionSort iEmotionSort) {
                        if (onClickEmotionSortListener != null) {
                            onClickEmotionSortListener.onClick(iEmotionSort);
                        }
                        currentIndex = position;
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }


    public void setEmotionSortList(List<IEmotionSort> list) {
        this.list = list;
        commonAdapter.setList(list);
    }

    public void smoothScrollToPosition(int position) {
        currentIndex = position;
        sortRV.smoothScrollToPosition(position);
        commonAdapter.notifyDataSetChanged();
    }

    public interface OnClickEmotionSortListener {
        void onClick(IEmotionSort sort);
    }

    private OnClickEmotionSortListener onClickEmotionSortListener;
    public void setOnClickEmotionSortListener(OnClickEmotionSortListener onClickEmotionSortListener) {
        this.onClickEmotionSortListener = onClickEmotionSortListener;
    }
}
