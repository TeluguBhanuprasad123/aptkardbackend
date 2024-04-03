package com.aptCard.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptCard.example.entity.NewUser;

public interface NewUserRepository extends JpaRepository<NewUser, Integer> {
	
	
	
	
	
    // any additional methods for custom queries
	
	Optional<NewUser> findById(int id);

	List<NewUser> findByFullName(String fullName);
	
	List<NewUser> findByUsername(String username);

	List<NewUser> findByDesignation(String designation);

	List<NewUser> findByPhoneNumber(String phoneNumber);
	//

	List<NewUser> findByWhatsappNumber(String whatsappNumber);

	List<NewUser> findByEmail(String email);

	List<NewUser> findByWebsiteUrl(String websiteUrl);

	List<NewUser> findByInstagramUrl(String instagramUrl);

	List<NewUser> findByFacebookUrl(String facebookUrl);

	List<NewUser> findByPinterestUrl(String pinterestUrl);

	List<NewUser> findByLinkedinUrl(String linkedinUrl);

	List<NewUser> findByYoutubeUrl(String youtubeUrl);

	List<NewUser> findByTwitterUrl(String twitterUrl);
	
	List<NewUser>  findByCreatedBy(String createdBy);
	
	
}




