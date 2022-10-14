package com.health.servicecenter.widget;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.servicecenter.R;
import com.health.servicecenter.adapter.SelectBackContentAdapter;
import com.healthy.library.base.BaseDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class SelectBackContentDialog extends BaseDialogFragment {

    private RecyclerView recyclerview;
    private ImageView closeBtn;
    private View view;
    private List<String> stringList = new ArrayList<>();
    private SelectBackContentAdapter adapter;
    private getContentListener getListener;
    private String result = "";
    private int selectPosition = -1;
    private int type = -1;

    public static SelectBackContentDialog newInstance(int position) {
        SelectBackContentDialog fragment = new SelectBackContentDialog();
        Bundle args = new Bundle();
        args.putInt("selectPosition", position);
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            selectPosition = getArguments().getInt("selectPosition");
        }
        if (view == null) {
            view = inflater.inflate(R.layout.dialog_select_back_content, container, false);
            initStringList();
            initView();
        }
        return view;
    }

    private void initView() {
        recyclerview = view.findViewById(R.id.recyclerview);
        closeBtn = view.findViewById(R.id.closeBtn);
        adapter = new SelectBackContentAdapter(getContext(), stringList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);
        if (selectPosition != -1) {
            adapter.setmPosition(selectPosition);
        }
        adapter.setServerListener(new SelectBackContentAdapter.SetListener() {
            @Override
            public void onClick(int position, String content) {
                //把点击条目的下标传给适配器
                adapter.setmPosition(position);
                adapter.notifyDataSetChanged();
                result = content;
                selectPosition = position;
                getListener.resultContent(result, selectPosition);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 100);//延迟100毫秒关闭 不然看不到点击效果
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getListener.resultContent(result, selectPosition);
                dismiss();
            }
        });

    }

    public interface getContentListener {

        void resultContent(String result, int position);
    }

    public void setResultListener(getContentListener getListener) {
        this.getListener = getListener;
    }

    public SelectBackContentDialog setType(int type) {
        this.type = type;
        return this;
    }

    private void initStringList() {
        stringList.clear();
        if (type == 1) {//服务退订跟标品退订可选理由不一样
            stringList.add("买多/买错/计划有变");
            stringList.add("商家让我退掉");
            stringList.add("商品与页面描述不符");
            stringList.add("商家未营业/已停业");
            stringList.add("其他");
        } else if (type == 2 || type == 3) {//目前积分只能换商品
            stringList.add("发错货");
            stringList.add("商品损坏");
            stringList.add("商品与页面描述不符");
            stringList.add("缺少件");
            stringList.add("质量问题");
            stringList.add("不想要了");
            stringList.add("其他");
        }
    }
}
