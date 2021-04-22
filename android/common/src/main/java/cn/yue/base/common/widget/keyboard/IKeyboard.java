package cn.yue.base.common.widget.keyboard;

/**
 * Description : 键盘操作需实现
 * Created by yue on 2018/11/14
 */
public interface IKeyboard {

    void onKeyboardOpen();

    void onKeyboardClose();

    int getKeyboardHeight();
}
