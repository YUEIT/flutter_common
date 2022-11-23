package cn.yue.base.flutter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.UUID;

import cn.yue.base.flutter.R;
import cn.yue.base.flutter.router.RouterCard;


/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class CommonActivity extends FragmentActivity {

    protected FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_layout);
        fragmentManager = getSupportFragmentManager();
        replace(getFragment(), null, false);
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

    //入场动画
    private int transition;

    public Fragment getFragment() {
        if (getIntent() == null || getIntent().getExtras() == null) {
            return null;
        }
        RouterCard fRouter = getIntent().getExtras().getParcelable(RouterCard.TAG);
        if (fRouter == null) {
            return null;
        }
        transition = fRouter.getTransition();
        try {
            return (Fragment) ARouter.getInstance()
                    .build(fRouter.getPath())
                    .with(getIntent().getExtras())
                    .setTimeout(fRouter.getTimeout())
//                        .withTransition(fRouter.getEnterAnim(), fRouter.getExitAnim())
                    .navigation(this, new NavigationCallback() {
                        @Override
                        public void onFound(Postcard postcard) {

                        }

                        @Override
                        public void onLost(Postcard postcard) {
                            //showError();

                        }

                        @Override
                        public void onArrival(Postcard postcard) {

                        }

                        @Override
                        public void onInterrupt(Postcard postcard) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void setExitAnim() {
        overridePendingTransition(TransitionAnimation.getStopEnterAnim(transition), TransitionAnimation.getStopExitAnim(transition));
    }

    @Override
    protected void onStop() {
        super.onStop();
        setExitAnim();
    }

}