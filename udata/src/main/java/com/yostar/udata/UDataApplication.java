package com.yostar.udata;

import android.app.Application;
import android.content.Context;

public class UDataApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        System.loadLibrary("msaoaidsec");
    }

    public static void setInstance(Context context) {
        mContext = context;
    }

    public static Context getInstance() {
        return mContext;
    }
}
