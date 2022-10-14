package com.health.index.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.index.R;
import com.health.index.adapter.NewsAdapter;
import com.health.index.contract.IndexMonNewsContract;
import com.health.index.model.IndexAllSee;
import com.health.index.model.IndexMonTool;
import com.health.index.presenter.IndexMonNewsMainPassPresenter;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.fragment.TmpFragment;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/25 10:58
 * @des 推荐阅读界面
 */

@Route(path = IndexRoutes.INDEX_MAINPASSNEWS)
public class NewsMainPassActivity extends BaseActivity implements IndexMonNewsContract.View, OnRefreshLoadMoreListener, TextView.OnEditorActionListener {
    @Autowired
    String knowOrInfoStatus;//先判断是不是资讯
    @Autowired
    String queryDate;
    @Autowired
    String categoryId;
    @Autowired
    String title;//在判断是不是搜索

    boolean isinittab = false;

    IndexMonNewsMainPassPresenter indexMonNewsPresenter;
    private NewsAdapter mNewsAdapter;
    private EditText serarchKeyWord;
    private ImageView imgBack;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerNews;
    private Map<String, Object> exmap = new HashMap<>();
    private Map<String, Object> normalmap = new HashMap<>();
    private Map<String, Object> typemap = new HashMap<>();
    private int page = 1;
    private android.widget.LinearLayout stLL;
    private com.flyco.tablayout.SlidingTabLayout st;
    private ViewPager vp;
    private View divider;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int currentIndex = 0;
    private List<IndexMonTool> indexMonToolList;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_news;
    }

    @Override
    protected void findViews() {
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if(queryDate==null){
            try {
                queryDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        mNewsAdapter = new NewsAdapter();
        mNewsAdapter.bindToRecyclerView(recyclerNews);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        indexMonNewsPresenter = new IndexMonNewsMainPassPresenter(mContext, this);
        if (TextUtils.isEmpty(title)) {
            if (!isinittab) {
                typemap.put("knowOrInfoStatus", knowOrInfoStatus+"");
                indexMonNewsPresenter.getNewTypeList(typemap);
            }
            stLL.setVisibility(View.VISIBLE);
        } else {
            serarchKeyWord.setText(title);
            stLL.setVisibility(View.GONE);
            normalmap.put("title", title);
            normalmap.put("categoryId", "");
            normalmap.put("knowOrInfoStatus", "");
            indexMonNewsPresenter.getNewList(page + "", normalmap);
        }
        exmap.put("queryDate", queryDate);
        normalmap.put("categoryId", TextUtils.isEmpty(title)?categoryId+"":"");
        normalmap.put("title", title);
        normalmap.put("knowOrInfoStatus", knowOrInfoStatus+"");
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if ("2".equals(knowOrInfoStatus)) {
            indexMonNewsPresenter.getNewListEx(page + "", exmap);
        } else {
            indexMonNewsPresenter.getNewList(page + "", normalmap);
        }
    }

    @Override
    public void getNewListSuccess(List<IndexAllSee> indexAllSees, PageInfoEarly pageInfoEarly) {

        if (pageInfoEarly == null) {
            if (page == 1) {
                showEmpty();
                layoutRefresh.setEnableLoadMore(false);
            } else {
                layoutRefresh.setEnableLoadMore(false);
                layoutRefresh.finishLoadMoreWithNoMoreData();
            }

            return;
        }
        page = pageInfoEarly.currentPage;
        if (page == 1) {
            mNewsAdapter.setNewData(indexAllSees);
            if (indexAllSees.size() == 0) {
                showEmpty();
            }
        } else {
            mNewsAdapter.addData(indexAllSees);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void getNewListSuccessEx(List<IndexAllSee> indexAllSees, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            if (page == 1) {
                showEmpty();
                layoutRefresh.setEnableLoadMore(false);
            } else {
                layoutRefresh.setEnableLoadMore(false);
                layoutRefresh.finishLoadMoreWithNoMoreData();
            }

            return;
        }
        page = pageInfoEarly.currentPage;
        if (page == 1) {
            mNewsAdapter.setNewData(indexAllSees);
            if (indexAllSees.size() == 0) {
                showEmpty();
            }
        } else {
            mNewsAdapter.addData(indexAllSees);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void getNewTypeSucess(List<IndexMonTool> indexAllSees) {
        isinittab = true;
        indexMonToolList = indexAllSees;
        if ("2".equals(knowOrInfoStatus)) {
            indexMonToolList.add(0, new IndexMonTool("综合"));
        }
        buildTabs();
    }

    public void buildTabs() {

        for (int i = 0; i < indexMonToolList.size(); i++) {
            fragments.add(new TmpFragment());
            titles.add(indexMonToolList.get(i).categoryName);
            if ("1".equals(knowOrInfoStatus) && (indexMonToolList.get(i).id + "").equals(categoryId)) {
                currentIndex = i;
            }
        }
        if(fragments.size()>4){
            st.setTabSpaceEqual(false);
        }else {
            st.setTabSpaceEqual(true);
        }
        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        fragmentPagerItemAdapter.notifyDataSetChanged();
        vp.setOffscreenPageLimit(4);
        // bind to SmartTabLayout
        st.setViewPager(vp);
        vp.setCurrentItem(currentIndex);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex = position;
                if (currentIndex == 0 && "2".equals(knowOrInfoStatus)) {
                    page = 1;
                    indexMonNewsPresenter.getNewListEx(page + "", exmap);
                } else {
                    normalmap.put("categoryId", indexMonToolList.get(currentIndex).id+"");
                    normalmap.put("title", "");
                    normalmap.put("knowOrInfoStatus", indexMonToolList.get(currentIndex).knowOrInfoStatus+"");
                    page = 1;
                    indexMonNewsPresenter.getNewList(page + "", normalmap);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    private void initView() {
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        imgBack = (ImageView) findViewById(R.id.img_back);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        serarchKeyWord.setOnEditorActionListener(this);
        stLL = (LinearLayout) findViewById(R.id.stLL);
        st = (SlidingTabLayout) findViewById(R.id.st);
        vp = (ViewPager) findViewById(R.id.vp);
        divider = (View) findViewById(R.id.divider);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page++;
        if (currentIndex == 0&& "2".equals(knowOrInfoStatus)) {

            indexMonNewsPresenter.getNewListEx(page + "", exmap);
        } else {

            indexMonNewsPresenter.getNewList(page + "", normalmap);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page = 1;
        if (currentIndex == 0&& "2".equals(knowOrInfoStatus)) {
            indexMonNewsPresenter.getNewListEx(page + "", exmap);
        } else {
            indexMonNewsPresenter.getNewList(page + "", normalmap);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            page = 1;
            if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
                normalmap.put("title", "");
                exmap.put("title", "");
                stLL.setVisibility(View.VISIBLE);
                if (!isinittab) {
                    typemap.put("knowOrInfoStatus", knowOrInfoStatus+"");
                    indexMonNewsPresenter.getNewTypeList(typemap);
                }
                if (currentIndex == 0&& "2".equals(knowOrInfoStatus)) {
                    indexMonNewsPresenter.getNewListEx(page + "", exmap);
                } else {
                    indexMonNewsPresenter.getNewList(page + "", normalmap);
                }
            } else {
                stLL.setVisibility(View.GONE);
                if (currentIndex == 0&& "2".equals(knowOrInfoStatus)) {
                    exmap.put("title", serarchKeyWord.getText().toString());
                    exmap.put("currentPage","1");
                    indexMonNewsPresenter.getNewListEx(page + "", exmap);
                } else {
                    normalmap.put("title", serarchKeyWord.getText().toString());
                    normalmap.put("categoryId", "");
                    normalmap.put("knowOrInfoStatus", knowOrInfoStatus+"");
                    indexMonNewsPresenter.getNewList(page + "", normalmap);
                }



            }
        }

        return false;
    }
}
