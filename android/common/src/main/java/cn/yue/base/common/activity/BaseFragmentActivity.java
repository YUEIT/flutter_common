package cn.yue.base.common.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.components.support.RxFragmentActivity;

import java.util.List;
import java.util.UUID;

import cn.yue.base.common.R;
import cn.yue.base.common.utils.app.BarUtils;
import cn.yue.base.common.utils.app.RunTimePermissionUtil;
import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.common.widget.TopBar;
import cn.yue.base.common.widget.dialog.HintDialog;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public abstract class BaseFragmentActivity extends RxFragmentActivity implements ILifecycleProvider<ActivityEvent>{

    private TopBar topBar;
    private FrameLayout topFL;
    private FrameLayout content;
    protected FragmentManager fragmentManager;
    protected BaseFragment currentFragment;
    private int resultCode;
    private Bundle resultBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setSystemBar(false, true, Color.WHITE);
        setContentView(getContentViewLayoutId());
        initView();
        replace(getFragment(), null, false);
    }

    protected int getContentViewLayoutId() {
        return R.layout.base_activity_layout;
    }

    private void initView() {
        topFL = findViewById(R.id.topBar);
        topFL.addView(topBar = new TopBar(this));
        content = findViewById(R.id.content);
        content.setBackgroundColor(Color.WHITE);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                currentFragment = getCurrentFragment();
                if (currentFragment != null && resultCode == RESULT_OK) {
                    currentFragment.onFragmentResult(resultCode, resultBundle);
                }
                resultCode = RESULT_CANCELED;
                resultBundle = null;
            }
        });
    }

    public void setSystemBar(boolean isFillUpTop, boolean isDarkIcon) {
        setSystemBar(isFillUpTop, isDarkIcon, Color.TRANSPARENT);
    }

    public void setSystemBar(boolean isFillUpTop, boolean isDarkIcon, int bgColor) {
        try {
            BarUtils.setStyle(this, isFillUpTop, isDarkIcon, bgColor);
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (isFillUpTop) {
            setFillUpTopLayout(isFillUpTop);
        }
    }

    public void setFillUpTopLayout(boolean isFillUpTop) {
        int systemBarPadding;
        int subject;
        if (isFillUpTop) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                systemBarPadding = 0;
            } else {
                systemBarPadding = Math.max(BarUtils.getStatusBarHeight(this), getResources().getDimensionPixelOffset(R.dimen.q50));
            }
            subject = 0;
            if (getTopBar() != null) {
                getTopBar().setBgColor(Color.TRANSPARENT);
            }
        } else {
            systemBarPadding = 0;
            subject = R.id.topBar;
        }

        RelativeLayout.LayoutParams topBarLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        topBarLayoutParams.topMargin = systemBarPadding;
        topFL.setLayoutParams(topBarLayoutParams);
        RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentLayoutParams.addRule(RelativeLayout.BELOW, subject);
        content.setLayoutParams(contentLayoutParams);
    }

    public TopBar getTopBar() {
        return topBar;
    }

    public void customTopBar(View view) {
        topFL.removeAllViews();
        topFL.addView(view);
    }

    public View getCustomTopBar() {
        return topFL.getChildAt(0);
    }

    public void removeTopBar() {
        topFL.removeView(topBar);
    }

    public void setContentBackground(@ColorInt int color) {
        content.setBackgroundColor(color);
    }

    public abstract Fragment getFragment();

    public void recreateFragment(String fragmentName) {
        replace(getFragment(), null, false);
    }

    public Fragment instantiate(Class mClass, Bundle args){
        return Fragment.instantiate(this, mClass.getSimpleName(), args);
    }

    public void replace(Fragment fragment, String tag , boolean canBack){
        if (null == fragment) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        if (TextUtils.isEmpty(tag)) {
            tag = UUID.randomUUID().toString();
        }
        transaction.replace(R.id.content, fragment, tag);
        if (canBack) {
            transaction.addToBackStack(tag);
        }
        transaction.commitAllowingStateLoss();
    }

    public BaseFragment getCurrentFragment() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.content);
        if (fragment != null && fragment instanceof BaseFragment) {
            return (BaseFragment) fragment;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setExitAnim();
    }

    protected void setExitAnim() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
            currentFragment = getCurrentFragment();
            if (currentFragment != null && currentFragment.onFragmentBackPressed()) {
                return;
            }
            superOnBackPressed();
    }

    public void superOnBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0 && resultCode != RESULT_CANCELED) {
            Intent data = null;
            if (resultBundle != null) {
                data = new Intent();
                data.putExtras(resultBundle);
            }
            setResult(resultCode, data);

        }
        super.onBackPressed();
        setExitAnim();
    }

    public void setFragmentResult(int resultCode, Bundle resultBundle) {
        this.resultCode = resultCode;
        this.resultBundle = resultBundle;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null || intent.getExtras() == null) {
            return;
        }
        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isAdded() && fragment instanceof BaseFragment && fragment.isVisible()) {
                        ((BaseFragment) fragment).onNewIntent(intent.getExtras());
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isAdded() && fragment instanceof BaseFragment && fragment.isVisible()) {
                        ((BaseFragment) fragment).onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        }
    }

    @Override
    public <T> SingleTransformer<T, T> toBindLifecycle() {
        return new SingleTransformer<T, T>() {

            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.
                        compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public <T> SingleTransformer<T, T> toBindLifecycle(ActivityEvent activityEvent) {
        return new SingleTransformer<T, T>() {

            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.
                        compose(bindUntilEvent(activityEvent))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 权限请求
     * @param permissions
     * @param requestCode
     */
    public void requestPermission(String[] permissions, int requestCode, PermissionCallBack permissionCallBack) {
        RunTimePermissionUtil.requestPermissions(this, requestCode, permissionCallBack, permissions);
    }

    private PermissionCallBack permissionCallBack;

    public void setPermissionCallBack(PermissionCallBack permissionCallBack) {
        this.permissionCallBack = permissionCallBack;
    }

    private HintDialog failDialog;
    public void showFailDialog() {
        if (failDialog == null) {
            failDialog = new HintDialog.Builder(this)
                    .setTitleStr("消息")
                    .setContentStr("当前应用无此权限，该功能暂时无法使用。如若需要，请单击确定按钮进行权限授权！")
                    .setLeftClickStr("取消")
                    .setRightClickStr("确定")
                    .setOnRightClickListener(new HintDialog.OnRightClickListener() {
                        @Override
                        public void onRightClick() {
                            startSettings();
                        }
                    })
                    .build();
        }
        failDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RunTimePermissionUtil.REQUEST_CODE) {
            if (permissionCallBack != null) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (verificationPermissions(grantResults)) {
                        permissionCallBack.requestSuccess(permissions[i]);
                    } else {
                        ToastUtils.showShortToast("获取" + RunTimePermissionUtil.getPermissionName(permissions[i]) + "权限失败~");
                        permissionCallBack.requestFailed(permissions[i]);
                    }
                }
            }
        }
    }

    private boolean verificationPermissions(int[] results) {
        for (int result : results) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;

    }

    private void startSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
