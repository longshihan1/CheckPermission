package com.longshihan.permissionlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;

import java.lang.ref.WeakReference;

/**
 * Created by LONGHE001.
 *
 * @time 2019/3/29 0029
 * @des 通知权限
 * @function
 */

public class NotifyBindingUtils {

    public static boolean checkNotifySetting(Context context) {
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
        if (contextWeakReference.get() != null) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(contextWeakReference.get());
            // areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
            boolean isOpened = manager.areNotificationsEnabled();
            if (isOpened) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static void openNotifyPermission(Context context) {
        try {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);

            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);

            // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"——然而这个玩意并没有卵用，我想对雷布斯说：I'm not ok!!!
            //  if ("MI 6".equals(Build.MODEL)) {
            //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            //      Uri uri = Uri.fromParts("package", getPackageName(), null);
            //      intent.setData(uri);
            //      // intent.setAction("com.android.settings/.SubSettings");
            //  }
            if (context instanceof Activity) {
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
                Intent intent = new Intent();
                //下面这种方案是直接跳转到当前应用的设置界面。
                //https://blog.csdn.net/ysy950803/article/details/71910806
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                if (context instanceof Activity) {
                    context.startActivity(intent);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

}
