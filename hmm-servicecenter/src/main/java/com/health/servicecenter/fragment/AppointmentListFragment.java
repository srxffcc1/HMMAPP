package com.health.servicecenter.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.AppointmentListAdapter;
import com.health.servicecenter.contract.AppointmentListContract;
import com.health.servicecenter.presenter.AppointmentListPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.model.ChatMessage;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 我的预约列表内容页
 */
public class AppointmentListFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, AppointmentListContract.View {

    private String[] mStatus;
    private RecyclerView mRv;
    private RefreshLayout mRefreshLayout;
    private ArrayList<AppointmentListItemModel> mDatas = new ArrayList<>();
    private AppointmentListAdapter mAdapter;
    private StatusLayout mStatusLayout;
    private AppointmentListPresenter mListPresenter;
    private boolean isLoadData = true;
    // 1 待接单 2 预约成功 3 已完成 4 已取消
    public String status;
    private List<Fragment> fragments;
    private int currentPage = 1;
    private int mRequestFinishCount = 0;

    public AppointmentListFragment() {
    }

    public static AppointmentListFragment newInstance(String status) {
        AppointmentListFragment fragment = new AppointmentListFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString("status");
            mStatus = new String[]{status};
        }
        mListPresenter = new AppointmentListPresenter(mContext, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_appointment_list;
    }

    @Override
    protected void findViews() {
        mRv = findViewById(R.id.rv);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mStatusLayout = findViewById(R.id.layout_status);
        mStatusLayout.setmEmptyContent("暂无预约记录");
        mStatusLayout.setEmptyBottomBtn(true, "去预约");
        setStatusLayout(mStatusLayout);
        initListener();
        //待接单情况下需要用到
        if ("1".equals(status)) {
            fragments = getFragmentManager().getFragments();
        }
    }

    private void initListener() {

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                getData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                getData();
            }
        });

        mStatusLayout.setEmptyBottomClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_APPOINTMENTMAIN)
                        .navigation();
            }
        });
    }

    public void setLoadData(boolean isLoadData) {
        currentPage = 1;
        this.isLoadData = isLoadData;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ListUtil.isEmpty(mDatas)) {
            isLoadData = true;
        }
        if (isLoadData) {
            getData();
            isLoadData = false;
        }
    }

    @Override
    public void getData() {
        getBasemap().clear();
        getBasemap().put("pageNum", String.valueOf(currentPage));
        getBasemap().put("statusList", mStatus);
        mListPresenter.getList(getBasemap());
    }

    private void setAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        } else {
            mRv.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new AppointmentListAdapter();
            mRv.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.wheel_cancel_appointment) {
                        //是否超出取消预约时间
                        if (isCancelTimeOut(position)) {
                            return;
                        }

                        getBasemap().clear();
                        getBasemap().put("id", String.valueOf(mDatas.get(position).getId()));
                        //mListPresenter.cancleAppointment(basemap);
                        //取消预约操作
                        /*if (1 == mDatas.get(position).getPayType()) {*/
                        isAgree("", "<br/>是否确定取消此服务！<br/>", 1, null);
                         /*}else {
                             isAgree("", "<br/>是否确定取消此服务！<br/><br/>资金将会从<font color=\"#ff0000\">原路退回</font>至您的账户中!", 1, null);
                         }*/
                    }
                }
            });
            mAdapter.setNewData(mDatas);
        }
    }

    /**
     * 用于判断是否超时取消预约时间范围
     *
     * @param position
     * @return
     */
    private boolean isCancelTimeOut(final int position) {
        AppointmentListItemModel model = mDatas.get(position);
        String mStartTimeStr = model.getAppointmentDate() + " " + model.getAppointmentRangeStartTime();
        int mAdvanceCancelNumber = model.getAdvanceCancelNumber();
        int mAdvanceCancelUnit = model.getAdvanceCancelUnit();
        boolean isError = false;
        String advanceCancelUnit = mAdvanceCancelUnit == 2 ? "天" : "小时";

        /**
         * 预约起始时间 （格式 2021-06-17）
         */
        String[] mStartDate = null;
        if (!TextUtils.isEmpty(mStartTimeStr)) {
            String[] splitAll = mStartTimeStr.split("\\s+");
            mStartDate = splitAll[0].split("-");
        }

        /**
         * 当前时间 （格式 2021-06-18）
         */
        String[] mThisDate = null;
        String mCurrentTimeMillis = String.valueOf(System.currentTimeMillis());
        String thisTime = DateUtils.formatLongAll(mCurrentTimeMillis);
        String[] splitAll = thisTime.split("\\s+");
        mThisDate = splitAll[0].split("-");

        //两个时间相差距离多少天
        String[] distanceTime = DateUtils.getDistanceTime(mStartTimeStr, thisTime);
        //单独只判断时间差不太能行
        if (mStartDate != null && mStartDate.length > 2 && mThisDate.length > 2) {
            //如果年份不同或月份不同
            if (!mStartDate[0].equals(mThisDate[0])
                    || !mStartDate[1].equals(mThisDate[1])) {
                isError = true;
            } else {
                //按天比
                if (2 == mAdvanceCancelUnit) {
                    //根据时间差天数小于规定时间 (错误情况)
                    if (Integer.parseInt(distanceTime[0]) < mAdvanceCancelNumber) {
                        isError = true;
                    }
                }
                //按小时比
                if (1 == mAdvanceCancelUnit) {
                    //根据预约时间天数小于当前天数 或 时间差小时小于规定时间 (错误情况)
                    if ((Integer.parseInt(mStartDate[2]) < Integer.parseInt(mThisDate[2]))
                            || (Integer.parseInt(distanceTime[1]) < mAdvanceCancelNumber)) {
                        isError = true;
                    }
                }
            }
        }

        if (isError) {
            showToast("到店前" + mAdvanceCancelNumber + advanceCancelUnit + "可取消");
        }
        return isError;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //执行跳转预约详情界面
        ARouter.getInstance()
                .build(ServiceRoutes.SERVICE_APPOINTMENTRESULT)
                //详情id
                .withLong("id", mDatas.get(position).getId())
                .navigation(mActivity, Constants.STATUS_APPOINTMENT_LIST_CODE);
    }

    @Override
    public void onGetListSuccess(List<AppointmentListItemModel> modelList) {
        if (currentPage == 1) {
            mDatas.clear();
        }
        mRequestFinishCount = 0;
        if (ListUtil.isEmpty(modelList)) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            mRefreshLayout.resetNoMoreData();
            mRefreshLayout.finishLoadMore();
        }
        mDatas.addAll(modelList);

        if (ListUtil.isEmpty(mDatas)) {
            showEmpty();
            return;
        }
        if (mDatas.size() < 10) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        }
        showContent();
        setAdapter();
    }

    @Override
    public void onGetDetailsSuccess(AppointmentListItemModel model) {

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        mRequestFinishCount++;
        mRefreshLayout.finishRefresh();
        if (mRequestFinishCount == 2) {
            mRefreshLayout.finishLoadMore();
            mRequestFinishCount = 0;
        }
    }

    @Override
    public void onCleanSuccess() {
        //取消成功回调
        showToast("取消成功");
        getData();

        //同时刷新取消栏内容
        if (!ListUtil.isEmpty(fragments)) {
            AppointmentListFragment fragment = (AppointmentListFragment) fragments.get(fragments.size() - 1);
            fragment.setLoadData(true);
            fragment.onResume();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.STATUS_APPOINTMENT_LIST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    //更新数据
                    isLoadData = data.getBooleanExtra("isLoadData", false);
                }
            }
        }
    }

    private void isAgree(String title, String msg, final int type, final ChatMessage chatMessage) {
        StyledDialog.init(mContext);
        StyledDialog.buildIosAlert(title, HtmlCompat.fromHtml(msg, HtmlCompat.FROM_HTML_MODE_LEGACY), new MyDialogListener() {
            @Override
            public void onFirst() {
            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                mListPresenter.cancleAppointment(getBasemap());
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
                .setBtnText("取消", "确定")
                .show();
    }
}