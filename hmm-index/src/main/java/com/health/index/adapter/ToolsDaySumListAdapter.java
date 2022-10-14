package com.health.index.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.healthy.library.model.ToolsSumType;
import com.healthy.library.constant.Functions;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.DateUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ToolsDaySumListAdapter extends BaseQuickAdapter<ToolsSumType, BaseViewHolder> {

    OnRecordLongClickListener onRecordLongClickListener;

    public void setOnRecordLongClickListener(OnRecordLongClickListener onRecordLongClickListener) {
        this.onRecordLongClickListener = onRecordLongClickListener;
    }

    private String childId;
    public void setChildId(String childId) {
        this.childId=childId;
    }

    public boolean checkIsLast(int postion, String date){
        List<ToolsSumType> seachs=getData();
        if(seachs.size()>postion+1){
            for (int i = postion+1; i <seachs.size() ; i++) {
                ToolsSumType item=seachs.get(i);
                String seachday=DateUtils.getDateDay(item.getTimeleft(),"yyyy-MM-dd HH:mm:ss");
                if(seachday.equals(date)){
                    return false;
                }
            }
        }else {
            return true;
        }
        return true;
    }
    public boolean checkIsFirst(int postion,String date){
        List<ToolsSumType> seachs=getData();
        if(postion!=0){
            for (int i = 0; i <postion ; i++) {
                ToolsSumType item=seachs.get(i);
                String seachday=DateUtils.getDateDay(item.getTimeleft(),"yyyy-MM-dd HH:mm:ss");
                if(seachday.equals(date)){
                    return false;
                }
            }
        }else {
            return true;
        }
        return true;
    }

    public ToolsDaySumListAdapter() {
        this(R.layout.index_activity_tools_diary_sum_item);
    }

    public ToolsDaySumListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ToolsSumType item) {
         LinearLayout toolsMenuIconLL;
         LinearLayout toolsMenuIconLLTmp;
         ImageView toolsMenuIcon;
         TextView toolsMenuTip;
         TextView toolsMenuTime;
        TextView toolsMenuTopTime;
        toolsMenuTopTime = (TextView) helper.itemView.findViewById(R.id.tools_menu_top_time);
        toolsMenuIconLL = (LinearLayout) helper.itemView.findViewById(R.id.tools_menu_iconLL);
        toolsMenuIconLLTmp = (LinearLayout)helper.itemView. findViewById(R.id.tools_menu_iconLLTmp);
        toolsMenuIcon = (ImageView) helper.itemView.findViewById(R.id.tools_menu_icon);
        toolsMenuTip = (TextView) helper.itemView.findViewById(R.id.tools_menu_tip);
        toolsMenuTime = (TextView) helper.itemView.findViewById(R.id.tools_menu_time);

        toolsMenuIcon.setImageResource(item.getImageLeftRes());
        toolsMenuTip.setText(item.getLeftTitle());
        toolsMenuTime.setText(DateUtils.getClassDateTools(DateUtils.formatLongSecAll(item.getTimeleft(),"yyyy-MM-dd HH:mm:ss")));

        toolsMenuIconLLTmp.setVisibility(View.VISIBLE);
        toolsMenuTopTime.setText(DateUtils.getClassDateNoHour(DateUtils.formatLongSecAll(item.getTimeleft(), "yyyy-MM-dd HH:mm:ss")));
        if(checkIsLast(helper.getPosition(),DateUtils.getDateDay(item.getTimeleft(),"yyyy-MM-dd HH:mm:ss"))){
            toolsMenuIconLLTmp.setVisibility(View.GONE);
        }
        if(!checkIsFirst(helper.getPosition(),DateUtils.getDateDay(item.getTimeleft(),"yyyy-MM-dd HH:mm:ss"))){
            toolsMenuTopTime.setText("");
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inittime="";
                try {
                    inittime=new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.getTimeleft()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(item.recordType<5){
                    ARouter.getInstance()
                            .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_FEED)
                            .withString("childId",childId)
                            .withInt("postion",item.recordType-1)
                            .withString("recordId",item.recordId)
                            .withString("init",inittime)
                            .navigation();
                }else {
                    ARouter.getInstance()
                            .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_OUT)
                            .withString("childId",childId)
                            .withInt("postion",item.recordType-5)
                            .withString("recordId",item.recordId)
                            .withString("init",inittime)
                            .navigation();
                }

            }
        });
        helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                StyledDialog.init(mContext);
                StyledDialog.buildIosAlert("", "是否删除此记录?", new MyDialogListener() {
                    @Override
                    public void onFirst() {

                    }

                    @Override
                    public void onThird() {
                        super.onThird();
                    }

                    @Override
                    public void onSecond() {
                        Map<String,Object> clearmap=new HashMap<>();
                        clearmap.put("recordId",item.recordId+"");
                        clearmap.put(Functions.FUNCTION, "9030");
                        if(onRecordLongClickListener!=null){
                            onRecordLongClickListener.onRecordLongClick(clearmap);
                        }
                    }
                }).setCancelable(true,true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark,0).setBtnText("取消", "确定").show();


                return true;
            }
        });

    }
    public interface OnRecordLongClickListener{
        void onRecordLongClick(Map<String,Object> clearmap);
        }


}
