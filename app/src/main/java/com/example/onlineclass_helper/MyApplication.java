package com.example.onlineclass_helper;

import android.app.Application;
import android.content.Context;

/**
 * Author: Jiali
 * Date: Nov. 2018
 * Description: format MainActivity.context();
 */
public class MyApplication extends Application {

    private static Context thecontext;

    public void onCreate() {
        super.onCreate();
        MyApplication.thecontext = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.thecontext;
    }
}