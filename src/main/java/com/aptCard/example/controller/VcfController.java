package com.aptCard.example.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.aptCard.example.entity.NewUser;
import com.aptCard.example.serviceImpl.NewUserServiceImpl;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class VcfController {
    @Autowired
    NewUserServiceImpl service;

    @GetMapping("/contacts.vcf/{username}")
    public void downloadVCF(@PathVariable String username,HttpServletResponse response) throws IOException {
        response.setContentType("text/vcard");
        response.setHeader("Content-Disposition", "attachment; filename=\"contacts.vcf\"");

        List<NewUser> contacts = service.findByUsername(username);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));

        for (NewUser contact : contacts) {
            writer.write("BEGIN:VCARD\n");
            writer.write("VERSION:3.0\n");
            writer.write("N:" + contact.getFullName() + "\n");
           //writer.write("ADR;TYPE=HOME:" + contact. + "\n");
            writer.write("TEL;TYPE=Work:" +contact.getPhoneNumber()+"\n");
            writer.write("EMAIL;TYPE=Work:"+contact.getEmail()+"\n");
            writer.write("TITLE: "+contact.getDesignation()+"\n");
            writer.write("URL: " + contact.getLocation() + "\n");
            writer.write("URL: "+contact.getFacebookUrl()+"\n");
try {

            byte[] imageData = contact.getLogo();

             // Encode the image data as a Base64 string
            String base64Image = Base64.encodeBase64String(imageData);

            writer.write("PHOTO;TYPE=JPG;ENCODING=BASE64: " + base64Image + "\n");

}
catch(Exception E) {
	E.printStackTrace();
}

            writer.write("END:VCARD\n");
        }

        writer.flush();
        writer.close();
    }


    }

