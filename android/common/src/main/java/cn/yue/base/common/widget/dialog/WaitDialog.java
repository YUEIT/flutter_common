package cn.yue.base.common.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.yue.base.common.R;
import cn.yue.base.common.image.ImageLoader;

/**
 * Description : 等待框
 * Created by yue on 2019/3/11
 */

public class WaitDialog {

    private Activity activity;
    private Dialog dialog;
    private TextView waitText;
    private Handler handler;

    public WaitDialog(@NonNull Activity activity) {
        this.activity = activity;
        handler = new Handler();
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = View.inflate(activity, R.layout.layout_wait_dialog, null);
        waitText = (TextView) view.findViewById(R.id.waitText);
        dialog.setContentView(view);
    }

    public void show(String title) {
        if (activity.isFinishing()) {
            return;
        }
        if (!TextUtils.isEmpty(title)) {
            waitText.setText(title);
            waitText.setVisibility(View.VISIBLE);
        } else {
            waitText.setVisibility(View.GONE);
        }
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        dialog.show();
    }

    public void cancel() {
        if (null != dialog && null != activity && !activity.isFinishing()) {
            dialog.cancel();
        }
    }

    public void delayCancel(int time) {
        if (null != handler) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            }, time);
        }
    }

    public void delayCancel(int time, final DelayCancelListener listener) {
        if (null != handler) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel();
                    if (null != listener) {
                        listener.onDeal();
                    }
                }
            }, time);
        }
    }

    public boolean isShowing() {
        if (null != dialog) {
            return dialog.isShowing();
        }
        return false;
    }

    public void setCancelable(boolean cancelable) {
        if (null != dialog) {
            dialog.setCancelable(cancelable);
        }
    }

    public interface DelayCancelListener {
        void onDeal();
    }
}
