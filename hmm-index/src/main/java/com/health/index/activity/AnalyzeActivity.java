package com.health.index.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.healthy.library.routes.IndexRoutes;
import com.health.index.R;
import com.health.index.adapter.AnalyzeAdapter;
import com.healthy.library.constant.UrlKeys;
import com.health.index.contract.AnalyzeContract;
import com.health.index.decoration.AnalyzeDecoration;
import com.health.index.model.AnalyzeModel;
import com.health.index.presenter.AnalyzePresenter;
import com.health.index.widget.SectionAnalysis;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.dialog.SingleWheelDialog;
import com.healthy.library.utils.JsoupCopy;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.PatternUtils;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/04/24 14:54
 * @des B超解读
 */
@Route(path = IndexRoutes.INDEX_ANALYZE_B)
public class AnalyzeActivity extends BaseActivity implements AnalyzeContract.View,
        BaseQuickAdapter.OnItemClickListener {

    private TopBar mTopBar;
    private AnalyzePresenter mPresenter;
    private RecyclerView mRecyclerProjects;
    private AnalyzeAdapter mAdapter;
    private SingleWheelDialog mWeekDialog;

    @Autowired
    String mWeekId;
    private StatusLayout mStatusLayout;
    private FrameLayout mLayoutAnalysis;

    private View.OnClickListener mShowWeekDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showWeekDialog(v);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_analyze;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mRecyclerProjects = findViewById(R.id.recycler_project);
        mStatusLayout = findViewById(R.id.layout_status);
        mLayoutAnalysis = findViewById(R.id.layout_analysis);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)
                mTopBar.getLayoutParams();
        params.topMargin = ResUtils.getStatusBarHeight(mContext);
        mTopBar.setLayoutParams(params);
        mPresenter = new AnalyzePresenter(this, this);

        mAdapter = new AnalyzeAdapter();
        mAdapter.setOnItemClickListener(this);
        mRecyclerProjects.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerProjects.addItemDecoration(new AnalyzeDecoration(mContext, 1));
        mAdapter.bindToRecyclerView(mRecyclerProjects);

        mPresenter.getAnalysis(mWeekId);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        AnalyzeModel model = mAdapter.getData().get(position);

        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_B);
        String url = String.format("%s?id=%s&scheme=%s", urlPrefix, model.getId()+"","BDetail");


        ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                .withString("title", model.getName())
                .withBoolean("needShare",true)
                .withBoolean("isinhome",true)
                .withBoolean("doctorshop",true)
                .withString("stitle","B超解读-"+model.getName())
                .withString("url", url)
                .navigation();
    }

    public void showWeekDialog(View view) {
        if (mWeekDialog == null) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 1; i <= 40; i++) {
                arrayList.add("孕" + i + "周");
            }
            mWeekDialog = SingleWheelDialog.newInstance(arrayList,
                    ParseUtils.parseInt(mWeekId, 1) - 1);
            mWeekDialog.setOnConfirmClick(new SingleWheelDialog.OnConfirmClickListener() {
                @Override
                public void onClick(int pos, String data) {
                    mWeekId = PatternUtils.getNum(data);
                    mPresenter.getAnalysis(mWeekId);
                }
            });
        }
        mWeekDialog.show(getSupportFragmentManager(), "weekDialog");
    }


    @Override
    public void getData() {
        mPresenter.getAnalysis(mWeekId);
    }

    @Override
    public void onGetAnalysisProjectsSuccess(List<AnalyzeModel> projectList,
                                             List<AnalyzeModel> analysisList,
                                             String weekId, String status) {
        addHeaderView(analysisList, weekId, status);
        mWeekId = weekId;
        mAdapter.setNewData(projectList);
    }

    private void addHeaderView(List<AnalyzeModel> analysisList,
                               String weekId, String status) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.index_analyze_header,
                mRecyclerProjects, false);

        FrameLayout layoutHeader = view.findViewById(R.id.layout_analysis);
        TextView tvWeek = view.findViewById(R.id.tv_week);
        tvWeek.setOnClickListener(mShowWeekDialogListener);
        view.findViewById(R.id.tv_week_prefix).setOnClickListener(mShowWeekDialogListener);
        view.findViewById(R.id.tv_week_suffix).setOnClickListener(mShowWeekDialogListener);
        view.findViewById(R.id.iv_week).setOnClickListener(mShowWeekDialogListener);
        tvWeek.setText(weekId);
        layoutHeader.removeAllViews();
        if ("1".equals(status)) {
            TextView textView = new TextView(mContext);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TransformUtil.dp2px(mContext, 14));
            textView.setTextColor(Color.parseColor("#FF6266"));
            if (analysisList.size() > 0) {
                textView.setText(analysisList.get(0).getValue());
            }
            layoutHeader.addView(textView);
        } else {
            SectionAnalysis sectionAnalysis = new SectionAnalysis(mContext);
            sectionAnalysis.setData(analysisList);
            layoutHeader.addView(sectionAnalysis);
        }
        mAdapter.setHeaderView(view);
    }
}