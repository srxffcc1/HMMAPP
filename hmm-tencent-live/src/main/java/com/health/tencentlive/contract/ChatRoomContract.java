package com.health.tencentlive.contract;

import com.health.tencentlive.model.Interaction;
import com.health.tencentlive.model.InteractionDetail;
import com.health.tencentlive.model.RedGift;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.ChatRoomConfigure;
import com.healthy.library.model.LiveRoomDecoration;
import com.healthy.library.model.MessageSendCode;
import com.healthy.library.model.OnLineNum;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface ChatRoomContract {

    interface View extends BaseView {

        void getChatRoomConfigureSuccess(ChatRoomConfigure result);

        void setChatRoomInfoSuccess();

        void sendTxtMessageSuccess(MessageSendCode result);

        void sendCustomerTxtMessageSuccess(MessageSendCode result);

        void getLiveRoomMappingSuccess(LiveRoomDecoration result);

        void getLiveRoomBannerSuccess(LiveRoomDecoration result);

        void onSucessGetHost(AnchormanInfo anchormanInfo);

        void onSuccessGetFansNum(String result);

        void onSuccessGetLookNum(OnLineNum result);

        void onSuccessGetInteractionList(List<Interaction> result);

        void onSuccessGetInteractionDetail(InteractionDetail result);

        void onSuccessAddHelp(String result);

        void onSuccessGetRedGift(RedGift result);

        void onSuccessAddGift(String result, String couponName);

    }


    interface Presenter extends BasePresenter {

        void getChatRoomConfigure(HashMap<String, Object> map);//获取聊天室配置

        void setChatRoomInfo(HashMap<String, Object> map);//设置聊天账户资料

        void sendTxtMessage(HashMap<String, Object> map);//用户发消息

        void sendCustomerTxtMessage(HashMap<String, Object> map);//主播发消息

        void getLiveRoomMapping(HashMap<String, Object> map, int type);//获取直播间贴图或轮播图

        void getHost(Map<String, Object> map);//获取主播信息

        void getFansNum(Map<String, Object> map);//获取主播粉丝量

        void getLookLivePeopleNum(Map<String, Object> map);//获取当前直播间在线人数

        void addLookLivePeopleNum(Map<String, Object> map);//添加im群组会员统计信息

        void getInteractionList(Map<String, Object> map);//获取互动任务列表

        void getInteractionDetail(Map<String, Object> map);//获取互动任务详情

        void addHelp(Map<String, Object> map);//加入助力

        void getRedGift(Map<String, Object> map);//抢红包

        void addGift(Map<String, Object> map, String couponName);//收下红包


    }
}
