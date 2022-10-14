package com.health.servicecenter.widget;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.servicecenter.R;
import com.health.servicecenter.adapter.PickDataLeftAdapter;
import com.health.servicecenter.fragment.FragmentPickDateChild;
import com.healthy.library.model.GoodsPickDay;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.interfaces.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsPickDateDialog extends BaseDialogFragment {

    private View view;
    private TextView tvChooseTimeTitle;
    private RecyclerView recyclerServerTime;
    private FrameLayout recyclerTimeSection;
    int currentIndex = -1;
    private FragmentPickDateChild fragmentPickDateChild;
    PickDataLeftAdapter pickDataLeftAdapter;
    private ImageView closeBtn;
    OnDialogDateClickListener onDialogDateClickListener;

    Map<String, String> chosemap = new HashMap<>();

    public void setOnDialogDateClickListener(OnDialogDateClickListener onDialogDateClickListener) {
        this.onDialogDateClickListener = onDialogDateClickListener;
    }

    public static GoodsPickDateDialog newInstance() {
        Bundle args = new Bundle();
        GoodsPickDateDialog fragment = new GoodsPickDateDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
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
        //getDialog().setCancelable(false);
        //getDialog().setCanceledOnTouchOutside(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {

        }
        view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_time_select, container, false);
            initView();
        }
        return view;
    }

    private void initView() {

        tvChooseTimeTitle = (TextView) view.findViewById(R.id.tv_choose_time_title);
        recyclerServerTime = (RecyclerView) view.findViewById(R.id.recycler_server_time);
        recyclerTimeSection = (FrameLayout) view.findViewById(R.id.recycler_time_section);
        closeBtn = (ImageView) view.findViewById(R.id.closeBtn);

        if (pickDataLeftAdapter == null) {

            pickDataLeftAdapter = new PickDataLeftAdapter(getContext());
//            pickDataLeftAdapter.setData(new SimpleArrayListBuilder<GoodsPickDay>()
//                    .adds(new GoodsPickDay("6月30号"))
//                    .adds(new GoodsPickDay("7月1号"))
//                    .adds(new GoodsPickDay("7月2号"))
//                    .adds(new GoodsPickDay("7月3号"))
//                    .adds(new GoodsPickDay("7月4号")));
            pickDataLeftAdapter.setData(getFiveDay());


        }

        pickDataLeftAdapter.setmItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                if (pos == currentIndex) {
                    return;
                }
                currentIndex = pos;
                pickDataLeftAdapter.setSelected(pos);
                fragmentPickDateChild.refresh(null, pickDataLeftAdapter.getSelectDay());
            }
        });
        recyclerServerTime.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerServerTime.setAdapter(pickDataLeftAdapter);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private List<GoodsPickDay> getFiveDay() {
        List<GoodsPickDay> goodsPickDayList = new ArrayList<>();
        Calendar calendarNow = Calendar.getInstance();
//        calendarNow.set(Calendar.MONTH,11);
//        calendarNow.set(Calendar.DAY_OF_MONTH,1);
        Date dateNow = calendarNow.getTime();

        Calendar calendar17 = Calendar.getInstance();
        calendar17.set(Calendar.MINUTE, 0);
        calendar17.set(Calendar.HOUR_OF_DAY, 17);
        calendar17.set(Calendar.SECOND, 0);

        if (calendarNow.getTimeInMillis() > calendar17.getTimeInMillis()) {//当前时间已经大于0点了 说明只能选明天了
            calendarNow.add(Calendar.DAY_OF_MONTH, 1);
            dateNow = calendarNow.getTime();
        }
        for (int i = 0; i < 5; i++) {
            calendarNow.setTime(dateNow);
            calendarNow.add(Calendar.DAY_OF_MONTH, i);
            goodsPickDayList.add(new GoodsPickDay(new SimpleDateFormat("yyyy-MM-dd").format(calendarNow.getTime())));
        }
        return goodsPickDayList;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentPickDateChild = FragmentPickDateChild.newInstance();
        fragmentPickDateChild.setOnFragmentDateClickListener(new FragmentPickDateChild.OnFragmentDateClickListener() {
            @Override
            public void onFragmentDateClick(String result) {
                if (onDialogDateClickListener != null) {
                    chosemap.put(pickDataLeftAdapter.getSelectDay(), result);
                    onDialogDateClickListener.onDialogDateClick(pickDataLeftAdapter.getSelectDay(), result);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 100);

            }
        });
        FragmentManager childFragmentManager = getChildFragmentManager();
        childFragmentManager.beginTransaction().replace(R.id.recycler_time_section, fragmentPickDateChild).commitAllowingStateLoss();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentPickDateChild.refresh(chosemap.get(pickDataLeftAdapter.getSelectDay()), pickDataLeftAdapter.getSelectDay());
            }
        }, 300);

    }

    public void clearSelect() {
        currentIndex = 0;
        pickDataLeftAdapter.setSelected(0);
    }

    public interface OnDialogDateClickListener {
        void onDialogDateClick(String day, String time);
    }
}
