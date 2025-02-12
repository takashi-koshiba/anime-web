package com.example.web.etc.sta;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ResizeImg {
	
	//指定したピクセル以下になるようにリサイズ
	public static void main(File file,String outputPath,Integer minPx,String mimeType) {
		try {
			if(!file.exists()) {
				throw new FileNotFoundException();
			}
			String mime=mimeType.substring(mimeType.lastIndexOf("/")+1); 
			BufferedImage original = ImageIO.read(file);
			
			Integer originalWidth = original.getWidth();
			Integer originalHeight = original.getHeight();
	    
	    
			Integer width;
			Integer height;
			if(originalWidth>originalHeight) {
				width=minPx;
				Double ratio=(double)minPx/(double)originalWidth;
				height=(int)((double)originalHeight*ratio);
			}else {
				height=minPx;
				Double ratio=(double)minPx/(double)originalHeight;
				width=(int)((double)originalWidth*ratio);
			}

	        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        
	        // Graphics2D を使用してスケーリング
	        Graphics2D g2d = resizedImage.createGraphics();
	        g2d.drawImage(original.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null);
	        g2d.dispose(); // リソースを解放


	        ImageIO.write(resizedImage, mime, new File(outputPath));

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}
}
