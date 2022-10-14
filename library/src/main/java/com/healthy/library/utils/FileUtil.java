package com.healthy.library.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.healthy.library.LibApplication;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by gxj on 2018/2/8.
 */
public class FileUtil {
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static final String DST_FOLDER_NAME = "hmmVoice";
    private static String imgPath;

    /**
     * 初始化保存路径
     *
     * @return
     */
    public static String initPath() {
        if (TextUtils.isEmpty(storagePath)) {
            storagePath = parentPath.getAbsolutePath() + "/" + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePath;
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param b 得到的图片
     */
    public static void saveBitmap(Bitmap b) {
        String imgPath = Environment.getExternalStorageDirectory() + "/hmm/upload_takephoto.jpg";
        try {
            FileOutputStream fout = new FileOutputStream(imgPath);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void saveBitmap(Bitmap b,String fileName) {
        String imgPath = Environment.getExternalStorageDirectory() + "/mmtmppic/"+fileName;
        if(new File(imgPath).exists()){
            return;
        }
        try {
            FileOutputStream fout = new FileOutputStream(imgPath);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @return 保存到sd卡的图片路径
     */
    public static String getImgPath() {
        return imgPath;
    }

    public static String saveStringToFile(String content) {
        BufferedWriter out = null;

        //获取SD卡状态
        String state = Environment.getExternalStorageState();
        //判断SD卡是否就绪
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(LibApplication.getAppContext(), "请检查SD卡", Toast.LENGTH_SHORT).show();
            return "";
        }
        //取得SD卡根目录
        String directory = Environment.getExternalStorageDirectory().toString() + "/hmm";
        File file = new File(directory);
        try {
            if (file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            /*
            输出流的构造参数1：可以是File对象  也可以是文件路径
            输出流的构造参数2：默认为False=>覆盖内容； true=>追加内容
             */
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getCanonicalPath() + "/payPinAn.html", false)));
            out.newLine();
            out.write(content);

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return directory + "/payPinAn.html";
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param delFile 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
//            Toast.makeText(LibApplication.getAppContext(), "删除文件失败:" + delFile + "不存在！", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (file.isFile()) {
                return deleteSingleFile(delFile);
            } else {
                return deleteDirectory(delFile);
            }
        }
    }



    public static void deleteFileUnderParent(File file) {
        try {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteFileUnderParent(f);
                }
    //            file.delete();//如要保留文件夹，只删除文件，请注释这行
            } else if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除单个文件
     *
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                Toast.makeText(LibApplication.getAppContext(), "删除单个文件" + filePath$Name + "失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(LibApplication.getAppContext(), "删除单个文件失败：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static String file2Base64(String filePath) {
        FileInputStream objFileIS = null;
        try {
            objFileIS = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream objByteArrayOS = new ByteArrayOutputStream();
        byte[] byteBufferString = new byte[1024];
        try {
            for (int readNum; (readNum = objFileIS.read(byteBufferString)) != -1; ) {
                objByteArrayOS.write(byteBufferString, 0, readNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String videodata = android.util.Base64.encodeToString(objByteArrayOS.toByteArray(), android.util.Base64.DEFAULT);
        return videodata;
    }
    /**
     * 删除目录及目录下的文件
     *
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            Toast.makeText(LibApplication.getAppContext(), "删除目录失败：" + filePath + "不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file
                        .getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            Toast.makeText(LibApplication.getAppContext(), "删除目录失败！", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            Log.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
            return true;
        } else {
            Toast.makeText(LibApplication.getAppContext(), "删除目录：" + filePath + "失败！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static void saveLogFile(String filename,String json){
        File file = new File(filename);// MYLOG_PATH_SDCARD_DIR
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        if (!file.exists()) {
            try {
                //在指定的文件夹中创建文件
                file.createNewFile();
            } catch (Exception e) {
            }
        }
        try {
            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(json);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean IsCanUseSdCard() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
