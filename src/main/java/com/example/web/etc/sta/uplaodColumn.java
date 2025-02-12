package com.example.web.etc.sta;

import java.util.List;

import com.example.web.uploader.view.getColumn;

public class uplaodColumn {

	//ファイルアップロード時のファイルタイプを設定
	
	public static getColumn columnList;
	
	public static enum orderEnum{
		ASC,
		DESC
	}
	public static enum sortColumnEnum{
		NAME("ファイル名"),
		DATE("日付");
		
		String column;
		sortColumnEnum(String column) {
			this.column=column;
		}
		
	}
	public static List<Columns> getColumnList(){
		return columnList.getColumns();
	}
	public static void setColumnList(){
		columnList = new getColumn();
		columnList.addValue(3, "ファイル名");
		columnList.addValue(7, "日付");
		//return columnList.getColumns();
		                          
	}

}
