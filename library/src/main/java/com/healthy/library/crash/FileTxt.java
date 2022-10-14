package com.healthy.library.crash;

/**
 *  @author x024
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 文本读取写入类
 * 
 * @author x024
 *
 */
public class FileTxt {
	/**
	 * 读取文本
	 * 
	 * @param filePath
	 * @return
	 */
	public static String ReadTxt(String filePath) {
		String content = ""; // 文件内容字符串
		// 打开文件
		File file = new File(filePath);
		if (!file.exists() || file.isDirectory()) {
			return "";
		} else {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					// 分行读取
					while ((line = buffreader.readLine()) != null) {
						content += line + "\n";
					}
					instream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 写入文本
	 * 
	 * @param filePath
	 * @param content
	 * @return
	 */
	public static Boolean WirteTxt(String filePath, String content) {
		Boolean isWirte = false;
		// 保存文件
		File file = new File(filePath);
		try {
			OutputStream outstream = new FileOutputStream(file);
			OutputStreamWriter out = new OutputStreamWriter(outstream);
			out.write(content);
			out.close();
			isWirte = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isWirte;
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}
}
