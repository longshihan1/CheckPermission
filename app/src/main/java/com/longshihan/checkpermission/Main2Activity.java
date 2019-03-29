package com.longshihan.checkpermission;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.longshihan.permissionlibrary.NotifyBindingUtils;

public class Main2Activity extends Activity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView=findViewById(R.id.mian2text);
        if (NotifyBindingUtils.checkNotifySetting(this)){
            textView.setText("开启");
        }else {
            textView.setText("没有");
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyBindingUtils.openNotifyPermission(Main2Activity.this);
            }
        });
    }
}
