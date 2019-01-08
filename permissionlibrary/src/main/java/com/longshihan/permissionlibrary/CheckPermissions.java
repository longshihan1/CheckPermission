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
    Lazy<PermissionFragment> mPermissionsFragment;

    public CheckPermissions(@NonNull final FragmentActivity activity) {
        mPermissionsFragment = getLazySingleton(activity.getSupportFragmentManager());
    }

    public CheckPermissions(@NonNull final Fragment fragment) {
        mPermissionsFragment = getLazySingleton(fragment.getChildFragmentManager());
    }


    @NonNull
    private Lazy<PermissionFragment> getLazySingleton(@NonNull final FragmentManager fragmentManager) {
        return new Lazy<PermissionFragment>() {
            private PermissionFragment rxPermissionsFragment;

            @Override
            public synchronized PermissionFragment get() {
                if (rxPermissionsFragment == null) {
                    rxPermissionsFragment = getPermissionsFragment(fragmentManager);
                }
                return rxPermissionsFragment;
            }

        };
    }

    public void request(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return;
        }
        if (mPermissionsFragment==null||mPermissionsFragment.get()==null){
            return;
        }
        List<Permission> list = new ArrayList<>(permissions.length);
        List<String> unrequestedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (TextUtils.isEmpty(permission)){
                break;
            }
            mPermissionsFragment.get().log("Requesting permission " + permission);
            if (isGranted(permission)) {
                mPermissionsFragment.get().onGrantedListener(permission);
                continue;
            }
            if (isRevoked(permission)) {
                mPermissionsFragment.get().onRevokedListener(permission);
                continue;
            }
            boolean isExist = mPermissionsFragment.get().containsByPermission(permission);
            if (!isExist) {
                unrequestedPermissions.add(permission);
                mPermissionsFragment.get().setSubjectForPermission(permission);
            }
        }
        if (!unrequestedPermissions.isEmpty()) {
            String[] unrequestedPermissionsArray = unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]);
            requestPermissionsFromFragment(unrequestedPermissionsArray);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissionsFromFragment(String[] permissions) {
        mPermissionsFragment.get().log("requestPermissionsFromFragment " + TextUtils.join(", ", permissions));
        mPermissionsFragment.get().requestPermissions(permissions);
    }


    private PermissionFragment getPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
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
        if (mPermissionsFragment==null||mPermissionsFragment.get()==null){
            return this;
        }
        mPermissionsFragment.get().setPermissionListener(lisener);
        return this;
    }

    public CheckPermissions setLogging(boolean logging) {
        if (mPermissionsFragment==null||mPermissionsFragment.get()==null){
            return this;
        }
        mPermissionsFragment.get().setLogging(logging);
        return this;
    }


    /**
     * Returns true if the permission is already granted.
     * <p>
     * Always true if SDK >= 23.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isGranted(String permission) {
        return !isMarshmallow() || mPermissionsFragment.get().isGranted(permission);
    }

    /**
     * Returns true if the permission has been revoked by a policy.
     * <p>
     * Always false if SDK >= 23.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isRevoked(String permission) {
        return isMarshmallow() && mPermissionsFragment.get().isRevoked(permission);
    }

    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @FunctionalInterface
    public interface Lazy<V> {
        V get();
    }
}
