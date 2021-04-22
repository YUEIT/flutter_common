package cn.yue.base.common.widget.keyboard;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.widget.keyboard.mode.IEmotionPage;

/**
 * Description : 表情页Adapter
 * Created by yue on 2018/11/15
 */
public class EmotionPageAdapter<T extends IEmotionPage> extends PagerAdapter {

    private List<T> pageList;
    public EmotionPageAdapter(List<T> pageList) {
        this.pageList = pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (pageList !=null && pageList.size() > 0) {
            return pageList.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_emotion_page, null);
        RecyclerView emotionRV = contentView.findViewById(R.id.emotionRV);
        if (pageList!= null && pageList.size() > position) {
            emotionRV.setLayoutManager(new GridLayoutManager(context, pageList.get(position).getRowNum()));
            emotionRV.setAdapter(new EmotionAdapter(context, pageList.get(position).getEmotionList()));
        }
        container.addView(contentView);
        return contentView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        if (view == (View)object) {
            return true;
        }
        return false;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }
}
