package cn.yue.base.common.widget.viewpager;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public abstract class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments = new ArrayList<>();
    public SampleFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public Fragment getFragment(int position) {
        if (position >= mFragments.size()) {
            return null;
        }
        return mFragments.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment =  (Fragment) super.instantiateItem(container, position);
        while (mFragments.size() <= position) {
            mFragments.add(null);
        }
        mFragments.set(position, fragment);
        return fragment;
    }

    int currentItem = -1;
    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        super.finishUpdate(container);
        if (container instanceof ViewPager) {
            ViewPager viewPager = (ViewPager)container;
            if (currentItem != viewPager.getCurrentItem()) {
                currentItem = viewPager.getCurrentItem();
                onFragmentSelected(getFragment(currentItem), currentItem);
//                if (currentFragment instanceof HeaderScrollHelper.ScrollableContainer) {
//                    HeaderScrollHelper.ScrollableContainer scrollContainer = (HeaderScrollHelper.ScrollableContainer)currentFragment;
//                    headerScrollView.setCurrentScrollableContainer(scrollContainer);
//                }
            }
        }
    }

    public abstract void onFragmentSelected(Fragment fragment, int position);
}
