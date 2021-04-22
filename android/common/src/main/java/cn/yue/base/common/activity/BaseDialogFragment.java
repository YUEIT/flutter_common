package cn.yue.base.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.RxLifecycle;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.android.RxLifecycleAndroid;

import java.util.List;
import java.util.UUID;

import cn.yue.base.common.R;
import cn.yue.base.common.widget.dialog.WaitDialog;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static cn.yue.base.common.activity.TransitionAnimation.TRANSITION_BOTTOM;
import static cn.yue.base.common.activity.TransitionAnimation.TRANSITION_CENTER;
import static cn.yue.base.common.activity.TransitionAnimation.TRANSITION_LEFT;
import static cn.yue.base.common.activity.TransitionAnimation.TRANSITION_RIGHT;
import static cn.yue.base.common.activity.TransitionAnimation.TRANSITION_TOP;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public abstract class BaseDialogFragment extends DialogFragment implements ILifecycleProvider<FragmentEvent> {

    protected View cacheView;
    protected FragmentManager mFragmentManager;
    protected BaseFragmentActivity mActivity;
    protected Bundle bundle;
    protected LayoutInflater mInflater;
    protected Handler mHandler = new Handler();
    protected String requestTag = UUID.randomUUID().toString();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
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
        lifecycleSubject.onNext(FragmentEvent.CREATE);
        bundle = getArguments();
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        initEnterStyle();
        if (!hasCache) {
            initView(savedInstanceState);
            initOther();
        }
    }

    protected void initOther() { }

    @Override
    public final View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView == null || !needCache()) {//如果view没有被初始化或者不需要缓存的情况下，重新初始化控件
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

    protected abstract void initEnterStyle();

    public void setEnterStyle(int transition) {
        if (this.getDialog() == null) {
            return;
        }
        setStyle(STYLE_NO_TITLE, 0);
        Window window = this.getDialog().getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        setWindowLayoutParams(transition, lp);
        lp.gravity = getWindowGravity(transition);
        lp.windowAnimations = TransitionAnimation.getWindowEnterStyle(transition);
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable());
    }

    public int getWindowGravity(int transition) {
        switch (transition) {
            case TRANSITION_BOTTOM:
                return Gravity.BOTTOM;
            case TRANSITION_TOP:
                return Gravity.TOP;
            case TRANSITION_LEFT:
                return Gravity.LEFT;
            case TRANSITION_RIGHT:
                return Gravity.RIGHT;
            case TRANSITION_CENTER:
            default:
                return Gravity.CENTER;
        }
    }

    public void setWindowLayoutParams(int transition, ViewGroup.LayoutParams layoutParams) {
        switch (transition) {
            case TRANSITION_BOTTOM:
            case TRANSITION_TOP:
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                break;
            case TRANSITION_LEFT:
            case TRANSITION_RIGHT:
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                break;
            case TRANSITION_CENTER:
            default:
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                break;
        }
    }

    private WaitDialog waitDialog;

    public void showWaitDialog(String title) {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(mActivity);
        }
        waitDialog.show(title, true, null);
    }

    public void dismissWaitDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.cancel();
        }
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

    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
        mHandler.removeCallbacksAndMessages(null);
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
                    if (fragment != null && fragment.isAdded() && fragment instanceof BaseDialogFragment && fragment.isVisible()) {
                        ((BaseDialogFragment) fragment).onNewIntent(bundle);
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

    public final void jumpFragment(BaseDialogFragment fragment, String tag) {
        mActivity.replace(fragment, tag, true);
    }

    public final void jumpFragment(BaseDialogFragment fragment) {
        mActivity.replace(fragment, getClass().getSimpleName(), true);
    }

    public final void jumpFragmentNoBack(BaseDialogFragment fragment) {
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

    public static BaseDialogFragment instance(Context context, Class<? extends BaseDialogFragment> clazz, Bundle bundle) {
        return (BaseDialogFragment) Fragment.instantiate(context, clazz.getName(), bundle);
    }
}
