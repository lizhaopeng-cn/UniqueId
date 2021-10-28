package com.yostar.uniqueid;

import android.app.Application;
import android.content.Context;

public class UniqueIDApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getInstance() {
        return mContext;
    }
}
