package com.healthy.library.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy.library.R;
import com.healthy.library.adapter.DropDownType;
import com.healthy.library.adapter.RoundGridDropDownAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/19 15:47
 * @des 时间选择器 阴历
 */

public class LunarDateDialog extends DialogFragment {
    private AlertDialog mAlertDialog;
    RecyclerView rv;

    RoundGridDropDownAdapter adapter;



    public static LunarDateDialog newInstance(String title) {
        Bundle args = new Bundle();
        LunarDateDialog fragment = new LunarDateDialog();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    public void setItemClickCallBack(ItemClickCallBack itemClickCallBack) {
        this.itemClickCallBack = itemClickCallBack;
    }

    ItemClickCallBack itemClickCallBack;
    private View.OnClickListener confirmCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LunarDateDialog.this.dismiss();
            if (itemClickCallBack != null) {
                itemClickCallBack.click(adapter.getSelectId(),adapter.getSelectName());
            }
        }
    };
    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LunarDateDialog.this.dismiss();
        }
    };

    public interface ItemClickCallBack {
        void click(String id, String name);

        void dismiss();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mAlertDialog == null && getContext() != null) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date_lunar, null);
            rv = view.findViewById(R.id.rv);
            rv.setLayoutManager(new GridLayoutManager(getContext(), 4));
            TextView tvTitle=view.findViewById(R.id.tv_title);
            if (getArguments() != null) {
                String title = getArguments().getString("title");
                if (TextUtils.isEmpty(title)) {
                    tvTitle.setVisibility(View.INVISIBLE);
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText(title);
                }
            }
            TextView tvCancel = view.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(cancelClick);

            TextView tvConfirm = view.findViewById(R.id.tv_confirm);
            tvConfirm.setOnClickListener(confirmCLick);

            adapter = new RoundGridDropDownAdapter(R.layout.item_choose_type, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            rv.setAdapter(adapter);
            List<DropDownType> list=new ArrayList<>();
            list.add(new DropDownType("1","正月"));
            list.add(new DropDownType("2","二月"));
            list.add(new DropDownType("3","三月"));
            list.add(new DropDownType("4","四月"));
            list.add(new DropDownType("5","五月"));
            list.add(new DropDownType("6","六月"));
            list.add(new DropDownType("7","七月"));
            list.add(new DropDownType("8","八月"));
            list.add(new DropDownType("9","九月"));
            list.add(new DropDownType("10","十月"));
            list.add(new DropDownType("11","十一月"));
            list.add(new DropDownType("12","腊月"));
            setData(list);
            adapter.setSelectId(list.get(0).getId());
            adapter.setSelectName(list.get(0).getName());
            mAlertDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .create();
            mAlertDialog.setCancelable(false);
            mAlertDialog.setCanceledOnTouchOutside(false);
            Window window = mAlertDialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.BottomDialogAnimation);
                View decorView = window.getDecorView();
                decorView.setPadding(0, 0, 0, 0);
                decorView.setBackgroundResource(R.drawable.shape_dialog);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.BOTTOM;
            }
        }

        return mAlertDialog;
    }

    public void setData(List<DropDownType> list) {
        adapter.setNewData(list);
        adapter.setSelectId(list.get(0).getId());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}