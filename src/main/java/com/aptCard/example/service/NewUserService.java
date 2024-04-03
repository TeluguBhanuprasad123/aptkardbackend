package com.aptCard.example.service;

import java.util.List;

import com.aptCard.example.entity.NewUser;

public interface NewUserService {
    NewUser saveUser(NewUser user);
    List<NewUser> getAllUsers();
    void deleteUserById(int id);
    NewUser findById(int id);
    List<NewUser> findByUsername(String username);
    List<NewUser> findByFullName(String fullName);
    List<NewUser> findByDesignation(String designation);
    List<NewUser> findByPhoneNumber(String phoneNumber);
    List<NewUser> findByWhatsappNumber(String whatsappNumber);
    List<NewUser> findByEmail(String email);
    List<NewUser> findByWebsiteUrl(String websiteUrl);
    List<NewUser> findByInstagramUrl(String instagramUrl);
    List<NewUser> findByFacebookUrl(String facebookUrl);
    List<NewUser> findByPinterestUrl(String pinterestUrl);
    List<NewUser> findByLinkedinUrl(String linkedinUrl);
    List<NewUser> findByYoutubeUrl(String youtubeUrl);
    List<NewUser> findByTwitterUrl(String twitterUrl);
   
    List<NewUser> findByCreatedBy(String createdBy);
    
    

    
}
