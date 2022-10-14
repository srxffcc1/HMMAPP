package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.banner.BannerAdapter;
import com.healthy.library.banner.ViewPager2Banner;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

public class AppointmentDetailTopAdapter extends BaseAdapter<AppointmentMainItemModel> {

    //private List<Fragment> fragments = new ArrayList<>();
    //private FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    private BannerAdapter bannerAdapter;
    private int selectedPos;
    private TextView goodMoneyValue;
    private double mDefaultPrice;

    public AppointmentDetailTopAdapter() {
        this(R.layout.appointment_detial_top_adapter_layout);
    }

    public AppointmentDetailTopAdapter(int viewId) {
        super(viewId);
    }

    /**
     * 设置价格
     *
     * @param price
     */
    public void setGoodsMoney(double price) {
        if (goodMoneyValue != null) {
            goodMoneyValue.setText(FormatUtils.moneyKeep2Decimals(price));
        }
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        ConstraintLayout goodsTopLL;
        ViewPager imgBanner;
        ViewPager2Banner mBanner;
        LinearLayout fpLL;
        TextView fpTime;
        LinearLayout dotLL;
        final TextView nowPage;
        TextView allPage;
        ConstraintLayout titleLL;
        ConstraintLayout goodsNormal;
        TextView goodMoneyFlag;
        TextView goodssalecount;
        TextView goodsTitle;

        goodsTopLL = (ConstraintLayout) holder.getView(R.id.goodsTopLL);
        imgBanner = (ViewPager) holder.getView(R.id.img_banner);
        mBanner = holder.getView(R.id.banner);
        fpLL = (LinearLayout) holder.getView(R.id.fpLL);
        fpTime = (TextView) holder.getView(R.id.fpTime);
        dotLL = (LinearLayout) holder.getView(R.id.dotLL);
        nowPage = (TextView) holder.getView(R.id.nowPage);
        allPage = (TextView) holder.getView(R.id.allPage);
        titleLL = (ConstraintLayout) holder.getView(R.id.titleLL);
        goodsNormal = (ConstraintLayout) holder.getView(R.id.goodsNormal);
        goodMoneyFlag = (TextView) holder.getView(R.id.goodMoneyFlag);
        goodMoneyValue = (TextView) holder.getView(R.id.goodMoneyValue);
        goodssalecount = (TextView) holder.getView(R.id.goodssalecount);
        goodsTitle = (TextView) holder.getView(R.id.goodsTitle);

        AppointmentMainItemModel model = getModel();
        String goodsMoney = "";
        if ("1".equals(model.getPriceType())) {
            goodsMoney = FormatUtils.moneyKeep2Decimals(model.getAttribute().getPrice());
        } else {
            List<AppointmentMainItemModel.AttributeList> attributeList = model.getAttributeList();
            if (!ListUtil.isEmpty(model.getAttributeList())) {
                mDefaultPrice = attributeList.get(0).getPrice();
                goodsMoney = FormatUtils.moneyKeep2Decimals(mDefaultPrice);
                /*+ "起"*/
            }
        }
        goodsTitle.setText(model.getName());
        int mNumber = model.getSoldNumber() + model.getVirtualSoldNumber();
        //if (mNumber > 0) {
        goodssalecount.setText("已售" + ParseUtils.parseNumber(String.valueOf(mNumber), 10000, "万") + "件");
        //}
        //goodssalecount.setVisibility(mNumber > 0 ? View.VISIBLE : View.GONE);
        goodMoneyValue.setText(goodsMoney);
        final List<String> imgList = model.getImgList();
        allPage.setText(String.valueOf(imgList.size()));

        bannerAdapter = new BannerAdapter();
        bannerAdapter.setData(Arrays.asList(imgList.toArray()));
        bannerAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                        .withCharSequenceArray("urls", imgList.toArray(new String[imgList.size()]))
                        .withInt("pos", selectedPos)
                        .navigation();
            }
        });
        mBanner.setAutoPlay(true)
                .setAutoTurningTime(3000)
                //.addPageTransformer(new ScaleInTransformer())
                .setOuterPageChangeListener(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        selectedPos = position;
                        nowPage.setText(String.valueOf(position + 1));
                    }
                })
                .setAdapter(bannerAdapter);

        /*for (int i = 0; i < imgList.size(); i++) {
            if (MediaFileUtil.isVideoFileType(imgList.get(i))) {
                fragments.add(VideoFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("url", imgList.get(i)).puts("clickPlay", true)));
            } else {
                fragments.add(PhotoFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("fit", true).puts("url", imgList.get(i))));
            }
        }

        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(((BaseActivity) context).getSupportFragmentManager(), fragments, null);
            // adapter
            imgBanner.setAdapter(fragmentPagerItemAdapter);

            imgBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    nowPage.setText(String.valueOf(position + 1));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, null);
        }*/
    }
}
