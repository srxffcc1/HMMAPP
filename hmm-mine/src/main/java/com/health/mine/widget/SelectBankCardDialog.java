package com.health.mine.widget;

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

import com.health.mine.R;
import com.health.mine.adapter.SelectBankCardAdapter;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.model.BankCardModel;
import com.healthy.library.utils.TransformUtil;

import java.util.ArrayList;
import java.util.List;

public class SelectBankCardDialog extends BaseDialogFragment {

    private RecyclerView recyclerview;
    private ImageView closeBtn;
    private View view;
    private List<BankCardModel> dataList = new ArrayList<>();
    private SelectBankCardAdapter adapter;
    private getContentListener getListener;
    private int position;

    public static SelectBankCardDialog newInstance(int position) {
        SelectBankCardDialog fragment = new SelectBankCardDialog();
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
            params.height = (int) TransformUtil.dp2px(getContext(), 420f);
            window.setAttributes(params);

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.dialog_select_bank_card_layout, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        recyclerview = view.findViewById(R.id.recyclerview);
        closeBtn = view.findViewById(R.id.closeBtn);
        adapter = new SelectBankCardAdapter(getContext(), dataList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);
        adapter.setServerListener(new SelectBankCardAdapter.SetListener() {
            @Override
            public void onClick(int position) {
                getListener.resultContent(position);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 200);
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public interface getContentListener {

        void resultContent(int position);
    }

    public void setResultListener(getContentListener getListener) {
        this.getListener = getListener;
    }

    public SelectBankCardDialog setData(List<BankCardModel> list) {
        this.dataList.clear();
        this.dataList.addAll(list);
        return this;
    }

}
