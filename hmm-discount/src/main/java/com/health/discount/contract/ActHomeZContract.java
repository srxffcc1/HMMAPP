package com.health.discount.contract;

import com.health.discount.model.PointTab;
import com.health.discount.model.SeckillTab;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActGoodsCityItem;
import com.healthy.library.model.ActGroup;
import com.healthy.library.model.ActKick;
import com.healthy.library.model.ActKill;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.model.MainIconModel;
import com.healthy.library.model.MainMenuModel;
import com.healthy.library.model.MainSearchModel;
import com.healthy.library.model.ShopInfo;

import java.util.List;
import java.util.Map;

public interface ActHomeZContract {
    interface View extends BaseView {

        void onSucessGetSeachTipList(List<MainSearchModel> list);
        void onGetFucList(List<MainMenuModel> list, MainIconModel mainIconModel,List<AdModel> adModels);
        void onSucessGetActKill(List<ActKill> list);
        void onSucessGetActGroup(List<ActGroup> list);
        void onSucessGetActKick(List<ActKick> list);
        void onPointRecommendSuccess(List<PointTab.PointGoods> pointGoodsList);
        void onSucessMainGetLiveList(List<LiveVideoMain> result);
        void onSucessGetBlockList(List<MainBlockModel> list);
        void onSucessGetActMan(boolean hasman);
        void onSucessGetAdvBlock(List<AdModel> adModels);
        void onSucessGetTopAds(List<AdModel> adModels);
        void onSucessGetTopTopAds(List<AdModel> adModels);
        void onSucessGetCenterAds(List<AdModel> adModels);
        void onSucessGetBottomAds(List<AdModel> adModels);
        void onSucessGetActKillHistoryList(SeckillTab seckillTab);
        void onSucessGetList(List<ActGoodsCityItem> result, ShopInfo shopInfo);
        void getZxingCode(String result);
    }
    interface Presenter extends BasePresenter {
        void getSeachRecommend(Map<String, Object> map);//获得搜索提示
        void getSeachTipList(Map<String, Object> map);//获得搜索猜
        void getPostContent(Map<String, Object> map);//获得海报 //其中应该包括上下的内容
        void getFucList(Map<String, Object> map);//获得导航菜单
        void getAdvBlock(Map<String, Object> map);//获得专区的广告
        void getKillList(Map<String, Object> map);//获得每日必抢 秒杀
        void getPinList(Map<String, Object> map);//获得每日必拼
        void getKickList(Map<String, Object> map);//获得每日必砍
        void getPointList(Map<String, Object> map);//获得积分
        void getVideoList(Map<String, Object> map);//获得直播
        void getBlockList(Map<String, Object> map);//获得专区
        void getBottomActTabs(Map<String, Object> map);//获得底部的配置
        void getAdvBottom(Map<String, Object> map);//获得中广告
        void getActBlockMan(Map<String, Object> map);//获得当前选择的门店
        void getActLocVip(Map<String, Object> map);
        void getBannerTop(Map<String, Object> map);//顶部banner
        void getBannerTopTop(Map<String, Object> map);//顶部banner
        void getBannerCenter(Map<String, Object> map);//中部banner
        void getBannerBottom(Map<String, Object> map);//底部bannner
        void getActShopGoodsList(Map<String, Object> map);//底部门店信息
        void getActKillHistoryList(Map<String, Object> map);//秒杀历史数据
        void getZxingCode();

    }
}
