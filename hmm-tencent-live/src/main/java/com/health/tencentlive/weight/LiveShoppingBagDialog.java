package com.health.tencentlive.weight;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.servicecenter.widget.GoodsSimpleDialog;
import com.health.tencentlive.R;
import com.health.tencentlive.adapter.LiveLookGoodsListAdapter;
import com.health.tencentlive.adapter.ShoppingBagDialogAdapter;
import com.health.tencentlive.contract.LiveShoppingBagDialogContract;
import com.health.tencentlive.model.JackpotList;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.LiveVideoGoods;
import com.health.tencentlive.presenter.LiveShoppingBagDialogPresenter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

public class LiveShoppingBagDialog extends BaseDialogFragment implements LiveShoppingBagDialogContract.View {

    private RecyclerView recyclerview;
    private ImageView closeBtn;
    private View view;
    private ShoppingBagDialogAdapter adapter;
    private getContentListener getListener;
    private LiveShoppingBagDialogPresenter liveShoppingBagDialogPresenter;
    public List<String> activityIdList;
    private List<ShopDetailModel> modelList = null;
    private String anchormanId;
    private String courseId;
    private String shopId;
    private boolean isHistoryLive = false;//是否是回放 回放购买跟直播购买有点区别
    private LiveRoomInfo mLiveRoomInfo;

    public LiveShoppingBagDialog setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    private String merchantId;

    public LiveShoppingBagDialog setShopId(String shopId) {
        this.shopId = shopId;
        return this;
    }

    public LiveShoppingBagDialog setHistoryLive(boolean historyLive) {
        this.isHistoryLive = historyLive;
        return this;
    }

    public LiveShoppingBagDialog setLiveInfo( LiveRoomInfo mLiveRoomInfo) {
        this.mLiveRoomInfo = mLiveRoomInfo;
        return this;
    }

    public static LiveShoppingBagDialog newInstance() {
        LiveShoppingBagDialog fragment = new LiveShoppingBagDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LiveShoppingBagDialog setList(List<String> activityIdList) {
        this.activityIdList = activityIdList;
        return this;
    }

    public void changeList(List<String> activityIdList) {
        this.activityIdList = activityIdList;
        getGoodsList();
    }

    public void changeGoodsList(List<LiveVideoGoods> list) {
        if (list != null && list.size() > 0) {
            if (adapter != null) {
                adapter.setData((ArrayList) list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setWindowAnimations(R.style.BottomDialogAnimation);
        if (window != null) {
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(R.drawable.shape_han_mom_teaching_detial_top_bg);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getGoodsList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liveShoppingBagDialogPresenter = new LiveShoppingBagDialogPresenter(getContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
        }
        if (view == null) {
            view = inflater.inflate(R.layout.dialog_live_shoppingbag_layout, container, false);
            initView();
        }
        return view;
    }

    private void getGoodsList() {
        if (isHistoryLive) {
            liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList).puts("isEncore", 1));
        } else {
            liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
        }
        liveShoppingBagDialogPresenter.getGoodsShopList(new SimpleHashMapBuilder<String, Object>().puts("shopId", TextUtils.isEmpty(shopId) ? SpUtils.getValue(getActivity(), SpKey.CHOSE_SHOP) : shopId));
    }

    private void initView() {
        recyclerview = view.findViewById(R.id.goodsRecycle);
        closeBtn = view.findViewById(R.id.closeBtn);
        adapter = new ShoppingBagDialogAdapter();
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        adapter.setDialog(this);
        adapter.setShopId(shopId, anchormanId, courseId);
        adapter.setMerchantId(merchantId);
        adapter.setHistoryLive(isHistoryLive);
        adapter.setLiveInfo(mLiveRoomInfo);
        adapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {
                if ("click".equals(function)) {
                    LiveVideoGoods liveVideoGoods = (LiveVideoGoods) obj;
                    goLiveGoods(liveVideoGoods);
//                    if (modelList != null && liveVideoGoods.shopIds != null) {
//                        List<NewStoreDetialModel> shopIdList = new ArrayList<>();
//                        for (int i = 0; i < modelList.size(); i++) {
//                            for (int j = 0; j < liveVideoGoods.shopIds.length; j++) {
//                                if (liveVideoGoods.shopIds[j].equals(modelList.get(i).id)) {
//                                    shopIdList.add(modelList.get(i));
//                                }
//                            }
//                        }
//                        if (shopIdList.size() > 0) {
//                            if (liveVideoGoods.goodsType == 1) {
//                                for (int i = 0; i < shopIdList.size(); i++) {
//                                    if (shopIdList.get(i).shopType == 2) {
//                                        shopIdList.remove(i);
//                                        i--;
//                                    }
//                                }
//                            }
//                            if (liveVideoGoods.goodsType == 2) {
//                                for (int i = 0; i < shopIdList.size(); i++) {
//                                    if (shopIdList.get(i).shopType == 1) {
//                                        shopIdList.remove(i);
//                                        i--;
//                                    }
//                                }
//                            }
//                            bulidShopdialog(shopIdList, liveVideoGoods);
//                        }
//                    }

                }
            }
        });

    }

    private void goLiveGoods(final LiveVideoGoods liveVideoGoods) {
        String errMsg = null;
        if (liveVideoGoods.offShelf == 0) {//上架可以抢
            if (liveVideoGoods.availableInventory == 0) {
                errMsg = "已抢完";
            }
        } else {
            errMsg = "等待主播开启抢购";

        }
        GoodsSimpleDialog goodsSimpleDialog = new GoodsSimpleDialog();
        goodsSimpleDialog.setErrMsg(errMsg)
                .setSeachMap(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "9292").puts("liveGoodsId", liveVideoGoods.id))
                .setLiveInfo(mLiveRoomInfo)
                .setOnDialogButtonOrderListener(new GoodsSimpleDialog.OnGoodsDialogOrderButtonListener() {
                    @Override
                    public void onOrderClick(GoodsSpecDetail goodsSpecDetailNew) {
                        if (liveVideoGoods.getMarkLimitMaxNowWithInventory() < liveVideoGoods.getMarkLimitMinNow()) {//可够小于最小起购商品库存不足
                            Toast.makeText(getContext(), "已抢光", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(null);
                        goodsMarketing.availableInventory = goodsSpecDetailNew.getAvailableInventory();
                        goodsMarketing.mapMarketingGoodsId = liveVideoGoods.id;
                        goodsMarketing.marketingType = "-1";
                        goodsMarketing.id = goodsSpecDetailNew.id;
                        goodsMarketing.marketingPrice = goodsSpecDetailNew.marketingPrice;
                        goodsMarketing.pointsPrice = 0;
                        goodsMarketing.salesMin = liveVideoGoods.salesMin;
                        goodsMarketing.salesMax = liveVideoGoods.salesMax;
                        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsSpecDetailNew.marketingPrice,
                                goodsSpecDetailNew.marketingPrice,
                                0,
                                (liveVideoGoods.isShopTakeOnly == 1 ? 1 : 2) + "",
                                "0",
                                "0", null);
                        goodsBasketCell.goodsSpecDesc = goodsSpecDetailNew.goodsSpecStr;
                        goodsBasketCell.goodsStock = goodsSpecDetailNew.getAvailableInventory();
                        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                        goodsBasketCell.mchId = SpUtils.getValue(getContext(), SpKey.CHOSE_MC) + "";
                        goodsBasketCell.goodsId = liveVideoGoods.goodsId;
                        try {
                            goodsBasketCell.setGoodsSpecId(goodsSpecDetailNew.goodsChildId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        goodsBasketCell.goodsTitle = liveVideoGoods.goodsTitle;
                        goodsBasketCell.goodsImage = liveVideoGoods.headImages.get(0).filePath;
                        goodsBasketCell.setGoodsQuantity(liveVideoGoods.salesMin == 0 ? 1 : liveVideoGoods.salesMin);
                        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                        List<GoodsBasketCell> list = new ArrayList<>();
                        goodsBasketCell.shopIdList = liveVideoGoods.shopIds;
                        goodsBasketCell.ischeck = true;
                        goodsBasketCell.goodsShopId = goodsSpecDetailNew.shopDetailModelSelect.id;
                        goodsBasketCell.goodsShopName = goodsSpecDetailNew.shopDetailModelSelect.shopName;
                        goodsBasketCell.goodsShopAddress = goodsSpecDetailNew.shopDetailModelSelect.addressDetails;
                        list.add(goodsBasketCell);

                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDER)
                                .withString("visitShopId", goodsSpecDetailNew.shopDetailModelSelect.id)
                                .withString("token", null)
                                .withString("course_id", null)
                                .withString("liveStatus", null)
                                .withString("live_anchor", anchormanId)
                                .withString("live_course", courseId)
                                .withObject("goodsbasketlist", list)
                                .withString("goodsMarketingType", "-1")
                                .navigation();
                    }
                })
                .show(getChildFragmentManager(), "商品弹窗");
    }
    private void bulidShopdialog(List<ShopDetailModel> shopIdList, final LiveVideoGoods liveVideoGoods) {
        final LiveShopPickDialog liveShopPickDialog = LiveShopPickDialog.newInstance();
        if (shopIdList == null || shopIdList.size() == 0) {
            showToast("暂无可选门店");
            return;
        }
        liveShopPickDialog.setShopList(shopIdList);
        liveShopPickDialog.setSelectId(shopIdList.get(0).id);
        liveShopPickDialog.setGoodsType(liveVideoGoods.goodsType);
        liveShopPickDialog.show(getChildFragmentManager(), "选择门店");
        liveShopPickDialog.setOnDialogShopClickListener(new LiveShopPickDialog.OnDialogShopClickListener() {
            @Override
            public void onDialogShopClick(ShopDetailModel model) {

                if (liveVideoGoods.getMarkLimitMaxNowWithInventory() < liveVideoGoods.getMarkLimitMinNow()) {//可够小于最小起购商品库存不足
                    Toast.makeText(getActivity(), "已抢光", Toast.LENGTH_SHORT).show();
                    return;
                }
                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(null);
                goodsMarketing.availableInventory = liveVideoGoods.goodsChildren.get(0).availableInventory;
                goodsMarketing.mapMarketingGoodsId = liveVideoGoods.id;
                goodsMarketing.marketingType = "-1";
                goodsMarketing.id = liveVideoGoods.goodsChildren.get(0).id;
                goodsMarketing.marketingPrice = liveVideoGoods.goodsChildren.get(0).livePrice;
                goodsMarketing.pointsPrice = 0;
                goodsMarketing.salesMin = liveVideoGoods.salesMin;
                goodsMarketing.salesMax = liveVideoGoods.salesMax;
                GoodsBasketCell goodsBasketCell = new GoodsBasketCell(liveVideoGoods.goodsChildren.get(0).livePrice,
                        liveVideoGoods.goodsChildren.get(0).livePrice,
                        0,
                        (liveVideoGoods.isShopTakeOnly == 1 ? 1 : 2) + "",
                        "0",
                        "0", null);
                goodsBasketCell.goodsSpecDesc = liveVideoGoods.goodsChildren.get(0).goodsSpecStr;
                goodsBasketCell.goodsStock = liveVideoGoods.goodsChildren.get(0).availableInventory;
                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                goodsBasketCell.mchId = SpUtils.getValue(getActivity(), SpKey.CHOSE_MC) + "";
                goodsBasketCell.goodsId = liveVideoGoods.goodsId;
                //goodsBasketCell.setGoodsSpecId(null);
                try {
                    goodsBasketCell.setGoodsSpecId(liveVideoGoods.goodsChildren.get(0).goodsChildId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                goodsBasketCell.goodsTitle = liveVideoGoods.goodsTitle;
                goodsBasketCell.goodsImage = liveVideoGoods.headImages.get(0).filePath;
                goodsBasketCell.setGoodsQuantity(liveVideoGoods.salesMin == 0 ? 1 : liveVideoGoods.salesMin);
                GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                List<GoodsBasketCell> list = new ArrayList<>();
                goodsBasketCell.shopIdList = liveVideoGoods.shopIds;
                goodsBasketCell.ischeck = true;
                goodsBasketCell.goodsShopId = model.id;
                goodsBasketCell.goodsShopName = model.shopName;
                goodsBasketCell.goodsShopAddress = model.addressDetails;
                list.add(goodsBasketCell);

                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER)
                        .withString("token", null)
                        .withString("visitShopId", model.id)
                        .withString("course_id", null)
                        .withString("liveStatus", null)
                        .withString("live_anchor", anchormanId)
                        .withString("live_course", courseId)
                        .withObject("goodsbasketlist", list)
                        .withString("goodsMarketingType", "-1")
                        .navigation();
                liveShopPickDialog.dismiss();
            }
        });
    }

    @Override
    public void getLiveRoomGoodsListSuccess(List<LiveVideoGoods> result) {
        if (recyclerview != null && recyclerview.getScrollState() != 0) {
            //recycleView正在滑动
            return;
        }
        if (result != null && result.size() > 0) {
            adapter.setData((ArrayList) result);
        }
    }

    @Override
    public void onGetStoreListSuccess(List<ShopDetailModel> result) {
        if (result != null && result.size() > 0) {
            modelList = result;
        }
    }

    @Override
    public void onGetMerchantStoreListSuccess(List<ShopDetailModel> result) {

    }

    @Override
    public void getSuccessJackpotList(List<JackpotList> result, PageInfoEarly pageInfo, int type) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {

    }

    @Override
    public void showDataErr() {

    }

    public void setAnchormanId(String anchormanId) {
        this.anchormanId = anchormanId;
    }

    public String getAnchormanId() {
        return anchormanId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }

    public interface getContentListener {

        void resultContent(String result, int position);
    }

    public void setResultListener(getContentListener getListener) {
        this.getListener = getListener;
    }

}
