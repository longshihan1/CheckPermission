<h1>CheckPermission</h1>


代码参照RXPermission。仅自己练手学习使用。
去除Rxjava框架，只用原生部分。
使用方法：

`implementation 'com.github.longshihan1:CheckPermission:1.0.2'`

    CheckPermissions checkPermissions=new CheckPermissions(this);
            checkPermissions.setLisener(this);
            maintext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE);
                }
            });

添加JumpPermissionUtils 跳转设置的模块，配置多部手机类型


    private static final String MANUFACTURER_HUAWEI = "Huawei";//华为
    private static final String MANUFACTURER_MEIZU = "Meizu";//魅族
    private static final String MANUFACTURER_XIAOMI = "Xiaomi";//小米   测试ok
    private static final String MANUFACTURER_SONY = "Sony";//索尼
    private static final String MANUFACTURER_OPPO = "OPPO";
    private static final String MANUFACTURER_LG = "LG";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_SAMSUNG = "samsung";//三星    测试ok
    private static final String MANUFACTURER_LETV = "Letv";//乐视
    private static final String MANUFACTURER_ZTE = "ZTE";//中兴
    private static final String MANUFACTURER_YULONG = "YuLong";//酷派
    private static final String MANUFACTURER_LENOVO = "LENOVO";//联想




添加PermissionHelper 判断单一权限的模块。可在Application中使用。

 
  
    public static boolean hasPermission(Context context,String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


