//package com.healthy.library.dialog;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.appcompat.app.AlertDialog;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.healthy.library.R;
//import com.healthy.library.widget.WheelPicker;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Li
// * @date 2019/03/22 16:18
// * @des 滚轮选择器
// */
//
//public class WheelDialog extends DialogFragment {
//
//    private AlertDialog mAlertDialog;
//    private List<List<String>> mLists;
//    private int[] mPositions;
//    private LinearLayout mLayoutPicker;
//    private OnConfirmListener mOnConfirmListener;
//    private View.OnClickListener confirmCLick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            WheelDialog.this.dismiss();
//            if (mOnConfirmListener != null) {
//                List<String> dataList = new ArrayList<>();
//                List<Integer> positionList = new ArrayList<>();
//                for (int i = 0; i < mLayoutPicker.getChildCount(); i++) {
//                    WheelPicker picker = (WheelPicker) mLayoutPicker.getChildAt(i);
//                    dataList.add(String.valueOf(picker.getCurrentData()));
//                    positionList.add(picker.getCurrentPosition());
//                }
//                mOnConfirmListener.onConfirm(dataList, positionList);
//            }
//        }
//    };
//    private View.OnClickListener cancelClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            WheelDialog.this.dismiss();
//        }
//    };
//
//    public static WheelDialog newInstance() {
//        Bundle args = new Bundle();
//        WheelDialog fragment = new WheelDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
//        mOnConfirmListener = onConfirmListener;
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        if (getContext() != null && mAlertDialog == null) {
//            View view = LayoutInflater.from(getContext())
//                    .inflate(R.layout.dialog_wheel, null);
//
//            mLayoutPicker = view.findViewById(R.id.layout_picker);
//            if (mLists != null) {
//                for (int i = 0; i < mLists.size(); i++) {
//                    WheelPicker<String> wheelPicker = new WheelPicker<>(getContext());
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT);
//                    params.weight = 1;
//                    mLayoutPicker.addView(wheelPicker, params);
//                    wheelPicker.setDataList(mLists.get(i));
//                    wheelPicker.setCurrentPosition(mPositions[i]);
//                }
//            }
//
//
//            TextView tvCancel = view.findViewById(R.id.tv_cancel);
//            tvCancel.setOnClickListener(cancelClick);
//
//            TextView tvConfirm = view.findViewById(R.id.tv_confirm);
//            tvConfirm.setOnClickListener(confirmCLick);
//
//
//            mAlertDialog = new AlertDialog.Builder(getContext())
//                    .setView(view)
//                    .create();
//            mAlertDialog.setCancelable(false);
//            mAlertDialog.setCanceledOnTouchOutside(false);
//            Window window = mAlertDialog.getWindow();
//            if (window != null) {
//                window.setWindowAnimations(R.style.BottomDialogAnimation);
//                View decorView = window.getDecorView();
//                decorView.setPadding(0, 0, 0, 0);
//                decorView.setBackgroundResource(R.drawable.shape_dialog);
//                WindowManager.LayoutParams params = window.getAttributes();
//                params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                params.gravity = Gravity.BOTTOM;
//            }
//        }
//        return mAlertDialog;
//
//    }
//
//    public void setLists(List<List<String>> lists) {
//        setListsPositions(lists, null);
//
//    }
//
//    public void setListsPositions(List<List<String>> lists, int[] positions) {
//        mLists = lists;
//        if (positions == null || positions.length != lists.size()) {
//            mPositions = new int[lists.size()];
//            for (int i = 0; i < mPositions.length; i++) {
//                mPositions[i] = 0;
//            }
//        } else {
//            mPositions = positions;
//        }
//    }
//
//    public interface OnConfirmListener {
//        /**
//         * 完成
//         *
//         * @param dataList  数据集
//         * @param positions index集
//         */
//        void onConfirm(List<String> dataList, List<Integer> positions);
//    }
//
//    @Override
//    public void show(FragmentManager manager, String tag) {
//        try {
//            super.show(manager, tag);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
