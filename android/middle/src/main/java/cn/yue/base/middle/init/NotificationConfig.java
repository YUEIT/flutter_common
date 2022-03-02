package cn.yue.base.middle.init;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import cn.yue.base.common.utils.Utils;
import cn.yue.base.common.utils.device.NotificationUtils;
import cn.yue.base.middle.R;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

public class NotificationConfig {

    private static final String CHANNEL_ID = "YUE_CHANNEL";

    public static void initChannel() {
        NotificationUtils.ChannelConfig channelConfig = new NotificationUtils.ChannelConfig(CHANNEL_ID, "通知", NotificationUtils.IMPORTANCE_DEFAULT);
        NotificationUtils.initChannelConfig(channelConfig);
    }

    public static void notify(int id, Notification notification) {
        android.app.NotificationManager nm = (android.app.NotificationManager) Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(id, notification);
        }
    }

    public static void notify(int id, String title, String content) {
        NotificationCompat.Builder builder = getNotification(title, content);
        android.app.NotificationManager nm = (android.app.NotificationManager) Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(id, builder.build());
        }
    }

    public static void notify(int id, String title, String content, Intent intent) {
        NotificationCompat.Builder builder = getNotification(title, content);
        PendingIntent pendingIntent = PendingIntent.getActivities(Utils.getApp(), 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        android.app.NotificationManager nm = (android.app.NotificationManager) Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(id, builder.build());
        }
    }

    public static NotificationCompat.Builder getNotification(String title, String content) {
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(Utils.getApp(), CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(Utils.getApp());
            builder.setPriority(PRIORITY_DEFAULT);
        }
        //标题
        builder.setContentTitle(title);
        //文本内容
        builder.setContentText(content);
        //小图标
//        builder.setSmallIcon(R.drawable.icon_share_qq_friend);
        //设置点击信息后自动清除通知
        builder.setAutoCancel(true);
        return builder;
    }

}
