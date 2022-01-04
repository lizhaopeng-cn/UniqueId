package com.yostar.uniqueid;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.yostar.uniqueid.Interface.CallbackValue;
import com.yostar.uniqueid.model.InitReq;
import com.yostar.uniqueid.net.BaseService;
import com.yostar.uniqueid.util.DemoHelper;
import com.yostar.uniqueid.util.DevicesUtils;
import com.yostar.uniqueid.util.FileUtils;
import com.yostar.uniqueid.util.IDUtils;
import com.yostar.uniqueid.util.LocationUtils;
import com.yostar.uniqueid.util.LogUtil;
import com.yostar.uniqueid.util.NetUtils;
import com.yostar.uniqueid.util.OtherUtils;
import com.yostar.uniqueid.util.SPUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, DemoHelper.AppIdsUpdater {

    private EditText et_uuid;
    private EditText et_imei;
    private EditText et_android_id;
    private EditText et_mac;
    private EditText et_gaid;
    private EditText et_oaid;
    private EditText et_sn;
    private EditText et_ua;
    private EditText et_ip_in;
    private EditText et_ip_out;
    private EditText et_language;
    private EditText et_manufacturer;
    private EditText et_device_type;
    private EditText et_model;
    private EditText et_os_type;
    private EditText et_os_version;
    private EditText et_carrier;
    private EditText et_screen_height;
    private EditText et_screen_width;
    private EditText et_network_type;
    private EditText et_timezone_offset;
    private EditText et_is_first_open;
    private EditText et_environment;
    private EditText et_first_open_time;
    private EditText et_country;
    private EditText et_province;
    private EditText et_city;
    private EditText et_longitude;
    private EditText et_latitude;

    private TextView tv_device_id;
    private TextView tv_udid;
    private EditText et_account_id;

    private Button btn_permission;
    private Button btn_init;
    private Button btn_restore;
    private Button btn_get_id;
    private Button btn_account_id;
    private Button btn_login;

    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> fileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFirstOpen();
        bindView();
        setOnClick();
        setView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissions();
        }
        DemoHelper demoHelper = new DemoHelper(this);
        demoHelper.getDeviceIds(this);
    }

    private void setFirstOpen() {
        // 是否第一次打开 0否1是
        int isFirstOpen = SPUtils.getInstance().getInt(SDKConst.SP_IS_FIRST_OPEN, -1);
        if (isFirstOpen == -1) {
            SPUtils.getInstance().put(SDKConst.SP_IS_FIRST_OPEN, 1);
        } else if (isFirstOpen == 1) {
            SPUtils.getInstance().put(SDKConst.SP_IS_FIRST_OPEN, 0);
        }
        // 第一次打开时间
        long timeFirstOpen = SPUtils.getInstance().getLong(SDKConst.SP_TIME_FIRST_OPEN, 0);
        if (timeFirstOpen == 0) {
            SPUtils.getInstance().put(SDKConst.SP_TIME_FIRST_OPEN, new Date().getTime());
        }
    }

    private void bindView() {
        et_uuid = findViewById(R.id.et_uuid);
        et_imei = findViewById(R.id.et_imei);
        et_android_id = findViewById(R.id.et_android_id);
        et_mac = findViewById(R.id.et_mac);
        et_gaid = findViewById(R.id.et_gaid);
        et_oaid = findViewById(R.id.et_oaid);
        et_sn = findViewById(R.id.et_sn);
        et_ua = findViewById(R.id.et_ua);
        et_ip_in = findViewById(R.id.et_ip_in);
        et_ip_out = findViewById(R.id.et_ip_out);
        et_language = findViewById(R.id.et_language);
        et_manufacturer = findViewById(R.id.et_manufacturer);
        et_device_type = findViewById(R.id.et_device_type);
        et_model = findViewById(R.id.et_model);
        et_os_type = findViewById(R.id.et_os_type);
        et_os_version = findViewById(R.id.et_os_version);
        et_carrier = findViewById(R.id.et_carrier);
        et_screen_height = findViewById(R.id.et_screen_height);
        et_screen_width = findViewById(R.id.et_screen_width);
        et_network_type = findViewById(R.id.et_network_type);
        et_timezone_offset = findViewById(R.id.et_timezone_offset);
        et_is_first_open = findViewById(R.id.et_is_first_open);
        et_environment = findViewById(R.id.et_environment);
        et_first_open_time = findViewById(R.id.et_first_open_time);
        et_country = findViewById(R.id.et_country);
        et_province = findViewById(R.id.et_province);
        et_city = findViewById(R.id.et_city);
        et_longitude = findViewById(R.id.et_longitude);
        et_latitude = findViewById(R.id.et_latitude);

        tv_device_id = findViewById(R.id.tv_device_id);
        tv_udid = findViewById(R.id.tv_udid);
        et_account_id = findViewById(R.id.et_account_id);

        btn_permission = findViewById(R.id.btn_permission);
        btn_init = findViewById(R.id.btn_init);
        btn_restore = findViewById(R.id.btn_restore);
        btn_get_id = findViewById(R.id.btn_get_id);
        btn_account_id = findViewById(R.id.btn_account_id);
        btn_login = findViewById(R.id.btn_login);
    }

    private void setOnClick() {
        btn_permission.setOnClickListener(MainActivity.this);
        btn_init.setOnClickListener(MainActivity.this);
        btn_restore.setOnClickListener(MainActivity.this);
        btn_get_id.setOnClickListener(MainActivity.this);
        btn_account_id.setOnClickListener(MainActivity.this);
        btn_login.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        BaseService baseService = new BaseService();
        switch (v.getId()) {
            case R.id.btn_permission:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getPermissions();
                }
                break;
            case R.id.btn_init:
                baseService.netInit(getInitList(), MainActivity.this);
                break;
            case R.id.btn_restore:
                setView();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getPermissions();
                }
                break;
            case R.id.btn_get_id:
                tv_device_id.setText(getDeviceId());
                tv_udid.setText(SPUtils.getInstance().getString(SDKConst.SP_UD_ID));
                break;
            case R.id.btn_account_id:
                Random ran = new Random();
                int num = ran.nextInt(999999);
                et_account_id.setText(String.format("%06d", num));
                break;
            case R.id.btn_login:
                String accoundId = et_account_id.getText().toString();
                Pattern pattern = Pattern.compile("\\d{6}");
                if (pattern.matcher(accoundId).matches()) {
                    baseService.netLogin(accoundId, MainActivity.this);
                } else {
                    Toast.makeText(this, "account_id需为6位数字", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setView() {
        et_uuid.setText(getRandomUUID());
        et_imei.setText(IDUtils.getImeiOnly(this, 0));
        et_android_id.setText(IDUtils.getAndroidId(this));
        et_mac.setText(IDUtils.getMacAddress(this));
        et_sn.setText(IDUtils.getDeviceSN(this));
        et_ua.setText(NetUtils.getUserAgent(this));
        et_ip_in.setText(NetUtils.getIpAddress(this));

        et_language.setText(OtherUtils.getDeviceDefaultLanguage());
        et_manufacturer.setText(DevicesUtils.getDeviceManufacturer());
        et_device_type.setText(OtherUtils.isPad(this));
        et_model.setText(DevicesUtils.getDeviceModel());
        et_os_type.setText("Android");
        et_os_version.setText(DevicesUtils.getDeviceRelease());
        et_carrier.setText(OtherUtils.getNetworkOperatorName());
        et_screen_height.setText(String.valueOf(OtherUtils.getDeviceHeight(this)));
        et_screen_width.setText(String.valueOf(OtherUtils.getDeviceWidth(this)));
        et_network_type.setText(OtherUtils.getNetworkState(this));
        et_timezone_offset.setText(OtherUtils.getTimeZone());
        et_is_first_open.setText(String.valueOf(SPUtils.getInstance().getInt(SDKConst.SP_IS_FIRST_OPEN, -1)));
        et_first_open_time.setText(String.valueOf(SPUtils.getInstance().getLong(SDKConst.SP_TIME_FIRST_OPEN, 0)));
        et_country.setText(LocationUtils.getInstance(this).getCountry());
        et_province.setText(LocationUtils.getInstance(this).getProvince());
        et_city.setText(LocationUtils.getInstance(this).getCity());
        et_longitude.setText(LocationUtils.getInstance(this).getLongitude());
        et_latitude.setText(LocationUtils.getInstance(this).getLatitude());

        tv_device_id.setText(getDeviceId());
        tv_udid.setText(SPUtils.getInstance().getString(SDKConst.SP_UD_ID));

        NetUtils.getOutNetIP(new CallbackValue() {
            @Override
            public void onCallback(String value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        et_ip_out.setText(value);
                    }
                });
            }
        });

        IDUtils.getGAID(this, new CallbackValue() {
            @Override
            public void onCallback(String value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        et_gaid.setText(value);
                    }
                });
            }
        });
    }

    private List<InitReq.TypeData> getInitList() {
        List<InitReq.TypeData> typeData = new ArrayList<>();
        if (SPUtils.getInstance().getInt(SDKConst.SP_IS_FIRST_OPEN, -1) == 1 && !TextUtils.isEmpty(tv_device_id.getText().toString())) {
            typeData.add(new InitReq.TypeData("device_id", tv_device_id.getText().toString()));
        }
        typeData.add(new InitReq.TypeData("uuid", et_uuid.getText().toString()));
        typeData.add(new InitReq.TypeData("imei", et_imei.getText().toString()));
        typeData.add(new InitReq.TypeData("android_id", et_android_id.getText().toString()));
        typeData.add(new InitReq.TypeData("mac", et_mac.getText().toString()));
        typeData.add(new InitReq.TypeData("sn", et_sn.getText().toString()));
        typeData.add(new InitReq.TypeData("ua", et_ua.getText().toString()));
        typeData.add(new InitReq.TypeData("gaid", et_gaid.getText().toString()));
        typeData.add(new InitReq.TypeData("oaid", et_oaid.getText().toString()));
        typeData.add(new InitReq.TypeData("ip_in", et_ip_in.getText().toString()));
        typeData.add(new InitReq.TypeData("ip_out", et_ip_out.getText().toString()));
        typeData.add(new InitReq.TypeData("ua", et_ua.getText().toString()));
        typeData.add(new InitReq.TypeData("language", et_language.getText().toString()));
        typeData.add(new InitReq.TypeData("manufacturer", et_manufacturer.getText().toString()));
        typeData.add(new InitReq.TypeData("device_type", et_device_type.getText().toString()));
        typeData.add(new InitReq.TypeData("model", et_model.getText().toString()));
        typeData.add(new InitReq.TypeData("os_type", et_os_type.getText().toString()));
        typeData.add(new InitReq.TypeData("os_version", et_os_version.getText().toString()));
        typeData.add(new InitReq.TypeData("carrier", et_carrier.getText().toString()));
        typeData.add(new InitReq.TypeData("screen_height", et_screen_height.getText().toString()));
        typeData.add(new InitReq.TypeData("screen_width", et_screen_width.getText().toString()));
        typeData.add(new InitReq.TypeData("network_type", et_network_type.getText().toString()));
        typeData.add(new InitReq.TypeData("timezone_offset", et_timezone_offset.getText().toString()));
        typeData.add(new InitReq.TypeData("first_open_time", et_first_open_time.getText().toString()));
        typeData.add(new InitReq.TypeData("is_first_open", et_is_first_open.getText().toString()));
        typeData.add(new InitReq.TypeData("country", et_country.getText().toString()));
        typeData.add(new InitReq.TypeData("province", et_province.getText().toString()));
        typeData.add(new InitReq.TypeData("city", et_city.getText().toString()));
        typeData.add(new InitReq.TypeData("longitude", et_longitude.getText().toString()));
        typeData.add(new InitReq.TypeData("latitude", et_latitude.getText().toString()));
        return typeData;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissions() {
        // R以上只同意ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION就够了，读写权限请不请求都行
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //检查是否已经有权限
            if (!Environment.isExternalStorageManager()) {
                LogUtil.i("ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION getPermissions false");
                fileLauncher.launch(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
            } else {
                LogUtil.i("ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION getPermissions true");
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LogUtil.i("All Permissions getPermissions true");
            setView();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
            LogUtil.i("READ_PHONE_STATE getPermissions false");
            requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_PHONE_STATE});
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            LogUtil.i("READ_EXTERNAL_STORAGE getPermissions false");
            requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            LogUtil.i("WRITE_EXTERNAL_STORAGE getPermissions false");
            requestPermissionLauncher.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            LogUtil.i("ACCESS_COARSE_LOCATION getPermissions false");
            requestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION});
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            LogUtil.i("ACCESS_FINE_LOCATION getPermissions false");
            requestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
        } else {
            LogUtil.i("some getPermissions false");
            requestPermissionLauncher.launch(new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION});
        }
    }

    private void selectedPermission() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), map -> {
            if (map.size() > 0){
                if (map.get(Manifest.permission.READ_PHONE_STATE)) {
                    LogUtil.i("READ_PHONE_STATE selectedPermission true");
                    et_imei.setText(IDUtils.getIMEI1(this));
                    et_sn.setText(IDUtils.getDeviceSN(this));
                } else {
                    LogUtil.i("READ_PHONE_STATE selectedPermission false");
                    Toast.makeText(this,"手机状态权限未授权，请自行前往设置同意授权",Toast.LENGTH_SHORT).show();
                }
                if (map.get(Manifest.permission.ACCESS_COARSE_LOCATION) || map.get(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    LogUtil.i("ACCESS_COARSE_LOCATION ACCESS_FINE_LOCATION selectedPermission true");
                    et_country.setText(LocationUtils.getInstance(this).getCountry());
                    et_province.setText(LocationUtils.getInstance(this).getProvince());
                    et_city.setText(LocationUtils.getInstance(this).getCity());
                    et_longitude.setText(LocationUtils.getInstance(this).getLongitude());
                    et_latitude.setText(LocationUtils.getInstance(this).getLatitude());
                } else {
                    LogUtil.i("ACCESS_COARSE_LOCATION ACCESS_FINE_LOCATION selectedPermission false");
                    Toast.makeText(this,"地理位置权限未授权，请自行前往设置同意授权",Toast.LENGTH_SHORT).show();
                }
                if (map.get(Manifest.permission.READ_EXTERNAL_STORAGE) || map.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    LogUtil.i("READ_EXTERNAL_STORAGE WRITE_EXTERNAL_STORAGE selectedPermission true");
                    et_uuid.setText(getRandomUUID());
                    tv_device_id.setText(getDeviceId());
                } else {
                    LogUtil.i("READ_EXTERNAL_STORAGE WRITE_EXTERNAL_STORAGE selectedPermission false");
                    Toast.makeText(this,"存储文件权限未授权，请自行前往设置同意授权",Toast.LENGTH_SHORT).show();
                }
            } else {
                LogUtil.i("All selectedPermission true");
                Toast.makeText(this,"所有权限已授权",Toast.LENGTH_SHORT).show();
            }
        });

        fileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    LogUtil.i("ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION selectedPermission true");
                    et_uuid.setText(getRandomUUID());
                } else {
                    LogUtil.i("ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION selectedPermission false");
                }
            }
        });
    }

    private String getDeviceId() {
        String deviceId = SPUtils.getInstance().getString(SDKConst.SP_DEVICE_ID);
        if (TextUtils.isEmpty(deviceId)) {
            LogUtil.i("getDeviceId deviceid null");
            deviceId = FileUtils.readData(FileUtils.FILE_NAME_DEVICE_ID);
        } else {
            LogUtil.i("getDeviceId deviceid" + deviceId);
        }
        return deviceId;
    }

    private String getRandomUUID() {
        String uuid = FileUtils.readData(FileUtils.FILE_NAME_UUID);
        if (TextUtils.isEmpty(uuid)) {
            LogUtil.i("getRandomUUID uuid null");
            FileUtils.writeData(FileUtils.FILE_NAME_UUID, UUID.randomUUID().toString());
            return FileUtils.readData(FileUtils.FILE_NAME_UUID);
        } else {
            LogUtil.i("getRandomUUID uuid " + uuid);
            return uuid;
        }
    }

    @Override
    public void onIdsValid(String ids) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et_oaid.setText(ids);
            }
        });
    }
}