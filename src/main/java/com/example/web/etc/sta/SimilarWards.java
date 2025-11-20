package com.example.web.etc.sta;

public class SimilarWards {


	public  static int maxLength(int len,boolean isShort,int maxLen) {

		
		int cost= (len * (len + 1)) / 2;
		int r=isShort?cost:cost-len;
		if ((len-maxLen<0)|| maxLen<=0) {
			return r;
			
		}
		
		
		int diff=len-maxLen;
		if (diff>0) {
			r=r-maxLength(diff,true,0);
		}
		return r;
	}
	

	
	public static Double exec(String target,int inputTotalCost
			,String[] split,boolean isShort,int maxLen) {

		int targetCost=maxLength(target.length(),isShort,maxLen);
		
		int inputCountMatch=countMatch(split,target);
		
		Double inputMatchRatio=(double)inputCountMatch/(double)inputTotalCost;
		//1以上にならないようにして一致率を計算
		
		Double targetMatchRatio = ((double) Math.min(inputCountMatch, targetCost)) / ((double) Math.max(inputCountMatch, targetCost));

        
		Double result=inputMatchRatio*targetMatchRatio;

		
		
		return result;
		
	}
	public static String[] splitStr(String str,int totalCost,boolean isShort,int maxLen) {
		String arr[]=new String[totalCost];
		
		Integer index=0;
		Integer strLen=str.length();
		for(Integer i=0;i<strLen;i++){
			for(Integer ii=i+1;ii<=strLen;ii++) {
				
				String subStr=str.substring(i,ii);
				if((!isShort &&subStr.length()<=1) || subStr.length()>maxLen) {
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
