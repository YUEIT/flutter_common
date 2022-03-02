package cn.yue.base.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.rx.ILifecycleProvider;
import cn.yue.base.common.activity.rx.RxLifecycleProvider;
import cn.yue.base.common.widget.TopBar;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public abstract class BaseFragment extends Fragment implements View.OnTouchListener {

    private ILifecycleProvider<Lifecycle.Event> lifecycleProvider;
    protected View cacheView;
    protected FragmentManager mFragmentManager;
    protected BaseFragmentActivity mActivity;
    protected TopBar topBar;
    protected Handler mHandler = new Handler();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof BaseFragmentActivity)) {
            throw new RuntimeException("BaseFragment必须与BaseActivity配合使用");
        }
        mActivity = (BaseFragmentActivity) context;
        mFragmentManager = getChildFragmentManager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleProvider = initLifecycleProvider();
        getLifecycle().addObserver(lifecycleProvider);
    }

    protected ILifecycleProvider<Lifecycle.Event> initLifecycleProvider() {
        return new RxLifecycleProvider();
    }

    public ILifecycleProvider<Lifecycle.Event> getLifecycleProvider() {
        return lifecycleProvider;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
        if (!hasCache) {
            initView(savedInstanceState);
            initOther();
        }
    }

    protected void initOther() {
    }

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView == null || !needCache()) {//如果view没有被初始化或者不需要缓存的情况下，重新初始化控件
            topBar = mActivity.getTopBar();
            initTopBar(topBar);
            if (getLayoutId() == 0) {
                cacheView = null;
            } else {
                cacheView = inflater.inflate(getLayoutId(), container, false);
            }
            hasCache = false;
        } else {
            hasCache = true;
            ViewParent v = cacheView.getParent();
            if (v instanceof ViewGroup) {
                ((ViewGroup) v).removeView(cacheView);
            }
        }
        return cacheView;
    }

    /**
     * true 避免当前Fragment被replace后回退回来重走onCreateView，导致重复初始化View和数据
     */
    protected boolean needCache() {
        return true;
    }

    /**
     * 是否有缓存，避免重新走initView方法
     */
    private boolean hasCache;

    /**
     * 获取布局
     */
    protected abstract int getLayoutId();


    /**
     * 直接findViewById()初始化组件
     */
    protected abstract void initView(Bundle savedInstanceState);


    protected void initTopBar(TopBar topBar) {
        if (null != topBar) {
            topBar.setVisibility(View.VISIBLE);
            topBar.setLeftImage(R.drawable.app_icon_back);
            topBar.setLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAll();
                }
            });
        }
    }

    public void customTopBar(View view) {
        mActivity.customTopBar(view);
    }

    public void hideTopBar() {
        if (mActivity.getTopBar() != null) {
            mActivity.getTopBar().setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(lifecycleProvider);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacksAndMessages(null);
    }

    public void clearCacheView() {
        cacheView = null;
    }

    public boolean onFragmentBackPressed() {
        return false;
    }

    public void setFragmentBackResult(int resultCode, Bundle data) {
        mActivity.setFragmentResult(resultCode, data);
    }

    public void setFragmentBackResult(int resultCode) {
        setFragmentBackResult(resultCode, null);
    }

    public void onFragmentResult(int resultCode, Bundle data) {

    }

    public void onNewIntent(Bundle bundle) {
        if (mFragmentManager != null) {
            List<Fragment> fragments = mFragmentManager.getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isAdded() && fragment instanceof BaseFragment && fragment.isVisible()) {
                        ((BaseFragment) fragment).onNewIntent(bundle);
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFragmentManager != null) {
            List<Fragment> fragments = mFragmentManager.getFragments();
            if (fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isAdded() && fragment.isVisible() && fragment.getUserVisibleHint()) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        }
    }

    public final void finishFragment() {
        mActivity.onBackPressed();
    }

    public final void finishFragmentWithResult() {
        setFragmentBackResult(Activity.RESULT_OK);
        mActivity.onBackPressed();
    }

    public final void finishFragmentWithResult(Bundle data) {
        setFragmentBackResult(Activity.RESULT_OK, data);
        mActivity.onBackPressed();
    }

    public final void finishAll() {
        mActivity.supportFinishAfterTransition();
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public final void finishAllWithResult(int resultCode, Intent data) {
        mActivity.setResult(resultCode, data);
        finishAll();
    }

    public final void finishAllWithResult(int resultCode) {
        finishAllWithResult(resultCode, null);
    }

    public final void finishAllWithResult(Bundle data) {
        Intent intent = new Intent();
        intent.putExtras(data);
        finishAllWithResult(Activity.RESULT_OK, intent);
    }

    public final <T extends View> T findViewById(int resId) {
        if (cacheView == null) {
            return null;
        }
        return (T) cacheView.findViewById(resId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    /*****************************************************************/
    /*********************    操作子fragment开始*************************/
    /*****************************************************************/

    public final void replace(int contaninViewId, Fragment fragment) {
        if (null != fragment && null != mFragmentManager) {
            mFragmentManager
                    .beginTransaction()
                    .replace(contaninViewId, fragment)
                    .commitAllowingStateLoss();
        }
    }

    public final void replace(int contaninViewId, Fragment fragment, String tag) {
        if (null != fragment) {
            mFragmentManager
                    .beginTransaction()
                    .replace(contaninViewId, fragment, tag)
                    .commitAllowingStateLoss();
        }
    }


    public void removeFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
    }

    public void removeFragment(String tag) {
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            removeFragment(fragment);
        }
    }


    public boolean attachFragment(Fragment fragment) {
        if (fragment != null && fragment.isDetached()) {
            mFragmentManager.beginTransaction().attach(fragment).commitAllowingStateLoss();
            return true;
        }
        return false;
    }

    public boolean attachFragment(String tag) {
        Fragment fragment = findFragmentByTag(tag);
        return attachFragment(fragment);
    }

    public boolean isAddFragment(String tag) {
        Fragment fragment = findFragmentByTag(tag);
        return fragment != null && fragment.isAdded();
    }

    public boolean detachFragment(Fragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            mFragmentManager.beginTransaction().detach(fragment).commitAllowingStateLoss();
            return true;
        }
        return false;
    }

    public boolean detachFragment(String tag) {
        Fragment fragment = findFragmentByTag(tag);
        return detachFragment(fragment);
    }

    public Fragment findFragmentByTag(String tag) {
        return mFragmentManager.findFragmentByTag(tag);
    }

    public void addFragment(int containerId, Fragment fragment, String tag) {
        mFragmentManager.beginTransaction()
                .add(containerId, fragment, tag)
                .commitAllowingStateLoss();
    }

    public void hideFragment(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();
    }

    public static BaseFragment instance(Context context, Class<? extends BaseFragment> clazz, Bundle bundle) {
        return (BaseFragment) Fragment.instantiate(context, clazz.getName(), bundle);
    }
}
