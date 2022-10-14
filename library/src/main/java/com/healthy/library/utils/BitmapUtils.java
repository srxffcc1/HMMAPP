package com.healthy.library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: Li
 * Date: 2018/10/20 0020
 * Description:
 */
public class BitmapUtils {

    public static Bitmap compressBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap;
        BitmapFactory.decodeFile(filePath, options);
        int maxWidth = 750;
        float ratio = 1;
        int optionMax = Math.max(options.outWidth, options.outHeight);
        if (optionMax >= maxWidth) {
            ratio = optionMax * 1.0f / maxWidth;
        }
//        options.inSampleSize = (int) ratio;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap = rotateBitmap(bitmap, readPictureDegrees(filePath));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
        return bitmap;
    }


    public static Bitmap changeColor(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] colorArray = new int[w * h];
        int n = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = getMixtureWhite(bitmap.getPixel(j, i));
                colorArray[n++] = color;
            }
        }
        Bitmap result=null;
        try {
            result= Bitmap.createBitmap(colorArray, w, h, Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }


    public static Bitmap gray2Binary(Bitmap graymap) {
        //得到图形的宽度和长度
        int width = graymap.getWidth();
        int height = graymap.getHeight();
        //创建二值化图像
        Bitmap binarymap = null;
        binarymap = graymap.copy(Bitmap.Config.ARGB_8888, true);
        //依次循环，对图像的像素进行处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到当前像素的值
                int col = binarymap.getPixel(i, j);
                //得到alpha通道的值
                int alpha = col & 0xFF000000;
                //得到图像的像素RGB的值
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                //对图像进行二值化处理
                if (gray <= 95) {
                    gray = 0;
                } else {
                    gray = 255;
                }
                // 新的ARGB
                int newColor = alpha | (gray << 16) | (gray << 8) | gray;
                //设置新图像的当前像素值
                binarymap.setPixel(i, j, newColor);
            }
        }
        return binarymap;
    }

    //获取和白色混合颜色
    private static int getMixtureWhite(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(getSingleMixtureWhite(red, alpha), getSingleMixtureWhite(green, alpha),
                getSingleMixtureWhite(blue, alpha));
    }

    // 获取单色的混合值
    private static int getSingleMixtureWhite(int color, int alpha) {
        int newColor = color * alpha / 255 + 255 - alpha;
        return newColor > 255 ? 255 : newColor;
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int w = bm.getWidth(); // 得到图片的宽，高
        int h = bm.getHeight();
        int retX;
        int retY;
        double wh = (double) w / (double) h;
        double nwh = (double) newWidth / (double) newHeight;
        if (wh > nwh) {
            retX = h * newWidth / newHeight;
            retY = h;
        } else {
            retX = w;
            retY = w * newHeight / newWidth;
        }
        int startX = w > retX ? (w - retX) / 2 : 0;//基于原图，取正方形左上角x坐标
        int startY = h > retY ? (h - retY) / 2 : 0;
        Bitmap bit = Bitmap.createBitmap(bm, startX, startY, retX, retY, null, false);
        bm.recycle();
        return bit;
    }
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static byte[] compressBitmapBytes(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap;
        BitmapFactory.decodeFile(filePath, options);
        int maxWidth = 1080;
        float ratio = 1;
        int optionMax = Math.max(options.outWidth, options.outHeight);
        if (optionMax >= maxWidth) {
            ratio = optionMax * 1.0f / maxWidth;
        }
//        options.inSampleSize = (int) ratio;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap = rotateBitmap(bitmap, readPictureDegrees(filePath));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
        return stream.toByteArray();
    }

    public static String bitmap2Base64(String filePath) {
        return Base64.encodeToString(compressBitmapBytes(filePath), Base64.DEFAULT);
    }

    public static Bitmap Base642Bitmap(String base64) {
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    public static File compressBitmapFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        File file;
        Bitmap bitmap;
        BitmapFactory.decodeFile(filePath, options);
        int maxWidth = 750;
        float ratio = 1;
        int optionMax = Math.max(options.outWidth, options.outHeight);
        if (optionMax >= maxWidth) {
            ratio = optionMax * 1.0f / maxWidth;
        }
//        options.inSampleSize = (int) ratio;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        bitmap = rotateBitmap(bitmap, readPictureDegrees(filePath));

        File dir = new File(Environment.getExternalStorageDirectory(), "technician");
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date()) + ".jpeg";
        if ((!dir.exists() && dir.mkdir()) || dir.exists()) {
            file = new File(dir, fileName);
        } else {
            file = null;
        }
        FileOutputStream outputStream;
        try {
            if (file == null) {
                return null;
            }
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        }
        return file;
    }

    /**
     * 获取照片角度
     */
    private static int readPictureDegrees(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转照片角度
     */
    private static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix m = new Matrix();
        m.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), m, true);
    }
//    private static Bitmap correct
}
