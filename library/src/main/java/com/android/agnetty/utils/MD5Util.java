/*
 * ========================================================
 * Copyright(c) 2012 杭州龙骞科技-版权所有
 * ========================================================
 * 本软件由杭州龙骞科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.hzdracom.com/
 * 
 * ========================================================
 */

package com.android.agnetty.utils;

import android.graphics.Bitmap;

import com.android.agnetty.constant.CharsetCst;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public class MD5Util {
	
	/**
	 * 对一个文件获取md5值
	 * @param path 文件路径
	 * @return
	 */
	public static String getFileMD5(String path) {
		if(!FileUtil.isFileExist(path)) return "";
        
		FileInputStream in = null;
        try {
        	MessageDigest md5 = MessageDigest.getInstance("MD5");
        	in = new FileInputStream(path);
            byte[] buffer = new byte[8192];
            int length = 0;
            while ((length = in.read(buffer)) != -1) {
            	md5.update(buffer, 0, length);
            }
            
            return BaseUtil.bytesToHexString(md5.digest());
        } catch (Exception ex) {
        	return "";
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getMD5(Bitmap bitmap) {

        ByteArrayOutputStream bos = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);// 将bitmap放入字节数组流中

            bos.flush();// 将bos流缓存在内存中的数据全部输出，清空缓存
            bos.close();
            byte[] bitmapByte = bos.toByteArray();
            md5.update(bitmapByte, 0, bitmapByte.length);

            return BaseUtil.bytesToHexString(md5.digest());
        } catch (Exception ex) {
            return "";
        } finally {
            try {
                if (bos != null) bos.close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getMD5(String str) {
        try {
            MessageDigest md= MessageDigest.getInstance("MD5");
            String token = BaseUtil.bytesToHexString(
                    md.digest(str.getBytes(CharsetCst.UTF_8)));
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
