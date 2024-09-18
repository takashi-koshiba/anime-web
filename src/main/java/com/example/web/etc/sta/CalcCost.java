package com.example.web.etc.sta;

public class CalcCost {


	private static Integer[] maxCost(Integer len,Integer totalCost) {
		if(len<1) {
			return  new Integer[] {1,totalCost};
		}
		totalCost+=maxCost(len-1,totalCost)[1]+len;
		return new  Integer[] {len,totalCost};
	}
	
	public static Double getMaxCost(String input,String target) {
		Integer inputTotalCost;
		inputTotalCost= maxCost(input.length(),0)[1];
		
		String split[]=splitStr(input,inputTotalCost);
		Integer targetCost=maxCost(target.length(),0)[1];
		
		Integer inputCountMatch=countMatch(split,target);
		
		Double inputMatchRatio=(double)inputCountMatch/(double)inputTotalCost;
		Double inputCost=inputMatchRatio*inputCountMatch;
		
		Double result=inputCost/(double)targetCost;

		return result;
		
	}
	private static String[] splitStr(String str,Integer totalCost) {
		String arr[]=new String[totalCost];
		Integer index=0;
		for(Integer i=0;i<str.length();i++){
			for(Integer ii=i+1;ii<=str.length();ii++) {
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
