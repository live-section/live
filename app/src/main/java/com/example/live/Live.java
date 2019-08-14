package com.example.live;

import android.app.Application;
import android.content.Context;

public class Live extends Application
{
    private static Context context;

    public void onCreate() {
        super.onCreate();
        Live.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return Live.context;
    }
}
