package com.health.discount.widget;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.discount.R;
import com.health.discount.adapter.DiscountSelectGoodsDialogAdapter;
import com.healthy.library.base.BaseDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class DiscountSelectGoodsDialog extends BaseDialogFragment {

    private RecyclerView recyclerview;
    private ImageView closeBtn;
    private View view;
    private DiscountSelectGoodsDialogAdapter adapter;
    private getContentListener getListener;
    private String result = "";
    private int selectPosition = -1;
    private int type = -1;

    public static DiscountSelectGoodsDialog newInstance() {
        DiscountSelectGoodsDialog fragment = new DiscountSelectGoodsDialog();
        Bundle args = new Bundle();
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
        }
        if (view == null) {
            view = inflater.inflate(R.layout.discount_select_goods_dialog_layout, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        recyclerview = view.findViewById(R.id.recyclerview);
        closeBtn = view.findViewById(R.id.closeBtn);
        adapter = new DiscountSelectGoodsDialogAdapter();
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);
        adapter.setModel(null);
//        if (selectPosition != -1) {
//            adapter.setmPosition(selectPosition);
//        }
//        adapter.setServerListener(new SelectBackContentAdapter.SetListener() {
//            @Override
//            public void onClick(int position, String content) {
//                //把点击条目的下标传给适配器
//                adapter.setmPosition(position);
//                adapter.notifyDataSetChanged();
//                result = content;
//                selectPosition = position;
//                getListener.resultContent(result, selectPosition);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getDialog().dismiss();
//                    }
//                }, 100);//延迟100毫秒关闭 不然看不到点击效果
//            }
//        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getListener.resultContent(result, selectPosition);
                getDialog().dismiss();
            }
        });

    }

    public interface getContentListener {

        void resultContent(String result, int position);
    }

    public void setResultListener(getContentListener getListener) {
        this.getListener = getListener;
    }

    public DiscountSelectGoodsDialog setType(int type) {
        this.type = type;
        return this;
    }
}

