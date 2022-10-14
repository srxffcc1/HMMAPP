package com.health.index.adapter;

import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import com.healthy.library.model.ToolsMedType;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.ImageTextView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ToolsFeedTypeListAdapter extends BaseQuickAdapter<ToolsMedType, BaseViewHolder> {
    boolean needsaveNewType=false;
    String saveKey;
    public void setSaveFlag(boolean needsaveNewType,String saveKey){
        this.needsaveNewType=needsaveNewType;
        this.saveKey=saveKey;
    }
    String dialogtitle;

    public void setDialogtitle(String dialogtitle) {
        this.dialogtitle = dialogtitle;
    }

    Map<Integer, ImageTextView> integerImageTextViewMap = new HashMap<>();

    public void setSelectS(String selectS) {
        this.selectS = selectS;
    }

    public String getSelectS() {
        return selectS.replace(",","");
    }

    public String selectS = "";

    public void setChoseCount(int choseCount) {
        this.choseCount = choseCount;
    }

    public int choseCount = 1;//默认为单选  -1为不限制数量 其它为数量


    public void setOnIndexClickListener(OnIndexClickListener onIndexClickListener) {
        this.onIndexClickListener = onIndexClickListener;
    }

    OnIndexClickListener onIndexClickListener;

    public ToolsFeedTypeListAdapter() {
        this(R.layout.index_fragment_item_tools_diary_type);
    }

    public ToolsFeedTypeListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ToolsMedType item) {
        final ImageTextView typeTxt;
        typeTxt = (ImageTextView) helper.itemView.findViewById(R.id.typeTxt);
        integerImageTextViewMap.put(helper.getPosition(), typeTxt);
        typeTxt.setText(item.name);
        if (item.getNormalIcon() != -1) {

            typeTxt.setDrawable(item.getNormalIcon(), mContext);
        }
        typeTxt.setTextColor(item.normalTextColor);
        typeTxt.setBackgroundResource(item.normalBg);
        if (selectS.contains(item.name + ",")) {//说明是被选中项
            if (item.getSelectIcon() != -1) {
                typeTxt.setDrawable(item.getSelectIcon(), mContext);
            }
            typeTxt.setTextColor(item.selectTextColor);
            typeTxt.setBackgroundResource(item.selectBg);
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectS.contains(item.name + ",")) {//说明点击的这个是当前选中的 那就把当前的项置为
                    selectS = selectS.replace(item.name + ",", "");
                    //System.out.println("当前选中");
                    if (item.getNormalIcon() != -1) {

                        typeTxt.setDrawable(item.getNormalIcon(), mContext);
                    }
                    typeTxt.setTextColor(item.normalTextColor);
                    typeTxt.setBackgroundResource(item.normalBg);
                } else {
                    if ("自定义".equals(item.name)) {
                        if(choseCount == -1){
                            buildCustom(true);
                        }else {
                            buildCustom(false);
                        }
                    } else {

                        if (choseCount == -1) {//说明是多 单选模式就是互相替换选择项
                            selectS = selectS + item.name + ",";
                            if (item.getSelectIcon() != -1) {
                                typeTxt.setDrawable(item.getSelectIcon(), mContext);
                            }
                            typeTxt.setTextColor(item.selectTextColor);
                            typeTxt.setBackgroundResource(item.selectBg);
                        } else if (choseCount == 1) {//说明是单选 单选模式就是互相替换选择项
                            selectS = item.name + ",";
                            refreshSelect();
                        } else {
                            if (selectS.split(",").length < choseCount && !"自定义".equals(item.name)) {//说明在可选范围内
                                selectS = selectS + item.name + ",";
                                refreshSelect();
                            } else {
                                Toast.makeText(mContext, "最多选择" + choseCount + "项", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                }
            }
        });
    }

    private void refreshSelect() {
        Iterator<Map.Entry<Integer, ImageTextView>> entries = integerImageTextViewMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, ImageTextView> entry = entries.next();
            int key = entry.getKey();
            ImageTextView typeTxt = entry.getValue();
            ToolsMedType item = getData().get(key);
            if (item.getNormalIcon() != -1) {
                typeTxt.setDrawable(item.getNormalIcon(), mContext);
            }
            typeTxt.setTextColor(item.normalTextColor);
            typeTxt.setBackgroundResource(item.normalBg);
            if (selectS.contains(item.name + ",")) {//说明是被选中项
                if (item.getSelectIcon() != -1) {
                    typeTxt.setDrawable(item.getSelectIcon(), mContext);
                }
                typeTxt.setTextColor(item.selectTextColor);
                typeTxt.setBackgroundResource(item.selectBg);
            }


        }
    }

    private void initView() {

    }

    /**
     *
     * @param flag 单选
     */
    public void buildCustom(final boolean flag) {
        StyledDialog.init(mContext);
        StyledDialog.buildMdInput(dialogtitle, "输入名称", "",
                "", "", new InputFilter[]{new InputFilter.LengthFilter(10)} , null, new MyDialogListener() {
                    @Override
                    public void onFirst() {

                    }

                    @Override
                    public void onSecond() {

                    }

                    @Override
                    public void onGetInput(CharSequence input1, CharSequence input2) {
                        super.onGetInput(input1, input2);
                    }

                    @Override
                    public boolean onInputValid(CharSequence input1, CharSequence input2, EditText editText1, EditText editText2) {
                        if(input1.toString().length()<1){
                            Toast.makeText(mContext,"名称不得小于1个字",Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        if (checkIsInList(input1.toString().trim() + "")) {
                            Toast.makeText(mContext, "已经存在", Toast.LENGTH_SHORT).show();
                        } else {
                            if(flag){
                                selectS = selectS + input1.toString().trim() + ",";
                            }else {
                                selectS =  input1.toString().trim() + ",";
                            }
                            if(needsaveNewType){//说明需要保存
                                if(!checkIsInSave(resolveHistoryData(SpUtils.getValue(mContext, saveKey)),new ToolsMedType(input1.toString().trim(), "R.drawable.aixin", "R.drawable.aixin_w"))){
                                    //System.out.println("需要保存");
                                    saveFunction(new ToolsMedType(input1.toString().trim(), "R.drawable.aixin", "R.drawable.aixin_w"),saveKey);
                                }
                            }
                            addData(getData().size()-1,new ToolsMedType(input1.toString().trim(), "R.drawable.aixin", "R.drawable.aixin_w"));
                            notifyDataSetChanged();
                        }

                        return true;
                    }
                })
                .setInput2HideAsPassword(true)
                .setCancelable(true, true)
                .show();
    }
    public void saveFunction(ToolsMedType item,String key){
        List<ToolsMedType> tmp=resolveHistoryData(SpUtils.getValue(mContext, key));
        if(!checkIsInSave(tmp,item)){
            tmp.add(item);
        }
        SpUtils.store(mContext,key,new Gson().toJson(tmp));
    }
    private boolean checkIsInSave(List<ToolsMedType> tmp, ToolsMedType item) {
        for (int i = 0; i <tmp.size() ; i++) {
            if(tmp.get(i).name.equals(item.name)){
                return true;
            }
        }
        return false;
    }
    private List<ToolsMedType> resolveHistoryData(String obj) {
        List<ToolsMedType> result = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ToolsMedType>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean checkIsInList(String org){
        for (int i = 0; i <getData().size() ; i++) {
            if(getData().get(i).name.contains(org)){
                return true;
            }
        }
        return false;
    }


    public interface OnIndexClickListener {
        void onIndexClick(String fun);
    }
}
