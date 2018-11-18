package com.longshihan.permissionlibrary;

/**
 * Created by LONGHE.
 *
 * @time 2018/3/14 0014
 * @des 权限的回调
 * @function
 */

public interface CheckPermissionListener {
    //失败的权限
    void CheckPermissionResult(Permission permission);
}
