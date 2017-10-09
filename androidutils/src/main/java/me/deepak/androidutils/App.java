package me.deepak.androidutils;

import android.app.Application;
import android.os.Bundle;

/**
 * Created by Deepak Mishra
 */

public class App extends Application {

    private static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static App getInstance() {
        return INSTANCE;
    }
}
