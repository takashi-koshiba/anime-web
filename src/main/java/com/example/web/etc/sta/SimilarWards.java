package com.example.web.etc.sta;

public class SimilarWards {


	public  static Integer maxLength(Integer len,boolean isShort) {
		Integer cost= (len * (len + 1)) / 2;

		return isShort?cost:cost-len;
	}
	

	
	public static Double exec(String target,Integer inputTotalCost
			,String[] split,boolean isShort) {

		Integer targetCost=maxLength(target.length(),isShort);
		
		Integer inputCountMatch=countMatch(split,target);
		
		Double inputMatchRatio=(double)inputCountMatch/(double)inputTotalCost;
		//1以上にならないようにして一致率を計算
		
		Double targetMatchRatio = ((double) Math.min(inputCountMatch, targetCost)) / ((double) Math.max(inputCountMatch, targetCost));

        
		Double result=inputMatchRatio*targetMatchRatio;

		
		
		return result;
		
	}
	public static String[] splitStr(String str,Integer totalCost,boolean isShort) {
		String arr[]=new String[totalCost];
		
		Integer index=0;
		Integer strLen=str.length();
		for(Integer i=0;i<strLen;i++){
			for(Integer ii=i+1;ii<=strLen;ii++) {
				
				String subStr=str.substring(i,ii);
				if(!isShort &&subStr.length()<=1) {
					continue;
				}
				
				arr[index]=str.substring(i,ii);
				index++;
				
			}
		}
		
		return arr;
	}
	private static Integer countMatch(String[] searchArr,String target) {
		Integer count=0;
		for(Integer i=0;i<searchArr.length;i++) {
			if(target.contains(searchArr[i])) {
				count++;
			}
		}
		return count;
	}
	

}
