package cn.yue.base.common.image;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.widget.TopBar;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

@Route(path = "/common/viewPhoto")
public class ViewPhotoFragment extends BaseFragment {

    private List<String> photoList = new ArrayList<>();
    private int currentIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_photo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (bundle.getStringArrayList("list") != null) {
            photoList = bundle.getStringArrayList("list");
        }
        currentIndex = bundle.getInt("position");
    }

    private TopBar topBar;

    @Override
    protected void initTopBar(TopBar topBar) {
        super.initTopBar(topBar);
        this.topBar = topBar;
        topBar.setCenterTextStr((currentIndex + 1) + "/" + photoList.size());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        PhotoViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(photoAdapter);
        viewPager.setCurrentItem(currentIndex);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                topBar.setCenterTextStr((position + 1) + "/" + photoList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private PagerAdapter photoAdapter = new PagerAdapter() {

        private HashMap<Integer, PhotoView> mViewCache = new HashMap<>();

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            PhotoView photoView = mViewCache.get(position);
            ((PhotoViewPager) container).setCurrentPhotoView(photoView, position, photoList.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PhotoView photoView = mViewCache.get(position);
            if (photoView == null) {
                photoView = new PhotoView(mActivity);
                photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                photoView.loadImage(photoList.get(position));
                mViewCache.put(position, photoView);
            }
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            PhotoView photoView = mViewCache.get(position);
            if (photoView != null) {
                mViewCache.remove(position);
            }
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    };
}
