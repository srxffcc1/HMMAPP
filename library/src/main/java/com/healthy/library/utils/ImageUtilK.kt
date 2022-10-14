package com.healthy.library.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.ref.SoftReference

class  ImageUtilK {

    private var mMemoryCache: LruCache<String, Bitmap>?= null
    private var cacheSize:Int = 0

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public fun bitmapToBase64(bitmap: Bitmap): String {
        var result = ""
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)

                baos!!.flush()
                baos!!.close()

                val bitmapBytes = baos!!.toByteArray()
                result = android.util.Base64.encodeToString(bitmapBytes, android.util.Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos!!.flush()
                    baos!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return result
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public fun base64ToBitmap(base64Data: String): Bitmap? {
        if(cacheSize == 0){
            // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
            // LruCache通过构造函数传入缓存值，以KB为单位。
            val maxMemory = Runtime.getRuntime().maxMemory() / 1024
            // 使用最大可用内存值的1/8作为缓存的大小。
            cacheSize = (maxMemory / 8).toInt()
        }

        if(mMemoryCache == null){
            mMemoryCache = object : LruCache<String, Bitmap>(cacheSize) {
                override fun sizeOf(key: String?, bitmap: Bitmap?): Int {
                    // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                    return bitmap!!.byteCount / 1024
                }
            }
        }

        var bitmap: Bitmap? = null
        var imgByte: ByteArray? = null
        var inputStream: InputStream?= null
        try {
            mMemoryCache?.get(base64Data)?.let {
                bitmap = it
            }
            if(bitmap == null){
                imgByte = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT)
                val option = BitmapFactory.Options()
                option.inSampleSize = 2
                option.inTempStorage = ByteArray(5*1024*1024)
                inputStream = ByteArrayInputStream(imgByte)
                val softReference = SoftReference(BitmapFactory.decodeStream(inputStream, null, option))
                bitmap = softReference.get()
                softReference.clear()
                mMemoryCache?.put(base64Data, bitmap)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            imgByte = null
            try {
                inputStream?.close()
//                System.gc()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

}