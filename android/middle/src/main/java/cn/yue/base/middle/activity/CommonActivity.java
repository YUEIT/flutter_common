package cn.yue.base.middle.activity;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.common.activity.TransitionAnimation;
import cn.yue.base.common.utils.debug.LogUtils;
import cn.yue.base.middle.router.FRouter;
import cn.yue.base.middle.router.RouterCard;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class CommonActivity extends BaseFragmentActivity {

    //入场动画
    private int transition;
    @Override
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
                            LogUtils.e("no find page " + postcard);
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

    @Override
    protected void setExitAnim() {
        overridePendingTransition(TransitionAnimation.getStopEnterAnim(transition), TransitionAnimation.getStopExitAnim(transition));
    }
}