package com.healthy.library.business;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.healthy.library.R;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.interfaces.IHmmCoupon;
import com.healthy.library.utils.FormatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CouponChoseDialog extends BaseDialogFragment {

    private RelativeLayout tabLL;
    private SlidingTabLayout st;
    private ImageView imgBack;
    private ViewPager vp;
    private LinearLayout submitLL;
    private TextView submit;
    CardChoseLeftFragment cardChoseLeftFragment;
    CardChoseRightFragment cardChoseRightFragment;
    List<CouponInfoZ> couponInfoLeftZList;
    List<CouponInfoZ> couponInfoRightZList;
    List<IHmmCoupon> selectInfo;

    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    private boolean showCheck = false;
    private TextView submitMoney;

    public static CouponChoseDialog newInstance() {
        Bundle args = new Bundle();
        CouponChoseDialog fragment = new CouponChoseDialog();
        fragment.setArguments(args);
        return fragment;
    }

    CouponOkButtonClickenlistener couponOkButtonClickenlistener;

    public void setSelectList(List<IHmmCoupon> selectInfo) {
        this.selectInfo = selectInfo;
    }

    private void initView() {
    }

    public interface CouponOkButtonClickenlistener {
        void getResult(List<IHmmCoupon> coupons);
    }

    public void setCouponOkButtonClickenlistener(CouponOkButtonClickenlistener couponOkButtonClickenlistener) {
        this.couponOkButtonClickenlistener = couponOkButtonClickenlistener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dis_couponchose_dialog, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }


    private void displayDialog(View view) {
        submitMoney = (TextView) view.findViewById(R.id.submitMoney);
        tabLL = (RelativeLayout) view.findViewById(R.id.tabLL);
        st = (SlidingTabLayout) view.findViewById(R.id.st);
        imgBack = (ImageView) view.findViewById(R.id.img_back);
        vp = (ViewPager) view.findViewById(R.id.pager);
        submitLL = (LinearLayout) view.findViewById(R.id.submitLL);
        submit = (TextView) view.findViewById(R.id.submit);
        List<String> titles = null;

        cardChoseLeftFragment = CardChoseLeftFragment.newInstance(null);
        cardChoseLeftFragment.setList(couponInfoLeftZList);
        cardChoseLeftFragment.setSelectList(selectInfo);
        cardChoseLeftFragment.setOnCouponCheckChangeListener(new OnCouponCheckChangeListener() {

            @Override
            public void onCheckChange() {
                submitMoney.setVisibility(View.GONE);
                List<IHmmCoupon> tmp = cardChoseLeftFragment.getResult();
                checkOrderDis(tmp);
            }
        });
        fragments.add(cardChoseLeftFragment);
        if(couponInfoRightZList!=null){
            titles = Arrays.asList("可用优惠券("+(couponInfoLeftZList==null?0:couponInfoLeftZList.size())+")", "不可用优惠券("+(couponInfoRightZList==null?0:couponInfoRightZList.size())+")");
            cardChoseRightFragment = CardChoseRightFragment.newInstance(null);
            cardChoseRightFragment.setList(couponInfoRightZList);
            fragments.add(cardChoseRightFragment);
        }else {
            titles = Arrays.asList("可用优惠券("+(couponInfoLeftZList==null?0:couponInfoLeftZList.size())+")");
        }

        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getChildFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        vp.setOffscreenPageLimit(fragments.size());
        // bind to SmartTabLayout
        st.setViewPager(vp);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (couponOkButtonClickenlistener != null) {
                    couponOkButtonClickenlistener.getResult(cardChoseLeftFragment.getResult());
                }
                dismiss();
            }
        });
        submitLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (couponOkButtonClickenlistener != null) {
                    couponOkButtonClickenlistener.getResult(cardChoseLeftFragment.getResult());
                }
                dismiss();
            }
        });
        checkOrderDis(selectInfo);
    }

    private void checkOrderDis(List<IHmmCoupon> tmp) {
        double allmoney = 0;
        for (int i = 0; i < tmp.size(); i++) {
            allmoney += Double.parseDouble(tmp.get(i).getCouponDenomination());
        }
        if(allmoney>0){
            submitMoney.setVisibility(View.VISIBLE);
            submitMoney.setText("（共优惠 ¥"+ FormatUtils.moneyKeep2Decimals(allmoney) +"）");
        }
    }

    public interface OnCouponCheckChangeListener {
        void onCheckChange();
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

    }

    public void setList(List<CouponInfoZ> couponInfoLeftZList, List<CouponInfoZ> couponInfoRightZList) {
        this.couponInfoLeftZList = couponInfoLeftZList;
        this.couponInfoRightZList = couponInfoRightZList;
    }
}
