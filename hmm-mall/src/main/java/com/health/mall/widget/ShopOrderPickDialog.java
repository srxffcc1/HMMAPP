package com.health.mall.widget;

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

import com.health.mall.R;
import com.health.mall.adapter.SelectShopAdapter;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.model.OrderSubShopListModel;
import com.healthy.library.widget.BaseFullBottomSheetFragment;
import com.healthy.library.widget.StatusLayout;

import java.util.ArrayList;
import java.util.List;

public class ShopOrderPickDialog extends BaseFullBottomSheetFragment {

    View view;
    private TextView tvChooseTimeTitle;
    private ImageView closeBtn;
    private StatusLayout layout_status;
    private RecyclerView recycler;
    private SelectShopAdapter pickOrderShopAdapter;
    public String selectIdOrg;
    public String title;
    public List<OrderSubShopListModel> shopList = new ArrayList<>();

    OnDialogShopClickListener onDialogShopClickListener;

    public void setOnDialogShopClickListener(OnDialogShopClickListener onDialogShopClickListener) {
        this.onDialogShopClickListener = onDialogShopClickListener;
    }

    public ShopOrderPickDialog setShopList(List<OrderSubShopListModel> listModelList) {
        this.shopList = listModelList;
        return this;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return super.show(transaction, tag);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.service_dialog_pick_shop, null);
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
        if (shopList==null||shopList.size()==0){
            layout_status.updateStatus(StatusLayout.Status.STATUS_EMPTY);
            layout_status.setmEmptyContent("暂无可预约的商家");
            return;
        }
        if (pickOrderShopAdapter == null) {
            pickOrderShopAdapter = new SelectShopAdapter();
            pickOrderShopAdapter.setShopIdSelect(selectIdOrg);
        }
        pickOrderShopAdapter.setNewData(shopList);
        pickOrderShopAdapter.setOnShopClickListener(new SelectShopAdapter.OnShopClickListener() {
            @Override
            public void onShopClick(OrderSubShopListModel model) {
                if (onDialogShopClickListener != null) {
                    onDialogShopClickListener.onDialogShopClick(model);
                }
                dismiss();

            }
        });
        recycler.setAdapter(pickOrderShopAdapter);
        layout_status.updateStatus(StatusLayout.Status.STATUS_CONTENT);
    }

    public static ShopOrderPickDialog newInstance() {

        Bundle args = new Bundle();
        ShopOrderPickDialog fragment = new ShopOrderPickDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getTopOffset() {
        return 500;
    }

    private void initView() {

        tvChooseTimeTitle = (TextView) view.findViewById(R.id.tv_choose_time_title);
        closeBtn = (ImageView) view.findViewById(R.id.closeBtn);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        layout_status = (StatusLayout) view.findViewById(R.id.layout_status);
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
        void onDialogShopClick(OrderSubShopListModel model);
    }
}
