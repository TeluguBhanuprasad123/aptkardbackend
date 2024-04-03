package com.aptCard.example.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptCard.example.entity.Qrcodeentity;
import com.aptCard.example.repository.Qrrepositroy;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class Qrservice {

	
	  @Autowired
	    private Qrrepositroy repo;
	  
	  public byte[] generateQRCode(String url) throws Exception {
		    int size = 300;
		    int borderSize = 1; // Set size of the border around the QR code image
		    int imageSize = size - (borderSize * 2); // Calculate size of the QR code image without the border

		    // Set hints to improve QR code quality
		    Map<EncodeHintType, Object> hints = new HashMap<>();
		    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		    hints.put(EncodeHintType.MARGIN, 0);

		    QRCodeWriter qrCodeWriter = new QRCodeWriter();
		    BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, imageSize, imageSize, hints);

		    // Create a new image with the desired size and fill it with white color
		    BufferedImage qrCodeImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		    Graphics2D graphics = qrCodeImage.createGraphics();
		    graphics.setColor(Color.WHITE);
		    graphics.fillRect(0, 0, size, size);

		    // Draw the QR code on the image, centered within the border
		    graphics.setColor(Color.BLACK);
		    for (int i = 0; i < imageSize; i++) {
		        for (int j = 0; j < imageSize; j++) {
		            if (bitMatrix.get(i, j)) {
		                graphics.fillRect(borderSize + i, borderSize + j, 1, 1);
		            }
		        }
		    }

		    // Draw the rounded border around the QR code
		    graphics.setColor(Color.BLACK);
		    graphics.setStroke(new BasicStroke(borderSize));
		    graphics.drawRoundRect(borderSize, borderSize, imageSize, imageSize, 20, 20);

		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    ImageIO.write(qrCodeImage, "png", out);
		    byte[] qrCodeBytes = out.toByteArray();

		    Qrcodeentity qrCodeEntity = new Qrcodeentity();
		    qrCodeEntity.setQrcode(qrCodeBytes);
		    repo.save(qrCodeEntity);

		    return qrCodeBytes;
		}


	
	
	
}
