package com.example.web.etc.db.uploadFile;

import java.sql.Date;

import com.example.web.uploader.sendFile.fileType;

import lombok.Data;

@Data
public class FileInfo  {
	Integer id;
	Integer user_id;
	String fname;
	String lname;
	String alias;
	String extension;
	Date hiduke;
	String searchTxt;
	Double matchRate;
    Integer fileId;
    Double rate;
    fileType type;
    
}
