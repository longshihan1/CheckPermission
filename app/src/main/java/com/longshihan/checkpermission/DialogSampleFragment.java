package com.longshihan.checkpermission;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longshihan.permissionlibrary.CheckPermissionListener;
import com.longshihan.permissionlibrary.CheckPermissions;
import com.longshihan.permissionlibrary.Permission;

import java.util.jar.Manifest;

/**
 * Created by LONGHE001.
 *
 * @time 2018/11/18 0018
 * @des
 * @function
 */

public class DialogSampleFragment extends DialogFragment implements CheckPermissionListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.activity_main2, null);
        final CheckPermissions checkPermissions=new CheckPermissions(this);
        checkPermissions.setLisener(this);
        TextView textView=view.findViewById(R.id.mian2text);
        builder.setView(view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions.request(android.Manifest.permission.CALL_PHONE);
            }
        });
        return builder.create();
    }

    @Override
    public void CheckPermissionResult(Permission permission) {
        Log.d("测试2",permission.toString());
    }
}
