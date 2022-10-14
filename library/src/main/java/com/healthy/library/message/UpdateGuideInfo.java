package com.healthy.library.message;

/**
 * @author Li
 * @date 2019/05/09 16:48
 * @des 更改用户信息
 */

public class UpdateGuideInfo {
    public int flag=0;//0 资料更群面服务更贴心 1 专家答疑 2 同城 4 快速发帖 5 悬赏求助 海量专家 6 安全到家服务便捷

    public UpdateGuideInfo(int flag) {
        this.flag = flag;
    }
}
