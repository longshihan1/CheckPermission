package com.longshihan.permissionlibrary;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LONGHE.
 *
 * @time 2018/11/18 0018
 * @des
 * @function
 */

public class CheckPermissions {

    static final String TAG = CheckPermissions.class.getSimpleName();
    @VisibleForTesting
    Lazy<PermissionFragment> mRxPermissionsFragment;
    public CheckPermissions(@NonNull final FragmentActivity activity) {
        mRxPermissionsFragment = getLazySingleton(activity.getSupportFragmentManager());
    }

    public CheckPermissions(@NonNull final Fragment fragment) {
        mRxPermissionsFragment = getLazySingleton(fragment.getChildFragmentManager());
    }




    @NonNull
    private Lazy<PermissionFragment> getLazySingleton(@NonNull final FragmentManager fragmentManager) {
        return new Lazy<PermissionFragment>() {
            private PermissionFragment rxPermissionsFragment;

            @Override
            public synchronized PermissionFragment get() {
                if (rxPermissionsFragment == null) {
                    rxPermissionsFragment = getRxPermissionsFragment(fragmentManager);
                }
                return rxPermissionsFragment;
            }

        };
    }

    public void request(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            Log.d(TAG, "Checkpermissions.request requires at least one input permission");
            return;
        }
        List<Permission> list = new ArrayList<>(permissions.length);
        List<String> unrequestedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            mRxPermissionsFragment.get().log("Requesting permission " + permission);
            if (isGranted(permission)) {
                mRxPermissionsFragment.get().onGrantedListener(permission);
                continue;
            }

            if (isRevoked(permission)) {
                mRxPermissionsFragment.get().onRevokedListener(permission);
                continue;
            }
            boolean isExist = mRxPermissionsFragment.get().containsByPermission(permission);
            if (!isExist) {
                unrequestedPermissions.add(permission);
                mRxPermissionsFragment.get().setSubjectForPermission(permission);
            }
        }
        if (!unrequestedPermissions.isEmpty()) {
            String[] unrequestedPermissionsArray = unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]);
            requestPermissionsFromFragment(unrequestedPermissionsArray);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
   private void requestPermissionsFromFragment(String[] permissions) {
        mRxPermissionsFragment.get().log("requestPermissionsFromFragment " + TextUtils.join(", ", permissions));
        mRxPermissionsFragment.get().requestPermissions(permissions);
    }


    private PermissionFragment getRxPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
        PermissionFragment rxPermissionsFragment = findRxPermissionsFragment(fragmentManager);
        boolean isNewInstance = rxPermissionsFragment == null;
        if (isNewInstance) {
            rxPermissionsFragment = new PermissionFragment();
            fragmentManager
                    .beginTransaction()
                    .add(rxPermissionsFragment, TAG)
                    .commitNow();
        }
        return rxPermissionsFragment;
    }

    private PermissionFragment findRxPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
        return (PermissionFragment) fragmentManager.findFragmentByTag(TAG);
    }

    public CheckPermissions setLisener(CheckPermissionListener lisener) {
        mRxPermissionsFragment.get().setPermissionListener(lisener);
        return this;
    }

    public CheckPermissions setLogging(boolean logging) {
        mRxPermissionsFragment.get().setLogging(logging);
        return this;
    }


    /**
     * Returns true if the permission is already granted.
     * <p>
     * Always true if SDK >= 23.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isGranted(String permission) {
        return !isMarshmallow() || mRxPermissionsFragment.get().isGranted(permission);
    }

    /**
     * Returns true if the permission has been revoked by a policy.
     * <p>
     * Always false if SDK >= 23.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isRevoked(String permission) {
        return isMarshmallow() && mRxPermissionsFragment.get().isRevoked(permission);
    }

    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @FunctionalInterface
    public interface Lazy<V> {
        V get();
    }
}
