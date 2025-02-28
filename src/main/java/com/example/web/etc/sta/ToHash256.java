package com.example.web.etc.sta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
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
public static String hashWithFile(File file) throws IOException, NoSuchAlgorithmException  {
	   MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

    try (FileInputStream fis = new FileInputStream(file);
         DigestInputStream dis = new DigestInputStream(fis, sha256)) {

        byte[] buffer = new byte[8192]; // 8KBバッファ
        while (dis.read(buffer) != -1) {
            // DigestInputStream が自動でハッシュを更新
        }
    }

    byte[] sha256Bytes = sha256.digest();
    HexFormat hex = HexFormat.of().withLowerCase();
    return hex.formatHex(sha256Bytes);
}
}
