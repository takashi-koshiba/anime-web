package com.example.web.etc.sta;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Img {
	
	private File file;
	private Integer width;
	private Integer height;
	private BufferedImage imageFile;
	 public Img(File f) {
	        this.file = f;

	        // ファイルの存在確認
	        if (!this.file.exists()) {
	            throw new IllegalArgumentException("File does not exist: " + this.file.getAbsolutePath());
	        }

	        try {
	            // WebP を含む画像の読み込み
	            imageFile = ImageIO.read(this.file);
	            if (imageFile == null) {
	                throw new IOException("Unsupported image format for file: " + this.file.getAbsolutePath());
	            }

	            // 幅と高さを取得
	            this.width = imageFile.getWidth();
	            this.height = imageFile.getHeight();
	        } catch (IOException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error reading the image file: " + e.getMessage());
	        }
	    }
	
	
	public  Integer getWidth() {

		return this.width;
	}
	public  Integer getHeight() {

		return this.height;
	}
	
	//指定したピクセル以下になるようにリサイズ
	public void Resize(String outputPath,Integer minPx,String mimeType) {
		try {
			if(!this.file.exists()) {
				throw new FileNotFoundException();
			}
			String mime=mimeType.substring(mimeType.lastIndexOf("/")+1); 
			
			
			Integer originalWidth = getWidth();
			Integer originalHeight = getHeight();
	    
	    
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

			// 入力画像を読み込む
			BufferedImage inputImage = ImageIO.read(this.file);

			// カラープロファイルを標準の sRGB に変換
			BufferedImage rgbImage = new BufferedImage(
			    inputImage.getWidth(),
			    inputImage.getHeight(),
			    BufferedImage.TYPE_INT_RGB
			);
			ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null);
			op.filter(inputImage, rgbImage);

			// リサイズ処理
			BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = resizedImage.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.drawImage(rgbImage, 0, 0, width, height, null);
			g2d.dispose();

	        System.out.println(mime);
	        
	        ImageIO.write(resizedImage, "png", new File(outputPath));

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}
}
