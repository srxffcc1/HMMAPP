package com.health.servicecenter.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.BackAddImgAdapter;
import com.health.servicecenter.adapter.SubmitBackGoodsAdapter;
import com.health.servicecenter.contract.SubmitBackContract;
import com.health.servicecenter.model.RefundDetails;
import com.health.servicecenter.presenter.SubmitBackPresenter;
import com.health.servicecenter.utils.ActivityManager;
import com.health.servicecenter.widget.SelectBackContentDialog;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.decoration.AddImgDecoration;
import com.healthy.library.model.OrderList;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.InputFilterUtils;
import com.healthy.library.utils.MediaFileUtil;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.IncreaseDecreaseView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.lihang.ShadowLayout;
import com.lxj.matisse.CaptureMode;
import com.lxj.matisse.Matisse;
import com.lxj.matisse.MimeType;
import com.lxj.matisse.engine.impl.GlideEngine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


@Route(path = ServiceRoutes.SERVICE_COMITBACK)
public class SubmitBack extends BaseActivity implements BackAddImgAdapter.OnImgChangeListener, SubmitBackContract.View {

    @Autowired
    List<OrderList.OrderDetailListBean> goodsList;

    @Autowired
    String type = "1";

    @Autowired
    String orderId;
    @Autowired
    String deliveryShopName;

    private BackAddImgAdapter backAddImgAdapter;
    private int mPos;
    private SelectBackContentDialog selectBackContentDialog;
    private int selectPosition = -1;
    private SubmitBackPresenter submitBackPresenter;
    private List<String> needuploadimgs = new ArrayList<>();
    private List<String> mBase64Imgs = new ArrayList<>();
    private String[] upimgUrls;

    private TopBar topBar;
    private ImageView ivClose;
    private StatusLayout layoutStatus;
    private LinearLayout topShadowLayout;
    private ImageTextView storeTitle;
    private RecyclerView goodsRecycle;
    private ShadowLayout numShadowLayout;
    private TextView numTitle;
    private IncreaseDecreaseView increaseDecrease;
    private TextView priceTitle;
    private TextView backPriceTitle;
    private TextView backPrice;
    private TextView backTypeTitle;
    private TextView backType;
    private ShadowLayout bottomShadowLayout;
    private TextView reasonTitle;
    private TextView tvSelect;
    private TextView contentTitle;
    private LinearLayout etEvall;
    private EditText etEva;
    private RecyclerView recyclerImgs;
    private TextView tishi;
    private TextView tvCommit;
    private SubmitBackGoodsAdapter submitBackGoodsAdapter;

    private List<RefundDetails> refundDetailsList = new ArrayList<>();
    private double refundAmount = 0;//退订总金额
    private double refundPoints = 0;//退订总积分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comit_back;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        ActivityManager.addActivity(this);
        submitBackPresenter = new SubmitBackPresenter(this, this);
        backAddImgAdapter = new BackAddImgAdapter(this);
        recyclerImgs.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerImgs.setAdapter(backAddImgAdapter);
        recyclerImgs.addItemDecoration(new AddImgDecoration(mContext));
        List<String> data = new ArrayList<>();
        data.add(null);
        backAddImgAdapter.setData(data);
        backAddImgAdapter.setListener(this);
        helper.attachToRecyclerView(recyclerImgs);
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBackContentDialog = SelectBackContentDialog.newInstance(selectPosition);
                selectBackContentDialog.setType(2);
                selectBackContentDialog.show(getSupportFragmentManager(), "select");
                selectBackContentDialog.setResultListener(new SelectBackContentDialog.getContentListener() {
                    @Override
                    public void resultContent(String result, int position) {
                        if (!TextUtils.isEmpty(result) && result != null) {
                            tvSelect.setText(result);
                            selectPosition = position;
                        }
                    }
                });
            }
        });
        initData();
    }

    private void commit() {
        Map<String, Object> map = new HashMap<>();
        map.put("refundDetails", refundDetailsList);
        map.put("refundPoints", FormatUtils.moneyKeep2Decimals(refundPoints));
        map.put("refundAmount", FormatUtils.moneyKeep2Decimals(refundAmount));
        map.put("refundReason", tvSelect.getText() + "");
        map.put("refundComment", etEva.getText() + "");
        map.put("attachList", upimgUrls);
        submitBackPresenter.addBack(map);
    }

    @Override
    public void onAddBackSuccess(String result, String refundId) {
        showToast(result);
        ARouter.getInstance()
                .build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
                .withString("refundId", refundId)
                .navigation();
        finish();
    }

    private void checkMoney() {
        refundDetailsList.clear();
        refundAmount = 0;
        refundPoints = 0;
        for (int i = 0; i < goodsList.size(); i++) {
            double itemAmount = new BigDecimal(goodsList.get(i).rawGoodsPayAmount).multiply(new BigDecimal(goodsList.get(i).backNum)).doubleValue();//先计算退款金额
            if (goodsList.get(i).backNum == goodsList.get(i).goodsQuantity) {
                if (goodsList.get(i).rawTotalTolerance > 0) {//如果是全退 并且差额大于0  就加上去
                    itemAmount = itemAmount + goodsList.get(i).rawTotalTolerance;
                }
            }
            //如果当前子订单算出来的退款金额 大于此子订单的实际付款金额  则让他们相等
            if (itemAmount > Double.parseDouble(goodsList.get(i).totalPayAmount)) {
                itemAmount = Double.parseDouble(goodsList.get(i).totalPayAmount);
            }
            refundAmount = refundAmount + itemAmount;
            refundPoints = refundPoints + (new BigDecimal(goodsList.get(i).goodsPayPoints).multiply(new BigDecimal(goodsList.get(i).backNum)).doubleValue());
            refundDetailsList.add(new RefundDetails(goodsList.get(i).orderDetailId, goodsList.get(i).backNum + ""));

        }
        if (refundPoints > 0) {
            if (refundAmount > 0) {
                backPrice.setText(FormatUtils.moneyKeep2Decimals(refundAmount) + "+" + FormatUtils.moneyKeep2Decimals(refundPoints) + "积分");
            } else {
                backPriceTitle.setText("");
                backPrice.setText(" " + FormatUtils.moneyKeep2Decimals(refundPoints) + "积分");
            }

        } else {
            backPrice.setText(FormatUtils.moneyKeep2Decimals(refundAmount));
        }
    }

    @Override
    public void onUpLoadSuccess(List<String> urlList, int type) {
        upimgUrls = new String[urlList.size()];
        for (int i = 0; i < urlList.size(); i++) {
            upimgUrls[i] = urlList.get(i);
        }
        commit();
    }

    @Override
    public void onFailPost() {

    }

    private void checkupload(final List<String> filePaths) {
        needuploadimgs.clear();
        for (int i = 0; i < filePaths.size(); i++) {
            String filepath = filePaths.get(i);
            if (!TextUtils.isEmpty(filepath)) {
                if (MediaFileUtil.isVideoFileType(filepath)) {
                } else {
                    needuploadimgs.add(filepath);
                }
            }
        }
        if (needuploadimgs.size() > 0) {
            uploadImgs(needuploadimgs);
        } else {
            commit();
        }

    }

    private void initData() {
        if (goodsList != null && goodsList.size() > 0) {
            storeTitle.setText(deliveryShopName);
            for (int i = 0; i < goodsList.size(); i++) {
                goodsList.get(i).backNum = goodsList.get(i).goodsQuantity;
            }
            submitBackGoodsAdapter = new SubmitBackGoodsAdapter();
            goodsRecycle.setLayoutManager(new LinearLayoutManager(this));
            goodsRecycle.setAdapter(submitBackGoodsAdapter);
            goodsRecycle.setNestedScrollingEnabled(false);
            submitBackGoodsAdapter.setData((ArrayList) goodsList);
            checkMoney();
            submitBackGoodsAdapter.setNumChanged(new SubmitBackGoodsAdapter.NumChangedListener() {
                @Override
                public void onNumChanged(String id, int num) {
                    for (int i = 0; i < goodsList.size(); i++) {
                        if (goodsList.get(i).orderDetailId.equals(id)) {
                            goodsList.get(i).backNum = num;
                            checkMoney();
                        }
                    }
                }
            });
        } else {
            showToast("退款商品为空");
            showEmpty();
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        topShadowLayout = (LinearLayout) findViewById(R.id.topShadowLayout);
        storeTitle = (ImageTextView) findViewById(R.id.store_title);
        goodsRecycle = (RecyclerView) findViewById(R.id.goodsRecycle);
        numShadowLayout = (ShadowLayout) findViewById(R.id.numShadowLayout);
        numTitle = (TextView) findViewById(R.id.num_title);
        increaseDecrease = (IncreaseDecreaseView) findViewById(R.id.increase_decrease);
        priceTitle = (TextView) findViewById(R.id.price_title);
        backPriceTitle = (TextView) findViewById(R.id.back_price_title);
        backPrice = (TextView) findViewById(R.id.back_price);
        backTypeTitle = (TextView) findViewById(R.id.back_type_title);
        backType = (TextView) findViewById(R.id.back_type);
        bottomShadowLayout = (ShadowLayout) findViewById(R.id.bottomShadowLayout);
        reasonTitle = (TextView) findViewById(R.id.reason_title);
        tvSelect = (TextView) findViewById(R.id.tv_select);
        contentTitle = (TextView) findViewById(R.id.content_title);
        etEvall = (LinearLayout) findViewById(R.id.et_evall);
        etEva = (EditText) findViewById(R.id.et_eva);
        recyclerImgs = (RecyclerView) findViewById(R.id.recycler_imgs);
        tishi = (TextView) findViewById(R.id.tishi);
        tvCommit = (TextView) findViewById(R.id.tv_commit);
        etEva.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(500),
                new InputFilterUtils.NoEmojiFilter()
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.finishAllActivity();
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIllegal()) {
                    return;
                }
                if (backAddImgAdapter.getData().size() >= 2) {
                    checkupload(backAddImgAdapter.getData());
                } else {
                    commit();
                }
            }
        });
    }

    private boolean checkIllegal() {
        if (TextUtils.isEmpty(tvSelect.getText())) {
            showToast("请选择退货原因");
            return false;
        }
//        if (TextUtils.isEmpty(backPrice.getText())) {
//            showToast("请填写退款金额");
//            return false;
//        }
        if (TextUtils.isEmpty(etEva.getText())) {
            showToast("请填写申请说明");
            return false;
        }
        return true;
    }

    /**
     * 上传图片
     *
     * @param filePaths 图片路径集合
     */
    private void uploadImgs(final List<String> filePaths) {
        showLoading();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                for (String filePath : filePaths) {
                    String base64 = BitmapUtils.bitmap2Base64(filePath);
                    emitter.onNext(base64);
                }
                emitter.onComplete();
            }
        })/*.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())*/
                .compose(RxThreadUtils.<String>Obs_io_main())
                .to(RxLifecycleUtils.<String>bindLifecycle(this))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(String s) {
                        mBase64Imgs.add(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContent();
                        showToast("上传图片失败");
                    }

                    @Override
                    public void onComplete() {
                        submitBackPresenter.uploadFile(mBase64Imgs, 0);
                    }
                });
    }

    @Override
    public void onAddImg() {
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            if (backAddImgAdapter.getData().size() == MAX_IMG_NUM) {
                showToast("最多选9张图片");
                return;
            }
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .capture(true)
                    .countable(true)
                    .maxSelectable(MAX_IMG_NUM - backAddImgAdapter.getData().size())
                    .imageEngine(new GlideEngine())
                    .theme(R.style.ImgPicker)
                    .showSingleMediaType(true)
                    .forResult(RC_CHOOSE_IMG);
        } else {
            PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
        }
    }


    @Override
    public void onUpdate(int pos) {
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            mPos = pos;
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .capture(true, CaptureMode.Image)
                    .countable(false)
                    .maxSelectable(1)
                    .imageEngine(new GlideEngine())
                    .theme(R.style.ImgPicker)
                    .showSingleMediaType(true)
                    .forResult(RC_UPDATE_IMG);
        } else {
            PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_CHOOSE_IMG) {
                if (data != null) {
                    String capturePath = null;
                    String videoPath = null;
                    String cropPath = null;
                    if ((capturePath = Matisse.obtainCaptureImageResult(data)) != null) {
                        backAddImgAdapter.addData(capturePath);
                    } else if ((cropPath = Matisse.obtainCropResult(data)) != null) {
                    } else {
                        List<String> filePaths = Matisse.obtainSelectPathResult(data);
                        if (filePaths != null) {
                            backAddImgAdapter.addDatas(filePaths);
                        }
                    }
                }
            } else if (requestCode == RC_UPDATE_IMG) {
                if (data != null) {
                    String capturePath = null;
                    String videoPath = null;
                    String cropPath = null;
                    if ((capturePath = Matisse.obtainCaptureImageResult(data)) != null) {
                        backAddImgAdapter.addData(capturePath);
                    } else if ((cropPath = Matisse.obtainCropResult(data)) != null) {
                    } else {
                        List<String> filePaths = Matisse.obtainSelectPathResult(data);
                        if (filePaths != null) {
                            backAddImgAdapter.updateData(filePaths.get(0), mPos);
                        }
                    }
                }

            }
        }
    }


    private String[] mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA};
    private static final int RC_CHOOSE_IMG = 764;
    private static final int RC_UPDATE_IMG = 765;
    private static final int RC_PERMISSION = 45;
    private static final int RC_PERMISSION_VIDEO = 46;
    private static final int MAX_IMG_NUM = 9 + 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("需要同意存储权限才能选择图片");
                PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
            }
        }
        if (requestCode == RC_PERMISSION_VIDEO) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("需要同意存储权限才能拍摄");
                PermissionUtils.requestPermissions(this, RC_PERMISSION_VIDEO, mPermissions);
            }
        }
    }

    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFrlg = 0;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return makeMovementFlags(dragFrlg, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //滑动事件  下面注释的代码，滑动后数据和条目错乱，被舍弃
//            Collections.swap(datas,viewHolder.getAdapterPosition(),target.getAdapterPosition());
//            ap.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());

            //得到当拖拽的viewHolder的Position
            int fromPosition = viewHolder.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            int toPosition = target.getAdapterPosition();
            if (fromPosition == backAddImgAdapter.getData().size() - 1) {
                return false;
            }
            if (toPosition == backAddImgAdapter.getData().size() - 1) {
                return false;
            }
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(backAddImgAdapter.getData(), i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(backAddImgAdapter.getData(), i, i - 1);
                }
            }
            backAddImgAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //侧滑删除可以使用；
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        /**
         * 长按选中Item的时候开始调用
         * 长按高亮
         * @param viewHolder
         * @param actionState
         */
        @SuppressLint("MissingPermission")
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.RED);
                //获取系统震动服务//震动70毫秒
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(70);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 手指松开的时候还原高亮
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
            backAddImgAdapter.notifyDataSetChanged();  //完成拖动后刷新适配器，这样拖动后删除就不会错乱
        }
    });
}