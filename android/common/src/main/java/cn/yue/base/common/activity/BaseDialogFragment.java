package cn.yue.base.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.rx.ILifecycleProvider;
import cn.yue.base.common.activity.rx.RxLifecycleProvider;
import cn.yue.base.common.widget.dialog.WaitDialog;

import static cn.yue.base.common.activity.TransitionAnimation.TRANSITION_BOTTOM;
import static cn.yue.base.common.activity.TransitionAnimation.TRANSITION_CENTER;
import static cn.yue.base.common.activity.TransitionAnimation.TRANSITION_LEFT;
import static cn.yue.base.common.activity.TransitionAnimation.TRANSITION_RIGHT;
import static cn.yue.base.common.activity.TransitionAnimation.TRANSITION_TOP;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public abstract class BaseDialogFragment extends DialogFragment {

    private ILifecycleProvider<Lifecycle.Event> lifecycleProvider;
    protected View cacheView;
    protected FragmentManager mFragmentManager;
    protected BaseFragmentActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof BaseFragmentActivity)) {
            throw new RuntimeException("BaseFragment必须与BaseFragmentActivity配合使用");
        }
        mActivity = (BaseFragmentActivity) context;
        mFragmentManager = getChildFragmentManager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleProvider = initLifecycleProvider();
        getLifecycle().addObserver(lifecycleProvider);
        setStyle(STYLE_NO_TITLE, 0);
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
        initEnterStyle();
        if (!hasCache) {
            initView(savedInstanceState);
            initOther();
        }
    }

    protected void initOther() { }

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
     *
     */
    protected abstract int getLayoutId();


    /**
     * 直接findViewById()初始化组件
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        boolean isShow = getShowsDialog();
        setShowsDialog(false);
        super.onActivityCreated(savedInstanceState);
        setShowsDialog(isShow);
        if (getDialog() == null) {
            return;
        }
        View view = getView();
        if (view != null) {
            if (view.getParent() != null) {
                throw new IllegalStateException(
                        "DialogFragment can not be attached to a container view");
            }
            getDialog().setContentView(view);
        }
        final Activity activity = getActivity();
        if (activity != null) {
            getDialog().setOwnerActivity(activity);
        }
        getDialog().setCancelable(isCancelable());
        // 使用静态内部类取代，防止message中持有fragment的引用而造成内存泄漏
        getDialog().setOnCancelListener(onCancelListener);
        getDialog().setOnDismissListener(onDismissListener);
        if (savedInstanceState != null) {
            Bundle dialogState = savedInstanceState.getBundle("android:savedDialogState");
            if (dialogState != null) {
                getDialog().onRestoreInstanceState(dialogState);
            }
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private final OnCancelListener onCancelListener = new OnCancelListener(this);
    public static class OnCancelListener implements DialogInterface.OnCancelListener {

        private WeakReference<BaseDialogFragment> fragmentWeakReference;

        public OnCancelListener(BaseDialogFragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            if (fragmentWeakReference != null) {
                BaseDialogFragment dialogFragment = fragmentWeakReference.get();
                if (dialogFragment != null) {
                    dialogFragment.onCancel(dialog);
                }
            }
        }
    }

    private final OnDismissListener onDismissListener = new OnDismissListener(this);
    public static class OnDismissListener implements DialogInterface.OnDismissListener {

        private WeakReference<BaseDialogFragment> fragmentWeakReference;

        public OnDismissListener(BaseDialogFragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            if (fragmentWeakReference != null) {
                BaseDialogFragment dialogFragment = fragmentWeakReference.get();
                if (dialogFragment != null) {
                    dialogFragment.onDismiss(dialog);
                }
            }
        }
    }

    private WaitDialog waitDialog;

    public void showWaitDialog(String title) {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(mActivity);
        }
        waitDialog.show(title);
    }

    public void dismissWaitDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.cancel();
        }
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
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isAdded()
                        && fragment instanceof BaseDialogFragment && fragment.isVisible()) {
                    ((BaseDialogFragment) fragment).onNewIntent(bundle);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFragmentManager != null) {
            List<Fragment> fragments = mFragmentManager.getFragments();
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isAdded()
                        && fragment.isVisible() && fragment.getUserVisibleHint()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
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
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    public static BaseDialogFragment instance(Context context, Class<? extends BaseDialogFragment> clazz, Bundle bundle) {
        return (BaseDialogFragment) Fragment.instantiate(context, clazz.getName(), bundle);
    }
}
