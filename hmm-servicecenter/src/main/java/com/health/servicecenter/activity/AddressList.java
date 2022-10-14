package com.health.servicecenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.AddressListAdapter;
import com.health.servicecenter.contract.AddressListContract;
import com.healthy.library.model.AddressListModel;
import com.health.servicecenter.presenter.AddressListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_ADDRESS_LIST)
public class AddressList extends BaseActivity implements  AddressListContract.View, BaseAdapter.OnOutClickListener, OnRefreshLoadMoreListener {
    private RecyclerView recycler_list;
    private AddressListAdapter addressListAdapter;
    private TextView add_address_txt;
    private LinearLayout address_null_linearlayout;
    private ConstraintLayout constraintlayout;
    private ImageView img_back;
    private AddressListPresenter listPresenter;
    private Map<String, Object> map = new HashMap<>();
    private boolean isFirstAddress = false;
    private StatusLayout statusLayout;
    private SmartRefreshLayout refreshLayout;


    @Autowired
    boolean isNeedSelect = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ARouter.getInstance().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        listPresenter = new AddressListPresenter(this, this);
        addressListAdapter = new AddressListAdapter();
        addressListAdapter.setOutClickListener(this);
        recycler_list.setLayoutManager(new LinearLayoutManager(this));
        recycler_list.setAdapter(addressListAdapter);
        addressListAdapter.setNeedSelect(isNeedSelect);
        addressListAdapter.setOnAddressClickListener(new AddressListAdapter.OnAddressClickListener() {
            @Override
            public void onAddressClick(AddressListModel addressListModel) {
                Intent intent = new Intent();
                intent.putExtra("result", new Gson().toJson(addressListModel));
                setResult(100, intent);
                finish();
            }
        });
        add_address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstAddress) {
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_ADD_ADDRESS)
                            .withBoolean("isFirstAddress", isFirstAddress)
                            .navigation();
                } else {
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_ADD_ADDRESS)
                            .withBoolean("isFirstAddress", isFirstAddress)
                            .navigation();
                }

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        recycler_list = findViewById(R.id.recycler_list);
        add_address_txt = findViewById(R.id.add_address_txt);
        img_back = findViewById(R.id.img_back);
        address_null_linearlayout = findViewById(R.id.address_null_linearlayout);
        constraintlayout = findViewById(R.id.constraintlayout);
        statusLayout = findViewById(R.id.layout_status);
        refreshLayout = findViewById(R.id.layout_refresh);
        refreshLayout.finishRefreshWithNoMoreData();
        refreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void getData() {

//        map.put("pageSize", 10 + "");
//        map.put("currentPage", mCurrentPage+"");

        listPresenter.getAddressList(map);
    }

    @Override
    public void onGetAddressListSuccess(List<AddressListModel> listModels) {
        if (listModels == null || listModels.size() == 0) {
            constraintlayout.setVisibility(View.GONE);
            address_null_linearlayout.setVisibility(View.VISIBLE);
            isFirstAddress = true;
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            isFirstAddress = false;
            address_null_linearlayout.setVisibility(View.GONE);
            constraintlayout.setVisibility(View.VISIBLE);
            addressListAdapter.setData((ArrayList) listModels);
            addressListAdapter.notifyDataSetChanged();
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }

    }

    @Override
    public void onDeleteAddressSuccess() {
        getData();
    }

    private void isAgree(final String id) {
        StyledDialog.init(this);
        StyledDialog.buildIosAlert("提示", "\n" + "是否确定删除该地址？", new MyDialogListener() {
            @Override
            public void onFirst() {
            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                listPresenter.deleteAddress(id);
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("delete".equals(function)) {
            isAgree((String) obj);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }
}