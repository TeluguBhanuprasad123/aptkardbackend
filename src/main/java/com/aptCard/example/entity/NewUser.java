package com.aptCard.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "new_UserDetails")
public class NewUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int id;

	@Column(name = "full_Name")
	private String fullName;
	
	@Column(name="username")
	private String username;

	@Column(name = "designation")
	private String designation;

	@Column(name = "phone_Number")
	private String phoneNumber;

	@Column(name = "whatsapp_Number")
	private String whatsappNumber;

	@Column(name = "Location",columnDefinition = "TEXT")
	private String location;

	@Column(name = "email")
	private String email;

	@Column(name = "website_Url")
	private String websiteUrl;

	@Column(name = "instagram_Url")
	private String instagramUrl;

	@Column(name = "facebook_Url")
	private String facebookUrl;

	@Column(name = "snapchatUrl")
	private String snapChatUrl;

	@Column(name = "pinterest_Url")
	private String pinterestUrl;

	@Column(name = "linkedin_Url")
	private String linkedinUrl;

	@Column(name = "youtube_Url")
	private String youtubeUrl;

	@Column(name = "twitter_Url")
	private String twitterUrl;

	@Column(name = "yahoo_Url")
	private String yahooUrl;

	@Column(name = "createdBy")
	private String createdBy;

	@Column(name="address")
	private String address;
	
	@Lob
	private byte[] logo;
	
	@Lob
	private byte[] photo;

	@Column(name = "about_Us",  columnDefinition = "TEXT")
	private String aboutUs;

	@Lob
	@Column(length = 500000)
	private byte[] qrcode;

	@Column(name = "views")
	private int views;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWhatsappNumber() {
		return whatsappNumber;
	}

	public void setWhatsappNumber(String whatsappNumber) {
		this.whatsappNumber = whatsappNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getInstagramUrl() {
		return instagramUrl;
	}

	public void setInstagramUrl(String instagramUrl) {
		this.instagramUrl = instagramUrl;
	}

	public String getFacebookUrl() {
		return facebookUrl;
	}

	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}

	public String getSnapChatUrl() {
		return snapChatUrl;
	}

	public void setSnapChatUrl(String snapChatUrl) {
		this.snapChatUrl = snapChatUrl;
	}

	public String getPinterestUrl() {
		return pinterestUrl;
	}

	public void setPinterestUrl(String pinterestUrl) {
		this.pinterestUrl = pinterestUrl;
	}

	public String getLinkedinUrl() {
		return linkedinUrl;
	}

	public void setLinkedinUrl(String linkedinUrl) {
		this.linkedinUrl = linkedinUrl;
	}

	public String getYoutubeUrl() {
		return youtubeUrl;
	}

	public void setYoutubeUrl(String youtubeUrl) {
		this.youtubeUrl = youtubeUrl;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}

	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}

	public String getYahooUrl() {
		return yahooUrl;
	}

	public void setYahooUrl(String yahooUrl) {
		this.yahooUrl = yahooUrl;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getAboutUs() {
		return aboutUs;
	}

	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
	}

	public byte[] getQrcode() {
		return qrcode;
	}

	public void setQrcode(byte[] qrcode) {
		this.qrcode = qrcode;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public NewUser(int id, String fullName, String username, String designation, String phoneNumber,
			String whatsappNumber, String location, String email, String websiteUrl, String instagramUrl,
			String facebookUrl, String snapChatUrl, String pinterestUrl, String linkedinUrl, String youtubeUrl,
			String twitterUrl, String yahooUrl, String createdBy, String address, byte[] logo, byte[] photo,
			String aboutUs, byte[] qrcode, int views) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.username = username;
		this.designation = designation;
		this.phoneNumber = phoneNumber;
		this.whatsappNumber = whatsappNumber;
		this.location = location;
		this.email = email;
		this.websiteUrl = websiteUrl;
		this.instagramUrl = instagramUrl;
		this.facebookUrl = facebookUrl;
		this.snapChatUrl = snapChatUrl;
		this.pinterestUrl = pinterestUrl;
		this.linkedinUrl = linkedinUrl;
		this.youtubeUrl = youtubeUrl;
		this.twitterUrl = twitterUrl;
		this.yahooUrl = yahooUrl;
		this.createdBy = createdBy;
		this.address = address;
		this.logo = logo;
		this.photo = photo;
		this.aboutUs = aboutUs;
		this.qrcode = qrcode;
		this.views = views;
	}

	public NewUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	
}
