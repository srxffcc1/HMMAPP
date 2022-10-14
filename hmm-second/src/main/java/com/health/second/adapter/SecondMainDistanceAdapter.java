package com.health.second.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.second.R;
import com.healthy.library.adapter.DropDownType;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.business.GridDropDownPopNoBack;
import com.healthy.library.model.ProvinceCityModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecondMainDistanceAdapter extends BaseAdapter<String> {
    private List<String> titles = Arrays.asList("商家", "商品");
    Context mcontext;
    float appha = 0;
    private int mTopHeight;
    private LinearLayout seachTopBgLL;

    boolean isStart = false;
    GridDropDownPopNoBack locationDropDownPop;
    Handler handlerSubmit = new Handler();


    public void setProvinceCityModels(List<ProvinceCityModel> provinceCityModels) {
        this.provinceCityModels = provinceCityModels;
    }

    private List<ProvinceCityModel> provinceCityModels = new ArrayList<>();

    public void setAreaString(String areaString) {
//        this.areaString = areaString;
    }




    public String distanceSort = "asc";//desc
    public String appSalesSort = "desc";//ase
    public String platformPriceSort = "";//desc


    public void setAreasCode(String areasCode) {
//        this.areasCode = areasCode;
    }
    public String areaString = "全城";
    public String areasCode = "";
    public int tabType = 0;

    public void setTopheight(int height) {
        if (stickyLayoutHelper != null) {
            if (mTopHeight < height) {
                System.out.println("设置吸顶高度:" + height);
                mTopHeight = height;
                stickyLayoutHelper.setOffset(mTopHeight);
            }
        }
    }

    public StickyLayoutHelper stickyLayoutHelper;

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public SecondMainDistanceAdapter(Context mcontext) {
        this(R.layout.item_second_distance_bar);
        this.mcontext = mcontext;
    }

    public void checkSticky() {
        if (stickyLayoutHelper != null && stickyLayoutHelper.isStickyNow()) {
            if (seachTopBgLL != null) {
                if (appha != 1) {
                    appha = 1;
                    seachTopBgLL.setAlpha(1);
                }
            }
        } else {
            if (locationDropDownPop != null) {
                locationDropDownPop.dismiss();
            }
            if (appha != 0) {
                appha = 0;
                seachTopBgLL.setAlpha(0);
            }

        }
    }

    private SecondMainDistanceAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {

        stickyLayoutHelper = new StickyLayoutHelper();
        stickyLayoutHelper.setStickyStart(true);
        stickyLayoutHelper.setOffset(332);
        return stickyLayoutHelper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
//        baseHolder.setIsRecyclable(true);
        System.out.println("刷新了" + baseHolder.itemView.getTag());
        seachTopBgLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.seachTopBgLL);
         LinearLayout seachTopBgNoLL;
         LinearLayout distanceTopLL;
         SlidingTabLayout st;
         final LinearLayout shopTabLL;
         LinearLayout areaParent;
         final TextView area;
         final ImageView areaAr;
         LinearLayout distanceLL;
         TextView distanceTxt;
         final ImageView distanceUpImg;
         final ImageView distanceDownImg;
         final LinearLayout goodsTabLL;
         LinearLayout saleLL;
         TextView saleTxt;
         final ImageView saleUpImg;
         final ImageView saleDownImg;
         LinearLayout priceLL;
         TextView priceTxt;
         final ImageView priceUpImg;
         final ImageView priceDownImg;
        seachTopBgNoLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.seachTopBgNoLL);
        distanceTopLL = (LinearLayout)baseHolder.itemView. findViewById(R.id.distanceTopLL);
        st = (SlidingTabLayout) baseHolder.itemView.findViewById(R.id.st);
        shopTabLL = (LinearLayout)baseHolder.itemView. findViewById(R.id.shopTabLL);
        areaParent = (LinearLayout) baseHolder.itemView.findViewById(R.id.areaParent);
        area = (TextView) baseHolder.itemView.findViewById(R.id.area);
        areaAr = (ImageView) baseHolder.itemView.findViewById(R.id.areaAr);
        distanceLL = (LinearLayout)baseHolder.itemView. findViewById(R.id.distanceLL);
        distanceTxt = (TextView) baseHolder.itemView.findViewById(R.id.distanceTxt);
        distanceUpImg = (ImageView) baseHolder.itemView.findViewById(R.id.distance_up_img);
        distanceDownImg = (ImageView) baseHolder.itemView.findViewById(R.id.distance_down_img);
        goodsTabLL = (LinearLayout)baseHolder.itemView. findViewById(R.id.goodsTabLL);
        saleLL = (LinearLayout)baseHolder.itemView. findViewById(R.id.saleLL);
        saleTxt = (TextView) baseHolder.itemView.findViewById(R.id.saleTxt);
        saleUpImg = (ImageView)baseHolder.itemView. findViewById(R.id.sale_up_img);
        saleDownImg = (ImageView) baseHolder.itemView.findViewById(R.id.sale_down_img);
        priceLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.priceLL);
        priceTxt = (TextView) baseHolder.itemView.findViewById(R.id.priceTxt);
        priceUpImg = (ImageView) baseHolder.itemView.findViewById(R.id.price_up_img);
        priceDownImg = (ImageView) baseHolder.itemView.findViewById(R.id.price_down_img);
        if (!isStart) {
            st.setViewPager(null, (String[]) titles.toArray());
            st.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    tabType = position;
                    if(0==tabType){
                        shopTabLL.setVisibility(View.VISIBLE);
                        goodsTabLL.setVisibility(View.GONE);
                    }else {
                        shopTabLL.setVisibility(View.GONE);
                        goodsTabLL.setVisibility(View.VISIBLE);
                        if (locationDropDownPop != null) {
                            locationDropDownPop.dismiss();
                        }
                    }
                    if (moutClickListener != null) {
                        moutClickListener.outClick("请求底部数据", null);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            isStart = true;
        }
        if(0==tabType){
            shopTabLL.setVisibility(View.VISIBLE);
            goodsTabLL.setVisibility(View.GONE);
        }else {
            shopTabLL.setVisibility(View.GONE);
            goodsTabLL.setVisibility(View.VISIBLE);
        }

        st.setCurrentTab(tabType);
        areaParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stickyLayoutHelper.isStickyNow()) {
                    if (moutClickListener != null) {
                        moutClickListener.outClick("滑动到吸顶", null);
                    }
                    if (provinceCityModels.size() > 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openLocDialog(area, areaAr);
                            }
                        }, 500);
                    }
                    return;
                }
                if (provinceCityModels.size() > 0) {
                    openLocDialog(area, areaAr);
                }
            }
        });
        distanceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stickyLayoutHelper.isStickyNow()) {
                    if (moutClickListener != null) {
                        moutClickListener.outClick("滑动到吸顶", null);
                    }
                }
                if ("desc".equals(distanceSort)) {
                    distanceSort = "asc";
                } else {
                    distanceSort = "desc";
                }
                setOrderArrow(distanceUpImg, distanceDownImg, distanceSort);
                if (moutClickListener != null) {
                    moutClickListener.outClick("请求底部数据", null);
                }

            }
        });
        priceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stickyLayoutHelper.isStickyNow()) {
                    if (moutClickListener != null) {
                        moutClickListener.outClick("滑动到吸顶", null);
                    }
                }
                if ("desc".equals(platformPriceSort)) {
                    platformPriceSort = "asc";
                } else {
                    platformPriceSort = "desc";
                }
                appSalesSort="";
                setOrderArrow(saleUpImg, saleDownImg, appSalesSort);
                setOrderArrow(priceUpImg, priceDownImg, platformPriceSort);
                if (moutClickListener != null) {
                    moutClickListener.outClick("请求底部数据", null);
                }

            }
        });
        saleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stickyLayoutHelper.isStickyNow()) {
                    if (moutClickListener != null) {
                        moutClickListener.outClick("滑动到吸顶", null);
                    }
                }
                if ("desc".equals(appSalesSort)) {
                    appSalesSort = "asc";
                } else {
                    appSalesSort = "desc";
                }
                platformPriceSort = "";
                setOrderArrow(priceUpImg, priceDownImg, platformPriceSort);
                setOrderArrow(saleUpImg, saleDownImg, appSalesSort);
                if (moutClickListener != null) {
                    moutClickListener.outClick("请求底部数据", null);
                }

            }
        });

        if ("全城".equals(areaString)) {
            setStrengthArrowNo(areaAr, true);
        } else {
            setStrengthArrowRNo(areaAr, true);
        }
        setStrengthArrowNo(area, areaString);
    }

    public int j = 0;

    private void openLocDialog(final TextView area, final ImageView areaAr) {
        if (locationDropDownPop == null) {
            locationDropDownPop = new GridDropDownPopNoBack(context, new GridDropDownPopNoBack.ItemClickCallBack() {
                @Override
                public void click(String id, String name) {
                    areasCode = id;
                    area.setText(name);
                    areaString = name;
                    distanceSort = "asc";
                    if (moutClickListener != null) {
                        moutClickListener.outClick("请求底部数据", null);
                    }
                    setStrengthArrow(area, name);
                    if ("全城".equals(name)) {
                        setStrengthArrow(areaAr, false);
                    } else {
                        setStrengthArrowR(areaAr, false);
                    }

                }

                @Override
                public void dismiss() {
                    if ("全城".equals(areaString)) {
                        setStrengthArrow(areaAr, true);
                    } else {
                        setStrengthArrowR(areaAr, true);
                    }
                }
            });
            List<DropDownType> droplist = new ArrayList<>();
            droplist.add(new DropDownType("", "全城"));
            boolean iscodeinarea = false;
            for (int i = 0; i < provinceCityModels.size(); i++) {
                droplist.add(new DropDownType(provinceCityModels.get(i).getAreaNo(), provinceCityModels.get(i).getName()));
                if (provinceCityModels.get(i).getAreaNo().equals(areasCode)) {
                    iscodeinarea = true;
                }
            }
            locationDropDownPop.setData(droplist);
            locationDropDownPop.setSelectId(areasCode);
        }
        if ("全城".equals(areaString)) {
            setStrengthArrow(areaAr, false);
        } else {
            setStrengthArrowR(areaAr, false);
        }
        locationDropDownPop.showPopupWindow(area);
    }

    public void setOrderArrow(ImageView price_up_img, ImageView price_down_img, String distanceOrderBy) {
        if ("desc".equals(distanceOrderBy)) {
            price_up_img.setImageResource(R.mipmap.service_price_sort_black);
            price_down_img.setImageResource(R.mipmap.service_price_sort_red);
        } else if(TextUtils.isEmpty(distanceOrderBy)){
            price_up_img.setImageResource(R.mipmap.service_price_sort_black);
            price_down_img.setImageResource(R.mipmap.service_price_sort_black);
        }else {
            price_up_img.setImageResource(R.mipmap.service_price_sort_red);
            price_down_img.setImageResource(R.mipmap.service_price_sort_black);
        }
    }


    public void setStrengthArrowNo(ImageView area, boolean flag) {

        area.setImageResource(flag ? R.drawable.ic_solid_triangle_down_gray : R.drawable.ic_solid_triangle_up_gray);

    }

    public void setStrengthArrowRNo(ImageView area, boolean flag) {
        area.setImageResource(flag ? R.drawable.ic_solid_triangle_down_red : R.drawable.ic_solid_triangle_up_red);

    }

    public void setStrengthArrowNo(TextView area, String name) {
        if ("全城".equals(name)) {
            area.setTextColor(Color.parseColor("#333333"));
            area.setText("全城");
        } else {
            area.setTextColor(Color.parseColor("#FF5353"));
            area.setText(name);
        }
    }


    public void setStrengthArrow(ImageView areaAr, boolean flag) {
        areaAr.setImageResource(flag ? R.drawable.ic_solid_triangle_down_gray : R.drawable.ic_solid_triangle_up_gray);
    }

    public void setStrengthArrowR(ImageView areaAr, boolean flag) {
        areaAr.setImageResource(flag ? R.drawable.ic_solid_triangle_down_red : R.drawable.ic_solid_triangle_up_red);
    }

    public void setStrengthArrow(TextView area, String name) {
        if ("全城".equals(name)) {
            area.setTextColor(Color.parseColor("#333333"));
            area.setText("全城");
        } else {
            area.setTextColor(Color.parseColor("#FF5353"));
            area.setText(name);
        }
    }
}
