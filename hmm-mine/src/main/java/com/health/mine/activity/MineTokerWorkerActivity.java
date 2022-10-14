package com.health.mine.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.lib_ShapeView.layout.ShapeConstraintLayout;
import com.health.mine.R;
import com.health.mine.adapter.RecommandWorksAdapter;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.QiYeWeiXinContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.QiYeWeXin;
import com.healthy.library.model.QiYeWeXinKey;
import com.healthy.library.model.QiYeWeXinWorkShop;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.presenter.QiYeWeiXinPresenter;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.MMiniPass;
import com.healthy.library.utils.PhoneUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

//选择顾问页面
@Route(path = MineRoutes.MINE_WORKS)
public class MineTokerWorkerActivity extends BaseActivity implements QiYeWeiXinContract.View ,IsFitsSystemWindows{

    private TopBar topBar;
    private com.example.lib_ShapeView.layout.ShapeConstraintLayout topLL;
    private com.healthy.library.widget.CornerImageView tokerImg;
    private android.widget.TextView tokerName;
    private android.widget.TextView tokerNum;
    private android.widget.TextView tokerPhone;
    private android.widget.TextView tokerShop;
    private android.widget.ImageView tokerZxing;
    QiYeWeiXinPresenter qiYeWeiXinPresenter;
    Bitmap sbitmapFianl;
    Bitmap sbitmapFianlSave;
    private ImageView tokerMerchantBg;
    private TextView tokerMerchant;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_tokerworker;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        StyledDialog.init(this);
        GlideCopy.with(mContext).load(R.drawable.ww_ic_launcher)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        sbitmapFianl = DrawableUtils.drawableToBitmap(resource);
                    }
                });
        qiYeWeiXinPresenter=new QiYeWeiXinPresenter(this,this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        qiYeWeiXinPresenter.getMineWorker(new SimpleHashMapBuilder<String, Object>());
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        topLL = (ShapeConstraintLayout) findViewById(R.id.topLL);
        tokerImg = (CornerImageView) findViewById(R.id.tokerImg);
        tokerName = (TextView) findViewById(R.id.tokerName);
        tokerNum = (TextView) findViewById(R.id.tokerNum);
        tokerPhone = (TextView) findViewById(R.id.tokerPhone);
        tokerShop = (TextView) findViewById(R.id.tokerShop);
        tokerZxing = (ImageView) findViewById(R.id.tokerZxing);
        tokerMerchantBg = (ImageView) findViewById(R.id.tokerMerchantBg);
        tokerMerchant = (TextView) findViewById(R.id.tokerMerchant);
    }
    public Bitmap viewConversionBitmap(View view) {
        view.clearFocus();
//        Runtime.getRuntime().gc();
        Bitmap bitmap = Bitmap.createBitmap((int) (view.getWidth() * 1), (int) (view.getHeight() * 1), Bitmap.Config.RGB_565);
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);
//            canvas.drawColor(Color.WHITE);
            view.draw(canvas);
            canvas.setBitmap(null);
        }
        return bitmap;
    }
    public void viewSaveToImage(View view) {
        // 把一个View转换成bitmap
        //bitmap转图片并保存本地
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = viewConversionBitmap(topLL);
                saveBmp2Gallery(bitmap, "hmm" + new Date().getTime());//用时间戳来命名图片名称
            }
        }).start();

//        GlideCopy.with(mContext).load(bindingListBean.workWxImg)
//                .placeholder(R.drawable.img_1_1_default2)
//                .error(R.drawable.img_1_1_default)
//                .into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        sbitmapFianlSave = DrawableUtils.drawableToBitmap(resource);
//                    }
//                });
    }
    public void saveBmp2Gallery(Bitmap bmp, String picName) {

        String fileName = null;
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;


        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;

        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName + ".jpg");

            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            }
            bmp.recycle();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //通知相册更新
//            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
//                    bmp, fileName, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            try {
                LibApplication.getAppContext().sendBroadcast(intent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showContent();
                        Toast.makeText(LibApplication.getAppContext(), "已保存至相册！", Toast.LENGTH_LONG).show();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Looper.prepare();//子线程中需手动创建Looper才能Toast
//            Looper.loop();
        }
    }

    @Override
    public void onSucessGetWeiXinKey(QiYeWeXinKey qiYeWeXinKey) {

    }

    @Override
    public void onSucessGetRecommandWeiXinGroup(List<QiYeWeXin> qiYeWeXins) {

    }

    @Override
    public void onSucessGetRecommandWorkerGroup(List<QiYeWeXinWorkShop> qiYeWeXins) {

    }
    TokerWorkerInfoModel.BindingListBean.BindingTokerWorkerBean bindingListBean;
    @Override
    public void onSucessGetMineWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {
        bindingListBean= tokerWorkerInfoModel.bindingList.get(0).bindingTokerWorker;
        if (bindingListBean == null) {
            return;
        }
        GlideCopy.with(mContext)
                .load(bindingListBean.professionalPhoto)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                .into(tokerImg);
        if(!TextUtils.isEmpty(bindingListBean.workWxUrl)){
            tokerZxing.setVisibility(View.VISIBLE);
            tokerZxing.setImageBitmap(CodeUtils.createImage(bindingListBean.workWxUrl,650, 650,sbitmapFianl));
            tokerZxing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MMiniPass.passMini("gh_f9b4fbd9d3b8",String.format("pages/mine/servicer/jionGroup?merchantId=%s&shopId=%s&workcode=%s&type=%s&groupId=%s"
                            , SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC)
                            ,SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP)
                            ,bindingListBean.referralCode
                            ,"2"
                            ,""
                    ));
                }
            });
        }

        tokerName.setText(bindingListBean.personName);
        tokerNum.setText(bindingListBean.personId);
        tokerPhone.setText(bindingListBean.personTel);
        if(!TextUtils.isEmpty(bindingListBean.personTel)){
            tokerPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhoneUtils.callPhone(mContext,bindingListBean.personTel);
                }
            });
        }
        try {
            if (SpUtils.getValue(LibApplication.getAppContext(), SpKey.CITYNAMEPARTNERNAME) != null) {
                tokerMerchant.setText(SpUtils.getValue(LibApplication.getAppContext(), SpKey.CITYNAMEPARTNERNAME));
            } else {
                tokerMerchant.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tokerShop.setText(bindingListBean.shopName);
    }

    @Override
    public void onSucessGetPublicWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {

    }
}
