package com.android.agnetty.external.helper.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES {

	private byte[] desKey;

	public DES(String desKey) {
		this.desKey = desKey.getBytes();
	}

	public byte[] desEncrypt(byte[] plainText) throws Exception {
		SecureRandom sr = new SecureRandom();
		byte rawKeyData[] = desKey;
		DESKeySpec dks = new DESKeySpec(rawKeyData);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);
		byte data[] = plainText;
		byte encryptedData[] = cipher.doFinal(data);
		return encryptedData;
	}

	public byte[] desDecrypt(byte[] encryptText) throws Exception {
		SecureRandom sr = new SecureRandom();
		byte rawKeyData[] = desKey;
		DESKeySpec dks = new DESKeySpec(rawKeyData);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key, sr);
		byte encryptedData[] = encryptText;
		byte decryptedData[] = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	public String encrypt(String input) throws Exception {
		return base64Encode(desEncrypt(input.getBytes()));
	}

	public String decrypt(String input) throws Exception {
		byte[] result = base64Decode(input);
		return new String(desDecrypt(result));
	}

	public String base64Encode(byte[] s) {
		if (s == null)
			return null;
		EDecoder b = new EDecoder();
		return b.encode(s);
	}

	public byte[] base64Decode(String s) throws IOException {
		if (s == null)
			return null;
		EDecoder decoder = new EDecoder();
		byte[] b = decoder.decode(s);
		return b;
	}
	
	private class EDecoder {
		private int decode(char c) {
			if (c >= 'A' && c <= 'Z')
				return ((int) c) - 65;
			else if (c >= 'a' && c <= 'z')
				return ((int) c) - 97 + 26;
			else if (c >= '0' && c <= '9')
				return ((int) c) - 48 + 26 + 26;
			else
				switch (c) {
				case '+':
					return 62;
				case '/':
					return 63;
				case '=':
					return 0;
				default:
					throw new RuntimeException("unexpected code: " + c);
				}
		}

		public byte[] decode(String s) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				decode(s, bos);
			} catch (IOException e) {
				throw new RuntimeException();
			}
			byte[] decodedBytes = bos.toByteArray();
			try {
				bos.close();
				bos = null;
			} catch (IOException ex) {
				System.err.println("Error while decoding BASE64: " + ex.toString());
			}
			return decodedBytes;
		}

		private void decode(String s, OutputStream os) throws IOException {
			int i = 0;

			int len = s.length();

			while (true) {
				while (i < len && s.charAt(i) <= ' ')
					i++;

				if (i == len)
					break;

				int tri = (decode(s.charAt(i)) << 18)
						+ (decode(s.charAt(i + 1)) << 12)
						+ (decode(s.charAt(i + 2)) << 6)
						+ (decode(s.charAt(i + 3)));

				os.write((tri >> 16) & 255);
				if (s.charAt(i + 2) == '=')
					break;
				os.write((tri >> 8) & 255);
				if (s.charAt(i + 3) == '=')
					break;
				os.write(tri & 255);

				i += 4;
			}
		}

		private final char last2byte = (char) Integer.parseInt("00000011", 2);
		private final char last4byte = (char) Integer.parseInt("00001111", 2);
		private final char last6byte = (char) Integer.parseInt("00111111", 2);
		private final char lead6byte = (char) Integer.parseInt("11111100", 2);
		private final char lead4byte = (char) Integer.parseInt("11110000", 2);
		private final char lead2byte = (char) Integer.parseInt("11000000", 2);
		private final char[] encodeTable = new char[] { 'A', 'B', 'C', 'D',
				'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
				'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
				'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
				'4', '5', '6', '7', '8', '9', '+', '/' };

		public String encode(byte[] from) {
			StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);
			int num = 0;
			char currentByte = 0;
			for (int i = 0; i < from.length; i++) {
				num = num % 8;
				while (num < 8) {
					switch (num) {
					case 0:
						currentByte = (char) (from[i] & lead6byte);
						currentByte = (char) (currentByte >>> 2);
						break;
					case 2:
						currentByte = (char) (from[i] & last6byte);
						break;
					case 4:
						currentByte = (char) (from[i] & last4byte);
						currentByte = (char) (currentByte << 2);
						if ((i + 1) < from.length) {
							currentByte |= (from[i + 1] & lead2byte) >>> 6;
						}
						break;
					case 6:
						currentByte = (char) (from[i] & last2byte);
						currentByte = (char) (currentByte << 4);
						if ((i + 1) < from.length) {
							currentByte |= (from[i + 1] & lead4byte) >>> 4;
						}
						break;
					}
					to.append(encodeTable[currentByte]);
					num += 6;
				}
			}
			if (to.length() % 4 != 0) {
				for (int i = 4 - to.length() % 4; i > 0; i--) {
					to.append("=");
				}
			}
			return to.toString();
		}
	}
 }