package com.yostar.uniqueid.util;

import android.app.Activity;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import java.util.Locale;

public class OtherUtils {
    /**
     * 获取设备宽度（px）
     */
    public static int getDeviceWidth(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return context.getWindowManager().getCurrentWindowMetrics().getBounds().width();
        } else {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
    }

    /**
     * 获取设备高度（px）
     */
    public static int getDeviceHeight(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return context.getWindowManager().getCurrentWindowMetrics().getBounds().width();
        } else {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
    }

    /**
     * 获取当前手机系统语言。
     */
    public static String getDeviceDefaultLanguage() {
        Locale locale = Locale.getDefault();
        //Locale.getDefault() 和 LocaleList.getAdjustedDefault().get(0) 同等效果，还不需要考虑版本问题，推荐直接使用
        return locale.getLanguage() + "-" + locale.getCountry();
    }

}
