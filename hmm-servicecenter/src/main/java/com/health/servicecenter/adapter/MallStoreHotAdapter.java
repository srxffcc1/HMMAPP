//package com.health.servicecenter.adapter;
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.style.AbsoluteSizeSpan;
//import android.text.style.StyleSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.bumptech.glide.Glide;
//import com.health.servicecenter.R;
//import com.healthy.library.model.HotGoodsList;
//import com.health.servicecenter.widget.SDAdaptiveTextView;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.routes.ServiceRoutes;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.utils.ScreenUtils;
//import com.healthy.library.utils.TransformUtil;
//import com.healthy.library.widget.CornerImageView;
//import com.liys.doubleclicklibrary.click.DoubleClickCancel;
//import com.umeng.analytics.MobclickAgent;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.regex.PatternSyntaxException;
//
//public class MallStoreHotAdapter extends BaseAdapter<ArrayList<HotGoodsList>> implements DoubleClickCancel {
//    private onGridItemClickListener clickListener;
//
//    public void setOnBasketClickListener(MallGoodsItemAdapter.OnBasketClickListener onBasketClickListener) {
//        this.onBasketClickListener = onBasketClickListener;
//    }
//
//    MallGoodsItemAdapter.OnBasketClickListener onBasketClickListener;
//
//    public MallStoreHotAdapter(int viewId) {
//        super(viewId);
//    }
//
//    public MallStoreHotAdapter() {
//        this(R.layout.mall_top_hot_layout);
//    }
//
//    public MallStoreHotAdapter(@NotNull ArrayList list, int viewId) {
//        super(list, viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        return new LinearLayoutHelper();
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
//        GridLayout functionGrid = holder.getView(R.id.mall_hot_grid);
//        TextView textViewEnd = holder.getView(R.id.textViewEnd);
//        textViewEnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Map nokmap = new HashMap<String, String>();
//                nokmap.put("soure","母婴商品页”爆款抢购\"处“>\"");
//                MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_HotGoodsMore",nokmap);
//                ARouter.getInstance()
//                        .build(ServiceRoutes.SERVICE_HOTGOODS)
//                        .navigation();
//            }
//        });
//        addFunctions(context, functionGrid, getDatas().get(0));
//    }
//
//    public interface onGridItemClickListener {
//        void onItemClick(HotGoodsList hotGoodsList);
//    }
//
//    public void setItemClickListener(onGridItemClickListener onGridItemClickListener) {
//        this.clickListener = onGridItemClickListener;
//    }
//
//    private void addFunctions(final Context context, final GridLayout gridLayout, final List<HotGoodsList> urls) {
//        gridLayout.removeAllViews();
//        {
//            int row = 3;
//            final int mMargin = (int) TransformUtil.dp2px(context, 40);
//            ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
//            gridlayoutparm.width = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3) * row;
//            gridLayout.setLayoutParams(gridlayoutparm);
//            gridLayout.removeAllViews();
//            gridLayout.setColumnCount(row);
//            int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3);
//            for (int i = 0; i < urls.size(); i++) {
//                final HotGoodsList goodsList = urls.get(i);
//                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                params.width = w;
//                final View view = LayoutInflater.from(context).inflate(R.layout.item_mall_hot_grid_layout, gridLayout, false);
//
//                final View parent_category = view.findViewById(R.id.parent_category);
//                parent_category.setLayoutParams(new RecyclerView.LayoutParams(((int) (ScreenUtils.getScreenWidth(context) - mMargin) / 3), RecyclerView.LayoutParams.WRAP_CONTENT));
//                com.healthy.library.businessutil.GlideCopy.with(context)
//                        .load(goodsList.getFilePath())
//                        .placeholder(R.drawable.img_1_1_default2)
//                        .error(R.drawable.img_1_1_default)
//
//                        .into((CornerImageView) view.findViewById(R.id.iv_category));
//                final SDAdaptiveTextView tv_category = view.findViewById(R.id.tv_category);
//                TextView tv_price = view.findViewById(R.id.tv_price);
//                final ImageView tv_sales = view.findViewById(R.id.tv_sales);
//                tv_category.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        tv_category.setAdaptiveText(StringFilter(goodsList.getGoodsTitle()));
//                    }
//                });
//                SpannableString priceStr = new SpannableString("¥" + FormatUtils.moneyKeep2Decimals(goodsList.getPlatformPrice()));
//                priceStr.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                priceStr.setSpan(new StyleSpan(Typeface.NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
//                tv_price.setText(priceStr);
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (moutClickListener != null) {
//                            // moutClickListener.outClick(goodsList.getGoodsTitle(), goodsList);
//                        }
//                    }
//                });
//                tv_sales.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        clickListener.onItemClick(goodsList);
//                        if (onBasketClickListener != null) {
//                            onBasketClickListener.onBasketClick(v);
//                        }
//                    }
//                });
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Map nokmap = new HashMap<String, String>();
//                        nokmap.put("soure","母婴商品页“爆款抢购\"栏商品");
//                        MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails",nokmap);
//                        ARouter.getInstance()
//                                .build(ServiceRoutes.SERVICE_DETAIL)
//                                .withString("id", goodsList.getGoodsId() + "")
//                                .navigation();
//                    }
//                });
//                gridLayout.addView(view, params);
//            }
//        }
//
//    }
//
//    // 替换、过滤特殊字符
//    public String StringFilter(String str) throws PatternSyntaxException {
//        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("，", ",");//替换中文标号
//        String regEx = "[『』]"; // 清除掉特殊字符
//        Pattern p = Pattern.compile(regEx);
//        Matcher m = p.matcher(str);
//        return m.replaceAll("").trim();
//    }
//}
