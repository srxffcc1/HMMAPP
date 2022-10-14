package com.healthy.library.widget;

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
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.message.UpdateCheckAllBasket;

import org.greenrobot.eventbus.EventBus;
@Deprecated
public class DisBasketDialog extends BaseDialogFragment {


    private TextView tvChooseTimeTitle;
    private ImageView closeBtn;
    private LinearLayout sumTop;
    private LinearLayout sumBottom;
    private LinearLayout basketBottom;
    private ConstraintLayout basketOrderBlock;
    private ConstraintLayout basketBottomLLLeft;
    private AutoFitCheckBox allCheck;
    private TextView allCheckRight;
    private TextView orderTranMoney;
    private TextView orderMoneyLeft;
    private TextView orderMoney;
    private ImageTextView discountMoney;
    private Group orderGroupChild;
    private Group orderGroupChildUnder;
    private TextView checkOrder;
    private TextView checkDelete;
    private Group delectGroup;
    private Group orderGroup;
    boolean allCheckCheck;
    String orderMoneyString;
    String checkOrderString;
    String discountMoneyString;

    public void setData(boolean allCheckCheck,String orderMoneyString,String checkOrderString,String discountMoneyString) {
        this.allCheckCheck = allCheckCheck;
        this.orderMoneyString=orderMoneyString;
        this.checkOrderString=checkOrderString;
        this.discountMoneyString=discountMoneyString;
    }

    public static DisBasketDialog newInstance() {

        Bundle args = new Bundle();
        DisBasketDialog fragment = new DisBasketDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dis_basket_dialog, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }


    private void displayDialog(View view) {
        tvChooseTimeTitle = (TextView) view.findViewById(R.id.tv_choose_time_title);
        closeBtn = (ImageView) view.findViewById(R.id.closeBtn);
        sumTop = (LinearLayout) view.findViewById(R.id.sumTop);
        sumBottom = (LinearLayout) view.findViewById(R.id.sumBottom);
        basketBottom = (LinearLayout) view.findViewById(R.id.basketBottom);
        basketOrderBlock = (ConstraintLayout) view.findViewById(R.id.basketOrderBlock);
        basketBottomLLLeft = (ConstraintLayout) view.findViewById(R.id.basketBottomLLLeft);
        allCheck = (AutoFitCheckBox) view.findViewById(R.id.allCheck);
        allCheckRight = (TextView) view.findViewById(R.id.allCheckRight);
        orderTranMoney = (TextView) view.findViewById(R.id.orderTranMoney);
        orderMoneyLeft = (TextView)view. findViewById(R.id.orderMoneyLeft);
        orderMoney = (TextView) view.findViewById(R.id.orderMoney);
        discountMoney = (ImageTextView)view. findViewById(R.id.discountMoney);
        orderGroupChild = (Group) view.findViewById(R.id.orderGroupChild);
        orderGroupChildUnder = (Group) view.findViewById(R.id.orderGroupChildUnder);
        checkOrder = (TextView) view.findViewById(R.id.checkOrder);
        checkDelete = (TextView) view.findViewById(R.id.checkDelete);
        delectGroup = (Group) view.findViewById(R.id.delectGroup);
        orderGroup = (Group) view.findViewById(R.id.orderGroup);
        orderMoney.setText(orderMoneyString);
        basketBottomLLLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        allCheck.setChecked(allCheckCheck);
        checkOrder.setText(checkOrderString);
        buildNowGoods();
        allCheck.setOnCheckedChangeListener(new AutoFitCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AutoFitCheckBox buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                buildNowCheckGoods();
                EventBus.getDefault().post(new UpdateCheckAllBasket(isChecked));
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void buildNowGoods() {
        checkOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_order_g);
        checkOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_order);
        orderMoney.setText(orderMoneyString);
        checkOrder.setText(checkOrderString);
        discountMoney.setText(discountMoneyString);
    }
    private void buildNowCheckGoods() {
        checkOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_order_g);
        checkOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_order);
        orderMoney.setText(orderMoneyString);
        checkOrder.setText(checkOrderString);
        discountMoney.setText(discountMoneyString);
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
}
