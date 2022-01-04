package com.yostar.udata;

import android.content.Context;

public class UData {
    public static void init(Context context) {
        UDataApplication.setInstance(context.getApplicationContext());
    }
}
