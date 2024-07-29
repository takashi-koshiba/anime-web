package com.example.web.etc.sta;

//一番最後の拡張子を削除する
public class RemoveExtension {
	public static String main(String text) {
		
		return removeExtension(text);
	}
	private static String removeExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // ドットが含まれていない場合はそのまま返す
            return filename;
        }
        return filename.substring(0, lastDotIndex);
    }
}
