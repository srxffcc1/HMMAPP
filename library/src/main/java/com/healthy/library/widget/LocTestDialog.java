//package com.healthy.library.widget;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Space;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.FragmentManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.gson.Gson;
//import com.healthy.library.R;
//import com.healthy.library.base.BaseDialogFragment;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.contract.LocVipContract;
//import com.healthy.library.message.UpdateUserLocationMsg;
//import com.healthy.library.model.AMapLocationBd;
//import com.healthy.library.model.LocVip;
//import com.healthy.library.presenter.LocVipPresenter;
//import com.healthy.library.businessutil.LocUtil;
//import com.healthy.library.utils.SpUtils;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.disposables.Disposable;
//
//public class LocTestDialog extends BaseDialogFragment implements LocVipContract.View {
//    private ConstraintLayout dialogBg;
//    private RecyclerView contentRecycler;
//    private ImageView dialogCloseButton;
//    private ImageView passButton;
//    private ImageView iconMessageDialog;
//    private TextView titleMessageDialog;
//    private TextView contentMessageDialog;
//    private Space space;
//    private TextView buttonMessageDialog;
//    private MessageOkClickListener messageOkClickListener;
//
//    public int messageTopRes;
//    public String messageCenterText;
//    public String messageBottomText;
//    private LinearLayout contentMessageLL;
//    private LinearLayout contentMessagePassLL;
//    LocVipPresenter locVipPresenter;
//
//    public void setMessageTopRes(int messageTopRes, String messageCenterText, String messageBottomText) {
//        this.messageTopRes = messageTopRes;
//        this.messageCenterText = messageCenterText;
//        this.messageBottomText = messageBottomText;
//    }
//
//    public LocTestDialog setMessageOkClickListener(MessageOkClickListener messageOkClickListener) {
//        this.messageOkClickListener = messageOkClickListener;
//        return this;
//    }
//
//    @Override
//    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
//        try {
//            super.show(manager, tag);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static LocTestDialog newInstance() {
//
//        Bundle args = new Bundle();
//        LocTestDialog fragment = new LocTestDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        final View view = inflater.inflate(R.layout.dialog_loc_change, null);
//        builder.setView(view);
//        locVipPresenter=new LocVipPresenter(getActivity(),this);
//        displayDialog(view);
//        return builder.create();
//    }
//
//    private void displayDialog(View view) {
//        dialogBg = (ConstraintLayout) view.findViewById(R.id.dialog_bg);
//        iconMessageDialog = (ImageView) view.findViewById(R.id.iconMessageDialog);
//        titleMessageDialog = (TextView) view.findViewById(R.id.titleMessageDialog);
//        contentMessageDialog = (TextView) view.findViewById(R.id.contentMessageDialog);
//        space = (Space) view.findViewById(R.id.space);
//        buttonMessageDialog = (TextView) view.findViewById(R.id.buttonMessageDialog);
//        contentMessageLL = (LinearLayout) view.findViewById(R.id.contentMessageLL);
//        contentMessagePassLL = (LinearLayout) view.findViewById(R.id.contentMessagePassLL);
//        contentMessagePassLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (messageOkClickListener != null) {
////                    messageOkClickListener.onMessageOkClick(v);
////                } else {
////                    NotificationUtil.gotoSet(getActivity());
////                }
//                dismiss();
//            }
//        });
////        locVipPresenter.getLocVip(new SimpleHashMapBuilder<String, Object>());
//
//        onSucessGetLocVip(null);
//    }
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        //设置背景半透明
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().setCancelable(true);
//        getDialog().setCanceledOnTouchOutside(true);
//
//    }
//
//    private void initView() {
//
//    }
//
//    @Override
//    public void onSucessGetLocVip(LocVip locVip) {
//        titleMessageDialog.setText("自选定位");
//        contentMessageDialog.setText("选择以下虚拟定位开始体验");
//        localList.clear();
//        localList.add(new AMapLocationBd("青岛定位", "李沧区", "青岛市", "山东省", "370213", 36.175661, 120.380807));
//        localList.add(new AMapLocationBd("宁波定位", "市辖区", "宁波市", "浙江省", "330201", 29.8642, 121.543299));
//        localList.add(new AMapLocationBd("苏州定位", "虎丘区", "苏州市", "江苏省", "320505", 31.296324, 120.569557));
//
//
//        buildLocView();
//
//    }
//    List<AMapLocationBd> localList=new ArrayList<>();
//    private void buildLocView() {
//        for (int i = 0; i <localList.size() ; i++) {
//            View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_item_loc_change,contentMessageLL,false);
//            final AMapLocationBd local=localList.get(i);
//            TextView textView= (TextView) view;
//            textView.setText(local.address);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String result = null;
//                    try {
//                        result = new Gson().toJson(local);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    SpUtils.store(getActivity(), SpKey.LOC_TEST, result);
//                    LocUtil.storeLocation(getActivity(), local.build());
//                    LocUtil.storeLocationChose(getActivity(), local.build());
//                    EventBus.getDefault().post(new UpdateUserLocationMsg());
//                    dismiss();
//                }
//            });
//            contentMessageLL.addView(view);
//        }
//
//    }
//
//    @Override
//    public void showLoading() {
//
//    }
//
//    @Override
//    public void showToast(CharSequence msg) {
//
//    }
//
//    @Override
//    public void showNetErr() {
//
//    }
//
//    @Override
//    public void onRequestStart(Disposable disposable) {
//
//    }
//
//    @Override
//    public void showContent() {
//
//    }
//
//    @Override
//    public void showEmpty() {
//
//    }
//
//    @Override
//    public void onRequestFinish() {
//
//    }
//
//    @Override
//    public void getData() {
//
//    }
//
//    @Override
//    public void showDataErr() {
//
//    }
//
//    public interface MessageOkClickListener {
//        public void onMessageOkClick(View view);
//    }
//}
