package com.healthy.library.model;

public class UpdateVersion {

        public int id;

//        public int type;

        public String version;

        public int forceUpate;

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
}
