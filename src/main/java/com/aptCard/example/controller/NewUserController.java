package com.aptCard.example.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptCard.example.entity.NewUser;
import com.aptCard.example.repository.NewUserRepository;
import com.aptCard.example.serviceImpl.NewUserServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@RestController
@CrossOrigin()
@RequestMapping("/aptkard")
public class NewUserController {

	@Autowired
	private NewUserServiceImpl newUserServiceImpl;

	@Autowired
	private NewUserRepository repo;

	private static Map<String, Integer> counts = new HashMap<>();

	@PostMapping("/upload")
	public ResponseEntity<?> uploadResumeAndGenerateQRCode(
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "photo", required = false) MultipartFile photo,
			@RequestParam("fullName") String fullName, @RequestParam("designation") String designation,
			@RequestParam("phoneNumber") String phoneNumber, @RequestParam("whatsappNumber") String whatsappNumber,
			@RequestParam("linkedinUrl") String linkedinUrl, @RequestParam("location") String location,
			@RequestParam("email") String email, @RequestParam("websiteUrl") String websiteUrl,
			@RequestParam("instagramUrl") String instagramUrl, @RequestParam("facebookUrl") String facebookUrl,
			@RequestParam("pinterestUrl") String pinterestUrl, @RequestParam("youtubeUrl") String youtubeUrl,
			@RequestParam("twitterUrl") String twitterUrl, @RequestParam("yahooUrl") String yahooUrl,
			@RequestParam("aboutUs") String aboutUs, @RequestParam("createdBy") String createdBy,
			@RequestParam("snapChatUrl") String snapChatUrl, @RequestParam("address") String address,
			@RequestParam("username") String username) throws WriterException {
		try {
			NewUser newUser = new NewUser();
			newUser.setFullName(fullName);
			newUser.setDesignation(designation);
			newUser.setPhoneNumber(phoneNumber);
			newUser.setWhatsappNumber(whatsappNumber);
			newUser.setLinkedinUrl(linkedinUrl);
			newUser.setLocation(location);
			newUser.setEmail(email);
			newUser.setWebsiteUrl(websiteUrl);
			newUser.setInstagramUrl(instagramUrl);
			newUser.setFacebookUrl(facebookUrl);
			newUser.setPinterestUrl(pinterestUrl);
			newUser.setYoutubeUrl(youtubeUrl);
			newUser.setTwitterUrl(twitterUrl);
			newUser.setYahooUrl(yahooUrl);
			newUser.setAboutUs(aboutUs);
			String user = username.replaceAll("\\s+", ""); // Remove spaces from the user input
			newUser.setUsername(user);

			// Handle the file parameter
			if (file != null) {
				newUser.setLogo(file.getBytes());
			} else {
				// Handle the case where the file is not present
				// For example, you can set a default logo or perform other actions
				// ...
			}

			newUser.setCreatedBy(createdBy);
			newUser.setSnapChatUrl(snapChatUrl);
			newUser.setAddress(address);

			// Handle the photo parameter
			if (photo != null) {
				newUser.setPhoto(photo.getBytes());
			} else {
				// Handle the case where the photo is not present
				// For example, you can set a default photo or perform other actions
				// ...
			}

			// Save candidate details with resume file
			newUserServiceImpl.saveUser(newUser);

			// Generate QR code for the user
			String url = "http://localhost:3000/" + newUser.getId() + "/" + newUser.getUsername();
			int size = 300;
			int borderSize = 1;
			int imageSize = size - (borderSize * 2);
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(EncodeHintType.MARGIN, 0);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, imageSize, imageSize, hints);
			BufferedImage qrCodeImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = qrCodeImage.createGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, size, size);
			graphics.setColor(Color.BLACK);
			for (int i = 0; i < imageSize; i++) {
				for (int j = 0; j < imageSize; j++) {
					if (bitMatrix.get(i, j)) {
						graphics.fillRect(borderSize + i, borderSize + j, 1, 1);
					}
				}
			}
			graphics.setColor(Color.BLACK);
			graphics.setStroke(new BasicStroke(borderSize));
			graphics.drawRoundRect(borderSize, borderSize, imageSize, imageSize, 20, 20);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(qrCodeImage, "png", out);
			byte[] qrCodeBytes = out.toByteArray();

			// Save the generated QR code for the user
			newUser.setQrcode(qrCodeBytes);
			newUserServiceImpl.saveUser(newUser);

			return ResponseEntity.ok().body("Logo uploaded and QR code generated successfully!");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload logo");
		}
	}

	@RequestMapping(value = "/incrementViewCount", method = RequestMethod.PUT)
	public void incrementViewCount(@RequestParam String username) {
		List<NewUser> employees = repo.findByUsername(username);
		for (NewUser employee : employees) {
			employee.setViews(employee.getViews() + 1);
			repo.save(employee);
		}
	}

	@GetMapping("/allusers")
	public ResponseEntity<List<NewUser>> getAllUsers() {
		List<NewUser> newUser = newUserServiceImpl.getAllUsers();
		return ResponseEntity.ok().body(newUser);
	}

	@GetMapping("/allusersxcel")
	public ResponseEntity<ByteArrayResource> getAllUserexcel() {
		List<NewUser> users = newUserServiceImpl.getAllUsers();

		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Users");
			HSSFRow headerRow = sheet.createRow(0);

			headerRow.createCell(0).setCellValue("ID");
			headerRow.createCell(1).setCellValue("Full Name");
			headerRow.createCell(2).setCellValue("Username");
			headerRow.createCell(3).setCellValue("Designation");
			headerRow.createCell(4).setCellValue("Phone Number");
			headerRow.createCell(5).setCellValue("WhatsApp Number");
			headerRow.createCell(6).setCellValue("Location");
			headerRow.createCell(7).setCellValue("Email");
			headerRow.createCell(8).setCellValue("Website URL");
			headerRow.createCell(9).setCellValue("Instagram URL");
			headerRow.createCell(10).setCellValue("Facebook URL");
			headerRow.createCell(11).setCellValue("Snapchat URL");
			headerRow.createCell(12).setCellValue("Pinterest URL");
			headerRow.createCell(13).setCellValue("LinkedIn URL");
			headerRow.createCell(14).setCellValue("YouTube URL");
			headerRow.createCell(15).setCellValue("Twitter URL");
			headerRow.createCell(16).setCellValue("Yahoo URL");
			headerRow.createCell(17).setCellValue("Created By");
			headerRow.createCell(18).setCellValue("Address");

			int dataRowIndex = 1;

			for (NewUser user : users) {
				HSSFRow dataRow = sheet.createRow(dataRowIndex);
				dataRow.createCell(0).setCellValue(user.getId());
				dataRow.createCell(1).setCellValue(user.getFullName());
				dataRow.createCell(2).setCellValue(user.getUsername());
				dataRow.createCell(3).setCellValue(user.getDesignation());
				dataRow.createCell(4).setCellValue(user.getPhoneNumber());
				dataRow.createCell(5).setCellValue(user.getWhatsappNumber());
				dataRow.createCell(6).setCellValue(user.getLocation());
				dataRow.createCell(7).setCellValue(user.getEmail());
				dataRow.createCell(8).setCellValue(user.getWebsiteUrl());
				dataRow.createCell(9).setCellValue(user.getInstagramUrl());
				dataRow.createCell(10).setCellValue(user.getFacebookUrl());
				dataRow.createCell(11).setCellValue(user.getSnapChatUrl());
				dataRow.createCell(12).setCellValue(user.getPinterestUrl());
				dataRow.createCell(13).setCellValue(user.getLinkedinUrl());
				dataRow.createCell(14).setCellValue(user.getYoutubeUrl());
				dataRow.createCell(15).setCellValue(user.getTwitterUrl());
				dataRow.createCell(16).setCellValue(user.getYahooUrl());
				dataRow.createCell(17).setCellValue(user.getCreatedBy());
				dataRow.createCell(18).setCellValue(user.getAddress());
				dataRowIndex++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			workbook.close();

			ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xls");

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/alluserspdf")
	public ResponseEntity<byte[]> getAllUsersPdf() {
		List<NewUser> userList = newUserServiceImpl.getAllUsers();

		if (userList.isEmpty()) {
			// Handle the case when no users are found
			return ResponseEntity.notFound().build();
		}

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			// Create a new PDF document
			PDDocument document = new PDDocument();

			for (NewUser user : userList) {
				PDPage page = new PDPage();
				document.addPage(page);

				PDPageContentStream contentStream = new PDPageContentStream(document, page);

				// Set up the font and font size
				PDFont font = PDType1Font.HELVETICA_BOLD;
				float fontSize = 12;

				// Set up the table dimensions
				float margin = 50;
				float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
				float yStart = page.getMediaBox().getHeight() - margin;
				float yPosition = yStart;
				float cellHeight = 20;
				float cellWidth = tableWidth / 2; // Assuming two columns

				// Draw the user information
				String[] headers = { "Field", "Value" };
				String[][] userData = { { "ID", String.valueOf(user.getId()) }, { "Full Name", user.getFullName() },
						{ "Username", user.getUsername() }, { "Designation", user.getDesignation() },
						{ "Phone Number", user.getPhoneNumber() }, { "WhatsApp Number", user.getWhatsappNumber() },
						{ "Location", user.getLocation() }, { "Email", user.getEmail() },
						{ "Website URL", user.getWebsiteUrl() }, { "Instagram URL", user.getInstagramUrl() },
						{ "Facebook URL", user.getFacebookUrl() }, { "Snapchat URL", user.getSnapChatUrl() },
						{ "Pinterest URL", user.getPinterestUrl() }, { "LinkedIn URL", user.getLinkedinUrl() },
						{ "YouTube URL", user.getYoutubeUrl() }, { "Twitter URL", user.getTwitterUrl() },
						{ "Yahoo URL", user.getYahooUrl() }, { "Created By", user.getCreatedBy() },
						{ "Address", user.getAddress() } };

				// Draw the table headers
				drawTableHeader(contentStream, yPosition, tableWidth, margin, cellHeight, cellWidth, font, fontSize,
						headers);
				yPosition -= cellHeight;

				// Draw the user information rows
				for (String[] data : userData) {
					drawTableRow(contentStream, yPosition, tableWidth, margin, cellHeight, cellWidth, font, fontSize,
							data);
					yPosition -= cellHeight;
				}

				contentStream.close();
			}

			// Save the document to a byte array
			document.save(outputStream);
			document.close();

			// Set the response headers
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.APPLICATION_PDF);
			responseHeaders.setContentDispositionFormData("attachment", "all_users.pdf");

			return ResponseEntity.ok().headers(responseHeaders).body(outputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception and return an appropriate response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private void drawTableHeader(PDPageContentStream contentStream, float y, float tableWidth, float margin,
			float cellHeight, float cellWidth, PDFont font, float fontSize, String[] headers) throws IOException {
		contentStream.setFont(font, fontSize);

		float textX = margin;
		float textY = y - cellHeight;

		for (String header : headers) {
			// Calculate the width of the header text
			float headerWidth = font.getStringWidth(header) / 1000 * fontSize;

			// Wrap the header text if it exceeds the cell width
			if (headerWidth > cellWidth) {
				List<String> lines = new ArrayList<>();
				int startIndex = 0;
				int endIndex = 0;

				while (endIndex < header.length()) {
					float lineWidth = 0;

					// Find the index at which the line should break
					while (endIndex < header.length()) {
						char c = header.charAt(endIndex);
						float charWidth = font.getStringWidth(String.valueOf(c)) / 1000 * fontSize;

						if (lineWidth + charWidth <= cellWidth) {
							lineWidth += charWidth;
							endIndex++;
						} else {
							break;
						}
					}

					// Add the line to the list
					lines.add(header.substring(startIndex, endIndex).trim());

					// Update the start and end indexes for the next line
					startIndex = endIndex;
				}

				// Draw the wrapped lines
				for (String line : lines) {
					contentStream.beginText();
					contentStream.newLineAtOffset(textX, textY);
					contentStream.showText(line);
					contentStream.endText();
					textY -= cellHeight;
				}
			} else {
				// Draw the header text as a single line
				contentStream.beginText();
				contentStream.newLineAtOffset(textX, textY);
				contentStream.showText(header);
				contentStream.endText();
			}

			textX += cellWidth;
			textY = y - cellHeight;
		}
	}

	private void drawTableRow(PDPageContentStream contentStream, float y, float tableWidth, float margin,
			float cellHeight, float cellWidth, PDFont font, float fontSize, String[] rowData) throws IOException {
		contentStream.setFont(font, fontSize);

		float textX = margin;
		float textY = y - cellHeight;

		for (String cellData : rowData) {
			// Draw the cell border
			contentStream.setLineWidth(1f);
			contentStream.setStrokingColor(Color.BLACK);
			contentStream.addRect(textX, textY, cellWidth, cellHeight);
			contentStream.stroke();

			// Calculate the width of the cell text
			float cellWidthRequired = font.getStringWidth(cellData) / 1000 * fontSize;

			// Wrap the cell text if it exceeds the cell width
			if (cellWidthRequired > cellWidth) {
				List<String> lines = new ArrayList<>();
				int startIndex = 0;
				int endIndex = 0;

				while (endIndex < cellData.length()) {
					float lineWidth = 0;

					// Find the index at which the line should break
					while (endIndex < cellData.length()) {
						char c = cellData.charAt(endIndex);
						float charWidth = font.getStringWidth(String.valueOf(c)) / 1000 * fontSize;

						if (lineWidth + charWidth <= cellWidth) {
							lineWidth += charWidth;
							endIndex++;
						} else {
							break;
						}
					}

					// Add the line to the list
					lines.add(cellData.substring(startIndex, endIndex).trim());

					// Update the start and end indexes for the next line
					startIndex = endIndex;
				}

				// Draw the wrapped lines
				for (String line : lines) {
					contentStream.beginText();
					contentStream.newLineAtOffset(textX + 2, textY + 2);
					contentStream.showText(line);
					contentStream.endText();
					textY -= cellHeight;
				}
			} else {
				// Draw the cell text as a single line
				contentStream.beginText();
				contentStream.newLineAtOffset(textX + 2, textY + 2);
				contentStream.showText(cellData);
				contentStream.endText();
			}

			textX += cellWidth;
			textY = y - cellHeight;
		}
	}

	// Add this delete method to delete a user by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUserById(@PathVariable int id) {
		newUserServiceImpl.deleteUserById(id);
		return ResponseEntity.ok().body("User with ID " + id + " has been deleted.");
	}

	@GetMapping("/{id}")
	public ResponseEntity<NewUser> findById(@PathVariable int id) {
		NewUser newUser = newUserServiceImpl.findById(id);
		if (newUser == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/count")
	public String index(HttpServletRequest request, @RequestParam(required = false) String fullName) {
		if (fullName == null) {
			fullName = "default"; // if full name is not provided, use a default value
		}
		HttpSession session = request.getSession();
		String sessionKey = fullName + "-" + session.getId();
		// Increment count for the given full name only if the user hasn't been counted
		// yet
		Map<String, Integer> userCounts = (Map<String, Integer>) session.getAttribute("userCounts");
		if (userCounts == null) {
			userCounts = new HashMap<>();
			session.setAttribute("userCounts", userCounts);
		}
		if (!userCounts.containsKey(sessionKey)) {
			counts.put(fullName, counts.getOrDefault(fullName, 0) + 1);
			userCounts.put(sessionKey, counts.get(fullName));
		}
		return "Views " + " " + " " + counts.get(fullName) + " ";
	}

//	@GetMapping("/fn")
//	public ResponseEntity<List<NewUser>> findByFullName(@RequestParam(required = false) String fullName) {
//	    // Retrieve the count for the given full name
//	    int count = counts.getOrDefault(fullName, 0);
//
//	    List<NewUser> newUserList = newUserServiceImpl.findByFullName(fullName);
//
//	    List<NewUser> matchingUsers = new ArrayList<>();
//
//	    for (NewUser newUser : newUserList) {
//	        String currentFullName = newUser.getFullName();
//	        if (currentFullName.equals(fullName)) {
//	            matchingUsers.add(newUser);
//	        }
//	    }
//
//	    return ResponseEntity.ok().header("Count", Integer.toString(count)).body(matchingUsers);
//	}
//

//
//	@GetMapping("/{id}/{fullName}")
//	public ResponseEntity<List<NewUser>> findByFullName(@PathVariable String id, @PathVariable String fullName) {
//	    // Retrieve the count for the given full name
//	    int count = counts.getOrDefault(fullName, 0);
//
//	    List<NewUser> newUserList = newUserServiceImpl.findByFullName(fullName);
//
//	    return ResponseEntity.ok().header("Count", Integer.toString(count)).body(newUserList);
//	}

	@GetMapping("/{id}/{username}")
	public ResponseEntity<List<NewUser>> findByFullName(@PathVariable int id, @PathVariable String username) {
		// Retrieve the count for the given usename name
		int count = counts.getOrDefault(username, 0);

		List<NewUser> userList = newUserServiceImpl.findByUsername(username);

		// Filter the list to find users that match the provided id
		List<NewUser> matchingUsers = userList.stream().filter(user -> user.getId() == id).collect(Collectors.toList());

		if (!matchingUsers.isEmpty()) {
			return ResponseEntity.ok().header("Count", Integer.toString(count)).body(matchingUsers);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/")
	public ResponseEntity<List<NewUser>> findByDesignation(@RequestParam(required = false) String designation) {
		List<NewUser> newUser = newUserServiceImpl.findByDesignation(designation);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/ph")
	public ResponseEntity<List<NewUser>> findByPhoneNumber(@RequestParam(required = false) String phoneNumber) {
		List<NewUser> newUser = newUserServiceImpl.findByPhoneNumber(phoneNumber);
		return ResponseEntity.ok(newUser);
	}

	//

	@GetMapping("/w")
	public ResponseEntity<List<NewUser>> findByWhatsappNumber(@RequestParam(required = false) String whatsappNumber) {
		List<NewUser> newUser = newUserServiceImpl.findByWhatsappNumber(whatsappNumber);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/e")
	public ResponseEntity<List<NewUser>> findByEmail(@RequestParam(required = false) String email) {
		List<NewUser> newUser = newUserServiceImpl.findByEmail(email);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/web")
	public ResponseEntity<List<NewUser>> findByWebsiteUrl(@RequestParam(required = false) String websiteUrl) {
		List<NewUser> newUser = newUserServiceImpl.findByWebsiteUrl(websiteUrl);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/in")
	public ResponseEntity<List<NewUser>> findByInstagramUrl(@RequestParam(required = false) String instagramUrl) {
		List<NewUser> newUser = newUserServiceImpl.findByInstagramUrl(instagramUrl);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/fs")
	public ResponseEntity<List<NewUser>> findByFacebookUrl(@RequestParam(required = false) String facebookUrl) {
		List<NewUser> newUser = newUserServiceImpl.findByFacebookUrl(facebookUrl);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/pi")
	public ResponseEntity<List<NewUser>> findByPinterestUrl(@RequestParam(required = false) String pinterestUrl) {
		List<NewUser> newUser = newUserServiceImpl.findByPinterestUrl(pinterestUrl);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/ln")
	public ResponseEntity<List<NewUser>> findByLinkedinUrl(@RequestParam(required = false) String linkedinUrl) {
		List<NewUser> newUser = newUserServiceImpl.findByLinkedinUrl(linkedinUrl);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/yt")
	public ResponseEntity<List<NewUser>> findByYoutubeUrl(@RequestParam(required = false) String youtubeUrl) {
		List<NewUser> newUser = newUserServiceImpl.findByYoutubeUrl(youtubeUrl);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/tw")
	public ResponseEntity<List<NewUser>> findByTwitterUrl(@RequestParam(required = false) String twitterUrl) {
		List<NewUser> newUser = newUserServiceImpl.findByTwitterUrl(twitterUrl);
		return ResponseEntity.ok(newUser);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateUserDetails(@PathVariable("id") int Id,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "photo", required = false) MultipartFile photo,
			@RequestParam("fullName") String fullName, @RequestParam("designation") String designation,
			@RequestParam("phoneNumber") String phoneNumber, @RequestParam("whatsappNumber") String whatsappNumber,
			@RequestParam("linkedinUrl") String linkedinUrl, @RequestParam("location") String location,
			@RequestParam("email") String email, @RequestParam("websiteUrl") String websiteUrl,
			@RequestParam("instagramUrl") String instagramUrl, @RequestParam("facebookUrl") String facebookUrl,
			@RequestParam("pinterestUrl") String pinterestUrl, @RequestParam("youtubeUrl") String youtubeUrl,
			@RequestParam("twitterUrl") String twitterUrl, @RequestParam("yahooUrl") String yahooUrl,
			@RequestParam("aboutUs") String aboutUs, @RequestParam("createdBy") String createdBy,
			@RequestParam("snapChatUrl") String snapChatUrl) {

		try {
			// Retrieve the existing user from the repository
			NewUser existingUser = repo.getById(Id);

			// Update the user details
			existingUser.setFullName(fullName);
			existingUser.setDesignation(designation);
			existingUser.setPhoneNumber(phoneNumber);
			existingUser.setWhatsappNumber(whatsappNumber);
			existingUser.setLinkedinUrl(linkedinUrl);
			existingUser.setLocation(location);
			existingUser.setEmail(email);
			existingUser.setWebsiteUrl(websiteUrl);
			existingUser.setInstagramUrl(instagramUrl);
			existingUser.setFacebookUrl(facebookUrl);
			existingUser.setPinterestUrl(pinterestUrl);
			existingUser.setYoutubeUrl(youtubeUrl);
			existingUser.setTwitterUrl(twitterUrl);
			existingUser.setYahooUrl(yahooUrl);
			existingUser.setAboutUs(aboutUs);
			existingUser.setCreatedBy(createdBy);
			existingUser.setSnapChatUrl(snapChatUrl);

			// Handle the file parameter
			if (file != null) {
				existingUser.setLogo(file.getBytes());
			}

			// Handle the photo parameter
			if (photo != null) {
				existingUser.setPhoto(photo.getBytes());
			}

			// Save the updated user details
			repo.save(existingUser);

			return ResponseEntity.ok().body("User details updated successfully!");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user details");
		}
	}

	@GetMapping("/createdby")
	public ResponseEntity<List<NewUser>> findByCreatedeBy(@RequestParam(required = false) String createdBy) {
		List<NewUser> newUser = newUserServiceImpl.findByCreatedBy(createdBy);
		return ResponseEntity.ok(newUser);
	}
	// This method use to count users

	@GetMapping("/userCount")
	public ResponseEntity<Long> getUserCount() {
		Long count = repo.count();
		return ResponseEntity.ok().body(count);
	}

}
