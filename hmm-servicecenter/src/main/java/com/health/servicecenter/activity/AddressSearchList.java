package com.health.servicecenter.activity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.SearchAddressListAdapter;
import com.healthy.library.model.AddressModel;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.ServiceRoutes;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Route(path = ServiceRoutes.SERVICE_SEARCH_ADDRESS_LIST)
public class AddressSearchList extends BaseActivity implements PoiSearch.OnPoiSearchListener, TextWatcher {

    private ImageView back, clearEdit;
    private TextView cancle, city_title, null_txt;
    private EditText serarchKeyWord;
    private RecyclerView list_recycle;
    private SearchAddressListAdapter searchAddressListAdapter;
    private LinearLayout null_linerlayout;
    private RelativeLayout bottom_rel;

    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_search_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (getIntent().getStringExtra("city") != null) {
            city_title.setText(getIntent().getStringExtra("city"));
        }
        searchAddressListAdapter = new SearchAddressListAdapter();
        list_recycle.setLayoutManager(new LinearLayoutManager(this));
        list_recycle.setAdapter(searchAddressListAdapter);

        city_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressSearchList.this, ProvinceCity.class);
                startActivityForResult(intent, 3);
            }
        });
        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serarchKeyWord.setText("");
            }
        });
        searchAddressListAdapter.setmItemClickListener(new SearchAddressListAdapter.ItemClickListener() {
            @Override
            public void onClick(AddressModel model) {
                Intent intent = new Intent();
                intent.putExtra("model", model);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        serarchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
                        showToast("请输入搜索关键字!");
                        return true;
                    }
                    searchButton();
                }
                return false;
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        back = findViewById(R.id.img_back);
        cancle = findViewById(R.id.txt_cancle);
        city_title = findViewById(R.id.city_title);
        serarchKeyWord = findViewById(R.id.serarchKeyWord);
        list_recycle = findViewById(R.id.list_recycle);
        bottom_rel = findViewById(R.id.bottom_rel);
        null_linerlayout = findViewById(R.id.null_linerlayout);
        null_txt = findViewById(R.id.null_txt);
        clearEdit = findViewById(R.id.clearEdit);
        serarchKeyWord.addTextChangedListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serarchKeyWord.setFocusable(true);
        serarchKeyWord.setFocusableInTouchMode(true);
        serarchKeyWord.requestFocus();
        new Timer().schedule(new TimerTask() {
                                 public void run() {
                                     InputMethodManager inputManager =
                                             (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                     inputManager.showSoftInput(serarchKeyWord, 0);
                                 }
                             },
                200);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                String city = data.getStringExtra("city");
                city_title.setText(city);
                serarchKeyWord.setText("");
                searchButton();
            }
        }
    }

    /**
     * 点击搜索按钮
     */
    public void searchButton() {
        String keyWord = serarchKeyWord.getText().toString();
        if ("".equals(keyWord) || TextUtils.isEmpty(keyWord)) {
            //showToast("请输入搜索关键字!");
            return;
        } else {
            doSearchQuery(keyWord);
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keyWord) {
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", city_title.getText().toString());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(50);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            searchButton();
        } else {
            searchAddressListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItems != null && poiItems.size() > 0) {
                        bottom_rel.setVisibility(View.VISIBLE);
                        null_linerlayout.setVisibility(View.GONE);
                        searchAddressListAdapter.setData((ArrayList) poiItems);
                        searchAddressListAdapter.notifyDataSetChanged();
                    } else {
                        String str = "<font color ='#F02846'>" + city_title.getText().toString() + "</font>";
                        null_txt.setText(Html.fromHtml("您所选的城市是“" + str + "”"));
                        bottom_rel.setVisibility(View.GONE);
                        null_linerlayout.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                showToast("对不起，没有搜索到相关数据！");
            }
        } else {
            showToast(rCode + "");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}