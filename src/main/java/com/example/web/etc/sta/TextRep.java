package com.example.web.etc.sta;

public class TextRep {
	public static String main(String text2) {
		String text=(text2.toUpperCase());
		text=text.trim();
		text=text.replace("ゐ", "い");
		text=text.replace("ゑ", "え");
		text=text.replace("ゎ", "わ");
		text=text.replace("ヰ", "イ");
		text=text.replace("ヱ", "エ");


		text=text.replace("0", "０");
		text=text.replace("1", "１");
		text=text.replace("2", "２");
		text=text.replace("3", "３");
		text=text.replace("4", "４");
		text=text.replace("5", "５");
		text=text.replace("6", "６");
		text=text.replace("7", "７");
		text=text.replace("8", "８");
		text=text.replace("9", "９");
		
		text=text.replace("a", "ａ");
		text=text.replace("b", "ｂ");
		text=text.replace("c", "ｃ");
		text=text.replace("d", "ｄ");
		text=text.replace("e", "ｅ");
		text=text.replace("f", "ｆ");
		text=text.replace("g", "ｇ");
		text=text.replace("h", "ｈ");
		text=text.replace("i", "ｉ");
		text=text.replace("j", "ｊ");
		text=text.replace("k", "ｋ");
		text=text.replace("l", "ｌ");
		text=text.replace("m", "ｍ");
		text=text.replace("n", "ｎ");
		text=text.replace("o", "ｏ");
		text=text.replace("p", "ｐ");
		text=text.replace("q", "ｑ");
		text=text.replace("r", "ｒ");
		text=text.replace("s", "ｓ");
		text=text.replace("t", "ｔ");
		text=text.replace("u", "ｕ");
		text=text.replace("v", "ｖ");
		text=text.replace("w", "ｗ");
		text=text.replace("x", "ｘ");
		text=text.replace("y", "ｙ");
		text=text.replace("z", "ｚ");
		
		
		text=text.replace("A", "Ａ");
		text=text.replace("B", "Ｂ");
		text=text.replace("C", "Ｃ");
		text=text.replace("D", "Ｄ");
		text=text.replace("E", "Ｅ");
		text=text.replace("F", "Ｆ");
		text=text.replace("G", "Ｇ");
		text=text.replace("H", "Ｈ");
		text=text.replace("I", "Ｉ");
		text=text.replace("J", "Ｊ");
		text=text.replace("K", "Ｋ");
		text=text.replace("L", "Ｌ");
		text=text.replace("M", "Ｍ");
		text=text.replace("N", "Ｎ");
		text=text.replace("O", "Ｏ");
		text=text.replace("P", "Ｐ");
		text=text.replace("Q", "Ｑ");
		text=text.replace("R", "Ｒ");
		text=text.replace("S", "Ｓ");
		text=text.replace("T", "Ｔ");
		text=text.replace("U", "Ｕ");
		text=text.replace("V", "Ｖ");
		text=text.replace("W", "Ｗ");
		text=text.replace("X", "Ｘ");
		text=text.replace("Y", "Ｙ");
		text=text.replace("Z", "Ｚ");
		text=text.replace("Ⅰ", "I");
		text=text.replace("Ⅱ", "II");
		text=text.replace("Ⅲ", "III");
		text=text.replace("Ⅳ", "IV");
		text=text.replace("Ⅴ", "V");
		text=text.replace("Ⅵ", "VI");
		text=text.replace("Ⅶ", "VII");
		text=text.replace("Ⅷ", "VII");
		text=text.replace("Ⅸ", "IX");
		text=text.replace("Ⅹ", "X");

		
		text=text.replace("ⅰ", "i");
		text=text.replace("ⅱ", "ii");
		text=text.replace("ⅲ", "iii");
		text=text.replace("ⅳ", "iv");
		text=text.replace("ⅴ", "v");
		text=text.replace("ⅵ", "vi");
		text=text.replace("ⅶ", "vii");
		text=text.replace("ⅷ", "viii");
		text=text.replace("ⅸ", "ix");
		text=text.replace("ⅹ", "x");
		text=text.replace("ⅺ", "xi");
		text=text.replace("ⅻ", "xii");
		text=text.replace("*", "＊");
		
		text=text.replace("#", "♯");
		text=text.replace("?", "？");
		text=text.replace(" ", "");
		text=text.replace("　", "");

		//text = text.replaceAll("[^Ａ-Ｚ A-Zａ-ｚ a-z 0-9 ０-９ぁ-ん ー]", "");
		//text = text.replaceAll("[^Ａ-Ｚ A-Zａ-ｚ a-z 0-9 ０-９ ぁ-ん ァ-ヶ 亜-熙", "");
		
		//アニメ検索でヒットしないため一部記号は削除
		text = text.replaceAll("[^Ａ-Ｚ A-Zａ-ｚ a-z 0-9 ０-９ぁ-ん ー ァ-ヶ 一-龠 !！ ＊ ♭ ♯ ？]", "");
		return text;
	}
}
