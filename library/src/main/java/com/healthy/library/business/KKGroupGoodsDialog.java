package com.healthy.library.business;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.model.AssemableTeam;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpanUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KKGroupGoodsDialog extends BaseDialogFragment {

    public KKGroupGoodsDialog setAssemableTeam(AssemableTeam assemableTeam) {
        this.assemableTeam = assemableTeam;
        this.teamMembers = assemableTeam.teamMemberList;
        return this;
    }
    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AssemableTeam assemableTeam;
    public List<AssemableTeam.TeamMember> teamMembers;
    private LinearLayout kkLl;
    private TextView pinWhoGroup;
    private TextView pinGroupNumber;
    private LinearLayout manList1;
    private LinearLayout manList2;
    private TextView groupButton;
    private ImageView closeMessageDialog;

    private View.OnClickListener groupGoodsDialogClicker;
    CountDownTimer countDownTimer;

    public KKGroupGoodsDialog setGroupGoodsDialogClicker(View.OnClickListener groupGoodsDialogClicker) {
        this.groupGoodsDialogClicker = groupGoodsDialogClicker;
        return this;
    }


    public KKGroupGoodsDialog setTeamMembers(List<AssemableTeam.TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
        return this;
    }

    public static KKGroupGoodsDialog newInstance() {

        Bundle args = new Bundle();
        KKGroupGoodsDialog fragment = new KKGroupGoodsDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.mall_dialog_group_goods_detail, null);
        builder.setView(view);
        displayDialog(view);
        return builder.create();
    }

    private void displayDialog(View view) {
        kkLl = (LinearLayout) view.findViewById(R.id.kk_ll);
        pinWhoGroup = (TextView) view.findViewById(R.id.pinWhoGroup);
        pinGroupNumber = (TextView) view.findViewById(R.id.pinGroupNumber);
        manList1 = (LinearLayout)view. findViewById(R.id.manList1);
        manList2 = (LinearLayout) view.findViewById(R.id.manList2);
        groupButton = (TextView)view. findViewById(R.id.groupButton);
        closeMessageDialog = (ImageView) view.findViewById(R.id.closeMessageDialog);
        closeMessageDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        pinWhoGroup.setText("参与“"+assemableTeam.memberNickName+"”的拼团");

        checkTimeOut(assemableTeam,pinGroupNumber,true);
        buildManList(manList1,manList2,assemableTeam.teamMemberList,true);
        groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupGoodsDialogClicker!=null){
                    groupGoodsDialogClicker.onClick(v);
                    dismiss();
                }
            }
        });
    }

    private void checkTimeOut(AssemableTeam url, final TextView destview, boolean needtimer) {
        if(countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
        if(needtimer){
            Date startTime= null;
            Date endTime= null;
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.regimentTime);
                endTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.realEndTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long nd = 1000 * url.regimentTimeLength * 60 * 60;//加入时间之后需要多少小时
            long desorg = startTime.getTime()+nd;
            long timer = startTime.getTime()+nd;
            if(endTime!=null&&endTime.getTime()<=timer){
                timer=endTime.getTime();
                desorg=endTime.getTime();
            }
            timer=timer-System.currentTimeMillis();

            if (timer >0) {
                //System.out.println("开始计时");
                final long finalTimer = timer;
                final long finalDesorg = desorg;
                countDownTimer = new CountDownTimer(finalTimer, 1000) {
                    public void onTick(long millisUntilFinished) {
                        String array= DateUtils.getDistanceTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg +""));
//                        destview.setText("仅剩"+assemableTeam.remainderNum+"个名额，"+array+"后结束");
                        destview.setText(SpanUtils.getBuilder(getContext(),"仅剩").setForegroundColor(Color.parseColor("#666666"))
                                .append(assemableTeam.remainderNum+"").setForegroundColor(Color.parseColor("#FC2347"))
                                .append("个名额，"+array+"后结束").setForegroundColor(Color.parseColor("#666666"))
                                .create());
                    }
                    public void onFinish() {
                        destview.setText("过期");
                    }
                }.start();
            } else {
                destview.setText("过期");
            }
        }

    }

    private void buildManList(LinearLayout manList1, LinearLayout manList2, List<AssemableTeam.TeamMember> teamMemberList, boolean b) {
        manList1.removeAllViews();
        manList2.removeAllViews();
        if(b){
            if(teamMemberList!=null&&teamMemberList.size()>0){
                List<AssemableTeam.TeamMember> desList=new ArrayList<>();
                for (int i = 0; i <assemableTeam.regimentSize ; i++) {
                    if(i>teamMemberList.size()-1){
                        desList.add(null);
                    }else {
                        desList.add(teamMemberList.get(i));
                    }
                }//重新填充一个数组
                if(assemableTeam.regimentSize==2){
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                }
                if(assemableTeam.regimentSize==3){
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                }
                if(assemableTeam.regimentSize==4){
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                }
                if(assemableTeam.regimentSize==5){
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                    manList1.addView(buildManListChild(desList.get(4)));
                }
                if(assemableTeam.regimentSize==6){
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                    manList2.addView(buildManListChild(desList.get(4)));
                    manList2.addView(buildManListChild(desList.get(5)));
                }
                if(assemableTeam.regimentSize==7){
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                    manList2.addView(buildManListChild(desList.get(4)));
                    manList2.addView(buildManListChild(desList.get(5)));
                    manList2.addView(buildManListChild(desList.get(6)));
                }
                if(assemableTeam.regimentSize==8){
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                    manList1.addView(buildManListChild(desList.get(4)));

                    manList2.addView(buildManListChild(desList.get(5)));
                    manList2.addView(buildManListChild(desList.get(6)));
                    manList2.addView(buildManListChild(desList.get(7)));

                }
                if(assemableTeam.regimentSize==9){
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                    manList1.addView(buildManListChild(desList.get(4)));

                    manList2.addView(buildManListChild(desList.get(5)));
                    manList2.addView(buildManListChild(desList.get(6)));
                    manList2.addView(buildManListChild(desList.get(7)));
                    manList2.addView(buildManListChild(desList.get(8)));

                }


            }
        }
    }

    public View buildManListChild(AssemableTeam.TeamMember teamMember){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.mall_item_group_detail_icon, null);
        ImageView groupIconMaster=view.findViewById(R.id.groupIconMaster);
        TextView groupTipMaster=view.findViewById(R.id.groupTipMaster);
        groupTipMaster.setVisibility(View.INVISIBLE);
        if(teamMember==null){
            com.healthy.library.businessutil.GlideCopy.with(getContext())
                    .load(R.drawable.img_avatar_default_q)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default_q)
                    
                    .into(groupIconMaster);
        }else {
            com.healthy.library.businessutil.GlideCopy.with(getContext())
                    .load(teamMember.memberFaceUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default_q)
                    
                    .into(groupIconMaster);
            if(teamMember.commanderStatus==1){
                groupTipMaster.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    private void initView() {


    }
}
