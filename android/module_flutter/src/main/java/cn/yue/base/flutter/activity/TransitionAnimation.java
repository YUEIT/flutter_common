package cn.yue.base.flutter.activity;
import cn.yue.base.flutter.R;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class TransitionAnimation {

    //根据启动时入场方式设置参数
    public final static int TRANSITION_RIGHT = 0;
    public final static int TRANSITION_LEFT = 1;
    public final static int TRANSITION_TOP = 2;
    public final static int TRANSITION_BOTTOM = 3;
    public final static int TRANSITION_CENTER = 4;

    /**
     * 启动一个activitiy时，入场的组件的动画
     * @param transition
     * @return
     */
    public static int getStartEnterAnim(int transition) {
        switch (transition) {
            case TRANSITION_BOTTOM:
                return R.anim.bottom_in;
            case TRANSITION_TOP:
                return R.anim.top_in;
            case TRANSITION_LEFT:
                return R.anim.left_in;
            case TRANSITION_RIGHT:
                default:
                return R.anim.right_in;
        }
    }

    /**
     * 启动一个activity时，退场的组件的动画
     * @param transition
     * @return
     */
    public static int getStartExitAnim(int transition) {
        switch (transition) {
            case TRANSITION_BOTTOM:
                return R.anim.top_out;
            case TRANSITION_TOP:
                return R.anim.bottom_out;
            case TRANSITION_LEFT:
                return R.anim.right_out;
            case TRANSITION_RIGHT:
            default:
                return R.anim.left_out;
        }
    }

    /**
     * 退出activity时，入场的activity的动画
     * @param transition
     * @return
     */
    public static int getStopEnterAnim(int transition) {
        switch (transition) {
            case TRANSITION_BOTTOM:
                return R.anim.top_in;
            case TRANSITION_TOP:
                return R.anim.bottom_in;
            case TRANSITION_LEFT:
                return R.anim.right_in;
            case TRANSITION_RIGHT:
            default:
                return R.anim.left_in;
        }
    }

    /**
     * 退出activity时，退出的activity的动画
     * @param transition
     * @return
     */
    public static int getStopExitAnim(int transition) {
        switch (transition) {
            case TRANSITION_BOTTOM:
                return R.anim.bottom_out;
            case TRANSITION_TOP:
                return R.anim.top_out;
            case TRANSITION_LEFT:
                return R.anim.left_out;
            case TRANSITION_RIGHT:
            default:
                return R.anim.right_out;
        }
    }

    /**
     * 启动一个activitiy时，入场的组件的动画
     * @param transition
     * @return
     */
    public static int getWindowEnterStyle(int transition) {
        switch (transition) {
            case TRANSITION_BOTTOM:
                return R.style.BottomAnimStyle;
            case TRANSITION_TOP:
                return R.style.TopAnimStyle;
            case TRANSITION_LEFT:
                return R.style.LeftAnimStyle;
            case TRANSITION_RIGHT:
                return R.style.RightAnimStyle;
            case TRANSITION_CENTER:
            default:
                return R.style.CenterAnimStyle;
        }
    }
}
