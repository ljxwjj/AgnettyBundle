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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import com.android.agnetty.constant.AgnettyCst;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 流工具类
 */
public class StreamUtil {
	
	/**
	 * 获取输入流的数据
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] toByteArray(InputStream inStream) throws Exception{
		byte data[] = null;
		ByteArrayOutputStream arrayOutStream = null;
		
		try {
			if(inStream != null) {
				BufferedInputStream bufferInStream = new BufferedInputStream(inStream);
				arrayOutStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[8192]; //8k
				int length = -1;
				
				while((length = bufferInStream.read(buffer)) != -1){
					arrayOutStream.write(buffer, 0, length);
				}
				
				arrayOutStream.flush();
				data = arrayOutStream.toByteArray();
			}
		} catch(Exception ex) {
			throw new Exception(ex);
		} finally {
			try {
				if(arrayOutStream != null) arrayOutStream.close();
			} catch(Exception ex) {
				throw new Exception(ex);
			}
		}
		
		return data;
	} 
	
	/**
	 * 获取输入流的数据
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] toHttpByteArray(InputStream inStream) throws Exception{
		byte data[] = null;
		ByteArrayOutputStream arrayOutStream = null;
		try {
			if(inStream != null) {
				BufferedInputStream bufferInStream = new BufferedInputStream(inStream);
				bufferInStream.mark(2);
				//取前两个字节
				byte[] header = new byte[2];
				int result = bufferInStream.read(header);
				// reset输入流到开始位置
				bufferInStream.reset();
				int headerData = (int)((header[0]<<8) | header[1]&0xFF);
				// Gzip 流 的前两个字节是 0x1f8b
				boolean isGZip = (result != -1 && headerData == 0x1f8b);
				
				if(isGZip) {
					inStream = new GZIPInputStream(bufferInStream);
				} else {
					inStream = bufferInStream;
				}
				 
				arrayOutStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[8192]; //8k
				int length = -1;
				
				while((length = inStream.read(buffer)) != -1){
					arrayOutStream.write(buffer, 0, length);
				}
				
				arrayOutStream.flush();
				data = arrayOutStream.toByteArray();
			}
		} catch(Exception ex) {
			throw new Exception(ex);
		} finally {
			try {
				if(arrayOutStream != null) arrayOutStream.close();
			} catch(Exception ex) {
				throw new Exception(ex);
			}
		}
		
		return data;
	} 
}
