package com.longshihan.checkpermission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.longshihan.permissionlibrary.CheckPermissionListener;
import com.longshihan.permissionlibrary.CheckPermissions;
import com.longshihan.permissionlibrary.Permission;

public class MainActivity extends AppCompatActivity implements CheckPermissionListener {

    TextView maintext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maintext=findViewById(R.id.maintext);
        final CheckPermissions checkPermissions=new CheckPermissions(this);
        checkPermissions.setLisener(this);
        maintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE);
//                DialogSampleFragment dialogSampleFragment=new DialogSampleFragment();
//                dialogSampleFragment.show(getSupportFragmentManager(),"ceshi");
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void CheckPermissionResult(Permission permission) {
        Log.d("测试",permission.toString());
    }
}
