package com.yostar.uniqueid;

import android.app.Application;
import android.content.Context;

import com.yostar.udata.UData;
import com.yostar.udata.UDataApplication;

public class UniqueIDApplication extends UDataApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        UData.init(this);
    }
}
