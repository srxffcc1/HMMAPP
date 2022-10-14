package com.healthy.library.business;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;

import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.SeckShareDialogContract;
import com.healthy.library.interfaces.OnLabelItemWidthListener;
import com.healthy.library.model.ActivityModel;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.model.EnlistActivityModel;
import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.model.Goods2DetailPin;
import com.healthy.library.model.Goods2ShopModelKick;
import com.healthy.library.model.Goods2ShopModelPin;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.GoodsModel;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.ShopModel;
import com.healthy.library.presenter.SeckShareDialogPresenter;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.StackLabel;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.Disposable;

public class MonthlyShareDialog extends BaseDialogFragment implements SeckShareDialogContract.View {

    private static final String TAG = MonthlyShareDialog.class.getSimpleName();
    private CardView dialogCardView;
    private LinearLayout shareViewLiner;
    private LinearLayout share_weixinLiner;
    private LinearLayout share_friendLiner;
    private LinearLayout share_downLiner;
    private static final int RC_PERMISSION = 45;
    private String[] mPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int mHeightPixels;
    private int mResourcesUmImg;
    private int mWidthPixels;
    private ImageView topView;
    private SeckShareDialogPresenter presenter;
    private ImageView appletsImg;
    private ConstraintLayout shareContentView;
    private LinearLayout itemShareGoodsLinearLayout;
    private ImageView bgImg;
    private ConstraintLayout shareHeaderView;
    private ImageView shareShopLogoImg;
    private ImageView shareThisLogo;
    private TextView longClickText;
    private ConstraintLayout userRecommendLayout;
    private CornerImageView userImg;
    private TextView userNickName;

    private String specialld;
    private String postimg;
    private String merchantId;
    private String personId;
    private String referral_code;
    private Bitmap mBase64Bitmap;
    private StringBuilder mStringBuilder = new StringBuilder();
    private Map<String, Object> mMap = new HashMap();
    private SimpleHashMapBuilder<String, String> mExtraMap = new SimpleHashMapBuilder();

    public MonthlyShareDialog setData(String specialld,String postimg) {
        this.specialld = specialld;
        this.postimg = postimg;
        return this;
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(LibApplication.getAppContext(), "分享成功！", Toast.LENGTH_LONG).show();
            getDialog().dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(LibApplication.getAppContext(), "分享失败！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(LibApplication.getAppContext(), "取消分享！", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (!PermissionUtils.hasPermissions(getActivity(), mPermissions)) {
                Toast.makeText(getActivity(), "需要同意存储权限才能保存图片", Toast.LENGTH_LONG).show();
                PermissionUtils.requestPermissions(getActivity(), RC_PERMISSION, mPermissions);
            }
        }
    }

    public static MonthlyShareDialog newInstance() {
        Bundle args = new Bundle();
        MonthlyShareDialog fragment = new MonthlyShareDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (inflater == null) {
            dismiss();
        }
        final View view = inflater.inflate(R.layout.dis_monthly_share_dialog_layout, null);
        if (TextUtils.isEmpty(merchantId)) {
            merchantId = SpUtils.getValue(getActivity(), SpKey.CHOSE_MC);
        }
        initView(view);
        presenter = new SeckShareDialogPresenter(getActivity(), this);
        //获取屏幕高度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mWidthPixels = outMetrics.widthPixels;
        mHeightPixels = outMetrics.heightPixels;
        Log.e("SeckShare", "当前屏幕宽度 = " + mWidthPixels + ";当前屏幕高度 = " + mHeightPixels);

        builder.setView(view);
        Dialog result = builder.create();
        getData();
        result.setCancelable(true);
        result.setCanceledOnTouchOutside(true);
        Window window = result.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomDialogAnimation);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        }
        initOnClick();
        return result;
    }

    private void initView(View view) {
        dialogCardView = view.findViewById(R.id.dialogCardView);
        shareViewLiner = view.findViewById(R.id.shareViewLiner);
        share_weixinLiner = view.findViewById(R.id.share_weixinLiner);
        share_friendLiner = view.findViewById(R.id.share_friendLiner);
        share_downLiner = view.findViewById(R.id.share_downLiner);
        topView = view.findViewById(R.id.topView);
        shareContentView = (ConstraintLayout) view.findViewById(R.id.share_content_view);
        itemShareGoodsLinearLayout = (LinearLayout) view.findViewById(R.id.item_share_goods_linearLayout);
        bgImg = (ImageView) view.findViewById(R.id.bgImg);
        shareHeaderView = (ConstraintLayout) view.findViewById(R.id.share_header_view);
        shareShopLogoImg = (ImageView) view.findViewById(R.id.shareShopLogoImg);
        shareThisLogo = (ImageView) view.findViewById(R.id.shareThisLogo);
        appletsImg = (ImageView) view.findViewById(R.id.appletsImg);
        longClickText = (TextView) view.findViewById(R.id.longClickText);
        userRecommendLayout = (ConstraintLayout) view.findViewById(R.id.user_recommend_Layout);
        userImg = (CornerImageView) view.findViewById(R.id.userImg);
        userNickName = (TextView) view.findViewById(R.id.userNickName);
    }

    public void initOnClick() {

        //分享微信
        share_weixinLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildWeixin(viewConversionBitmap(dialogCardView));
            }
        });

        //分享朋友圈
        share_friendLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildWeixinCircel(viewConversionBitmap(dialogCardView));
            }
        });

        //保存图片
        share_downLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtils.hasPermissions(getActivity(), mPermissions)) {//读写存储权限
                    if (!bitmapHasPic) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                viewSaveToImage(dialogCardView);
                            }
                        }).start();
                    } else {
                        Toast.makeText(getActivity(), "已保存至相册！", Toast.LENGTH_LONG).show();
                    }

                } else {
                    PermissionUtils.requestPermissions(getActivity(), RC_PERMISSION, mPermissions);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        Window window = getDialog().getWindow();
        window.setWindowAnimations(R.style.BottomDialogAnimation);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }


    /**
     * 分享朋友圈
     *
     * @param bitmap
     */
    private void buildWeixinCircel(Bitmap bitmap) {
        UMImage thumb = new UMImage(getActivity(), bitmap);
        thumb.setThumb(thumb);
        new ShareAction(getActivity())
                .withText("限时秒杀")
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .withMedia(thumb)
                .setCallback(umShareListener)
                .share();
        dismiss();
    }

    private void buildWeixin(Bitmap bitmap) {
        UMImage thumb = new UMImage(getActivity(), bitmap);
        thumb.setThumb(thumb);
        new ShareAction(getActivity())
                .withText("限时秒杀")
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .withMedia(thumb)
                .setCallback(umShareListener)
                .share();
        dismiss();
    }

    private void share(String url, String des, String title, Bitmap bitmap) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(getContext(), bitmap));
        web.setDescription(des);
        new ShareAction(getActivity())
                .withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener)
                .share();
        dismiss();
    }


    boolean bitmapHasPic = false;

    public void viewSaveToImage(View view) {
        // 把一个View转换成bitmap
        Bitmap bitmap = viewConversionBitmap(view);
        bitmapHasPic = true;
        //bitmap转图片并保存本地
        saveBmp2Gallery(bitmap, "hmm" + new Date().getTime());//用时间戳来命名图片名称
    }


    /**
     * view转bitmap
     */
    public Bitmap viewConversionBitmap(View view) {
        view.clearFocus();
//        Runtime.getRuntime().gc();
        Bitmap bitmap = Bitmap.createBitmap((int) (view.getWidth() * 1), (int) (view.getHeight() * 1), Bitmap.Config.RGB_565);
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            canvas.setBitmap(null);
        }
        return bitmap;
    }

    /**
     * @param bmp     获取的bitmap数据
     * @param picName 自定义的图片名
     */
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
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
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
                getActivity().sendBroadcast(intent);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getActivity(), "已保存至相册！", Toast.LENGTH_LONG).show();

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getData() {
        presenter.getZxingCode();
    }

    @Override
    public void onGetUserInfoSuccess(String faceUrl, String nickName) {

    }

    @Override
    public void getZxingCode(String result, String personId) {
        this.referral_code = result;
        this.personId = personId;
        buildShare();
        mMap.clear();
        mExtraMap.clear();
        mMap.put("path", "hmmm://hmm/corresponding");
        mMap.put("params", mExtraMap
                .puts("scheme", "MonthlySpecial")
                .puts("specialld", specialld)
                .puts("merchantId", merchantId)
                .puts("referral_code", this.referral_code)
                .puts("type", "8"));
        presenter.setAppProgram(mMap);
    }

    private void buildShare() {
        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(postimg)
                .placeholder(R.drawable.img_1_1_default)
                .into(bgImg);
        String mUserIcon = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ICON);
        String mUserNick = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_NICK);
        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mBase64Bitmap)
                .placeholder(R.drawable.img_1_1_default)
                .into(appletsImg);
        com.healthy.library.businessutil.GlideCopy.with(getActivity())
                .load(mUserIcon)
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)
                .into(userImg);
        if (TextUtils.isEmpty(personId)) {
            if (mUserNick.length() > 6) {
                mUserNick = mUserNick.substring(0, 6) + "...";
            }
            userNickName.setText("来自\"" + mUserNick + "\"的推荐");
        } else {
            Date today = new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(today);
            calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
            Date tomorrow = calendar.getTime(); //这个时间就是日期往后推一天的结果
            String tomorrowString = new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);

            mUserNick = "<strong>" + personId + "母婴顾问</strong>";
            String timeP = "</p><strong>" + "有效期至 " + tomorrowString + "<strong>";
            userNickName.setText(HtmlCompat.fromHtml("来自“" + mUserNick + "”的推荐" + timeP, HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
    }

    @Override
    public void onGetStoreDetialSuccess(String shopLogo, String partnerMerchantLogo) {

    }

    @Override
    public void onGetBase64DataSuccess(String data) {

    }

    @Override
    public void onGet32DataSuccess(String data) {
        mBase64Bitmap = CodeUtils.createImage(getNormalSurl(SpUtils.getValue(getContext(), UrlKeys.H5_BargainUrl), "HMMDEFAULT", new SimpleHashMapBuilder<>().puts("keyFrame", data)), 650, 650, null);
        if (appletsImg != null) {
            if (getContext() != null) {
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(mBase64Bitmap)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.hmhg_zxing)
                        .into(appletsImg);
            }
        }
    }

    //获取通常形式的分享二维码
    public String getNormalSurl(String urlPrefix, String scheme, Map<String, String> extra) {
        String url = String.format("%s?type=8&scheme=%s&referral_code=%s",
                urlPrefix,
                scheme,
                referral_code);
        mStringBuilder.setLength(0);
        mStringBuilder.append(url);
        for (Map.Entry<String, String> entry : extra.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //url += "&" + key + "=" + value;
            mStringBuilder.append("&").append(key).append("=").append(value);
        }
        url = mStringBuilder.toString();
        System.out.println("分享的二维码:" + url);
        return url;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void showDataErr() {

    }
}

