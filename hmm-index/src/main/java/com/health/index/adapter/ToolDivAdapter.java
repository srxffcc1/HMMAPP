package com.health.index.adapter;


import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.healthy.library.model.ToolsDiv;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.ImageTextView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author Li
 * @date 2019/03/12 16:14
 * @des 评论列表
 */

public class ToolDivAdapter extends BaseQuickAdapter<ToolsDiv, BaseViewHolder> {

    OnCheckClickListener onCheckClickListener;

    public int id;

    public int status=0;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOnCheckClickListener(OnCheckClickListener onCheckClickListener) {
        this.onCheckClickListener = onCheckClickListener;
    }

    public ToolDivAdapter() {
        this(R.layout.index_activity_tools_item_divcheck);
    }


    ToolDivAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ToolsDiv item) {
        TextView vacTitle;
        TextView vacDiv;
        TextView vacContent;
        ImageTextView vacTime;
        ImageView more;        ImageView more2;

        TextView dateTip;

         TextView vacTip;
         final ImageView vacCheck;
        vacTitle = (TextView) helper.itemView.findViewById(R.id.vacTitle);
        vacDiv = (TextView) helper.itemView.findViewById(R.id.vacDiv);
        vacContent = (TextView) helper.itemView.findViewById(R.id.vacContent);
        vacTime = (ImageTextView) helper.itemView.findViewById(R.id.vacTime);
        more = (ImageView) helper.itemView.findViewById(R.id.more);
        more2 = (ImageView) helper.itemView.findViewById(R.id.more2);
        dateTip=helper.itemView.findViewById(R.id.dateTip);
        vacTip = (TextView) helper.itemView.findViewById(R.id.vacTip);
        vacCheck = (ImageView) helper.itemView.findViewById(R.id.vacCheck);

        vacTitle.setText(item.count);
        vacContent.setText(item.keyInspectionItems);
        vacTime.setText(item.day);
        vacTip.setVisibility(View.GONE);
        vacTip.setBackgroundResource(R.drawable.shape_tools_vacc_rr);
        more2.setVisibility(View.GONE);
        dateTip.setVisibility(View.GONE);

        vacTitle.setTextColor(Color.parseColor("#ff444444"));

        try {
            if(new Date().after(new SimpleDateFormat("yyyy-MM-dd").parse(item.day.split(" ")[0]))){
                dateTip.setVisibility(View.VISIBLE);
                if(item.prenatalOverStatus==1){
                    dateTip.setVisibility(View.GONE);
                }
                dateTip.setBackgroundResource(R.drawable.shape_tools_vacc_gr);
                vacTitle.setTextColor(Color.parseColor("#ff999999"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(item.id==id){
            vacTip.setVisibility(View.VISIBLE);
            vacTip.setText("本次");
            vacTitle.setTextColor(Color.parseColor("#ffff6266"));

        }


        if(status!=2){
            vacTip.setBackgroundResource(R.drawable.shape_tools_vacc_rr);
            vacTitle.setTextColor(Color.parseColor("#ff444444"));
            vacTime.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
            more2.setVisibility(View.VISIBLE);
            vacTip.setVisibility(View.GONE);

        }
        vacCheck.setImageResource(item.prenatalOverStatus==0?R.drawable.tools_vac_radio_normal:R.drawable.tools_vac_radio_select);
        vacCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCheckClickListener!=null){
                    onCheckClickListener.onCheck(item.prenatalOverStatus,item);
                    item.prenatalOverStatus=item.prenatalOverStatus==0?1:0;
                    vacCheck.setImageResource(item.prenatalOverStatus==0?R.drawable.tools_vac_radio_normal:R.drawable.tools_vac_radio_select);
//                    StyledDialog.init(mContext);
//                    StyledDialog.buildIosAlert("", "是否更改选中状态?", new MyDialogListener() {
//                        @Override
//                        public void onFirst() {
//
//                        }
//
//                        @Override
//                        public void onThird() {
//                            super.onThird();
//                        }
//
//                        @Override
//                        public void onSecond() {
//
//
//
//                        }
//                    }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark,0).setBtnText("取消", "确定").show();




                }
            }
        });
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoExModel userInfoExModel=resolveTmpData(SpUtils.getValue(mContext, SpKey.USE_TOKEN));
                String weekidHas=item.careWeek.replace("孕","").replace("周产检","");
                String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_tableDetailUrl);
                String url = String.format("%s?prenatalId=%s&type=%s&id=%s", urlPrefix,item.id+"",status!=2?"0":"1",userInfoExModel.id+"");



                String urlPrefixs = SpUtils.getValue(mContext, UrlKeys.H5_babyNameUrl);
                String surl = String.format("%s?type=2", urlPrefixs);
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                        .withString("title", "产检详情")
                        .withString("otherMap",new SimpleHashMapBuilder<String,String>().puts("mWeekId",weekidHas).text())
                        .withString("url", url)
                        .withString("stitle",item.careWeek)
                        .withString("otherFun","Pass")
                        .withString("otherBitmap","R.drawable.index_ic_b")
                        .withString("otherFunString","/index/analyzeB")
                        .withBoolean("isinhome",true)
                        .withBoolean("doctorshop",true)
                        .navigation();
            }
        });




    }
    private UserInfoExModel resolveTmpData(String obj) {
        UserInfoExModel result = new UserInfoExModel();
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<UserInfoExModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private void initView() {


    }

    public interface OnCheckClickListener {
        void onCheck(int flag, ToolsDiv vaccineTimeResult);
    }
}