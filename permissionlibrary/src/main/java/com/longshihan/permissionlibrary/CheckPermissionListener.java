package com.longshihan.permissionlibrary;

import android.Manifest;

import java.util.List;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/14 0014
 * @des 权限的回调
 * @function
 */

public interface CheckPermissionListener {
    //失败的权限
    void CheckFailurePermission(List<String> permissionList);
    //成功的权限
    void CheckSuccessPermission(List<String> permissionList);
}
