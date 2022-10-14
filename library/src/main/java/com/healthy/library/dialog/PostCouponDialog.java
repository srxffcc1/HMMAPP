package com.healthy.library.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy.library.R;
import com.healthy.library.adapter.PostCouponAdapter;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.PostCouponDialogContract;
import com.healthy.library.model.PostActivityList;
import com.healthy.library.presenter.PostCouponDialogPresenter;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;


public class PostCouponDialog extends BaseDialogFragment implements PostCouponDialogContract.View {

    private TextView title;
    private ImageView closeBtn;
    private StatusLayout layoutStatus;
    private RecyclerView couponRecycle;

    private PostCouponAdapter postCouponAdapter;
    private String id;
    private PostCouponDialogPresenter postCouponDialogPresenter;
    private Dialog loading;
    private List<PostActivityList.ActivityCoupon> list = new ArrayList<>();

    public PostCouponDialog setId(String id) {
        this.id = id;
        return this;
    }

    public void setOnConfirmClick(OnSelectConfirm onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
    }

    public interface OnSelectConfirm {
        void onClick(int result);
    }

    private OnSelectConfirm mOnConfirmClick;

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PostCouponDialog newInstance() {
        Bundle args = new Bundle();
        PostCouponDialog fragment = new PostCouponDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomDialogAnimation);
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.5f;
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(R.drawable.shape_dialog);
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);

        }
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(false);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.post_coupon_dialog_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    PostCouponDialog.this.dismiss();
                    return true;
                }
                return false;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void displayDialog(View view) {
        StyledDialog.init(getContext());
        loading = StyledDialog.buildMdLoading().setForceWidthPercent(0.3f).show();
        postCouponDialogPresenter = new PostCouponDialogPresenter(getContext(), this);
        title = (TextView) view.findViewById(R.id.title);
        closeBtn = (ImageView) view.findViewById(R.id.closeBtn);
        layoutStatus = (StatusLayout) view.findViewById(R.id.layout_status);
        couponRecycle = (RecyclerView) view.findViewById(R.id.couponRecycle);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCouponDialog.this.dismiss();
            }
        });
        postCouponAdapter = new PostCouponAdapter();
        couponRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        couponRecycle.setAdapter(postCouponAdapter);
        getData();
        postCouponAdapter.setClickListener(new PostCouponAdapter.OnClickListener() {
            @Override
            public void onClick(String couponId, String activityId) {
                postCouponDialogPresenter.getCoupon(new SimpleHashMapBuilder<String, Object>().puts("couponId", couponId).puts("activityId", activityId)
                        .puts("memberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
            }
        });
    }

    @Override
    public void onSuccessGetList(List<PostActivityList> result) {
        if (result != null && result.size() > 0) {
            layoutStatus.updateStatus(StatusLayout.Status.STATUS_CONTENT);
            list.clear();
            for (int i = 0; i < result.size(); i++) {
                for (int j = 0; j < result.get(i).activityCoupon.size(); j++) {
                    list.add(result.get(i).activityCoupon.get(j));
                }
            }
            postCouponAdapter.setData((ArrayList) list);
        } else {
            layoutStatus.updateStatus(StatusLayout.Status.STATUS_EMPTY);
            layoutStatus.setmEmptyContent("暂无优惠券");
        }
        StyledDialog.dismiss(loading);
    }

    @Override
    public void onSuccessGetCoupon(String result) {
        getData();
        if (result.contains("成功")) {
            Toast.makeText(getContext(), "领取成功", Toast.LENGTH_LONG).show();
        } else {
            showToast(result);

        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mOnConfirmClick != null) {
            mOnConfirmClick.onClick(1);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {
        postCouponDialogPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>().puts("postingId", id));
    }

    @Override
    public void showDataErr() {

    }

}
