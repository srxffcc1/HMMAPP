package com.healthy.library.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;

/**
 * @author Li
 * @date 2019/03/12 09:34
 * @des 导航
 */

public class NavigateUtils {
    /**
     * 导航
     *
     * @param context  上下文
     * @param location 显示地址名称
     * @param lat      目的地维度
     * @param lng      目的地经度
     */
    public static void navigate(final Context context, final String location, final String lat, final String lng) {
        Log.e("daohang", "navigate: " + location + "-" + lat + "-" + lng);
        try {
            String gaodeMap = "com.autonavi.minimap";
            String baiduMap = "com.baidu.BaiduMap";
            final Intent mapIntent = new Intent();
//            intent://map/direction
            final String baiduUri = String.format("intent://map/direction?destination=%s" +
                    "|latlng:%s,%s&src=andr.health.mall", location, lat, lng);
            final String gaodeUri = String.format("androidamap://navi?sourceApplication" +
                    "=mall&poiname=%s&lat=%s&lon=%s&dev=1&style=2", location, lat, lng);
            boolean hasBaidu = PackageUtils.isAppInstalled(baiduMap, context);
            boolean hasGaode = PackageUtils.isAppInstalled(gaodeMap, context);
            if (!hasBaidu && !hasGaode) {
                Toast.makeText(context, "导航仅支持百度地图、高德地图", Toast.LENGTH_SHORT).show();
            } else if (hasGaode && hasBaidu) {
                new AlertDialog.Builder(context)
                        .setItems(new String[]{"百度地图", "高德地图"},
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            Intent intent = null;
                                            try {
                                                intent = Intent.getIntent("intent://map/direction?destination=latlng:" + lat + "," + lng + "|name:" + location + "&mode=driving&src=andr.health.mall#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
                                            } catch (URISyntaxException e) {
                                                e.printStackTrace();
                                            }
                                            context.startActivity(intent);
                                        } else {
                                            mapIntent.setData(Uri.parse(gaodeUri));
                                            context.startActivity(mapIntent);
                                        }
                                    }
                                }).setTitle("请选择导航地图")
                        .create().show();
            } else if (hasGaode) {
                mapIntent.setData(Uri.parse(gaodeUri));
                context.startActivity(mapIntent);
            } else if (hasBaidu) {
                Intent intent = null;
                try {
                    intent = Intent.getIntent("intent://map/direction?destination=latlng:" + lat + "," + lng + "|name:" + location + "&mode=driving&src=andr.health.mall#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
                    context.startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("tag", "navigate: " + e);
            Toast.makeText(context, "导航失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 不需要方法内创建dialog
     *
     * @param context  上下文
     * @param location 显示地址名称
     * @param lat      目的地维度
     * @param lng      目的地经度
     * @param which    类别 0 百度 1 搞的
     */
    public static void navigateNoDialog(final Context context, final String location, final String lat, final String lng, final int which) {
        Log.e("daohang", "navigate: " + location + "-" + lat + "-" + lng);
        try {
            String gaodeMap = "com.autonavi.minimap";
            String baiduMap = "com.baidu.BaiduMap";
            final Intent mapIntent = new Intent();
//            intent://map/direction
            final String baiduUri = String.format("intent://map/direction?destination=%s" +
                    "|latlng:%s,%s&src=andr.health.mall", location, lat, lng);
            final String gaodeUri = String.format("androidamap://navi?sourceApplication" +
                    "=mall&poiname=%s&lat=%s&lon=%s&dev=1&style=2", location, lat, lng);
            boolean hasBaidu = PackageUtils.isAppInstalled(baiduMap, context);
            boolean hasGaode = PackageUtils.isAppInstalled(gaodeMap, context);
            if (!hasBaidu && !hasGaode) {
                Toast.makeText(context, "导航仅支持百度地图、高德地图", Toast.LENGTH_SHORT).show();
            } else if (hasGaode && hasBaidu) {
                if (which == 0) {
                    Intent intent = null;
                    try {
                        intent = Intent.getIntent("intent://map/direction?destination=latlng:" + lat + "," + lng + "|name:" + location + "&mode=driving&src=andr.health.mall#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(intent);
                } else {
                    mapIntent.setData(Uri.parse(gaodeUri));
                    context.startActivity(mapIntent);
                }
            } else if (hasGaode) {
                mapIntent.setData(Uri.parse(gaodeUri));
                context.startActivity(mapIntent);
            } else if (hasBaidu) {
                Intent intent = null;
                try {
                    intent = Intent.getIntent("intent://map/direction?destination=latlng:" + lat + "," + lng + "|name:" + location + "&mode=driving&src=andr.health.mall#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
                    context.startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("tag", "navigate: " + e);
            Toast.makeText(context, "导航失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean openGPSSettings(Context context) {
        boolean isopen = true;
        LocationManager alm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(context, "GPS模块正常", Toast.LENGTH_SHORT).show();
            isopen = true;
        } else {
            isopen = false;
//            Toast.makeText(context, "请开启GPS！", Toast.LENGTH_SHORT).show();

        }
        return isopen;

    }
}
