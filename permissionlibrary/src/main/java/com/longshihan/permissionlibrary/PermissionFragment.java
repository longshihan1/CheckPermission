package com.longshihan.permissionlibrary;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import java.util.Set;
import java.util.TreeSet;


/**
 * 专用于判断权限的fragment
 */
public class PermissionFragment extends Fragment {
    private Set<String> permissionSets;
    private static final int PERMISSIONS_REQUEST_CODE = 42;
    private boolean mLogging;
    private CheckPermissionListener permissionListener;

    public PermissionFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        permissionSets=new TreeSet<>();
    }

    public void setPermissionListener(CheckPermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissions(@NonNull String[] permissions) {
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSIONS_REQUEST_CODE) return;
        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }
        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
    }

    void onRequestPermissionsResult(String permissions[], int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        for (int i = 0, size = permissions.length; i < size; i++) {
            log("onRequestPermissionsResult  " + permissions[i]);
            if (TextUtils.isEmpty(permissions[i])) {
                // No subject found
                Log.e(CheckPermissions.TAG, "CheckPermission.onRequestPermissionsResult invoked but didn't find the corresponding permission request.");
                return;
            }
            if (permissionSets.contains(permissions[i])) {
                permissionSets.remove(permissions[i]);
            }
            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            if (permissionListener!=null){
                permissionListener.CheckPermissionResult(new Permission(permissions[i],granted,shouldShowRequestPermissionRationale[i]));
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    boolean isGranted(String permission) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            throw new IllegalStateException("This fragment must be attached to an activity.");
        }
        return fragmentActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    boolean isRevoked(String permission) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            throw new IllegalStateException("This fragment must be attached to an activity.");
        }
        return fragmentActivity.getPackageManager().isPermissionRevokedByPolicy(permission, getActivity().getPackageName());
    }

    public void setLogging(boolean logging) {
        mLogging = logging;
    }

    public boolean containsByPermission(@NonNull String permission) {
        return permissionSets.contains(permission);
    }

    public void setSubjectForPermission(@NonNull String permission) {
        permissionSets.add(permission);
    }

    void log(String message) {
        if (mLogging) {
            Log.d(CheckPermissions.TAG, message);
        }
    }

    public void onGrantedListener(String permission) {
        if (permissionListener!=null){
            permissionListener.CheckPermissionResult(new Permission(permission, true, false));
        }
    }

    public void onRevokedListener(String permission) {
        if (permissionListener!=null){
            permissionListener.CheckPermissionResult(new Permission(permission, false, false));
        }
    }
}
