package com.aptCard.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aptCard.example.entity.Qrcodeentity;
import com.aptCard.example.service.Qrservice;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class QrController {

	@Autowired
	private Qrservice qrCodeService;

	
	@PostMapping("/generateQRCode")
	public Qrcodeentity generateQRCode(@RequestParam("url") String url) throws Exception {
	    byte[] qrCodeBytes = qrCodeService.generateQRCode(url);
	    Qrcodeentity qrcodeentity = new Qrcodeentity();
	    qrcodeentity.setQrcode(qrCodeBytes);
	    return qrcodeentity;
	}

}
