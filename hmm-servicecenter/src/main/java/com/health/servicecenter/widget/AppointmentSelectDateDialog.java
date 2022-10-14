package com.health.servicecenter.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.servicecenter.R;
import com.health.servicecenter.adapter.SelectServerDataAdapter;
import com.health.servicecenter.adapter.SelectServerDialogTimeAdapter;
import com.health.servicecenter.contract.AppointmentMainContract;
import com.health.servicecenter.presenter.AppointmentMainPresenter;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.interfaces.OnItemClickListener;
import com.healthy.library.interfaces.OnNetRetryListener;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.model.AppointmentTimeSettingModel;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.ServerDateTool;
import com.healthy.library.widget.StatusLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.Disposable;


public class AppointmentSelectDateDialog extends BaseDialogFragment implements AppointmentMainContract.View, OnNetRetryListener {

    private AlertDialog mAlertDialog;
    private RecyclerView recyclerView;
    private SelectServerDataAdapter adapter;
    //private FragmentSelectDateTools fragmentSelectDateTools;
    private RecyclerView sectionRecycler;
    private View view;
    private getTimeListener getListener;
    private String resultDate, resultTime, shopId, projectId;
    private int currenIndex, currenPosition;
    private Map<String, Object> mMap = new HashMap<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private AppointmentMainPresenter mMainPresenter;
    private SelectServerDialogTimeAdapter timeAdapter;
    private int mTimePosition = -1;

    private List<AppointmentTimeSettingModel.ShopTimeSetting> listAll = new ArrayList<>();
    private List<ServerDateTool> dateList = new ArrayList<>();

    private View.OnClickListener confirmCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mTimePosition == -1) {
                showToast("请选择时间");
                return;
            }
            if (getListener != null) {
                getListener.resultTime(dateList.get(currenIndex), listAll.get(mTimePosition), currenIndex, mTimePosition);
            }
            AppointmentSelectDateDialog.this.dismiss();

        }
    };
    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTimePosition = -1;
            AppointmentSelectDateDialog.this.dismiss();
        }
    };
    private int mRange;
    private StatusLayout mStatusLayout;
    private String formatDate;
    private List<ServerDateTool> dates;

    public static AppointmentSelectDateDialog newInstance(int index, int position, String projectId, String shopId) {
        Bundle args = new Bundle();
        AppointmentSelectDateDialog fragment = new AppointmentSelectDateDialog();
        args.putInt("currenIndex", index);
        args.putInt("currenPosition", position);
        args.putString("projectId", projectId);
        args.putString("shopId", shopId);
        fragment.setArguments(args);
        return fragment;
    }

    public void setPosition(int currenIndex, int mTimePosition, String formatDate) {
        this.currenIndex = currenIndex;
        this.mTimePosition = mTimePosition;
        this.formatDate = formatDate;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null) {
            adapter.setSelected(currenIndex);
        }

        if (timeAdapter != null) {
            timeAdapter.setPosition(mTimePosition);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if (mAlertDialog == null && getContext() != null) {
            if (getArguments() != null) {
                currenIndex = getArguments().getInt("currenIndex");
                currenPosition = getArguments().getInt("currenPosition");
                shopId = getArguments().getString("shopId");
                projectId = getArguments().getString("projectId");

                mMap.put("projectId", projectId);
                mMap.put("shopId", shopId);
            } else {
                return super.onCreateDialog(savedInstanceState);
            }

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_service_data_layout, null);
                initView();
            }

            mMainPresenter = new AppointmentMainPresenter(getActivity(), this);

            mAlertDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .create();

            Window window = mAlertDialog.getWindow();
            window.setWindowAnimations(R.style.BottomDialogAnimation);
            if (window != null) {
                View decorView = window.getDecorView();
                decorView.setPadding(0, 0, 0, 0);
                decorView.setBackgroundResource(R.drawable.shape_dialog);
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.BOTTOM;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(params);

            }
            mAlertDialog.setCancelable(true);
            mAlertDialog.setCanceledOnTouchOutside(true);
            //获取当天时间 格式（yyyy-MM-dd)
            Calendar calendar = Calendar.getInstance();
            formatDate = dateFormat.format(calendar.getTime());

        }
        getData();
        //if (ListUtil.isEmpty(dateList) && mTimePosition == -1) {

        //}

        return mAlertDialog;
    }

    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            currenIndex = getArguments().getInt("currenIndex");
            currenPosition = getArguments().getInt("currenPosition");
            shopId = getArguments().getString("shopId");
            projectId = getArguments().getString("projectId");

            mMap.put("shopId", shopId);
            mMap.put("projectId", projectId);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.dialog_select_service_data, container, false);
            initView();
        }

        Window window = getDialog().getWindow();
        window.setWindowAnimations(R.style.BottomDialogAnimation);
        if (window != null) {
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(R.drawable.shape_dialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);

        }
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        return view;
    }*/

    private void initView() {

        mStatusLayout = view.findViewById(R.id.layout_status);
        mStatusLayout.setOnNetRetryListener(this);
        recyclerView = view.findViewById(R.id.recycler_server_time);
        TextView tvCancel = view.findViewById(com.healthy.library.R.id.tv_cancel);
        tvCancel.setOnClickListener(cancelClick);
        TextView tvConfirm = view.findViewById(com.healthy.library.R.id.tv_confirm);
        tvConfirm.setOnClickListener(confirmCLick);
        adapter = new SelectServerDataAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ToolDecoration());
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                if (pos == currenIndex && !ListUtil.isEmpty(listAll)) {
                    return;
                }
                mTimePosition = -1;
                currenIndex = pos;
                adapter.setSelected(pos);
                listAll.clear();
                timeAdapter.setPosition(-1);
                timeAdapter.setData(listAll);
                formatDate = dateList.get(currenIndex).getDate();
                getData();
                //fragmentSelectDateTools.refresh(pos);
            }
        });

        //initList();
        //右侧列表
        sectionRecycler = view.findViewById(R.id.time_section_recycler);
        sectionRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        timeAdapter = new SelectServerDialogTimeAdapter(getActivity());
        sectionRecycler.setAdapter(timeAdapter);
        timeAdapter.setServerListener(new SelectServerDialogTimeAdapter.SetListener() {
            @Override
            public void onClick(int position) {
                timeAdapter.setPosition(position);
                mTimePosition = position;
                timeAdapter.notifyDataSetChanged();
            }
        });
        //dateList = getSevendate(7);//我写死了7   后面要按门店配置的时间来
    }

    private void initList() {//把上午下午时间段拼到一起
/*        listAll.clear();
        listAll.add(new ShopServerTime("上午", 0,
                0, 0, 1, true, false));
        listAll.add(new ShopServerTime("9:30-10:30", 0,
                0, 0, 1, false, false));
        listAll.add(new ShopServerTime("下午", 0,
                0, 0, 2, true, false));
        listAll.add(new ShopServerTime("2:30-3:30", 0,
                1, 0, 2, false, false));
        if (currenPosition != 0) {
            listAll.get(currenPosition).setClick(true);
            currenPosition = 0;
        }*/
    }

    @Override
    public void getData() {
        mMap.put("appointmentDate", formatDate);
        mMainPresenter.getSettingTimeList(mMap);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //要等dialogFragment加载完成之后再添加子fragment
       /* fragmentSelectDateTools = FragmentSelectDateTools.newInstance();
        fragmentSelectDateTools.setData(dateList);
        FragmentManager childFragmentManager = getChildFragmentManager();
        childFragmentManager.beginTransaction().replace(R.id.recycler_time_section, fragmentSelectDateTools).commitAllowingStateLoss();
        fragmentSelectDateTools.setResultDataListener(new FragmentSelectDateTools.getResultDataListener() {
            @Override
            public void resultData(String date, String timeslot, int ind, int pos) {
                resultDate = date;
                resultTime = timeslot;
                getListener.resultTime(resultDate, resultTime, ind, pos);
                AppointmentSelectDateDialog.this.dismiss();
            }
        });
        if (currenPosition != 0) {
            currentIndex = currenIndex;
            adapter.setSelected(currenIndex);
            fragmentSelectDateTools.refresh2(currenIndex, currenPosition);
        }*/
    }

    @Override
    public void onGetMainListSuccess(List<AppointmentMainItemModel> modelList) {

    }

    @Override
    public void onGetStoreDetailSuccess(ShopDetailModel shopDetailModel) {

    }

    @Override
    public void onGetMainDetailSuccess(AppointmentMainItemModel detailModel) {

    }

    @Override
    public void onGetTimeSettingSuccess(AppointmentTimeSettingModel timeSettingModel) {
        //配置时间
        AppointmentTimeSettingModel.ShopSettingModel shopSetting = timeSettingModel.getShopSetting();
        if (shopSetting != null) {
            int appointmentRange = shopSetting.getAppointmentRange();
            if (mRange != appointmentRange) {
                mRange = appointmentRange;
                dateList = getSevenDate(mRange);
                adapter.setData(dateList);
            }
        }
        listAll.clear();
        List<AppointmentTimeSettingModel.ShopTimeSetting> shopTimeSettings = timeSettingModel.getShopTimeSettings();
        if (!ListUtil.isEmpty(shopTimeSettings)) {
            /*if (currenPosition != 0) {
                listAll.get(currenPosition).setChecked(true);
                currenPosition = 0;
            }*/
            listAll.addAll(shopTimeSettings);
            timeAdapter.setPosition(mTimePosition);
            timeAdapter.setData(listAll);
        }

    }

    @Override
    public void onGetPayInfoSuccess(Map<String, Object> payInfoMap) {

    }

    @Override
    public void onAddServiceSuccess(long id, String payOrderId) {

    }

    @Override
    public void getPayStatusSuccess(String status) {

    }

    @Override
    public void showLoading() {
        if (mStatusLayout != null) mStatusLayout.updateStatus(StatusLayout.Status.STATUS_LOADING);
    }

    @Override
    public void showToast(CharSequence msg) {
        Toast roast = Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        if (cantoast) {
            cantoast = false;
            ////System.out.println("展示Toast");
            roast.show();
            cantoast = true;
        }
    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {
        if (mStatusLayout != null) mStatusLayout.updateStatus(StatusLayout.Status.STATUS_CONTENT);
    }

    @Override
    public void showEmpty() {
        if (mStatusLayout != null) mStatusLayout.updateStatus(StatusLayout.Status.STATUS_EMPTY);
    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void showDataErr() {
        if (mStatusLayout != null) mStatusLayout.updateStatus(StatusLayout.Status.STATUS_DATA_ERR);

    }

    @Override
    public void onRetryClick() {
        getData();
    }

    public interface getTimeListener {

        void resultTime(ServerDateTool date, AppointmentTimeSettingModel.ShopTimeSetting timeSetting, int ind, int pos);
    }

    public void setResultTimeListener(getTimeListener getListener) {
        this.getListener = getListener;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取今天往后时间段的日期集合（几月几号）
     */
    public List<ServerDateTool> getSevenDate(int advanceDays) {

        if (advanceDays == 0) {
            AppointmentSelectDateDialog.this.dismiss();
            return null;
        }
        String mYear; // 当前年
        String mMonth; // 月
        String mDay;//日

        if (dates == null) {
            dates = new ArrayList<ServerDateTool>();
        } else {
            dates.clear();
        }
        //i <= advanceDays 2021-07-06 洋洋提出只根据后台(advanceDays)排出日期
        for (int i = 0; i < advanceDays; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + i);
            mYear = String.valueOf(calendar.get(Calendar.YEAR));//获得当前年份
            mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);// 获取当前月份
            mDay = String.valueOf(calendar.get(Calendar.DATE));// 获取当前日份的日期号码
            if (i == 0) {
                dates.add(new ServerDateTool(dateFormat.format(calendar.getTime()), mYear, mMonth, mDay, mMonth + "月" + mDay + "日(今天)"));
            } else if (i == 1) {
                dates.add(new ServerDateTool(dateFormat.format(calendar.getTime()), mYear, mMonth, mDay, mMonth + "月" + mDay + "日(明天)"));
            } else {
                dates.add(new ServerDateTool(dateFormat.format(calendar.getTime()), mYear, mMonth, mDay, mMonth + "月" + mDay + "日"));
            }

        }
        return dates;
    }
}
