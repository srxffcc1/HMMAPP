package com.health.servicecenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.AppointmentShopListAdapter;
import com.health.servicecenter.contract.AppointmentShopListContract;
import com.health.servicecenter.presenter.AppointmentShopListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.widget.StatusLayout;
import com.hyb.library.KeyboardUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 门店列表页
 */
@Route(path = ServiceRoutes.SERVICE_APPOINTMENTSHOPLIST)
public class AppointmentShopListActivity extends BaseActivity implements IsFitsSystemWindows, AppointmentShopListContract.View {
    private ImageView imgBack;
    private LinearLayout seachLL;
    private EditText serarchKeyWord;
    private ImageView clearEdit;
    private ImageView topSearchImg;
    private TextView tag;
    private RecyclerView recyclerView;

    private AppointmentShopListAdapter mShopListAdapter;
    private AppointmentShopListPresenter mShopListPresenter;
    private StatusLayout mStatusLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_shop_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mShopListPresenter = new AppointmentShopListPresenter(this, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        //请求数据
        mShopListPresenter.getShopList(new SimpleHashMapBuilder<String, Object>());
    }

    @Override
    protected void findViews() {
        super.findViews();
        imgBack = (ImageView) findViewById(R.id.img_back);
        seachLL = (LinearLayout) findViewById(R.id.seachLL);
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
        topSearchImg = (ImageView) findViewById(R.id.top_searchImg);
        tag = (TextView) findViewById(R.id.tag);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mStatusLayout = findViewById(R.id.layout_status);
        setStatusLayout(mStatusLayout);

        initListener();
    }

    private void initListener() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        serarchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //搜索
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String mKeyWord = serarchKeyWord.getText().toString().trim();
                    serarchKeyWord.findFocus();
                    KeyboardUtils.hideSoftInput(serarchKeyWord);
                    if (mShopListAdapter != null) {
                        mShopListAdapter.getFilter().filter(mKeyWord);
                    }
                    return true;
                }
                return false;
            }
        });

        serarchKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    clearEdit.setVisibility(View.GONE);
                } else {
                    clearEdit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mKeyWord = serarchKeyWord.getText().toString().trim();
                serarchKeyWord.findFocus();
                KeyboardUtils.hideSoftInput(serarchKeyWord);
                if (mShopListAdapter != null) {
                    mShopListAdapter.getFilter().filter(mKeyWord);
                }
            }
        });

        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serarchKeyWord.setText("");
                if (mShopListAdapter != null) {
                    mShopListAdapter.getFilter().filter("");
                }
            }
        });
    }


    @Override
    public void onGetShopListSuccess(ArrayList<ShopDetailModel> datas) {
        if (ListUtil.isEmpty(datas)) {
            showEmpty();
            return;
        }
        //排序
        Collections.sort(datas);
        buildRecyclerView(datas);
    }

    @Override
    public void onGetStoreDetailSuccess(ShopDetailModel detailModel) {

    }

    private void buildRecyclerView(final ArrayList<ShopDetailModel> datas) {

        if (mShopListAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mShopListAdapter = new AppointmentShopListAdapter(datas);
            recyclerView.setAdapter(mShopListAdapter);
            mShopListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent();
                    intent.putExtra("id", mShopListAdapter.getFilterData().get(position).id);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
            //04-14 优化检索门店为空数据
            mShopListAdapter.setOnSearchResultDataListener(new AppointmentShopListAdapter.OnSearchResultDataListener() {
                @Override
                public void onSearchResultData(boolean isEmpty) {
                    if (isEmpty) showEmpty();
                    else showContent();
                }
            });
            mShopListAdapter.setNewData(datas);
        } else {
            mShopListAdapter.notifyDataSetChanged();
        }
    }
}