package com.healthy.library.model;

import com.google.gson.annotations.SerializedName;

public class UpdatePatchVersion {

        public int id;

//        public int type;

        @SerializedName("patchVersion")
        public String version;

        public int forceUpate;
        @SerializedName("patchUrl")
        public String downloadUrl;

        public String size;

        public String remark;

    public int getVersionCode() {
        int result=0;
        if(version!=null&&version.contains("patch")){
            version=version.replace("-patch","");
        }
        try {
            String versionCode=version.replace(".","");
            result=Integer.parseInt(versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public int getVersionCodeWithBase() {
        int result=0;
        if(version!=null&&version.contains("patch")){
            version=version.replace("-patch","");
        }
        try {
            String versionCode=version.replace(".","");
            result=Integer.parseInt(versionCode.substring(0,4));
            System.out.println("补丁测试:补丁基础版本-" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
