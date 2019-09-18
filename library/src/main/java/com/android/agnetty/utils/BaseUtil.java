package com.android.agnetty.utils;

public class BaseUtil {
	private static String mHexStr =  "0123456789ABCDEF";
	private static String[] mBinaryArray = {
			"0000",	"0001",	"0010",	"0011",
			"0100",	"0101",	"0110",	"0111",
			"1000",	"1001",	"1010",	"1011",
			"1100",	"1101",	"1110",	"1111"};
	
	/**
	 * 
	 * @param bytes
	 * @return 转换为二进制字符串
	 */
	public static String bytesToBinaryString(byte[] bytes){
		StringBuilder result = new StringBuilder();
		int pos = 0;
		for(byte b:bytes){
			//高四位
			pos = (b&0xF0)>>4;
			result.append(mBinaryArray[pos]);
			//低四位
			pos=b&0x0F;
			result.append(mBinaryArray[pos]);
		}
		
		return result.toString();
	}
	/**
	 * 
	 * @param bytes
	 * @return 将二进制转换为十六进制字符输出
	 */
	public static String bytesToHexString(byte[] bytes){
		StringBuilder result = new StringBuilder();
		int count = bytes.length; 
		for(int i=0;i<count;i++){
			//字节高4位
			result.append(String.valueOf(mHexStr.charAt((bytes[i]&0xF0)>>4)));
			//字节低4位
			result.append(String.valueOf(mHexStr.charAt(bytes[i]&0x0F)));
		}
		return result.toString();
	}
	/**
	 * 
	 * @param hexString
	 * @return 将十六进制转换为字节数组
	 */
	public static byte[] hexStringToBytes(String hexString){
		//hexString的长度对2取整，作为bytes的长度
		int len = hexString.length()/2;
		byte[] bytes = new byte[len];
		byte high = 0;//字节高四位
		byte low = 0;//字节低四位

		for(int i=0;i<len;i++){
			 //右移四位得到高位
			 high = (byte)((mHexStr.indexOf(hexString.charAt(2*i)))<<4);
			 low = (byte)mHexStr.indexOf(hexString.charAt(2*i+1));
			 bytes[i] = (byte) (high|low);//高地位做或运算
		}
		return bytes;
	}
}

