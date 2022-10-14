package com.health.servicecenter.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.servicecenter.R;
import com.health.servicecenter.adapter.PickOrderShopAdapter;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.model.LocVip;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.BaseFullBottomSheetFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShopOrderPickDialog extends BaseFullBottomSheetFragment {

    View view;
    private TextView tvChooseTimeTitle;
    private ImageView closeBtn;
    private RecyclerView recycler;
    PickOrderShopAdapter pickOrderShopAdapter;
    public String selectIdOrg;
    public String title;

    OnDialogShopClickListener onDialogShopClickListener;

    public void setOnDialogShopClickListener(OnDialogShopClickListener onDialogShopClickListener) {
        this.onDialogShopClickListener = onDialogShopClickListener;
    }

    public void setGoodsShopList(List<GoodsShop> goodsShopList) {
        this.goodsShopList = goodsShopList;
        try {
            pickOrderShopAdapter.setNewData(goodsShopList);
            ////System.out.println("重新设置选择id2:" + pickOrderShopAdapter.getShopIdSelect());
            pickOrderShopAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    List<GoodsShop> goodsShopList = new ArrayList<>();

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return super.show(transaction, tag);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.service_dialog_pick_ordershop, null);
        initView();
        buildView();
        return view;

    }

    public void setTitle(String title) {
        this.title = title;
        try {
            tvChooseTimeTitle.setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildView() {
        tvChooseTimeTitle.setText(title);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        if (pickOrderShopAdapter == null) {
            pickOrderShopAdapter = new PickOrderShopAdapter();
            pickOrderShopAdapter.setShopIdSelect(selectIdOrg);
        }
        Map<String, GoodsShop> clearMap = new HashMap<>();
        for (int i = 0; i <goodsShopList.size() ; i++) {
            clearMap.put(goodsShopList.get(i).shopId, goodsShopList.get(i));
        }
        goodsShopList.clear();
        Set<Map.Entry<String, GoodsShop>> set = clearMap.entrySet();
        // 遍历键值对对象的集合，得到每一个键值对对象
        for (Map.Entry<String, GoodsShop> me : set) {
            goodsShopList.add(me.getValue());
        }
        Collections.sort(goodsShopList);

        pickOrderShopAdapter.setNewData(goodsShopList);
        pickOrderShopAdapter.setOnShopClickListener(new PickOrderShopAdapter.OnShopClickListener() {
            @Override
            public void onShopClick(GoodsShop goodsShop) {
                if (onDialogShopClickListener != null) {
                    onDialogShopClickListener.onDialogShopClick(goodsShop);
                }
                dismiss();

            }
        });
        recycler.setAdapter(pickOrderShopAdapter);
    }

    public static ShopOrderPickDialog newInstance() {

        Bundle args = new Bundle();
        ShopOrderPickDialog fragment = new ShopOrderPickDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getTopOffset() {
        return 700;
    }

    private void initView() {

        tvChooseTimeTitle = (TextView) view.findViewById(R.id.tv_choose_time_title);
        closeBtn = (ImageView) view.findViewById(R.id.closeBtn);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setSelectId(String shopId) {
        selectIdOrg = shopId;
        try {
            pickOrderShopAdapter.setShopIdSelect(selectIdOrg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetAdapterSelect(String id) {
        pickOrderShopAdapter.setShopIdSelect(id);
    }

    public interface OnDialogShopClickListener {
        void onDialogShopClick(GoodsShop goodsShop);
    }
}
