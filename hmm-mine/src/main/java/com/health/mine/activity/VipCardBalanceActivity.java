package com.health.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.adapter.VipCardBalanceFooterAdapter;
import com.health.mine.contract.VipBalanceContract;
import com.health.mine.model.BalanceListModel;
import com.healthy.library.model.BalanceModel;
import com.health.mine.presenter.VipBalancePresenter;
import com.healthy.library.banner.ViewPager2Banner;
import com.healthy.library.banner.ScaleInTransformer;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = MineRoutes.MINE_VIPCARDBALANCEACTIVITY)
public class VipCardBalanceActivity extends BaseActivity implements IsFitsSystemWindows, OnRefreshLoadMoreListener, VipBalanceContract.View {
    private RecyclerView recycler;
    private ImageView img_back;
    private TextView logTxt;
    private StatusLayout layout_status;
    private SmartRefreshLayout layout_refresh;
    //private VipCardBalanceHeaderAdapter headerAdapter;
    private VipCardBalanceFooterAdapter footerAdapter;
    private ViewPager2Banner banner;
    private VipBalancePresenter presenter;
    private int currentPage = 1;
    private List<BalanceModel> cardList = new ArrayList<>();
    private Map<String, Object> map = new HashMap<>();
    private String cardNo = null;
    private String ytbAppId = null;
    private CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        init(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_card_balance;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter = new VipBalancePresenter(this, this);
        cardAdapter = new CardAdapter();
        banner.setPageMargin((int) TransformUtil.dp2px(mContext, 10), (int) TransformUtil.dp2px(mContext, 10))
                .setAutoPlay(false)
                .addPageTransformer(new ScaleInTransformer())
                .setOuterPageChangeListener(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        if (cardList != null) {
                            currentPage = 1;
                            footerAdapter.setData(new ArrayList<BalanceListModel>());
                            cardNo = cardList.get(position).CardNo;
                            ytbAppId = cardList.get(position).ytbAppId;
                            getCardLog(cardNo, ytbAppId);
                        }
                    }
                });
        layout_refresh.setOnRefreshLoadMoreListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        footerAdapter = new VipCardBalanceFooterAdapter();
        recycler.setAdapter(footerAdapter);
        getData();

    }

    @Override
    public void getData() {
        super.getData();
        presenter.getBalanceList();
    }

    private void getCardLog(String cardNo, String ytbAppId) {
        map.put("cardNo", cardNo);
        map.put("ytbAppId", ytbAppId);
        map.put("page", currentPage);
        map.put("pageSize", "10");
        presenter.getBalanceLogList(map);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        if (cardNo != null && ytbAppId != null) {
            getCardLog(cardNo, ytbAppId);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layout_refresh.finishRefresh();
        layout_refresh.finishLoadMore();
    }

    @Override
    public void onGetBalanceListSuccess(List<BalanceModel> list) {
        if (list != null && list.size() > 0) {
            cardList = list;
            if (cardAdapter != null) {
                banner.setAdapter(cardAdapter);
                cardAdapter.setData(list);
                cardAdapter.notifyDataSetChanged();
            }
        } else {
            showEmpty();
        }
    }

    @Override
    public void onGetBalanceLogListSuccess(PageInfoEarly pageInfoEarly, List<BalanceListModel> list) {
        if (currentPage == 1) {
            footerAdapter.setData((ArrayList) list);
            if (list.size() == 0) {
                logTxt.setText("暂无消费记录");
            } else {
                logTxt.setText("储值卡记录");
            }
        } else {
            footerAdapter.addDatas((ArrayList) list);
        }
        if (pageInfoEarly.isMore != 1) {
            layout_refresh.finishLoadMoreWithNoMoreData();
        } else {
            layout_refresh.setNoMoreData(false);
            layout_refresh.setEnableLoadMore(true);
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        recycler = findViewById(R.id.recycler);
        img_back = findViewById(R.id.img_back);
        layout_status = findViewById(R.id.layout_status);
        logTxt = findViewById(R.id.logTxt);
        layout_refresh = findViewById(R.id.layout_refresh);
        banner = findViewById(R.id.banner);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    static class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<BalanceModel> items;

        public void setData(List<BalanceModel> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_image, parent, false);
            return new CardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            CardViewHolder cardViewHolder = (CardViewHolder) holder;
            DecimalFormat df = new DecimalFormat(",###,##0.00");
            cardViewHolder.cardId.setText("ID" + items.get(position).CardNo);
            if (items.get(position).CardYe != null && !TextUtils.isEmpty(items.get(position).CardYe)) {
                BigDecimal bd = new BigDecimal(items.get(position).CardYe);
                cardViewHolder.balance.setText(df.format(bd) + "");
            } else {
                cardViewHolder.balance.setText("0.00");
            }
            if (items.get(position).CardTypeName != null && !TextUtils.isEmpty(items.get(position).CardTypeName)) {
                String str = items.get(position).CardTypeName
                        .replaceAll("【", "[").replaceAll("】", "]")
                        .replaceAll("（", "(").replaceAll("）", ")")
                        .replaceAll(" ", "").trim();

                cardViewHolder.CardTypeName.setText(str.substring(str.lastIndexOf("]") + 1, str.length()));//线下返回的一会是中文括号 一会是英文括号  前后中间还有可能有莫名其妙的空格
            } else {
                cardViewHolder.CardTypeName.setText("");
            }
            if (items.get(position).NormalCardYe != null && !TextUtils.isEmpty(items.get(position).NormalCardYe)) {
                BigDecimal bd = new BigDecimal(items.get(position).NormalCardYe);
                cardViewHolder.normalYe.setText(df.format(bd) + "");
            } else {
                cardViewHolder.normalYe.setText("0.00");
            }
            if (items.get(position).ClassCardYe != null && !TextUtils.isEmpty(items.get(position).ClassCardYe)) {
                BigDecimal bd = new BigDecimal(items.get(position).ClassCardYe);
                cardViewHolder.otherYe.setText(df.format(bd) + "");
            } else {
                cardViewHolder.otherYe.setText("0.00");
            }
            cardViewHolder.otherLinerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(MineRoutes.MINE_VIPCARDBALANCELOGSACTIVITY)
                            .withObject("list",items.get(position).VipClassInfo)
                            .withString("classCardYe",items.get(position).ClassCardYe)
                            .navigation();
                }
            });
        }

        @Override
        public int getItemCount() {
            return items != null ? items.size() : 0;
        }
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        private final TextView cardId;
        private final TextView balance;
        private final TextView CardTypeName;
        private final TextView normalYe;
        private final TextView otherYe;
        private final LinearLayout otherLinerLayout;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardId = itemView.findViewById(R.id.cardId);
            normalYe = itemView.findViewById(R.id.normalYe);
            otherYe = itemView.findViewById(R.id.otherYe);
            balance = itemView.findViewById(R.id.balance);
            CardTypeName = itemView.findViewById(R.id.CardTypeName);
            otherLinerLayout = itemView.findViewById(R.id.otherLinerLayout);
        }
    }
}