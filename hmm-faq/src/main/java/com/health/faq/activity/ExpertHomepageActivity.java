package com.health.faq.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.health.faq.R;
import com.health.faq.adapter.ExpertHomepageAdapter;
import com.health.faq.contract.ExpertHomepageContract;
import com.health.faq.model.ExpertHomepageModel;
import com.health.faq.presenter.ExpertHomepagePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.JsoupCopy;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.TopBar;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/07/16 15:27
 * @des 专家主页
 */
@Route(path = FaqRoutes.FAQ_EXPERT_HOMEPAGE)
public class ExpertHomepageActivity extends BaseActivity implements ExpertHomepageContract.View,
        SwipeRefreshLayout.OnRefreshListener, IsNeedShare {

    @Autowired
    String id;

    private TextView mTvAsk;
    private RecyclerView mRecycler;
    private ExpertHomepagePresenter mPresenter;
    private ExpertHomepageAdapter mAdapter;
    private SwipeRefreshLayout mLayoutRefresh;
    private com.healthy.library.widget.TopBar topBar;
    private String surl;
    private String sdes;
    private String stitle;
    private Bitmap sBitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_expert_homepage;
    }

    @Override
    protected void findViews() {
        mTvAsk = findViewById(R.id.tv_ask);
        mRecycler = findViewById(R.id.recycler);
        mLayoutRefresh = findViewById(R.id.layout_refresh);
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mLayoutRefresh.setOnRefreshListener(this);
        mPresenter = new ExpertHomepagePresenter(this, this);
        mAdapter = new ExpertHomepageAdapter(null);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.bindToRecyclerView(mRecycler);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_ask) {
                    routeAsk();
                } else if (view.getId() == R.id.iv_ask) {
                    if (mAdapter != null && mAdapter.getData().size() > 0
                            && mAdapter.getData().get(0).getItemType() == ExpertHomepageModel.TYPE_HEADER) {
                        //System.out.println("跳问答详情2");
                        ARouter.getInstance()
                                .build(FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                                .withString("questionId", mAdapter.getData()
                                        .get(position).getAnswer().questionId)
                                .navigation();
                    }
                }
            }
        });
    topBar.setSubmitListener(new OnSubmitListener() {
        @Override
        public void onSubmitBtnPressed() {
            showShare();
        }
    });
        mTvAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeAsk();
            }
        });
        getData();
    }

    @Override
    public void onGetExpertInfoSuccess(List<ExpertHomepageModel> list, int answerCount, boolean online) {
        mTvAsk.setVisibility(answerCount == 0 ? View.GONE : View.VISIBLE);
        mTvAsk.setEnabled(online);
        mTvAsk.setText(online ? "立即提问" : "专家已下线，不可提问");
        sdes="【"+list.get(0).getHeaderInfo().realName+"】"+""+ JsoupCopy.parse(list.get(0).getHeaderInfo().goodSkills).text();
        com.healthy.library.businessutil.GlideCopy.with(mContext).load(list.get(0).getHeaderInfo().faceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(new SimpleTarget<Drawable>() {

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        sBitmap= DrawableUtils.drawableToBitmap(resource);
                    }
                });
//        sBitmap=
        mAdapter.setNewData(list);
    }

    @Override
    public void getData() {
        mPresenter.getExpertInfo(id);
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onRequestFinish() {
        mLayoutRefresh.setRefreshing(false);
    }

    public void routeAsk() {

        if (mAdapter != null && mAdapter.getData().size() > 0
                && mAdapter.getData().get(0).getItemType() == ExpertHomepageModel.TYPE_HEADER) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure","专家主页");
            MobclickAgent.onEvent(mContext, "event2QuestExportFrom",nokmap);
            ExpertHomepageModel.HeaderInfo headerInfo = mAdapter.getData().get(0).getHeaderInfo();
            ARouter.getInstance()
                    .build(FaqRoutes.FAQ_ASK_EXPERT)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK)
                    .withString("id", headerInfo.id)
                    .navigation();

        }


    }

    @Override
    public String getSurl() {

        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_proAnswer);
        String url = String.format("%s?id=%s&scheme=HMMExpertHomePage",urlPrefix,id);
        surl=url;
        return surl;
    }

    @Override
    public String getSdes() {
        return sdes;
    }

    @Override
    public String getStitle() {
        return "憨妈妈专家";
    }

    @Override
    public Bitmap getsBitmap() {
        return sBitmap;
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
    }
}
