package com.aptCard.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="QRgenrate")
public class Qrcodeentity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int qr_id;
	
	@Lob
	@Column(length=500000)
	private byte[] qrcode;

	public int getQr_id() {
		return qr_id;
	}

	public void setQr_id(int qr_id) {
		this.qr_id = qr_id;
	}

	public byte[] getQrcode() {
		return qrcode;
	}

	public void setQrcode(byte[] qrcode) {
		this.qrcode = qrcode;
	}

	public Qrcodeentity(int qr_id, byte[] qrcode) {
		super();
		this.qr_id = qr_id;
		this.qrcode = qrcode;
	}

	public Qrcodeentity() {
		super();
		// TODO Auto-generated constructor stub
	}



	
	

}
