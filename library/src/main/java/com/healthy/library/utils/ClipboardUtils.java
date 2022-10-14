package com.healthy.library.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Author: Li
 * Date: 2018/10/18 0018
 * Description:
 */
public class ClipboardUtils {

    public static boolean copy(Context context,CharSequence data){
        ClipboardManager manager= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData=ClipData.newPlainText("wx",data);
        if (manager == null) {
            return false;
        }
        manager.setPrimaryClip(clipData);
        return true;
    }
    /**
     * 获取剪切板里内容
     * @param context
     * @return
     */
    public static String getClipeBoardContent(Context context){
        //System.out.println("尝试访问剪切板");
        ClipboardManager clipboardManager= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData primaryClip = clipboardManager.getPrimaryClip();
        String content=null;
        if (primaryClip!=null&&primaryClip.getItemCount()>0){
            ClipData.Item itemAt = primaryClip.getItemAt(0);
            if (itemAt!=null){
                try {
                    content=itemAt.getText().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }
    /**
     * 清空剪贴板内容
     */
    public static void clearClipboard(Context context) {
        ClipboardManager clipboardManager= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            try {
                clipboardManager.setPrimaryClip(clipboardManager.getPrimaryClip());
                clipboardManager.setText(null);
            } catch (Exception e) {

            }
        }
    }

}
