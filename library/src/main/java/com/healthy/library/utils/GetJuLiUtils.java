package com.healthy.library.utils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

public class GetJuLiUtils {

    public static double getDistance(String longitude,String latitue,String longitude2,String latitue2){
        LatLng latLng2=new LatLng(Double.parseDouble(latitue),Double.parseDouble(longitude));
        LatLng latLng=new LatLng(Double.parseDouble(latitue2),Double.parseDouble(longitude2));
        float distance = AMapUtils.calculateLineDistance(latLng,latLng2);
        return distance;
    }
}
