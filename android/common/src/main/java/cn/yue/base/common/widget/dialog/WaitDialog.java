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

import cn.yue.base.common.R;
import cn.yue.base.common.image.ImageLoader;

/**
 * Description : 等待框
 * Created by yue on 2019/3/11
 */

public class WaitDialog {

    private Activity activity;
    private Dialog dialog;
    //    private AppProgressBar progressBar;
    private ImageView image;
    private TextView titleText;
    private Handler handler;

    public WaitDialog(Activity activity){
        this.activity = activity;
        handler = new Handler();
    }

    private void init(){
        if (null != activity){
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            View view = ((LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_wait_dialog, null);
            image = (ImageView) view.findViewById(R.id.cst_wait_dialog_img);
            ImageLoader.getLoader().loadGif(image, R.drawable.app_icon_wait);
//            progressBar = (AppProgressBar) view.findViewById(R.id.cst_wait_dialog_progressBar);
            titleText = (TextView) view.findViewById(R.id.cst_wait_dialog_text);
//            progressBar.setProgressBarBackgroundColor(Color.parseColor("#80000000"));
            dialog.setContentView(view);
//            progressBar.startAnimation();
        }
    }

    /**
     *
     * @param title
     * @param isProgress
     * @param imgRes     显示滚动条的时候该值传递null
     */
    public void show(String title, boolean isProgress, Integer imgRes){
        if (null == dialog){
            init();
        }
        if (null != activity && activity.isFinishing()){
            return;
        }

        if (null != dialog){
            if (dialog.isShowing()){
                dialog.cancel();
            }
            dialog.show();
        }
        setDialog(title, isProgress, imgRes);
    }

    private void setDialog(String title, boolean isProgress, Integer imgRes){
        if (null != titleText && !TextUtils.isEmpty(title)){
            titleText.setText(title);
            titleText.setVisibility(View.VISIBLE);
        } else {
            titleText.setVisibility(View.GONE);
        }
        if (null != image){
            if (isProgress){
                image.setVisibility(View.VISIBLE);
            }else{
                image.setVisibility(View.VISIBLE);
            }
        }
        if (null != imgRes && null != image && null != activity){
            image.setBackgroundDrawable(activity.getResources().getDrawable(imgRes));
        }
    }


    public void cancel(){
        if (null != dialog && null != activity && !activity.isFinishing()){
            dialog.cancel();
        }
    }

    public void delayCancel(int time){
        if (null != handler){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            }, time);
        }
    }

    public void delayCancel(int time, final DelayCancelListener listener){
        if (null != handler){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel();
                    if (null != listener){
                        listener.onDeal();
                    }
                }
            }, time);
        }
    }

    public boolean isShowing() {
        if(null != dialog) {
            return dialog.isShowing();
        }
        return false;
    }

    public void setCancelable(boolean cancelable){
        if (null != dialog){
            dialog.setCancelable(cancelable);
        }
    }

    public interface DelayCancelListener{
        void onDeal();
    }
}
