package com.aptCard.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptCard.example.entity.Qrcodeentity;

public interface Qrrepositroy extends JpaRepository<Qrcodeentity, Integer>{

}
