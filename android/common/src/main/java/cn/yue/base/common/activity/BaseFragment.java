package cn.yue.base.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;


import java.util.List;
import java.util.UUID;

import cn.yue.base.common.R;
import cn.yue.base.common.widget.TopBar;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public abstract class BaseFragment extends RxFragment implements View.OnTouchListener, ILifecycleProvider<FragmentEvent>{

    protected View cacheView;
    protected FragmentManager mFragmentManager;
    protected BaseFragmentActivity mActivity;
    protected Bundle bundle;
    protected LayoutInflater mInflater;
    protected Handler mHandler = new Handler();
    protected TopBar topBar;
    protected String requestTag = UUID.randomUUID().toString();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == context || !(context instanceof BaseFragmentActivity)) {
            throw new RuntimeException("BaseFragment必须与BaseActivity配合使用");
        }
        mActivity = (BaseFragmentActivity) context;
        mFragmentManager = getChildFragmentManager();
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
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

    protected void initOther() { }

    @Override
    public final View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
            ViewGroup v = (ViewGroup) cacheView.getParent();
            if (v != null) {
                v.removeView(cacheView);
            }
        }
        return cacheView;
    }

    /**
     * true 避免当前Fragment被repalce后回退回来重走oncreateview，导致重复初始化View和数据
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
     *
     * @return
     */
    protected abstract int getLayoutId();


    /**
     * 直接findViewById()初始化组件
     *
     * @param savedInstanceState
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

    /**
     * 自定义topbar
     * @param view
     */
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
    public <T> SingleTransformer<T, T> toBindLifecycle() {
        return new SingleTransformer<T, T>() {

            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.
                        compose(bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public <T> SingleTransformer<T, T> toBindLifecycle(FragmentEvent fragmentEvent) {
        return new SingleTransformer<T, T>() {

            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.
                        compose(bindUntilEvent(fragmentEvent))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacksAndMessages(null);
    }

    public BaseFragment newInstance(String fragmentName, Bundle bundle) {
        BaseFragment fragment = (BaseFragment) Fragment.instantiate(getActivity(), fragmentName, bundle);
        return fragment;
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
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isAdded() && fragment.isVisible() && fragment.getUserVisibleHint()) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        }
    }

    public final void jumpFragment(BaseFragment fragment, String tag) {
        mActivity.replace(fragment, tag, true);
    }

    public final void jumpFragment(BaseFragment fragment) {
        mActivity.replace(fragment, getClass().getSimpleName(), true);
    }

    public final void jumpFragmentNoBack(BaseFragment fragment) {
        mActivity.replace(fragment, null, false);
    }

    //--------------------------------------------------------------------------------------------------------------

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

    //--------------------------------------------------------------------------------------------------------------

    public final <T extends View> T findViewById(int resId) {
        if (cacheView == null) {
            return null;
        }
        return (T) cacheView.findViewById(resId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
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
