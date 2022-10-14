package com.health.index.adapter;


import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.healthy.library.model.ToolsVacc;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.SpUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author Li
 * @date 2019/03/12 16:14
 * @des 评论列表
 */

public class ToolVaccAdapter extends BaseQuickAdapter<ToolsVacc, BaseViewHolder> {

    public int id;

    public int status=0;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    OnCheckClickListener onCheckClickListener;

    public void setOnCheckClickListener(OnCheckClickListener onCheckClickListener) {
        this.onCheckClickListener = onCheckClickListener;
    }

    public ToolVaccAdapter() {
        this(R.layout.index_activity_tools_item_vacc_p);
    }


    ToolVaccAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ToolsVacc item) {
        TextView vaccDate;
        LinearLayout vaccS;
        TextView vacTip;
        vaccDate = (TextView) helper.itemView.findViewById(R.id.vaccDate);
        vaccS = (LinearLayout) helper.itemView.findViewById(R.id.vaccS);
        vacTip=(TextView) helper.itemView.findViewById(R.id.vacTip);
        vacTip.setVisibility(View.GONE);
        vaccDate.setText(item.dateAndAge.replace("null",""));
        vaccDate.setTextColor(Color.parseColor("#ff444444"));
        if(TextUtils.isEmpty(item.dateAndAge)){
            vaccDate.setText("");
        }
        if(id==item.vaccineAgeId){
            vaccDate.setTextColor(Color.parseColor("#ffff6266"));
            vacTip.setVisibility(View.VISIBLE);
        }
        if(status<3){
            try {
                vaccDate.setVisibility(View.GONE);
                vacTip.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        buildVaccList(vaccS, item.vaccineTimeResultList,item);
        try {
            if(new Date().after(new SimpleDateFormat("yyyy-MM-dd").parse(item.dateAndAge.split(" ")[0]))){
                vaccDate.setTextColor(Color.parseColor("#ff999999"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void buildVaccList(LinearLayout vaccS, List<ToolsVacc.VaccineTimeResult> vaccineTimeResultList, ToolsVacc item) {
        vaccS.removeAllViews();
        if (vaccineTimeResultList != null) {
            for (int i = 0; i < vaccineTimeResultList.size(); i++) {
                final ToolsVacc.VaccineTimeResult vaccineTimeResult=vaccineTimeResultList.get(i);
                 View view = LayoutInflater.from(mContext).inflate(R.layout.index_activity_tools_item_vacc, vaccS, false);
                 ConstraintLayout hasContent;
                 TextView vacName;
                 TextView mustTip;
                 TextView dateTip;
                 TextView vacCount;
                 TextView vacTip;
                 final ImageView vacCheck;
                 TextView lastLine;

                hasContent = (ConstraintLayout) view.findViewById(R.id.hasContent);
                vacName = (TextView) view.findViewById(R.id.vacName);
                mustTip = (TextView) view.findViewById(R.id.mustTip);
                dateTip = (TextView) view.findViewById(R.id.dateTip);
                vacCount = (TextView) view.findViewById(R.id.vacCount);
                vacTip = (TextView) view.findViewById(R.id.vacTip);
                vacCheck = (ImageView) view.findViewById(R.id.vacCheck);
                lastLine = (TextView) view.findViewById(R.id.lastLine);
                mustTip.setVisibility(View.GONE);
                dateTip.setVisibility(View.GONE);

                mustTip.setBackgroundResource(R.drawable.shape_tools_vacc_rr);
                dateTip.setBackgroundResource(R.drawable.shape_tools_vacc_rr);

                vacName.setText(vaccineTimeResult.vaccineName);
                if(TextUtils.isEmpty(vaccineTimeResult.vaccineName)){

                    vacName.setText("");
                }
                vacCount.setText(vaccineTimeResult.times);
                vacTip.setText(vaccineTimeResult.reason);
                if(vaccineTimeResult.mustStatus==1){
                    mustTip.setVisibility(View.VISIBLE);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_VACCINE_DETAIL);
                        String url = String.format("%s?id=%s&date=%s&type=%s", urlPrefix,vaccineTimeResult.id+"",vaccineTimeResult.vaccinationTime+"",status<3?"0":"1");
                        ARouter.getInstance()
                                .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                                .withString("title", "疫苗详情")
                                .withString("url", url)
                                .withBoolean("isinhome",true)
                                .withBoolean("doctorshop",true)
                                .navigation();
                    }
                });

                vacCheck.setImageResource(vaccineTimeResult.prenatalOverStatus==0?R.drawable.tools_vac_radio_normal:R.drawable.tools_vac_radio_select);
                vacCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onCheckClickListener!=null&&status>=3){
                            onCheckClickListener.onCheck(vaccineTimeResult.prenatalOverStatus,vaccineTimeResult);
                            vaccineTimeResult.prenatalOverStatus=vaccineTimeResult.prenatalOverStatus==0?1:0;
                            vacCheck.setImageResource(vaccineTimeResult.prenatalOverStatus==0?R.drawable.tools_vac_radio_normal:R.drawable.tools_vac_radio_select);
//                            StyledDialog.init(mContext);
//                            StyledDialog.buildIosAlert("", "是否更改选中状态?", new MyDialogListener() {
//                                @Override
//                                public void onFirst() {
//
//                                }
//
//                                @Override
//                                public void onThird() {
//                                    super.onThird();
//                                }
//
//                                @Override
//                                public void onSecond() {
//
//
//                                }
//                            }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark,0).setBtnText("取消", "确定").show();



                        }

                    }
                });
                vacName.setTextColor(Color.parseColor("#434343"));
                vacCount.setTextColor(Color.parseColor("#656565"));
                try {
                    if(new Date().after(new SimpleDateFormat("yyyy-MM-dd").parse(vaccineTimeResult.vaccinationTime))){
                        dateTip.setVisibility(View.VISIBLE);
                        if(vaccineTimeResult.prenatalOverStatus==1){
                            dateTip.setVisibility(View.GONE);
                        }
                        vacName.setTextColor(Color.parseColor("#999999"));
                        vacCount.setTextColor(Color.parseColor("#999999"));
//                        mustTip.setBackgroundResource(R.drawable.shape_tools_vacc_gr);
                        dateTip.setBackgroundResource(R.drawable.shape_tools_vacc_gr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if(id==item.vaccineAgeId){
                    vacName.setTextColor(Color.parseColor("#434343"));
                    vacCount.setTextColor(Color.parseColor("#656565"));
                    dateTip.setVisibility(View.GONE);
                }


                if(status<3){
                    try {
//                        mustTip.setBackgroundResource(R.drawable.shape_tools_vacc_rr);
                        dateTip.setBackgroundResource(R.drawable.shape_tools_vacc_rr);
                        vacName.setTextColor(Color.parseColor("#434343"));
                        vacCount.setTextColor(Color.parseColor("#656565"));
                        dateTip.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (i == vaccineTimeResultList.size() - 1) {
                    lastLine.setVisibility(View.INVISIBLE);
                }
                vaccS.addView(view);
            }
        }

    }
    public interface OnCheckClickListener{
         void onCheck(int flag, ToolsVacc.VaccineTimeResult vaccineTimeResult);
    }
}