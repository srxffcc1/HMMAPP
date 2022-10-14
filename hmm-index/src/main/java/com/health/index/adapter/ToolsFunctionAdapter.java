package com.health.index.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.healthy.library.model.ToolsMenu;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToolsFunctionAdapter extends BaseAdapter<ToolsMenu> {
    public void setTmprecyclerView(RecyclerView tmprecyclerView) {
        this.tmprecyclerView = tmprecyclerView;
    }

    RecyclerView tmprecyclerView;

//    BaseAdapter.OnOutClickListener moutClickListener;

//    public void setOutClickListener(BaseAdapter.OnOutClickListener onOutClickListener) {
//        this.moutClickListener = onOutClickListener;
//    }

    @Override
    public int getItemViewType(int position) {
        return 2;
    }

    protected void convert(BaseViewHolder helper, ToolsMenu item) {

    }

    public ToolsFunctionAdapter() {
        this(R.layout.index_tools_function);
    }


    private ToolsFunctionAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(5);
        gridLayoutHelper.setAutoExpand(false);
        return gridLayoutHelper;
    }


    @Override
    public void onBindViewHolder(@NonNull final BaseHolder baseHolder, int i) {
        final ToolsMenu indexMenu= getDatas().get(i);
        final View parent_category=baseHolder.getView(R.id.parent_category);
        final View hotTip=baseHolder.getView(R.id.hotTip);
        parent_category.setLayoutParams(new RecyclerView.LayoutParams(((int)ScreenUtils.getScreenWidth(context)/5),RecyclerView.LayoutParams.WRAP_CONTENT));
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(indexMenu.getImageRes())
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)

                .into((ImageView) baseHolder.getView(R.id.iv_category));

//        if(indexMenu.name.equals("专家答疑")){
//
//        }
        hotTip.setVisibility(View.GONE);
        if(indexMenu.name.contains("百科")){
            hotTip.setVisibility(View.VISIBLE);
        }

        baseHolder.setText(R.id.tv_category,indexMenu.name);
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                saveFunction(indexMenu);
                moutClickListener.outClick(indexMenu.name,indexMenu);
            }
        });
    }
    public void saveFunction(ToolsMenu item){
        List<ToolsMenu> tmp=resolveHistoryData(SpUtils.getValue(context, SpKey.TOOL_TMP));
        if(!checkIsInSave(tmp,item)){
            tmp.add(item);
        }
        SpUtils.store(context,SpKey.TOOL_TMP,new Gson().toJson(tmp));
    }

    private boolean checkIsInSave(List<ToolsMenu> tmp, ToolsMenu item) {
        for (int i = 0; i <tmp.size() ; i++) {
            if(tmp.get(i).name.equals(item.name)){
                return true;
            }
        }
        return false;
    }

    private List<ToolsMenu> resolveHistoryData(String obj) {
        List<ToolsMenu> result = new ArrayList<>();
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
            Type type = new TypeToken<List<ToolsMenu>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
