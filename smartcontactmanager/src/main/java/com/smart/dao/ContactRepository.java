package com.smart.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	
	//Implement pagination
	//Page is a sublist of a List type so we can user Page in place of List
	//current page, item per page
	
	@Query("select c from Contact c where c.user.id=:userId")
	public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);
}
