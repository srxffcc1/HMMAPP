package com.healthy.library.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Li
 * @date 2019/03/28 09:58
 * @des json
 */

public class JsonUtils {

    public static boolean checkExists(JSONObject jsonObject, String name) {
        return jsonObject.has(name) && !jsonObject.isNull(name);
    }

    public static boolean checkJsonObjectEmpty(JSONObject jsonObject, String name) {
        return !(checkExists(jsonObject, name) && JsonUtils.getJsonObject(jsonObject, name).length() > 0);
    }

    public static String getString(JSONObject jsonObject, String name) {
        return getString(jsonObject, name, "");
    }

    public static String getString(JSONObject jsonObject, String name, String defaultValue) {
        if (checkExists(jsonObject, name)) {
            try {
                return jsonObject.getString(name);
            } catch (Exception e) {
                e.printStackTrace();
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static String getString(JSONArray jsonArray, int index) {
        try {
            return jsonArray.getString(index);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean getBoolean(JSONObject jsonObject, String name) {
        if (checkExists(jsonObject, name)) {
            try {
                return jsonObject.getBoolean(name);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static JSONArray getJsonArray(JSONObject jsonObject, String name) {
        JSONArray jsonArray = new JSONArray();
        if (checkExists(jsonObject, name)) {
            try {
                jsonArray = jsonObject.getJSONArray(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, String name) {
        JSONObject object = new JSONObject();
        try {
            object = jsonObject.getJSONObject(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject getJsonObject(JSONArray jsonArray, int index) {
        try {
            return jsonArray.getJSONObject(index);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, String name, JSONObject defaultObj) {
        JSONObject object;
        try {
            object = jsonObject.getJSONObject(name);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultObj;
        }
        return object;
    }

    public static int getInt(JSONObject jsonObject, String name, int defaultValue) {
        if (checkExists(jsonObject, name)) {
            try {
                return jsonObject.getInt(name);
            } catch (Exception e) {
                e.printStackTrace();
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static int getInt(JSONObject jsonObject, String name) {
        return getInt(jsonObject, name, 0);
    }

    public static double getDouble(JSONObject jsonObject, String name, double defaultValue) {
        if (checkExists(jsonObject, name)) {
            try {
                return jsonObject.getDouble(name);
            } catch (Exception e) {
                e.printStackTrace();
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static double getDouble(JSONObject jsonObject, String name) {
        return getDouble(jsonObject, name, 0);
    }

    public static long getLong(JSONObject jsonObject, String name, long defaultValue) {
        try {
            return jsonObject.getLong(name);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long getLong(JSONObject jsonObject, String name) {
        return getLong(jsonObject, name, 0);
    }

}
