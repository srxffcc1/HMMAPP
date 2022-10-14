package com.healthy.library.model;

import java.util.ArrayList;
import java.util.List;

//推荐返回
public class AppIndexCustomRecommandAll {
    public List<TipPostOther> getRealHotTopic() {
        List<TipPostOther> result=new ArrayList<>();
        try {
            for (int i = 0; i < hotTopic.size(); i++) {
                if(!hotTopic.get(i).checkPostError()){
                    result.add(hotTopic.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<TipPostOther> hotTopic=new ArrayList<>();

    public SoundHomeSplit xmly;
    public List<VideoListModel> video;
}
