package me.deepak.androidutils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Deepak Mishra
 */

public class DeviceInfo {

    public enum Orientation{
        LAND, PORTRAIT
    }

    public static Orientation getOrientation(){

        Context context = App.getInstance();
        int o = context.getResources().getConfiguration().orientation;
        if(o == Configuration.ORIENTATION_LANDSCAPE) return Orientation.LAND;
        else if(o == Configuration.ORIENTATION_PORTRAIT) return Orientation.PORTRAIT;
        else return null;
    }

    public static boolean isInLandscape(){
        return Orientation.LAND.equals(getOrientation());
    }

    public static boolean isInPortrait(){
        return Orientation.PORTRAIT.equals(getOrientation());
    }
}
