package com.healthy.library.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.LogUtils;
import com.healthy.library.utils.WaterMarkUtil;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PhotoFragment extends BaseFragment {


    private PhotoView ivLoading;
    private ProgressBar mProgress;
    Bitmap sbitmapFianl;

    public static PhotoFragment newInstance(Map<String, Object> maporg) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo;
    }

    @Override
    protected void findViews() {
        initView();
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(get("url").toString())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        sbitmapFianl = DrawableUtils.drawableToBitmap(resource);
                        mProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(ivLoading);
        ivLoading.setOnClickListener(onClickListener);
        ivLoading.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (sbitmapFianl != null) {
                    StyledDialog.init(getActivity());
                    final List<String> strings = new ArrayList<>();
                    final List<Integer> stringsColors = new ArrayList<>();
                    stringsColors.add(Color.parseColor("#2440BA"));
                    strings.add("保存");
                    StyledDialog.buildBottomItemDialog(strings, stringsColors, "取消", new MyItemDialogListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            if ("保存".equals(text.toString())) {
                                if (sbitmapFianl != null) {
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inJustDecodeBounds = false;
                                    Bitmap watermark = BitmapFactory.decodeResource(getResources(), R.drawable.hmm_watermark_logo, options);
                                    if (!isEmptyBitmap(watermark) && !isEmptyBitmap(watermark)) {
                                        saveBmp2Gallery(WaterMarkUtil.createWaterMaskRightBottom(LibApplication.getAppContext(), sbitmapFianl, watermark, 20, 20), "hmm" + new Date().getTime());//用时间戳来命名图片名称
                                    } else {
                                        showToast("保存图片出错，请退出页面重试");
                                    }
                                }
                            }
                        }

                        @Override
                        public void onBottomBtnClick() {

                        }
                    }).setCancelable(true, true).show();
                }

                return true;
            }
        });
    }

    private void initView() {
        ivLoading = (PhotoView) findViewById(R.id.iv_loading);
        mProgress = findViewById(R.id.progress_view);
        mProgress.setVisibility(View.VISIBLE);
    }

    public void saveBmp2Gallery(Bitmap bmp, String picName) {
        String insertImage = null;
        try {
            insertImage = MediaStore.Images.Media.insertImage(LibApplication.getAppContext().getContentResolver(), bmp, picName, picName);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (!TextUtils.isEmpty(insertImage)) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.parse(insertImage);
                    intent.setData(uri);
                    LibApplication.getAppContext().sendBroadcast(intent);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showContent();
                            Toast.makeText(LibApplication.getAppContext(), "已保存至相册！", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sbitmapFianl.recycle();
    }

    /**
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

}
