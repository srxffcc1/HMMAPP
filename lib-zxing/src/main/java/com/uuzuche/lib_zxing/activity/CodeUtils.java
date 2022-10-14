package com.uuzuche.lib_zxing.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.uuzuche.lib_zxing.camera.BitmapLuminanceSource;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.decoding.DecodeFormatManager;

import java.util.Hashtable;
import java.util.Vector;

import me.devilsen.czxing.code.BarcodeReader;
import me.devilsen.czxing.code.CodeResult;

/**
 * Created by aaron on 16/7/27.
 * 二维码扫描工具类
 */
public class CodeUtils {

    public static final String RESULT_TYPE = "result_type";
    public static final String RESULT_STRING = "result_string";
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAILED = 2;

    public static final String LAYOUT_ID = "layout_id";


    public static BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

    public static Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
                                        int desiredWidth, int desiredHeight) {
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, format, desiredWidth,
                    desiredHeight, null);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static Bitmap addWhiteBg(Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        Bitmap bmpWithBorder = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bmpWithBorder;
    }

    /**
     * 解析二维码图片工具类
     *
     * @param analyzeCallback
     */
    public static void analyzeBitmap(String path, AnalyzeCallback analyzeCallback) {
        //System.out.println("获得的图片地址:" + path);
        /**
         * 首先判断图片的大小,若图片过大,则执行图片的裁剪操作,防止OOM
         */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap mBitmap;
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 400);

        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        mBitmap = addWhiteBg(BitmapFactory.decodeFile(path, options));

        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        // 设置继续的字符编码格式为UTF8
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(mBitmap))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap zxingBitmap= null;
        try {
            int boderwith=50;
            ResultPoint[] resultPoint = rawResult.getResultPoints();
            System.out.println("二维码点位Op-----------------------");
            for (int i = 0; i < resultPoint.length; i++) {
                System.out.println(resultPoint[i].getX()+":"+resultPoint[i].getY());
            }
            System.out.println("二维码点位End-----------------------");
            float point1X = resultPoint[0].getX();
            float point2X = resultPoint[1].getX();
            float point1Y = resultPoint[0].getY();
            float point2Y = resultPoint[1].getY();
            int wz=(int)Math.abs(point2Y-point1Y);
            boderwith=(int)(wz/7.1);
            int x = Math.round(point1X)-boderwith;//向外括张一下
            int y = Math.round(point2Y)-boderwith;//向外括张一下
            final int w = (int)Math.abs(point2Y-point1Y)+(2*boderwith);//计算出原始的宽度 然后 增加2倍的双向扩张
            final int h = w;
            zxingBitmap = Bitmap.createBitmap(mBitmap,x,y,w,h,null,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rawResult != null) {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeSuccess(zxingBitmap, rawResult.getText());
            }
        } else {
            CodeResult result = BarcodeReader.getInstance().read(mBitmap);
            result=null;
            if(result!=null){
                rawResult=new Result(result.getText(),null,null,null);
                if (analyzeCallback != null) {
                    analyzeCallback.onAnalyzeSuccess(zxingBitmap, rawResult.getText());
                }
            }else {
                if (analyzeCallback != null) {
                    analyzeCallback.onAnalyzeFailed();
                }
            }


        }
    }

    /**
     * 生成二维码图片
     *
     * @param text
     * @param w  (17+4*12)的倍数 com.google.zxing.qrcode.decoder.getDimensionForVersion 方法详见
     * @param h  (17+4*12)的倍数 com.google.zxing.qrcode.decoder.getDimensionForVersion 方法详见
     * @param logo
     * @return
     */
    public static Bitmap createImage(String text, int w, int h, Bitmap logo) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        try {
//            Runtime.getRuntime().gc();
            Bitmap scaleLogo = getScaleLogo(logo, w, h);

            int offsetX = w / 2;
            int offsetY = h / 2;

            int scaleWidth = 0;
            int scaleHeight = 0;
            if (scaleLogo != null) {
                scaleWidth = scaleLogo.getWidth();
                scaleHeight = scaleLogo.getHeight();
                offsetX = (w - scaleWidth) / 2;
                offsetY = (h - scaleHeight) / 2;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (x >= offsetX && x < offsetX + scaleWidth && y >= offsetY && y < offsetY + scaleHeight) {
                        int pixel = scaleLogo.getPixel(x - offsetX, y - offsetY);
                        if (pixel == 0) {
                            if (bitMatrix.get(x, y)) {
                                pixel = 0xff000000;
                            } else {
                                pixel = 0xffffffff;
                            }
                        }
                        pixels[y * w + x] = pixel;
                    } else {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * w + x] = 0xff000000;
                        } else {
                            pixels[y * w + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
//            bitmap=createCenterBitmap(bitMatrix,bitmap);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Bitmap createCenterBitmap(BitMatrix bitMatrix, Bitmap bitmap) {
        try {
            int[] topLeftOnBit = bitMatrix.getTopLeftOnBit();
            int[] bottomRightOnBit = bitMatrix.getBottomRightOnBit();
            int left = topLeftOnBit[0];
            int top = topLeftOnBit[1];
            int right = bottomRightOnBit[0];
            int bottom = bottomRightOnBit[1];
            if (left > 0 && top > 0 && left < right && top < bottom) {
                int width = right - left;
                int height = bottom - top;
                return Bitmap.createBitmap(bitmap, left, top, width, height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    private static Bitmap getScaleLogo(Bitmap logo, int w, int h) {
        if (logo == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 / logo.getHeight());
        matrix.postScale(scaleFactor, scaleFactor);
        Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true);
        return result;
    }

    /**
     * 解析二维码结果
     */
    public interface AnalyzeCallback {

        public void onAnalyzeSuccess(Bitmap mBitmap, String result);

        public void onAnalyzeFailed();
    }


    /**
     * 为CaptureFragment设置layout参数
     *
     * @param captureFragment
     * @param layoutId
     */
    public static void setFragmentArgs(CaptureFragment captureFragment, int layoutId) {
        if (captureFragment == null || layoutId == -1) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        captureFragment.setArguments(bundle);
    }

    /**
     * 扫码时可选打开或关闭闪光灯
     */
    public static void isLightEnable(boolean isEnable) {
        if (isEnable) {
            try {
                Camera camera = CameraManager.get().getCamera();
                if (camera != null) {
                    Camera.Parameters parameter = camera.getParameters();
                    parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Camera camera = CameraManager.get().getCamera();
                if (camera != null) {
                    Camera.Parameters parameter = camera.getParameters();
                    parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
