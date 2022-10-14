package com.health.mine.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.health.mine.R;
import com.health.mine.adapter.HanMomAdapter;
import com.health.mine.contract.HanMomFragmentContract;
import com.health.mine.model.HanMomGoodsListModel;
import com.health.mine.presenter.HanMomFragmentPresenter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Ids;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.widget.StatusLayout;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HanMomFragment extends BaseFragment implements HanMomFragmentContract.View, BaseAdapter.OnOutClickListener {
    private RecyclerView hanMomRecycler;
    private StatusLayout layout_status;
    private HanMomAdapter hanMomAdapter;
    private HanMomFragmentPresenter presenter;
    private Map<String, Object> map = new HashMap<>();
    private String goodsType;
    private int pageNum = 1;
    private String partnerId;

    public HanMomFragment() {
    }

    public static HanMomFragment newInstance(String goodsType, String partnerId) {
        HanMomFragment fragment = new HanMomFragment();
        Bundle args = new Bundle();
        args.putString("goodsType", goodsType);
        args.putString("partnerId", partnerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            goodsType = getArguments().getString("goodsType");
            partnerId = getArguments().getString("partnerId");
        }
        presenter = new HanMomFragmentPresenter(getContext(), this);
        if (partnerId == null || TextUtils.isEmpty(partnerId)) {
            showEmpty();
        } else {
            getData();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_han_mom;
    }

    @Override
    protected void findViews() {
        hanMomRecycler = findViewById(R.id.hanMomRecycler);
        layout_status = findViewById(R.id.layout_status);
        hanMomAdapter = new HanMomAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);//网格布局
        hanMomRecycler.setLayoutManager(layoutManager);
        hanMomRecycler.setAdapter(hanMomAdapter);
        hanMomAdapter.setOutClickListener(this);
    }

    @Override
    public void getData() {
        super.getData();
        map.put("goodsType", goodsType);
        map.put("pageNum", pageNum + "");
        map.put("pageSize", "10");
        map.put("partnerId", partnerId);
        //map.put("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_ORG));
        //map.put("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_ORG));
        //map.put("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG));
        presenter.getGoods(map);
    }


    @Override
    public void onGetGoodsSuccess(List<HanMomGoodsListModel> listModels) {
        if (listModels == null || listModels.size() == 0) {
            showEmpty();
            return;
        }
        hanMomAdapter.setData((ArrayList) listModels);

    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("item".equals(function)) {
            String appId = Ids.WX_APP_ID; // 本应用微信AppId
            IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = "gh_93d673cec6a8"; // 小程序原始id
            req.path = "/pages/goodsDetails/goodsDetails?id=" + (String) obj; //拉起小程序商品详情页
            if (ChannelUtil.isIpRealRelease()) {
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
            } else {
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
            }
            api.sendReq(req);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}