package com.healthy.library.message;

import com.healthy.library.model.PopListInfo;

import java.util.ArrayList;

/**
 * @author Li
 * @date 2019/05/09 16:48
 * @des 更改用户信息
 */

public class UpdateActTab {
    public String id;//0 资料更群面服务更贴心 1 专家答疑 2 同城 4 快速发帖 5 悬赏求助 海量专家 6 安全到家服务便捷
    public boolean flag;//0 资料更群面服务更贴心 1 专家答疑 2 同城 4 快速发帖 5 悬赏求助 海量专家 6 安全到家服务便捷
    public ArrayList<PopListInfo> popListInfos;
    public String name;
    public UpdateActTab(String id,String name,boolean flag) {
        this.id = id;
        this.flag=flag;
        this.name=name;
    }
    public UpdateActTab(String id,String name,boolean flag,ArrayList<PopListInfo> popListInfos) {
        this.id = id;
        this.flag=flag;
        this.name=name;
        this.popListInfos=popListInfos;
    }
}
