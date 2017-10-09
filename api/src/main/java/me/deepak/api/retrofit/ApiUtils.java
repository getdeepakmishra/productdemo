package me.deepak.api.retrofit;

import static me.deepak.api.retrofit.WalmartApiInterface.BASE_URL;

/**
 * Created by Deepak Mishra
 */

public class ApiUtils {
    public static WalmartApiInterface getWalmartApi(){
        return RetrofitClient.getClient(BASE_URL).create(WalmartApiInterface.class);
    }
}