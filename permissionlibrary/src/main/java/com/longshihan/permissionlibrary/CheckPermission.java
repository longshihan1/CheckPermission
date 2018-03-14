package com.longshihan.permissionlibrary;

import android.app.Activity;
import android.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/14 0014
 * @des 用于判断是否有权限
 * @function
 */

public class CheckPermission {
    static final String TAG="CheckPermission";
    private Activity activity;
    private CheckPermissionListener listener;
    private PermissionFragment fragment;
    public static class Builder {
        private CheckPermission target;

        public Builder() {
            target = new CheckPermission();
        }

        public Builder setActivity(Activity activity) {
            target.activity = activity;
            return this;
        }
        public Builder setPermissionListener(CheckPermissionListener listener){
            target.listener=listener;
            return this;
        }

        public CheckPermission build(){
            target.fragment=findPermissionFragment(target.activity);
            boolean isNewInstance=target.fragment==null;
            if (isNewInstance){
                target.fragment=new PermissionFragment();
                target.fragment.setPermissionListener(target.listener);
                FragmentManager fragmentManager = target.activity.getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .add(target.fragment, TAG)
                        .commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
            return target;
        }

        private PermissionFragment findPermissionFragment(Activity activity) {
            if (activity==null){
                Log.d(TAG,"Activity不能为空");
                return null;
            }
            return (PermissionFragment) activity.getFragmentManager().findFragmentByTag(TAG);
        }
    }

    public void checkPermission(String permission){
        if (!TextUtils.isEmpty(permission)){
            fragment.checkPermission(permission);
        }
    }
    public void checkPermissions(List<String> permissions){
        if (permissions!=null&&!permissions.isEmpty()){
            fragment.checkPermissions(permissions);
        }
    }

}
