package com.example.web.etc.sta;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class ToHash256 {
	public static String main(String value) {
		
		try {
			MessageDigest sha256;
			sha256 = MessageDigest.getInstance("SHA-256");
			byte[] sha256Bytes = sha256.digest(value.getBytes());
			
			HexFormat hex = HexFormat.of().withLowerCase();
			String hexStr = hex.formatHex(sha256Bytes);
			return hexStr;
			
		} catch (NoSuchAlgorithmException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
		
	}
public static String hashByte(byte[] b) {
		
		try {
			MessageDigest sha256;
			sha256 = MessageDigest.getInstance("SHA-256");
			byte[] sha256Bytes = sha256.digest(b);
			
			HexFormat hex = HexFormat.of().withLowerCase();
			String hexStr = hex.formatHex(sha256Bytes);
			return hexStr;
			
		} catch (NoSuchAlgorithmException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
		
	}
}
