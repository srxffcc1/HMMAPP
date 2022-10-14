package com.health.index.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.index.R;
import com.health.index.contract.IndexMonToolContract;
import com.health.index.model.IndexMonTool;
import com.health.index.presenter.IndexMonToolPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.health.index.BuildConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentTools extends BaseFragment implements View.OnClickListener, IndexMonToolContract.View {
    private android.widget.GridLayout layoutTools;
    private android.widget.GridLayout layoutWiki;
    private List<IndexMonTool> indexMonToolList=new ArrayList<>();
    private List<IndexMonTool> indexMonWikiList=new ArrayList<>();
    IndexMonToolPresenter indexMonToolPresenter;
    private android.widget.LinearLayout tollLL;
    private android.widget.LinearLayout wikiLL;

    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_tools;
    }

    @Override
    protected void findViews() {
        initView();
        indexMonToolPresenter=new IndexMonToolPresenter(mContext,this);
        indexMonToolPresenter.getToolList(getBasemap());
        buildTools();
    }
    public static FragmentTools newInstance(Map<String, Object> maporg) {
        FragmentTools fragment = new FragmentTools();
        Bundle args = new Bundle();
        for (Map.Entry<String, Object> entry : maporg.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) {
                args.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                args.putBoolean(key, (Boolean) value);
            } else if (value instanceof String) {
                args.putString(key, (String) value);
            } else {
                args.putSerializable(key, (Serializable) value);
            }
        }
        fragment.setArguments(args);
        return fragment;
    }
    public void putMap(String key,String value){
        getBasemap().put(key,value);
    }

    public void refresh(){
        //System.out.println("重新刷新");
        indexMonToolPresenter.getToolList(getBasemap());
        buildTools();
    }

    private void buildTools() {
        indexMonToolList.clear();
        if(BuildConfig.VERSION_CODE>=2205){

        tollLL.setVisibility(View.GONE);
            layoutTools.setVisibility(View.GONE);
        }else {

            tollLL.setVisibility(View.VISIBLE);
            layoutTools.setVisibility(View.VISIBLE);
        }
        if("1".equals(get("stage"))){
            indexMonToolList.add(new IndexMonTool("能不能吃"));
        }
        if("2".equals(get("stage"))){
            indexMonToolList.add(new IndexMonTool("产检表"));
            indexMonToolList.add(new IndexMonTool("B超解读"));
            indexMonToolList.add(new IndexMonTool("能不能吃"));
            indexMonToolList.add(new IndexMonTool("待产包"));
        }
        if("3".equals(get("stage"))){
            indexMonToolList.add(new IndexMonTool("月子餐"));
            indexMonToolList.add(new IndexMonTool("疫苗助手"));
            indexMonToolList.add(new IndexMonTool("能不能吃"));
        }
        if("4".equals(get("stage"))){
            indexMonToolList.add(new IndexMonTool("疫苗助手"));
            indexMonToolList.add(new IndexMonTool("能不能吃"));
            indexMonToolList.add(new IndexMonTool("宝宝辅食"));
        }
        if("5".equals(get("stage"))){
            indexMonToolList.add(new IndexMonTool("疫苗助手"));
            indexMonToolList.add(new IndexMonTool("宝宝辅食"));

        }
        if("6".equals(get("stage"))) {
            indexMonToolList.add(new IndexMonTool("疫苗助手"));
        }
        if("7".equals(get("stage"))){
            indexMonToolList.add(new IndexMonTool("能不能吃"));
        }
        addTools(layoutTools,indexMonToolList);
    }


    private void initView() {
        layoutTools = (GridLayout) findViewById(R.id.layout_tools);
        layoutWiki = (GridLayout) findViewById(R.id.layout_wiki);
        tollLL = (LinearLayout) findViewById(R.id.tollLL);
        wikiLL = (LinearLayout) findViewById(R.id.wikiLL);
    }

    @Override
    public void getToolListSuccess(List<IndexMonTool> indexMonToolList) {
        indexMonWikiList.clear();
        indexMonWikiList.addAll(indexMonToolList);
        addTools(layoutWiki,indexMonWikiList);
    }

    private void addTools(final GridLayout gridLayout, final List<IndexMonTool> urls) {
        final int margin = (int) TransformUtil.dp2px(mContext, 2);
        gridLayout.removeAllViews();
        if(urls!=null&&urls.size()>0){
            gridLayout.post(new Runnable() {
                @Override
                public void run() {
                    int row = 2;
                    int w = (gridLayout.getWidth()-gridLayout.getPaddingLeft()-gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * margin) / row;
                    gridLayout.removeAllViews();
                    for (int i = 0; i < urls.size(); i++) {
                        final int pos = i;
                        final IndexMonTool monTool=urls.get(pos);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = w;
                        params.height = (int) (w * 0.45f);
                        params.setMargins(margin, margin, margin, margin);
                        View imageparent= LayoutInflater.from(mContext).inflate(R.layout.index_fragment_tools_item,gridLayout,false);
                        TextView name=imageparent.findViewById(R.id.tvName);
                        if(monTool.id!=-1){
                            name.setBackgroundResource(R.drawable.shape_tool_bg2);
                        }else {
                            name.setBackgroundResource(R.drawable.shape_tool_bg);
                        }
                        name.setText(monTool.categoryName);
                        imageparent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(monTool.id!=-1){
                                    passWiki(monTool);
                                }else {
                                    passTool(monTool);
                                }
                            }
                        });
                        gridLayout.addView(imageparent, params);
                    }
                }
            });
        }

    }

    private void passTool(IndexMonTool monTool) {
        if("能不能吃".equals(monTool.categoryName)){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "能不能吃")
                    .withString("url", SpUtils.getValue(mContext, UrlKeys.H5_CAN_EAT_ALL))
                    .navigation();
            return;
        }
        if("宝宝辅食".equals(monTool.categoryName)){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "辅食大全")
                    .withString("url", SpUtils.getValue(mContext, UrlKeys.H5_FOOD_LIST))
                    .navigation();
            return;
        }
        if("产检表".equals(monTool.categoryName)){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_BIRTH_CHECK_LIST)
                    .withInt("id", -1)
                    .navigation();
            return;
        }
        if(monTool.categoryName.contains("B超解读")){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_ANALYZE_B)
                    .navigation();
            return;
        }
        if("待产包".equals(monTool.categoryName)){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_BIRTH_PACKAGE)
                    .navigation();
            return;
        }
        if("月子餐".equals(monTool.categoryName)){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "月子餐")
                    .withString("url", SpUtils.getValue(mContext, UrlKeys.H5_MEAL))
                    .navigation();
            return;
        }
        if("疫苗助手".equals(monTool.categoryName)){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_VACCINE_LIST)
                    .withInt("id", -1)
                    .navigation();
            return;
        }
    }

    private void passWiki(IndexMonTool monTool) {
//        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_KNOWLEDGE);
//        String url = String.format("%s?id=%s", urlPrefix, monTool.id);

//        ARouter.getInstance()
//                .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY)
//                .navigation();

        ARouter.getInstance()
                .build(IndexRoutes.INDEX_MAINPASSNEWS)
                .withString("knowOrInfoStatus", 1+"")
                .withString("stage",get("stage").toString())
                .withString("categoryId", monTool.id+"")
                .navigation();
        return;
    }


}
