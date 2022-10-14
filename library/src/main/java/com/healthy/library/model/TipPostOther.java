package com.healthy.library.model;

import com.alibaba.android.arouter.utils.TextUtils;

import java.util.List;

public class TipPostOther {
    public String id;
    public String topicName;
    public String partInNum;
    public String newestPostingNum;
    public String postingNum;
    public List<String> faceUrlList;
    public List<PostTop> postingList;
    public boolean checkPostError(){
        int hasContentLine=0;
        for (int i = 0; i < postingList.size(); i++) {
            if(TextUtils.isEmpty(postingList.get(i).postingTitle)){
                postingList.remove(i);
                i--;
            }else {
                hasContentLine++;
            }
        }
        return hasContentLine==0?true:false;
    }
}
