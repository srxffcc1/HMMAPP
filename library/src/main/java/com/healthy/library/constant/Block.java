package com.healthy.library.constant;
//屏蔽用的 主要在sound模块
public class Block {
    public static final String keywords="年糕妈妈,崔玉涛,妈妈网,妈妈圈,爱他美,美赞臣,育学园,周易,易经,八卦,八字";
    public static boolean checkIsBlock(String org){
        boolean result=false;
        String[] keywordsarray=keywords.split(",");
        for (int i = 0; i < keywordsarray.length; i++) {
            org.contains(keywordsarray[i].trim());
            return true;
        }
        return result;
    }
    public static boolean checkIsBlockReplace(String org){
        boolean result=false;
        String[] keywordsarray=keywords.split(",");
        for (int i = 0; i < keywordsarray.length; i++) {
            if(!org.replace(keywordsarray[i].trim(),"").equals(org)){//说明被替换掉了这个关键词则说明存在此关键词
                return true;
            }
        }
        return result;
    }

}
