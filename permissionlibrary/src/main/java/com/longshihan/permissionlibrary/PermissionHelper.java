package com.longshihan.permissionlibrary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by LONGHE001.
 *
 * @time 2019/1/8 0008
 * @des 权限辅助类，只有判断权限的，可用于Application里面处理判断有无权限
 * @function
 */
public class PermissionHelper {
    private PermissionHelper() {
    }

    private static boolean hasStoragePermission = false;

    /**
     * 判断有没有存储权限
     * @param context
     * @return
     */
    public static boolean hasStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        //如果已经有权限了，就不再检查
        if (hasStoragePermission) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            hasStoragePermission = false;
            return false;
        }
        hasStoragePermission = true;
        return true;
    }

    /**
     * 单一可用于判断权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Context context,String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

}
