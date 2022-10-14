package com.health.servicecenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallSortLeftAdapter;
import com.health.servicecenter.contract.ServiceSortContract;
import com.health.servicecenter.fragment.MallSortFragment;
import com.healthy.library.model.CategoryListModel;
import com.health.servicecenter.presenter.ServiceSortPresenter;
import com.health.servicecenter.widget.ToolDecoration;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnItemClickListener;
import com.healthy.library.routes.ServiceRoutes;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_SORT)
public class ServiceSort extends BaseActivity implements IsFitsSystemWindows, ServiceSortContract.View {
    private ImageView img_back;
    private TextView serarchKeyWord;
    private MallSortLeftAdapter mallSortLeftAdapter;
    private RecyclerView recyclerView;
    int currentIndex = -1;
    private MallSortFragment mallSortFragment;
    private ServiceSortPresenter serviceSortPresenter;
    private int currenPosition = 0;
    private List<CategoryListModel> listModels = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.service_sort_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        showContent();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        serviceSortPresenter = new ServiceSortPresenter(this, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        serviceSortPresenter.getCategoryList();
    }

    @Override
    public void onGetCategoryListSuccess(List<CategoryListModel> list) {
        if (list == null || list.size() == 0) {
            showEmpty();
            return;
        }
        listModels.addAll(list);
        mallSortLeftAdapter.setData(list);
        mallSortFragment = MallSortFragment.newInstance((int)list.get(0).getId(), currenPosition);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.recycler_sort_section, mallSortFragment).commitAllowingStateLoss();

    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_sort_left);
        img_back = findViewById(R.id.img_back);
        serarchKeyWord = findViewById(R.id.serarchKeyWord);
        mallSortLeftAdapter = new MallSortLeftAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ToolDecoration());
        recyclerView.setAdapter(mallSortLeftAdapter);

        mallSortLeftAdapter.setmItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                if (pos == currentIndex||listModels.size()==0) {
                    return;
                }
                try {
                    currentIndex = pos;
                    mallSortLeftAdapter.setSelected(pos);
                    mallSortFragment.refresh(pos,(int)listModels.get(pos).getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure","分类页左侧一级分类列表");
                MobclickAgent.onEvent(mActivity, "btn_APP_MaternalandChildGoods_FirstClass",nokmap);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serarchKeyWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure","分类页搜索栏");
                MobclickAgent.onEvent(mActivity, "btn_APP_MaternalandChildGoods_Search",nokmap);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_SEACH)
                        .navigation();
            }
        });
    }


}
