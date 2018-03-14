package com.longshihan.permissionlibrary;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * 专用于判断权限的fragment
 */
public class PermissionFragment extends Fragment {
    private Set<String> permissions;
    private Set<String> successpermission;
    private Set<String> failurepermission;


    private CheckPermissionListener permissionListener;

    public PermissionFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions=new TreeSet<>();
        successpermission=new TreeSet<>();
        failurepermission=new TreeSet<>();
    }
    public void setPermissionListener(CheckPermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    public void checkPermission(String permission) {
        permissions.add(permission);
        notifyPermission();
    }

    public void checkPermissions(List<String> permissions) {
        this.permissions.addAll(permissions);
        notifyPermission();
    }

    /**
     * 判断权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void notifyPermission() {

    }


}
