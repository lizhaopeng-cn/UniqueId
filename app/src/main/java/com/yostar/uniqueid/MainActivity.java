package com.yostar.uniqueid;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yostar.uniqueid.Interface.CallbackGAID;
import com.yostar.uniqueid.net.BaseService;
import com.yostar.uniqueid.util.DevicesUtils;
import com.yostar.uniqueid.util.IDUtils;
import com.yostar.uniqueid.util.MemoryUtils;
import com.yostar.uniqueid.util.OtherUtils;
import com.yostar.uniqueid.util.SPUtils;


public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText etOldAccountId = findViewById(R.id.et_old_account_id);
        TextView tvDeviceID = findViewById(R.id.tv_device_id);
        TextView tvAttrID = findViewById(R.id.tv_attr_id);
        findViewById(R.id.btn_permission).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermissions();
            }
        });
        setView();
        setPermissionView();
        findViewById(R.id.btn_init).setOnClickListener(v -> {
            BaseService baseService = new BaseService();
            baseService.netInit();
        });

        findViewById(R.id.btn_login).setOnClickListener(v -> {
            BaseService baseService = new BaseService();
            baseService.netLogin(etOldAccountId.getText().toString());
        });

        findViewById(R.id.btn_get_id).setOnClickListener(v -> {
            tvDeviceID.setText(SPUtils.getInstance().getString(BaseService.SP_DEVICE_ID));
            tvAttrID.setText(SPUtils.getInstance().getString(BaseService.SP_DEVICE_ID));
        });
    }

    private void setView() {
        ((TextView)findViewById(R.id.tv_android_id)).setText(IDUtils.getAndroidId(this));
        ((TextView)findViewById(R.id.tv_wifi_mac)).setText(IDUtils.getMacAddress(this));
        ((TextView)findViewById(R.id.tv_bluetooth_mac)).setText(IDUtils.getBluetoothMac(this));
        ((TextView)findViewById(R.id.tv_uuid)).setText(IDUtils.getUUID());
        ((TextView)findViewById(R.id.tv_ip)).setText(IDUtils.getIpAddress(this));
        ((TextView)findViewById(R.id.tv_ua)).setText(IDUtils.getUserAgent(this));
        ((TextView)findViewById(R.id.tv_sn)).setText(IDUtils.getDeviceSN(this));
        IDUtils.getGAID(this, new CallbackGAID() {
            @Override
            public void onCallback(String gaid) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.tv_gaid)).setText(gaid);
                    }
                });
            }
        });
        ((TextView)findViewById(R.id.tv_other)).setText(getDeviceAllInfo(this));

    }

    private void setPermissionView() {
        ((TextView)findViewById(R.id.tv_imei)).setText(IDUtils.getIMEI1(this));
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissions() {
        //4、检测权限也需要判断多个，用&&符号
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            setView();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
        || shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("备份通讯录需要访问 “通讯录” 和 “外部存储器”，请到 “应用信息 -> 权限” 中授予！");
//            builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            builder.setNegativeButton("取消", null);
//            builder.show();

//            requestPermissions(,
//                    new String[] { Manifest.permission.REQUESTED_PERMISSION },
//                    REQUEST_CODE);
//            Toast.makeText(this,"您有未授权的权限，请自行前往设置同意授权",Toast.LENGTH_SHORT).show();
            requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE});

        } else {
            requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE});
        }
    }

    private void selectedPermission() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), map -> {
            // 3、isGranted的类型由boolean变成map，map的键值对是<String,Boolean>
            //String对应的是权限，Boolean对应的是是否授权，需要判断处理
            if (map.size() > 0){
                if (!map.get(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this,"读取内存权限未授权，请自行前往设置同意授权",Toast.LENGTH_SHORT).show();
                } else {
                    ((TextView)findViewById(R.id.tv_imei)).setText(IDUtils.getIMEI1(this));
                }
                if (!map.get(Manifest.permission.READ_PHONE_STATE)) {
                    Toast.makeText(this,"手机状态权限未授权，请自行前往设置同意授权",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,"所有权限已授权",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void call() {
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        intent.setData(Uri.parse("tel:1234567890"));
//        startActivity(intent);
    }


//    requestPermissions
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 &&
//                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission is granted. Continue the action or workflow
//                    // in your app.
//                } else {
//                    // Explain to the user that the feature is unavailable because
//                    // the features requires a permission that the user has denied.
//                    // At the same time, respect the user's decision. Don't link to
//                    // system settings in an effort to convince the user to change
//                    // their decision.
//                }
//                return;
//        }
//        // Other 'case' lines to check for other
//        // permissions this app might request.
//    }

    public static String getDeviceAllInfo(Context context) {

        return
//                "\n\n1. IMEI:\n\t\t" + getIMEI(context)
//                +
                "\n\n2. 设备宽度:\n\t\t" + OtherUtils.getDeviceWidth((Activity) context)
                        + "\n\n3. 设备高度:\n\t\t" + OtherUtils.getDeviceHeight((Activity) context)
                        + "\n\n4. 是否有内置SD卡:\n\t\t" + MemoryUtils.isSDCardMount()
                        + "\n\n5. RAM 信息:\n\t\t" + MemoryUtils.getRAMInfo(context)
                        + "\n\n6. 内部存储信息\n\t\t" + MemoryUtils.getStorageInfo(context, 0)
                        + "\n\n7. SD卡 信息:\n\t\t" + MemoryUtils.getStorageInfo(context, 1)
//                + "\n\n8. 是否联网:\n\t\t" + Utils.isNetworkConnected(context)
//                + "\n\n9. 网络类型:\n\t\t" + Utils.GetNetworkType(context)
                        + "\n\n10. 系统默认语言:\n\t\t" + OtherUtils.getDeviceDefaultLanguage()

                        + "\n\n11. ID:\n\t\t" + DevicesUtils.getDeviceId()
                        + "\n\n12. DISPLAY:\n\t\t" + DevicesUtils.getDeviceDisplay()
                        + "\n\n13. PRODUCT:\n\t\t" + DevicesUtils.getDeviceProduct()
                        + "\n\n14. DEVICE:\n\t\t" + DevicesUtils.getDeviceDevice()
                        + "\n\n15. BOARD:\n\t\t" + DevicesUtils.getDeviceBoard()
                        + "\n\n16. SUPPORTED_ABIS:\n\t\t" + DevicesUtils.getDeviceAbis()
                        + "\n\n17. MANUFACTURER:\n\t\t" + DevicesUtils.getDeviceManufacturer()
                        + "\n\n18. BRAND:\n\t\t" + DevicesUtils.getDeviceBrand()
                        + "\n\n19. MODEL:\n\t\t" + DevicesUtils.getDeviceModel()
                        + "\n\n20. SOC_MANUFACTURER:\n\t\t" + DevicesUtils.getDeviceSocManufacturer()
                        + "\n\n21. SOC_MODEL:\n\t\t" + DevicesUtils.getDeviceSocModel()
                        + "\n\n22. BOOTLOADER:\n\t\t" + DevicesUtils.getDeviceBootloader()
                        + "\n\n23. RADIO_VERSION:\n\t\t" + DevicesUtils.getDeviceRadioVersion()
                        + "\n\n24. HARDWARE:\n\t\t" + DevicesUtils.getDeviceHardware()
                        + "\n\n25. SKU:\n\t\t" + DevicesUtils.getDeviceSKU()
                        + "\n\n26. ODM_SKU:\n\t\t" + DevicesUtils.getDeviceOdmSKU()
                        + "\n\n27. TYPE:\n\t\t" + DevicesUtils.getDeviceType()
                        + "\n\n28. TAGS:\n\t\t" + DevicesUtils.getDeviceTags()
                        + "\n\n29. FINGERPRINT:\n\t\t" + DevicesUtils.getDeviceFubgerprint()
                        + "\n\n30. TIME:\n\t\t" + DevicesUtils.getDeviceTime()
                        + "\n\n31. USER:\n\t\t" + DevicesUtils.getDeviceUser()
                        + "\n\n32. HOST:\n\t\t" + DevicesUtils.getDeviceHost()
                        + "\n\n33. INCREMENTAL:\n\t\t" + DevicesUtils.getDeviceIncremental()
                        + "\n\n34. RELEASE:\n\t\t" + DevicesUtils.getDeviceRelease()
                        + "\n\n35. RELEASE_OR_CODENAME:\n\t\t" + DevicesUtils.getDeviceReleaseOrCodename()
                        + "\n\n36. BASE_OS:\n\t\t" + DevicesUtils.getDeviceBaseOS()
                        + "\n\n37. SECURITY_PATCH:\n\t\t" + DevicesUtils.getDeviceSecurityPatch()
                        + "\n\n38. MEDIA_PERFORMANCE_CLASS:\n\t\t" + DevicesUtils.getDeviceMediaPerformanceClass()
                        + "\n\n39. SDK_INT:\n\t\t" + DevicesUtils.getDeviceSdkInt()
                        + "\n\n40. PREVIEW_SDK_INT:\n\t\t" + DevicesUtils.getDevicePreviewSdkInt();

    }
}